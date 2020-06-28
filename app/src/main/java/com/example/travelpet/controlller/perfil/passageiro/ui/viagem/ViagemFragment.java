package com.example.travelpet.controlller.perfil.passageiro.ui.viagem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.example.travelpet.dao.ConfiguracaoFirebase;
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

    private View bsView;
    private BottomSheetDialog bsDialog;
    private Dialog dialogBuscarMotorista;
    private MultiViewAdapter adapter;
    private RecyclerView recyclerBs;
    private ListSection<Animal> listSection;
    private ArrayList<Animal> listaAnimais = new ArrayList<>();
    private ArrayList<Animal> listaAnimaisSelecionados;
    private ArrayList<String> motoristaCancelados = new ArrayList<>();
    private Dialog dialogOrigemDestino;

    private CountDownLatch contador;
    private AnimalDAO animalDAO;
    private LocalDAO localDAO;
    private ViagemDAO viagemDAO;
    private DonoAnimalDAO donoAnimalDAO;
    private DisponibilidadeMotoristaDao disponibilidadeMotoristaDao;



    private DisponibilidadeMotorista motoristaDisponivel;
    private Viagem viagemAtual;
    private LinearLayout linearOrigemDestino;

    private DonoAnimal meuPerfil;


    // Variáveis para recuperar localização de um usuário
    private Local localOrigem, localDestino;
    private Location locationAtual, locationMotorista;
    private LocationCallback donoAnimalLocationCallback, motoristaLocationCallback;
    private Address addressDestino;
    private Geocoder geocoder;
    private GoogleMap gMap;
    private MapView mapView;
    private MarkerOptions marcadorDonoAnimal, marcadorMotorista, marcadorLocalEmbarque;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder builderlocationsSettingsRequest;
    private SettingsClient settingsClient;
    private LatLng localPassageiro;

    private ChildEventListener listenerStatusViagem, listenerAguardandoMotorista;
    private DatabaseReference motoristaDisponivelReferencia, confirmacaoViagemReferencia;

    private EditText editDestino, editOrigem;
    private Button buttonChamarMotorista, btSelecionarAnimais;


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

        // Criando mapa
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        mapView = view.findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        IniciarPerfil();
        iniciarMapa();
        btNovaViagemOnClick();
        return view;
    }

    private void iniciarMapa() {
        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        recuperarLocalizacaoUsuario();
    }

    private void recuperarLocalizacaoUsuario() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    localPassageiro = new LatLng(latitude, longitude);
                    locationAtual = location;

                    gMap.clear();
                    gMap.addMarker(new MarkerOptions().position(localPassageiro).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario_passageiro)));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localPassageiro, 19));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showInTerminal("<< Falha ao receber a Localização >>");
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 1000);// intervalo de tempo para buscar localizacao em milisegundos;
        locationRequest.setFastestInterval(2 * 1000);// intervalo de tempo para receber localização de outros apps que estejam utilizando o mesmo recurso
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        builderlocationsSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        settingsClient = LocationServices.getSettingsClient(requireActivity());
        settingsClient.checkLocationSettings(builderlocationsSettingsRequest.build()).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(requireActivity(), 10);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        addLocationCallbackDonoAnimal();
    }

    private void exibirBottomSheet() {
        configuraBottomSheet();
        threadPopularAdapter();
        bsDialog.show();
    }

    private void btNovaViagemOnClick() {
        // Envento de clique do botão "Chamar Motorista"
        buttonChamarMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pegando as String de Origem e destino
                String enderecoOrigem = editOrigem.getText().toString();
                String enderecoDestino = editDestino.getText().toString();

                //pegando as coordenadas de origem
                if (enderecoOrigem.equals("")) {
                    Address addressOrigem = null;

                    if (locationAtual != null) {
                        try {
                            addressOrigem = recuperaEnderecoViaLocation(locationAtual);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addressOrigem != null) {
                            localOrigem = new Local();
                            localOrigem = addressToLocal(addressOrigem, Local.ORIGEM);
                        }

                    } else {
                        toastThis(" Aguarde o aplicativo recuperar a sua Localização");
                    }

                } else {
                    //Address addressOrigem = recuperaEnderecoViaString(enderecoOrigem);
                    Address addressOrigem = recuperaEnderecoViaStringDebug(enderecoOrigem);
                    localOrigem = new Local();
                    localOrigem = addressToLocal(addressOrigem, Local.ORIGEM);
                }

                //pegando as coordenadas do destino
                if (!enderecoDestino.equals("")) {
                    //Address addressDestino = recuperaEnderecoViaString(enderecoDestino);
                    Address addressDestino = recuperaEnderecoViaStringDebug(enderecoDestino);

                    if (addressDestino != null) {
                        localDestino = new Local();
                        localDestino = addressToLocal(addressDestino, Local.DESTINO);

                        exibirDialogOrigemDestino();
                    }

                } else {
                    // não esta aparecendo a mensagem
                    Toast.makeText(getActivity(), "Informe o endereço de destino!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void threadPopularAdapter() {
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                contador = new CountDownLatch(1);
                listaAnimais.clear();
                listaAnimais = animalDAO.receberListaAnimal(contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configuraMultiViewAdapter();
                    }
                });
            }
        });

        thread1.start();
    }

    private void configuraMultiViewAdapter() {
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


    @SuppressLint("SetTextI18n")
    private void exibirDialogOrigemDestino() {
        TextView ruaOrigem, numOrigem, bairroOrigem, cidadeOrigem, estadoOrigem, cepOrigem;
        TextView ruaDestino, numDestino, bairroDestino, cidadeDestino, estadoDestino, cepDestino;
        Button btConfirmar, btCancelar;

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

        if (localOrigem.getNumero() == null || localOrigem.getNumero().equals(localOrigem.getRua())) {
            numOrigem.setVisibility(View.GONE);
        } else {
            numOrigem.setText("Número: " + localOrigem.getNumero());
        }

        if (localOrigem.getBairro() == null) {
            bairroOrigem.setVisibility(View.GONE);
        } else {
            bairroOrigem.setText("Bairro: " + localOrigem.getBairro());
        }

        if (localOrigem.getCep() == null) {
            cepOrigem.setVisibility(View.GONE);
        } else {
            cepOrigem.setText("Cep: " + localOrigem.getCep());
        }

        //SetText Destino
        ruaDestino.setText(localDestino.getRua());
        cidadeDestino.setText("Cidade: " + localDestino.getCidade());
        estadoDestino.setVisibility(View.GONE);

        if (localDestino.getNumero() == null || localDestino.getNumero().equals(localDestino.getRua())) {
            numDestino.setVisibility(View.GONE);
        } else {
            numDestino.setText("Número: " + localDestino.getNumero());
        }

        if (localDestino.getBairro() == null) {
            bairroDestino.setVisibility(View.GONE);
        } else {
            bairroDestino.setText("Bairro: " + localDestino.getBairro());
        }

        if (localDestino.getCep() == null) {
            cepDestino.setVisibility(View.GONE);
        } else {
            cepDestino.setText("Cep: " + localDestino.getCep());
        }

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOrigemDestino.dismiss();
            }
        });

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOrigemDestino.dismiss();
                exibirBottomSheet();
            }
        });

        dialogOrigemDestino.setCanceledOnTouchOutside(false);
        dialogOrigemDestino.show();
    }

    private void exibirDialogBuscarMotorista() {
        dialogBuscarMotorista = new Dialog(requireActivity());
        dialogBuscarMotorista.setContentView(R.layout.dialog_buscar_motorista);

        Button btCancelarViagem = dialogBuscarMotorista.findViewById(R.id.bt_cancelDialog_buscarMotorista);

        btCancelarViagem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogBuscarMotorista.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface)
                    {
                        configTela(1);
                    }
                });
                dialogBuscarMotorista.dismiss();
                removeListenerMotoristaDisponivel(listenerStatusViagem);
                threadCancelarViagem();
            }
        });

        dialogBuscarMotorista.setCanceledOnTouchOutside(false);
        dialogBuscarMotorista.show();
    }

    public Address recuperaEnderecoViaString(String endereco) {
        //Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // Recuperando dados baseado no endereço do usuário
        try {
            // Cria lista endereço
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 10);
            //List<Address> listaEnderecos = geocoder.getFromLocationName(endereco,5,-23.047354,-42.233379,-22.727898,-41.855037);
            //Verificando se a lista não está vazia
            if (listaEnderecos != null && listaEnderecos.size() > 0) {
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

    private Address recuperaEnderecoViaStringDebug(String qualquerString) {
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

    private Address recuperaEnderecoViaLocation(Location location) throws IOException {
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getActivity());
        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        if (addresses.size() > 0) {
            address = addresses.get(0);
        }
        return address;
    }

    private Local addressToLocal(Address address, String tipo) {
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

    private double calcularDistancia(Location origem, Location destino) {
        double distancia = 0;
        distancia = origem.distanceTo(destino);

        return distancia;
    }

    public float contarDistanciadoUsuario(Address destino)
    {
        float distancia;
        Location pontoA;

        pontoA = new Location("Local Inicial");
        pontoA.setLatitude(destino.getLatitude());
        pontoA.setLongitude(destino.getLongitude());

        distancia = locationAtual.distanceTo(pontoA);

        return distancia;
    }

    @SuppressLint("InflateParams")
    private void configuraBottomSheet() {
        bsDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
        bsView = requireActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_donoanimal, null);
        btSelecionarAnimais = bsView.findViewById(R.id.bt_bs_donoanimal);
        recyclerBs = bsView.findViewById(R.id.recycler_bs_donoanimal);

        //configura o Recycler da bottomSheet
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerBs.setLayoutManager(layoutManager);
        recyclerBs.setHasFixedSize(true);

        //configura o Botao Selecionar
        btSelecionarAnimais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaAnimaisSelecionados = new ArrayList<>();
                listaAnimaisSelecionados = (ArrayList<Animal>) listSection.getSelectedItems();

                viagemAtual = new Viagem();
                viagemAtual.setIdDonoAnimal(meuPerfil.getIdUsuario());
                viagemAtual.setFotoDonoAnimalUrl(meuPerfil.getFotoPerfilUrl());
                viagemAtual.setNomeDonoAnimal(meuPerfil.getNome());

                switch (listaAnimaisSelecionados.size()) {
                    case 0: {
                        System.out.println("Selecione ao menos um animal para a viagem ");
                        toastThis("Selecione ao menos um animal para a viagem ");
                    }
                    break;

                    case 1: {
                        viagemAtual.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagemAtual.setCusto(10.50);
                        viagemAtual.setData("14/06/2020");
                        viagemAtual.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagemAtual.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (1 Animal)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }
                    break;

                    case 2: {
                        viagemAtual.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagemAtual.setIdAnimal2(listaAnimais.get(1).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl2(listaAnimais.get(1).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal2(listaAnimais.get(1).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal2(listaAnimais.get(1).getObservacaoAnimal());

                        viagemAtual.setCusto(10.50);
                        viagemAtual.setData("14/06/2020");
                        viagemAtual.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagemAtual.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (2 Animais)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }
                    break;

                    case 3: {
                        viagemAtual.setIdAnimal1(listaAnimais.get(0).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl1(listaAnimais.get(0).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal1(listaAnimais.get(0).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal1(listaAnimais.get(0).getObservacaoAnimal());

                        viagemAtual.setIdAnimal2(listaAnimais.get(1).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl2(listaAnimais.get(1).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal2(listaAnimais.get(1).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal2(listaAnimais.get(1).getObservacaoAnimal());

                        viagemAtual.setIdAnimal3(listaAnimais.get(2).getIdAnimal());
                        viagemAtual.setFotoAnimalUrl3(listaAnimais.get(2).getFotoAnimalUrl());
                        viagemAtual.setNomeAnimal3(listaAnimais.get(2).getNomeAnimal());
                        viagemAtual.setObservacaoAnimal3(listaAnimais.get(2).getObservacaoAnimal());

                        viagemAtual.setCusto(10.50);
                        viagemAtual.setData("14/06/2020");
                        viagemAtual.setDistancia(calcularDistancia(localOrigem.getLocation(), localDestino.getLocation()));
                        viagemAtual.setStatusViagem(Viagem.BUSCANDO_MOTORISTA);

                        toastThis("continuando a viagem (3 Animais)");
                        threadPrepararViagem();
                        bsDialog.dismiss();
                    }
                    break;

                    default: {
                        System.out.println("Você deve selecionar no máximo 3 Animais");
                        toastThis("Você deve selecionar no máximo 3 Animais");
                    }
                    break;
                }
            }
        });

        bsDialog.setContentView(bsView);
    }

    private void IniciarPerfil() {
        Thread threadIniciarPerfil = new Thread(new Runnable() {
            @Override
            public void run() {
                contador = new CountDownLatch(1);
                meuPerfil = donoAnimalDAO.receberPerfil(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()), contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInTerminal("donoAnimal Recebido :" + meuPerfil.getNome());
                    }
                });
            }
        });
        threadIniciarPerfil.start();
    }

    private void threadPrepararViagem() {
        Thread prepViagem = new Thread(new Runnable() {
            @Override
            public void run() {
                contador = new CountDownLatch(1);
                localOrigem.setIdLocal(LocalDAO.gerarPushKeyIdLocal());
                localDAO.salvarLocal(localOrigem, contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                contador = new CountDownLatch(1);
                localDestino.setIdLocal(LocalDAO.gerarPushKeyIdLocal());
                localDAO.salvarLocal(localDestino, contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                contador = new CountDownLatch(1);
                viagemAtual.setIdViagem(ViagemDAO.gerarPushKeyIdViagem());
                viagemAtual.setIdOrigem(localOrigem.getIdLocal());
                viagemAtual.setIdDestino(localDestino.getIdLocal());
                viagemDAO.salvarViagem(viagemAtual, contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configTela(3);
                        threadMotoristasDisponiveis();
                    }
                });
            }
        });
        prepViagem.start();
    }

    private void threadCancelarViagem() {
        Thread threadCancelarViagem = new Thread(new Runnable() {
            @Override
            public void run() {
                localDAO.excluirLocal(localOrigem);
                localOrigem = null;
                localDAO.excluirLocal(localDestino);
                localDestino = null;
                viagemDAO.excluirViagem(viagemAtual);
                viagemAtual = null;

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastThis("Viagem Cancelada");
                        configTela(1);
                    }
                });
            }
        });
        threadCancelarViagem.start();
    }

    private void threadMotoristasDisponiveis() {

        final int distanciaBuscarMotorista = 5*1000; //5km
        exibirDialogBuscarMotorista();

        Thread threadMotoristasDisponiveis = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                contador = new CountDownLatch(1);
                showInTerminal("executando queryMotorista");
                motoristaDisponivel = disponibilidadeMotoristaDao.queryMotoristaDisponivel(localOrigem, contador, distanciaBuscarMotorista, listaAnimaisSelecionados, motoristaCancelados);

                try {contador.await();} catch (InterruptedException e) {e.printStackTrace();}

                if (motoristaDisponivel != null)
                {
                    contador = new CountDownLatch(1);
                    viagemAtual.setIdMotorista(motoristaDisponivel.getIdMotorista());
                    showInTerminal("adicionando motorista à viagem ");
                    viagemDAO.salvarViagem(viagemAtual, contador); //atualizando
                }
                else
                {
                    showInTerminal("nenhum motorista encontrado");
                }

                try {contador.await();} catch (InterruptedException e) {e.printStackTrace();}

                requireActivity().runOnUiThread(new Runnable()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run()
                    {
                        if (motoristaDisponivel != null)
                        {
                            TextView tvBuscarMotorista = dialogBuscarMotorista.findViewById(R.id.tv_dialog_buscarMotorista);
                            tvBuscarMotorista.setText("Aguardando Confirmação...");
                            locationMotorista = new Location("localização Motorista");
                            locationMotorista.setLatitude(motoristaDisponivel.getLatitudeMotorista());
                            locationMotorista.setLongitude(motoristaDisponivel.getLongitudeMotorista());
                            //addListenerAguardarMotorista(motorista);
                            addListenerStatusViagem();
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


    private void addListenerStatusViagem()
    {
        showInTerminal("AddListenerConfirmacao de viagem ");
        confirmacaoViagemReferencia = ViagemDAO.getRootViagens().child(viagemAtual.getIdViagem());
        listenerStatusViagem = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                showInTerminal("AddListenerConfirmacao de viagem /OnchildChanged");
                showInTerminal("<<<Key = " + snapshot.getKey() + ">>>");
                showInTerminal("<<<childKey = " + snapshot.getKey() + ">>>");

                if(snapshot.getKey().equals("statusViagem"))
                {
                    String status = snapshot.getValue(String.class);

                    switch (status)
                    {
                        case Viagem.CANCELADA :
                            {
                                Toast.makeText(requireActivity(),"Viagem Cancelada pelo Motorista",Toast.LENGTH_LONG).show();
                                dialogBuscarMotorista.dismiss();
                                showInTerminal("Viagem cancelada pelo Motorista");
                                threadCancelarViagem();
                            }break;

                        case Viagem.AGUARDANDO_MOTORISTA :
                        {
                            showInTerminal("Aguarde o motorista no local de Embarque");
                            dialogBuscarMotorista.dismiss();
                            configTela(2);
                            addListenerAguardandoMotorista();
                            Toast.makeText(requireActivity(),"Aguarde o motorista no local de Embarque",Toast.LENGTH_LONG).show();

                        }break;

                        case Viagem.EM_ANDAMENTO :
                            {
                                Toast.makeText(requireActivity(),"Viagem até o destino Iniciada",Toast.LENGTH_LONG).show();
                                showInTerminal("Viagem até o destino iniciada!!");
                                configTela(4);
                                //listener
                            }break;

                        case Viagem.FINALIZADA :
                        {
                            Toast.makeText(requireActivity(), "Viagem Finalizada", Toast.LENGTH_LONG).show();
                            //mostra tela de Avaliacao
                        }
                        default:{}break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        confirmacaoViagemReferencia.addChildEventListener(listenerStatusViagem);
    }

    private void addListenerAguardandoMotorista()
    {
        showInTerminal("addListenerAguardando Motorista " + motoristaDisponivel.getIdMotorista());
        removeLocationCallback(donoAnimalLocationCallback);
        addLocationCallbackMotorista();

        motoristaDisponivelReferencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("disponibilidadeMotorista").child(motoristaDisponivel.getIdMotorista());

        listenerAguardandoMotorista = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if(snapshot.getKey().equals("latitudeMotorista"))
                {
                    showInTerminal("<<<<<<<<<<<<<<<<<< EXECUTOU >>>>>>>>>>>>>>>>>>>>");
                    if (snapshot.getValue(double.class) != null)
                    {
                        showInTerminal("<<<<<<<<<<<<<<<<<< LATITUDE PEGA >>>>>>>>>>>>>>>>>>>>");
                        motoristaDisponivel.setLatitudeMotorista(snapshot.getValue(double.class));
                    }
                }

                if(snapshot.getKey().equals("longitudeMotorista"))
                {
                    showInTerminal("<<<<<<<<<<<<<<<<<< EXECUTOU >>>>>>>>>>>>>>>>>>>>");
                    if (snapshot.getValue(double.class) != null)
                    {
                        showInTerminal("<<<<<<<<<<<<<<<<<< LONGITUDE PEGA >>>>>>>>>>>>>>>>>>>>");
                        motoristaDisponivel.setLongitudeMotorista(snapshot.getValue(double.class));
                    }

                }
            }

            @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        motoristaDisponivelReferencia.addChildEventListener(listenerAguardandoMotorista);
    }

    private void addLocationCallbackDonoAnimal ()
    {
       donoAnimalLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            showInTerminal("<< onLocationResult : ok >>");

            Location location = locationResult.getLastLocation();

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            localPassageiro = new LatLng(latitude, longitude);
            locationAtual = location;

            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(localPassageiro).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario_passageiro)));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localPassageiro, 19));
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
        }
    };
        client.requestLocationUpdates(locationRequest, donoAnimalLocationCallback, null);
    }

    private void addLocationCallbackMotorista ()
    {
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        motoristaLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Location location =  locationResult.getLastLocation();
                gMap.clear();

                localPassageiro = new LatLng(location.getLatitude(),location.getLongitude());
                LatLng localMotorista = new LatLng(motoristaDisponivel.getLatitudeMotorista(),motoristaDisponivel.getLongitudeMotorista());
                LatLng localEmbarque = new LatLng(localOrigem.getLatitude(), localOrigem.getLongitude());

                marcadorMotorista = new MarkerOptions().position(localMotorista).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro));
                marcadorDonoAnimal = new MarkerOptions().position(localPassageiro).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario_passageiro));
                marcadorLocalEmbarque = new MarkerOptions().position(localEmbarque).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario));

                gMap.addMarker(marcadorDonoAnimal);
                gMap.addMarker(marcadorMotorista);
                gMap.addMarker(marcadorLocalEmbarque);

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localPassageiro,19));
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {}
        };
        client.requestLocationUpdates(locationRequest,motoristaLocationCallback,null);
    }

    private void removeLocationCallback(LocationCallback callback)
    {
        if(client != null)
        {
            client.removeLocationUpdates(callback);
        }
    }

    private void removeListenerMotoristaDisponivel(ChildEventListener childListener)
    {
        if (motoristaDisponivelReferencia != null)
        {
            motoristaDisponivelReferencia.removeEventListener(childListener);
        }
    }

    private void configTela(int config) {
        //1 - Tela Inicial (pesquisa de endereço + botao chamar Motorista)
        //2 - Tela esperando motorista
        //3 - Tela em viagem
        //4 - Tela Avaliação

        switch (config) {
            case 1:
                {
                    buttonChamarMotorista.setVisibility(View.VISIBLE);
                    linearOrigemDestino.setVisibility(View.VISIBLE);
                }
            break;

            case 2:
                {
                    buttonChamarMotorista.setVisibility(View.GONE);
                    linearOrigemDestino.setVisibility(View.GONE);
                    //mostra Textview Esperar o motorista no local combinado
                }
            break;
            case 3:
            case 4: {
                        buttonChamarMotorista.setVisibility(View.GONE);
                        linearOrigemDestino.setVisibility(View.GONE);
                    }
            break;
            default:
                {

                }
            break;
        }
    }

    private void toastThis(String mensagem) {
        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
    }

    private void showInTerminal(String mensagem) {
        System.out.println(mensagem);
    }





}
