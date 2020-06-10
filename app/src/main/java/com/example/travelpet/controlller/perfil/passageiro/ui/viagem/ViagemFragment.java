package com.example.travelpet.controlller.perfil.passageiro.ui.viagem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
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
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.Destino;
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
import java.util.List;
import java.util.Locale;

public class ViagemFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private MapView mapView;
    private BottomSheetDialog bsDialog;
    private View bsView;
    private MultiViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerBs;
    ListSection<Animal> listSection;


    // Variáveis para recuperar localização de um usuário
    private LocationManager locationManager;
    private LocationListener locationListener;

    private EditText editDestino;
    private Button buttonChamarMotorista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viagem, container, false);
        //return inflater.inflate(R.layout.fragment_viagem,container, false);
        buttonChamarMotorista = view.findViewById(R.id.buttonChamarMotorista);
        // Destino para onde vai
        editDestino = view.findViewById(R.id.editDestino);

        // Criando mapa
        mapView = (MapView) view.findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        //btChamarOnClick();
        setBottomSheet();
        btchamar();
        return view;
    }

    public void setBottomSheet()
    {
        //BottomSheet
        bsDialog    = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
        bsView      = getActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_donoanimal,null);
        recyclerBs = bsView.findViewById(R.id.recycler_bs_donoanimal);
        bsDialog.setContentView(bsView);

        //MultiViewAdapter
        adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new AnimalBinder());

        listSection = new ListSection<>();
        listSection.add(testeAnimal("1","1","PERNALONGA"));
        listSection.add(testeAnimal("2","2","FRAJOLA"));
        listSection.add(testeAnimal("3","3","JERRY"));
        listSection.add(testeAnimal("4","4","TOM"));
        listSection.setSelectionMode(Mode.MULTIPLE);

        adapter.addSection(listSection);
        adapter.setSelectionMode(Mode.MULTIPLE);

        layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerBs.setLayoutManager(layoutManager);
        recyclerBs.setHasFixedSize(true);
        recyclerBs.setAdapter(adapter);

    }

    public void btChamarOnClick()
    {
        // Envento de clique do botão "Chamar Motorista"
        buttonChamarMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pega o endereço digitado pelo usuário
                String enderecoDestino = editDestino.getText().toString();

                // Verifica se está vazio
                if( !enderecoDestino.equals("") || enderecoDestino != null ){
                    // Recuperando dados de Destino
                    Address addressDestino = recuperarEndereco( enderecoDestino );

                    if( addressDestino != null ){
                        // Destino = Classe de apoio criado na pasta "model"
                        final Destino destino = new Destino();

                        destino.setCidade( addressDestino.getAdminArea() );
                        destino.setCep( addressDestino.getPostalCode() );
                        destino.setBairro( addressDestino.getSubLocality() );
                        destino.setRua( addressDestino.getThoroughfare() );
                        destino.setNumero( addressDestino.getFeatureName() );
                        // String.valueof = transformando o valor double em String
                        destino.setLatitude( String.valueOf(addressDestino.getLatitude()) );
                        destino.setLongitude( String.valueOf(addressDestino.getLongitude()) );

                        // Mensagem para Confirmação do endereço
                        StringBuilder mensagem = new StringBuilder();
                        mensagem.append( "Cidade: " + destino.getCidade() );
                        mensagem.append( "\nRua: " + destino.getRua() );
                        mensagem.append( "\nBairro: " + destino.getBairro() );
                        mensagem.append( "\nNúmero: " + destino.getNumero() );
                        mensagem.append( "\nCep: " + destino.getCep() );

                        // Exibindo mensagem em caixa de dialogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("Confirme seu endereco!")
                                .setMessage(mensagem)
                                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }else { // não esta aparecendo a mensagem
                    Toast.makeText(getActivity(),
                            "Informe o endereço de destino!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btchamar()
    {
        buttonChamarMotorista.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bsDialog.show();
            }
        });

        bsView.findViewById(R.id.bt_bs_donoanimal).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                bsDialog.dismiss();
            }
        });

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("racaAnimal");

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //System.out.println("toString: " + dataSnapshot.toString());
                for (DataSnapshot dados : dataSnapshot.getChildren())
                {
                    System.out.println("getKey: "+ dados.getKey()); // pegou a especie
                    String teste = dados.child("iconeURL").getValue(String.class);//pega a imagem da especie

                    System.out.println("teste = " + teste);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

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

    public Address recuperarEndereco(String endereco)
    {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        // Recuperando dados baseado no endereço do usuário
        try {
            // Cria lista endereço
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco,1);
            // Verficando se a lista não está vazia
            if( listaEnderecos != null && ((List) listaEnderecos).size() > 0){
                // pega o primeiro endereço
                Address address = listaEnderecos.get(0);

                //double lat = address.getLatitude();
                //double lon = address.getLongitude();

                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método Recuperar Localização do Usuário 494
    public void recuperarLocalizacaoUsuario()
    {
        // LocationManager = tradução = gerente de localização= gerencia a localização
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        // locationListener = tradução = ouvinte de localização
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
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
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,  // tempo de resposta de atualização = 10 segundo
                    10,  // distância de atualização = 10 metros
                    locationListener
            );

        }

    }

    public Animal testeAnimal (String idUsuario, String idAnimal, String nomeAnimal)
    {
        Animal animal = new Animal();
        animal.setIdUsuario(idUsuario);
        animal.setIdAnimal(idAnimal);
        animal.setNomeAnimal(nomeAnimal);

        return animal;
    }
}
