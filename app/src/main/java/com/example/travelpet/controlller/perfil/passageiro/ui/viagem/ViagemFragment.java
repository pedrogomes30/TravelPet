package com.example.travelpet.controlller.perfil.passageiro.ui.viagem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.Local;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ViagemFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private MapView mapView;
    private BottomSheetDialog bsDialog;
    private Local localOrigem, localDestino;
    private View bsView;
    private MultiViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerBs;
    private ListSection<Animal> listSection;
    private ArrayList<Animal> listaAnimais = new ArrayList<>();
    private Dialog dialogOrigemDestino;
    private CountDownLatch contador;
    private AnimalDAO animalDAO;

    // Variáveis para recuperar localização de um usuário
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location localizacaoAtual;
    private Address addressDestino;

    private EditText editDestino, editOrigem;
    private Button buttonChamarMotorista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viagem, container, false);
        //return inflater.inflate(R.layout.fragment_viagem,container, false);
        buttonChamarMotorista = view.findViewById(R.id.buttonChamarMotorista);
        editDestino = view.findViewById(R.id.editDestino);
        editOrigem = view.findViewById(R.id.editOrigem);
        animalDAO = new AnimalDAO();

        // Criando mapa
        mapView = (MapView) view.findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        iniciarMapa();
        //btchamar();
        btNovaViagemOnClick();
        return view;
    }

    public void iniciarMapa() {
        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
    }

    public void exibirBottomSheet() {
        //BottomSheet

        bsDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        bsView = getActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_donoanimal, null);
        recyclerBs = bsView.findViewById(R.id.recycler_bs_donoanimal);
        bsDialog.setContentView(bsView);

        //MultiViewAdapter

        popularAdapter();
        //listSection = new ListSection<>();
        //listSection.add(testeAnimal("1", "1", "PERNALONGA"));
        //listSection.add(testeAnimal("2", "2", "FRAJOLA"));
        //listSection.add(testeAnimal("3", "3", "JERRY"));
        //listSection.add(testeAnimal("4", "4", "TOM"));
        //listSection.setSelectionMode(Mode.MULTIPLE);

        //adapter.addSection(listSection);
        //adapter.setSelectionMode(Mode.MULTIPLE);


        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerBs.setLayoutManager(layoutManager);
        recyclerBs.setHasFixedSize(true);

        bsDialog.show();

    }

    public ArrayList<Animal> getAnimais () throws ExecutionException, InterruptedException
    {
        final ArrayList<Animal> animais = new ArrayList<>();
        final DatabaseReference referenciaAnimais = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("animais")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        FutureTask<ArrayList<Animal>> receberAnimais = new FutureTask<>(new Callable<ArrayList<Animal>>()
        {
            @Override
            public ArrayList<Animal> call() throws Exception
            {
                final ArrayList<Animal>listaInterna = new ArrayList<>();


                referenciaAnimais.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot data : dataSnapshot.getChildren())
                        {
                            Animal animal = dataSnapshot.getValue(Animal.class);
                            listaInterna.add(animal);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

                return listaInterna;
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(receberAnimais);

        return (ArrayList<Animal>) receberAnimais.get();
    }

    public void popularAdapter()
    {

       final Thread thread1 =  new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                listaAnimais.clear();

                listaAnimais = animalDAO.receberListaAnimal(contador);

                /*

                DatabaseReference referenciaAnimais = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                        .child("animais")
                        .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

                System.out.println("referencia =" + referenciaAnimais.toString());

                referenciaAnimais.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot data : dataSnapshot.getChildren())
                        {
                            Animal animal = data.getValue(Animal.class);
                            listaAnimais.add(animal);
                        }
                        contador.countDown();
                        System.out.println("lista animais após o for: "+ listaAnimais.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        contador.countDown();
                    }

                });*/

                try
                {contador.await();}
                catch (InterruptedException e)
                { e.printStackTrace(); }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setMultiViewAdapter();
                    }
                });
            }
        });

       thread1.start();
    }

    public void btNovaViagemOnClick()
    {
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
                    try
                    {
                        addressOrigem = recuperaEnderecoViaLocation(localizacaoAtual);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    localOrigem = new Local();
                    localOrigem = addressToLocal(addressOrigem);
                }
                else
                {
                    Address addressOrigem = recuperaEnderecoViaString(enderecoOrigem);
                    localOrigem = new Local();
                    localOrigem = addressToLocal(addressOrigem);
                }

                //pegando as coordenadas do destino
                if (!enderecoDestino.equals("") || enderecoDestino != null)
                {
                    Address addressDestino = recuperaEnderecoViaString(enderecoDestino);

                    if (addressDestino != null)
                    {
                        localDestino = new Local();
                        localDestino = addressToLocal(addressDestino);

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

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;

        // Recuperar Localização do usuário - Aula 494
        recuperarLocalizacaoUsuario();

    }

    public void exibirDialogOrigemDestino()
    {
        TextView ruaOrigem,numOrigem,bairroOrigem,cidadeOrigem,estadoOrigem,cepOrigem;
        TextView ruaDestino,numDestino,bairroDestino,cidadeDestino,estadoDestino,cepDestino;
        Button btConfirmar,btCancelar;

        dialogOrigemDestino = new Dialog(getActivity());
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

        dialogOrigemDestino.show();

    }

    public Address recuperaEnderecoViaString(String endereco)
    {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // Recuperando dados baseado no endereço do usuário
        try {
            // Cria lista endereço
            //List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 10);
                List<Address> listaEnderecos = geocoder.getFromLocationName(endereco,5,-23.047354,-42.233379,-22.727898,-41.855037);
            // Verificando se a lista não está vazia
                if (listaEnderecos != null && ((List) listaEnderecos).size() > 0)
                {
                    // pega o primeiro endereço
                    //Address address = listaEnderecos.get(0);
                    Address address = DestinoMaisProximo(listaEnderecos);
                    return address;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Address recuperaEnderecoViaLocation(Location location) throws IOException
    {
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

    // Método Recuperar Localização do Usuário 494
    public void recuperarLocalizacaoUsuario()
    {
        // LocationManager = tradução = gerente de localização= gerencia a localização
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // locationListener = tradução = ouvinte de localização
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location)
            {
                //seta local do usuario
                localizacaoAtual = location;

                // Recuprar Latitude e Longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // definindo Local com LatLng, para colocar no position
                LatLng localPassageiro = new LatLng(latitude, longitude);

                // Método para limpar mapa, pois sempre que o usuário mudar o local ele
                // exibe um marcador só
                gMap.clear();

                // Definindo Marcador- Aula 494
                gMap.addMarker(
                        new MarkerOptions()
                                .position(localPassageiro) // posição
                                .title("Meu Local") // Titulo
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario_passageiro)) // Incone do Marcador
                );
                // Definindo Zoom no mapa- Aula 494
                gMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(localPassageiro, 15)
                );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // Solicitar atualizações de localização - Aula 494
        // if = caso tenha permissão para usar mapa então executa
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,  // tempo de resposta de atualização = 10 segundo
                    10,  // distância de atualização = 10 metros
                    locationListener
            );

        }

    }

    public void calcDistanciaLocais(LatLng origem, LatLng destino)
    {

    }

    public Local addressToLocal(Address address)
    {
        Local local = new Local();
        local.setRua(address.getThoroughfare());
        local.setBairro(address.getSubLocality());
        local.setCidade(address.getSubAdminArea());
        local.setCep(address.getPostalCode());
        local.setNumero(address.getFeatureName());

        local.setLatitude(String.valueOf(address.getLatitude()));
        local.setLongitude(String.valueOf(address.getLongitude()));

        return local;
    }

    public Animal testeAnimal(String idUsuario, String idAnimal, String nomeAnimal)
    {
        Animal animal = new Animal();
        animal.setIdUsuario(idUsuario);
        animal.setIdAnimal(idAnimal);
        animal.setNomeAnimal(nomeAnimal);

        return animal;
    }

    public Address DestinoMaisProximo(List<Address> listaenderecos)
    {

        float distancia = 0;
        int selecionado = 0;

        for (int i = 0; i < listaenderecos.size(); i++)
        {
            if (distancia == 0)
            {
                //inicio = enderecos.get(i);
                distancia = contarDistanciadoUsuario(listaenderecos.get(i));
                selecionado = i;
                System.out.println("Tamanho da lista = " + listaenderecos.size());
            }
            else
            {
                float distanciaDestino = contarDistanciadoUsuario(listaenderecos.get(i));
                if (distanciaDestino < distancia)
                {
                    System.out.println(String.valueOf(distancia) +" ou " + String.valueOf(distanciaDestino));
                    distancia = distanciaDestino;
                    selecionado = i;
                    System.out.println("selecionado =" + distancia);
                }
            }
        }
        return listaenderecos.get(selecionado);
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

    public void setMultiViewAdapter()
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

}//fim do fragment



