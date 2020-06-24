package com.example.travelpet.controlller.perfil.passageiro.ui.viagem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

import com.example.travelpet.R;
import com.example.travelpet.adapter.AnimalBinder;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DisponibilidadeMotoristaDao;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.LocalDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.dao.ViagemDAO;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DisponibilidadeMotorista;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Local;
import com.example.travelpet.model.Viagem;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class ViagemFragment extends Fragment implements OnMapReadyCallback {

    private BottomSheetDialog bsDialog;
    private Local localOrigem, localDestino;
    private View bsView;
    private MultiViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerBs;
    private ListSection<Animal> listSection;
    private ArrayList<Animal> listaAnimais = new ArrayList<>();
    private ArrayList<Animal> listaAnimaisSelecionados;
    private ArrayList<String> motoristaCancelados = new ArrayList<>();
    private Dialog dialogOrigemDestino;
    private Dialog dialogBuscarMotorista;
    private CountDownLatch contador;
    private AnimalDAO animalDAO;
    private LocalDAO localDAO;
    private ViagemDAO viagemDAO;
    private DisponibilidadeMotoristaDao disponibilidadeMotoristaDao;
    private DisponibilidadeMotorista motoristaDisponivel;
    private Viagem viagem;
    private LinearLayout linearOrigemDestino;

    private DonoAnimal meuPerfil;
    private DonoAnimalDAO donoAnimalDAO;

    // Variáveis para recuperar localização de um usuário
    private Location localizacaoAtual;
    private Address addressDestino;
    private Geocoder geocoder;
    private GoogleMap gMap;
    private MapView mapView;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder builderlocationsSettingsRequest;
    private SettingsClient settingsClient;
    private LatLng localPassageiro;

    private ChildEventListener listenerAguardandoMotorista;
    private DatabaseReference motoristaDisponivelReferencia;

    private EditText editDestino, editOrigem;
    private Button buttonChamarMotorista,btSelecionarAnimais;

    private Thread threadMotoristasDisponiveis;
    private Thread threadIniciarPerfil;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viagem, container, false);
        //return inflater.inflate(R.layout.fragment_viagem,container, false);
        buttonChamarMotorista = view.findViewById(R.id.buttonChamarMotorista);
        editDestino = view.findViewById(R.id.editDestino);
        editOrigem = view.findViewById(R.id.editOrigem);
        linearOrigemDestino = view.findViewById(R.id.linear_origem_destino);

        //inicia Daos
        animalDAO = new AnimalDAO();
        localDAO = new LocalDAO();
        viagemDAO = new ViagemDAO();
        donoAnimalDAO = new DonoAnimalDAO();
        disponibilidadeMotoristaDao = new DisponibilidadeMotoristaDao();

        //meuPerfil = donoAnimalDAO.receberPerfil(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()),);

        // Criando mapa
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mapView = (MapView) view.findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        IniciarPerfil();
        iniciarMapa();
        btNovaViagemOnClick();
        return view;
    }

    private void iniciarMapa()
    {
        try
        {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        recuperarLocalizacaoUsuario();
    }

    private void recuperarLocalizacaoUsuario()
    {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if (location != null)
                {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    localPassageiro = new LatLng(latitude, longitude);

                    gMap.clear();
                    gMap.addMarker(new MarkerOptions().position(localPassageiro).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localPassageiro,19));
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                showInTerminal("<< Falha ao receber a Localização >>");
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 1000);// intervalo de tempo para buscar localizacao em milisegundos;
        locationRequest.setFastestInterval(2 * 1000);// intervalo de tempo para receber localização de outros apps que estejam utilizando o mesmo recurso
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        builderlocationsSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        settingsClient = LocationServices.getSettingsClient(requireActivity());
        settingsClient.checkLocationSettings(builderlocationsSettingsRequest.build()).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>()
        {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {}


        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                if( e instanceof ResolvableApiException)
                {
                    try
                    {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(requireActivity(),10);
                    }
                    catch (IntentSender.SendIntentException ex)
                    { ex.printStackTrace();}
                }
            }
        });

        LocationCallback locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                showInTerminal("<< onLocationResult : ok >>");

                Location location = locationResult.getLastLocation();

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                localPassageiro = new LatLng(latitude, longitude);

                gMap.clear();
                gMap.addMarker(new MarkerOptions().position(localPassageiro).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localPassageiro,19));
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {}
        };

        client.requestLocationUpdates(locationRequest,locationCallback,null);

    }

    private void exibirBottomSheet() {
        configuraBottomSheet();
        popularAdapter();

        bsDialog.show();
    }

    private void popularAdapter()
    {
       final Thread thread1 =  new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                listaAnimais.clear();
                listaAnimais = animalDAO.receberListaAnimal(contador);

                try
                {contador.await();}
                catch (InterruptedException e)
                { e.printStackTrace(); }

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        configuraMultiViewAdapter();
                    }
                });
            }
        });

       thread1.start();
    }

    private void btNovaViagemOnClick()
    {

        buttonChamarMotorista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //
                return false;
            }
        });

        // Envento de clique do botão "Chamar Motorista"
        buttonChamarMotorista.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // Pegando as String de Origem e destino
                String enderecoOrigem = editOrigem.getText().toString();
                String enderecoDestino = editDestino.getText().toString();

                //pegando as coordenadas de origem
                if (enderecoOrigem.equals("") || enderecoOrigem == null)
                {
                    Address addressOrigem = null;

                        if(localizacaoAtual != null)
                        {
                            try
                            { addressOrigem = recuperaEnderecoViaLocation(localizacaoAtual); }
                            catch (IOException e)
                            { e.printStackTrace();}

                            if(addressOrigem != null)
                            {
                                localOrigem = new Local();
                                localOrigem = addressToLocal(addressOrigem,Local.ORIGEM);
                            }

                        }
                        else
                        {
                            toastThis(" Aguarde o aplicativo recuperar a sua Localização");
                        }
                }
                else
                {
                    //Address addressOrigem = recuperaEnderecoViaString(enderecoOrigem);
                    Address addressOrigem = recuperaEnderecoViaStringDebug(enderecoOrigem);
                    localOrigem = new Local();
                    localOrigem = addressToLocal(addressOrigem,Local.ORIGEM);
                }

                //pegando as coordenadas do destino
                if (!enderecoDestino.equals("") || enderecoDestino != null)
                {
                    //Address addressDestino = recuperaEnderecoViaString(enderecoDestino);
                    Address addressDestino = recuperaEnderecoViaStringDebug(enderecoDestino);

                    if (addressDestino != null)
                    {
                        localDestino = new Local();
                        localDestino = addressToLocal(addressDestino, Local.DESTINO);

                        exibirDialogOrigemDestino();
                    }

                }
                else
                {
                    // não esta aparecendo a mensagem
                    Toast.makeText(getActivity(),"Informe o endereço de destino!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void exibirDialogOrigemDestino()
    {
        TextView ruaOrigem,numOrigem,bairroOrigem,cidadeOrigem,estadoOrigem,cepOrigem;
        TextView ruaDestino,numDestino,bairroDestino,cidadeDestino,estadoDestino,cepDestino;
        Button btConfirmar,btCancelar;

        dialogOrigemDestino = new Dialog(requireActivity());
        dialogOrigemDestino.setContentView(R.layout.dialog_origem_destino);

        //findviews Origem
        ruaOrigem = dialogOrigemDestino.findViewById(R.id.tv_ruaOrigem);
        numOrigem = dialogOrigemDestino.findViewById(R.id.tv_numeroOrigem);
        bairroOrigem = dialogOrigemDestino.findViewById(R.id.tv_bairroOrigem);
        cidadeOrigem = dialogOrigemDestino.findViewById(R.id.tv_cidadeOrigem);
        estadoOrigem = dialogOrigemDestino.findViewById(R.id.tv_estadoOrigem);
        cepOrigem = dialogOrigemDestino.findViewById(R.id.tv_cepOrigem);

        //findviews Destino
        ruaDestino = dialogOrigemDestino.findViewById(R.id.tv_ruaDestino);
        numDestino = dialogOrigemDestino.findViewById(R.id.tv_numeroDestino);
        bairroDestino = dialogOrigemDestino.findViewById(R.id.tv_bairroDestino);
        cidadeDestino = dialogOrigemDestino.findViewById(R.id.tv_cidadeDestino);
        estadoDestino = dialogOrigemDestino.findViewById(R.id.tv_estadoDestino);
        cepDestino = dialogOrigemDestino.findViewById(R.id.tv_cepDestino);

        btConfirmar = dialogOrigemDestino.findViewById(R.id.bt_confirmDialog_origemDestino);
        btCancelar = dialogOrigemDestino.findViewById(R.id.bt_cancelDialog_origemDestino);

        //SetText Origem
        ruaOrigem.setText(localOrigem.getRua());
        cidadeOrigem.setText("Cidade: " + localOrigem.getCidade());
        estadoOrigem.setVisibility(View.GONE);

        if(localOrigem.getNumero() ==  null || localOrigem.getNumero().equals(localOrigem.getRua()))
        {numOrigem.setVisibility(View.GONE);}
        else {numOrigem.setText("Número: " + localOrigem.getNumero());}

        if(localOrigem.getBairro() ==  null)
        {bairroOrigem.setVisibility(View.GONE);}
        else {bairroOrigem.setText("Bairro: " + localOrigem.getBairro());}

        if(localOrigem.getCep() ==  null)
        {cepOrigem.setVisibility(View.GONE);}
        else {cepOrigem.setText("Cep: " + localOrigem.getCep());}

        //SetText Destino
        ruaDestino.setText(localDestino.getRua());
        cidadeDestino.setText("Cidade: " + localDestino.getCidade());
        estadoDestino.setVisibility(View.GONE);

        if(localDestino.getNumero() ==  null || localDestino.getNumero().equals(localDestino.getRua()))
        {numDestino.setVisibility(View.GONE);}
        else {numDestino.setText("Número: " + localDestino.getNumero());}

        if(localDestino.getBairro() ==  null)
        {bairroDestino.setVisibility(View.GONE);}
        else {bairroDestino.setText("Bairro: " + localDestino.getBairro());}

        if(localDestino.getCep() ==  null)
        {cepDestino.setVisibility(View.GONE);}
        else {cepDestino.setText("Cep: " + localDestino.getCep());}

        btCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogOrigemDestino.dismiss();
            }
        });

        btConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogOrigemDestino.dismiss();
                exibirBottomSheet();
            }
        });

        dialogOrigemDestino.setCanceledOnTouchOutside(false);
        dialogOrigemDestino.show();
    }

    private void exibirDialogBuscarMotorista()
    {
        dialogBuscarMotorista = new Dialog(requireActivity());
        dialogBuscarMotorista.setContentView(R.layout.dialog_buscar_motorista);

        Button btCancelarViagem = dialogBuscarMotorista.findViewById(R.id.bt_cancelDialog_buscarMotorista);

        btCancelarViagem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogBuscarMotorista.dismiss();
                configTela(1);
            }
        });

        dialogBuscarMotorista.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                configTela(1);
            }
        });
        dialogBuscarMotorista.setCanceledOnTouchOutside(false);
        dialogBuscarMotorista.show();
    }

    public Address recuperaEnderecoViaString(String endereco)
    {
        //Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // Recuperando dados baseado no endereço do usuário
        try {
            // Cria lista endereço
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 10);
            //List<Address> listaEnderecos = geocoder.getFromLocationName(endereco,5,-23.047354,-42.233379,-22.727898,-41.855037);
            //Verificando se a lista não está vazia
            if (listaEnderecos != null && ((List) listaEnderecos).size() > 0)
            {
                // pega o primeiro endereço
                Address address = listaEnderecos.get(0);
                //Address address = DestinoMaisProximo(listaEnderecos);
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Address recuperaEnderecoViaStringDebug(String qualquerString)
    {

        Address newAddres = new Address(Locale.getDefault());
        newAddres.setThoroughfare("Rua Almirante Barroso");
        newAddres.setSubLocality("Passagem");
        newAddres.setSubAdminArea("Cabo Frio");
        newAddres.setPostalCode("28905020");
        newAddres.setFeatureName("360");
        newAddres.setLatitude(-22.876962);
        newAddres.setLongitude(-42.008073);

            return newAddres;
    }

    private Address recuperaEnderecoViaLocation(Location location) throws IOException
    {
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getActivity());
        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        if (addresses.size() > 0)
        {
            address = addresses.get(0);
        }
        return address;
    }

    private Local addressToLocal(Address address, String tipo)
    {
        Local local = new Local();
        local.setRua(address.getThoroughfare());
        local.setBairro(address.getSubLocality());
        local.setCidade(address.getSubAdminArea());
        local.setCep(address.getPostalCode());
        local.setNumero(address.getFeatureName());
        local.setTipoLocal(tipo);
        local.setLatitude(address.getLatitude());
        local.setLongitude(address.getLongitude());

        return local;
    }

    private double calcularDistancia(Location origem, Location destino)
    {
        double distancia =0;
        distancia = origem.distanceTo(destino);

        return distancia;
    }

    public float contarDistanciadoUsuario (Address destino)
    {
        float distancia = 0;
        Location pontoA;

        pontoA = new Location("Local Inicial");
        pontoA.setLatitude(destino.getLatitude());
        pontoA.setLongitude(destino.getLongitude());

        distancia = localizacaoAtual.distanceTo(pontoA);

        return distancia;
    }

    private void configuraMultiViewAdapter()
    {
        //cria Adapter
        adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new AnimalBinder(getActivity()));

        //Instancia o ListSection para incluir listaAnimais
        listSection = new ListSection<>();
        listSection.setSelectionMode(Mode.MULTIPLE);
        listSection.addAll(listaAnimais);

        //adiciona o ListSection
        adapter.addSection(listSection);
        adapter.setSelectionMode(Mode.MULTIPLE);

        recyclerBs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    private void configuraBottomSheet()
    {
        bsDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
        bsView = requireActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_donoanimal, null);
        btSelecionarAnimais = bsView.findViewById(R.id.bt_bs_donoanimal);
        recyclerBs = bsView.findViewById(R.id.recycler_bs_donoanimal);

        //configura o Recycler da bottomSheet
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerBs.setLayoutManager(layoutManager);
        recyclerBs.setHasFixedSize(true);

        //configura o Botao Selecionar
        btSelecionarAnimais.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listaAnimaisSelecionados = new ArrayList<>();
                listaAnimaisSelecionados = (ArrayList<Animal>) listSection.getSelectedItems();

                viagem = new Viagem();
                viagem.setIdDonoAnimal(meuPerfil.getIdUsuario());
                viagem.setFotoDonoAnimalUrl(meuPerfil.getFotoPerfilUrl());
                viagem.setNomeDonoAnimal(meuPerfil.getNome());

                switch (listaAnimaisSelecionados.size())
                {
                    case 0 :
                    {
                        System.out.println("Selecione ao menos um animal para a viagem ");
                        toastThis("Selecione ao menos um animal para a viagem ");
                    }break;

                    case 1 :
                    {
                        viagem.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagem.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagem.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagem.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagem.setCusto(10.50);
                        viagem.setData("14/06/2020");
                        viagem.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagem.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (1 Animal)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }break;

                    case 2 :
                    {
                        viagem.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagem.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagem.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagem.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagem.setIdAnimal2(listaAnimais.get(1).getIdAnimal());
                        viagem.setFotoAnimalUrl2(listaAnimais.get(1).getFotoAnimalUrl());
                        viagem.setNomeAnimal2(listaAnimais.get(1).getNomeAnimal());
                        viagem.setObservacaoAnimal2(listaAnimais.get(1).getObservacaoAnimal());

                        viagem.setCusto(10.50);
                        viagem.setData("14/06/2020");
                        viagem.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagem.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (2 Animais)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }break;

                    case 3 :
                    {
                        viagem.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagem.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagem.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagem.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagem.setIdAnimal2(listaAnimais.get(1).getIdAnimal());
                        viagem.setFotoAnimalUrl2(listaAnimais.get(1).getFotoAnimalUrl());
                        viagem.setNomeAnimal2(listaAnimais.get(1).getNomeAnimal());
                        viagem.setObservacaoAnimal2(listaAnimais.get(1).getObservacaoAnimal());

                        viagem.setIdAnimal3(listaAnimais.get(2).getIdAnimal());
                        viagem.setFotoAnimalUrl3(listaAnimais.get(2).getFotoAnimalUrl());
                        viagem.setNomeAnimal3(listaAnimais.get(2).getNomeAnimal());
                        viagem.setObservacaoAnimal3(listaAnimais.get(2).getObservacaoAnimal());

                        viagem.setCusto(10.50);
                        viagem.setData("14/06/2020");
                        viagem.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagem.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (3 Animais)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }break;

                    default:
                    {
                        System.out.println("Você deve selecionar no máximo 3 Animais");
                        toastThis("Você deve selecionar no máximo 3 Animais");
                    }break;
                }
            }
        });

        bsDialog.setContentView(bsView);
    }

    private void IniciarPerfil()
        {
            threadIniciarPerfil = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    contador = new CountDownLatch(1);
                    meuPerfil = donoAnimalDAO.receberPerfil(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()),contador);

                    try { contador.await();}
                    catch (InterruptedException e) { e.printStackTrace(); }

                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            showInTerminal("donoAnimal Recebido :" + meuPerfil.getNome());
                        }
                    });
                }
            });
            threadIniciarPerfil.start();
        }

    private void threadPrepararViagem()
    {
        Thread prepViagem = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                localOrigem.setIdLocal(LocalDAO.gerarPushKeyIdLocal());
                localDAO.salvarLocal(localOrigem,contador);

                try { contador.await();}
                catch (InterruptedException e) { e.printStackTrace(); }

                contador = new CountDownLatch(1);
                localDestino.setIdLocal(LocalDAO.gerarPushKeyIdLocal());
                localDAO.salvarLocal(localDestino,contador);

                try { contador.await();}
                catch (InterruptedException e) { e.printStackTrace(); }

                contador = new CountDownLatch(1);
                viagem.setIdViagem(ViagemDAO.gerarPushKeyIdViagem());
                viagem.setIdOrigem(localOrigem.getIdLocal());
                viagem.setIdDestino(localDestino.getIdLocal());
                viagemDAO.salvarViagem(viagem,contador);

                try { contador.await();}
                catch (InterruptedException e) { e.printStackTrace(); }

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        configTela(3);
                        exibirDialogBuscarMotorista();
                        threadMotoristasDisponiveis();
                    }
                });
            }
        });
        prepViagem.start();
    }


    private void threadMotoristasDisponiveis ()
    {
        threadMotoristasDisponiveis = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final DisponibilidadeMotorista motorista ;
                contador = new CountDownLatch(1);
                showInTerminal("executando queryMotorista");
                motorista = disponibilidadeMotoristaDao.queryMotoristaDisponivel(localOrigem, contador,5000 , listaAnimaisSelecionados,motoristaCancelados);

                try { contador.await(); }
                catch (InterruptedException e) { e.printStackTrace();}

                if (motorista != null)
                {
                    contador = new CountDownLatch(1);
                    viagem.setIdMotorista(motorista.getIdMotorista());
                    showInTerminal("adicionando motorista à viagem ");
                    viagemDAO.salvarViagem(viagem,contador);
                }
                else
                {
                    showInTerminal("nenhum motorista encontrado");
                 }

                try { contador.await(); }
                catch (InterruptedException e) { e.printStackTrace();}

                requireActivity().runOnUiThread(new Runnable()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run()
                    {

                        if (motorista != null)
                        {
                            TextView tvBuscarMotorista = dialogBuscarMotorista.findViewById(R.id.tv_dialog_buscarMotorista);
                            tvBuscarMotorista.setText("Aguardando Confirmação...");
                            addListenerAguardarMotorista(motorista);
                        }
                        else
                        {
                            toastThis("Motorista Não encontrado");
                        }

                    }
                });
            }
        });
        threadMotoristasDisponiveis.start();
    }


    private void addListenerAguardarMotorista(DisponibilidadeMotorista motorista)
    {
        motoristaDisponivelReferencia = disponibilidadeMotoristaDao.receberDisponibilidaReferencia(motorista);
        motoristaDisponivelReferencia.addChildEventListener( listenerAguardandoMotorista = new ChildEventListener()
        {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        if("disponibilidade".equals(dataSnapshot.getKey()))
                        {
                            String disponibilidade = dataSnapshot.getValue(String.class);

                            if (disponibilidade != null)
                            {
                                switch (disponibilidade)
                                {
                                    case DisponibilidadeMotorista.EM_VIAGEM:
                                    {
                                        dialogBuscarMotorista.dismiss();
                                        configTela(3);
                                        toastThis("Viagem Aceita");
                                    }break;

                                    case DisponibilidadeMotorista.DISPONIVEL:
                                    {
                                        dialogBuscarMotorista.dismiss();
                                        toastThis("viagem Recusada");
                                    }break;

                                    case DisponibilidadeMotorista.INDISPONIVEL: {}break;

                                    default: {}break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
    }


    private void configTela(int config)
    {
        //1 - Tela Inicial (pesquisa de endereço + botao chamar Motorista)
        //2 - Tela esperando motorista
        //3 - Tela em viagem
        //4 - Tela Avaliação

        switch (config)
        {
            case 1 :
                {
                    buttonChamarMotorista.setVisibility(View.VISIBLE);
                    linearOrigemDestino.setVisibility(View.VISIBLE);
                }break;

            case 2 : {}break;
            case 3 :
            case 4 :
                {
                    buttonChamarMotorista.setVisibility(View.GONE);
                    linearOrigemDestino.setVisibility(View.GONE);
                }break;
            default: {}break;
        }
    }

    private void toastThis (String mensagem)
    {
        Toast.makeText(getActivity(),mensagem,Toast.LENGTH_SHORT).show();
    }

    private void showInTerminal (String mensagem)
    {
        System.out.println(mensagem);
    }

}//fim do fragment
