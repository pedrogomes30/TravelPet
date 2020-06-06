package com.example.travelpet.controlller.perfil.motorista.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

import com.example.travelpet.R;
import com.example.travelpet.adapter.VeiculoBinder;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.helper.ConfiguracaoFirebase;
import com.example.travelpet.helper.UsuarioFirebase;
import com.example.travelpet.model.Veiculo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MapMotoristaFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap gMap;
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FloatingActionButton fab;
    private BottomSheetDialog bsDialog;
    private View bsView;
    private LinearLayout linearLayout;
    private MultiViewAdapter adapter;
    private ArrayList<Veiculo> listaVeiculos = new ArrayList<Veiculo>();
    private List<Veiculo> veiculos= new ArrayList<Veiculo>();
    private ValueEventListener valueEventListenerListaVeiculos;
    private RecyclerView recyclerBS;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference referenciaVeiculos;

    public MapMotoristaFragment() {}

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_motorista, container, false);

        //Referencia dos veiculos
        referenciaVeiculos = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        //Setar Fab
        fab = view.findViewById(R.id.fab_disponibilidade);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.about_youtube_color))); //Funciona!!!
        fabOnClick();

        //Setando o BottomSheet
        bsDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
        bsView = getActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_motorista,null);
        bsDialog.setContentView(bsView);

        //Setando RecyclerView do BottomSheet
        recyclerBS = (RecyclerView) bsView.findViewById(R.id.recycler_bs_motorista);
        //adapter = new ListaVeiculosAdapter(getActivity(),listaVeiculos);

        // MultiviewAdapter
        adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new VeiculoBinder());
        ListSection<Veiculo> listSection = new ListSection<>();
        listSection.add(testeVeiculo("1","1","Fusca","Volkswagem","KNF-9812","1990","1"));
        listSection.add(testeVeiculo("1","1","Astra","Volkswagem","GFC-1597","2010","1"));
        listSection.add(testeVeiculo("1","1","Onix","Chevrolet","PLW-7927","2020","1"));
        //listSection.addAll(veiculos);
        listSection.setSelectionMode(Mode.SINGLE);
        adapter.addSection(listSection);
        adapter.setSelectionMode(Mode.SINGLE);
        //toastThis(String.valueOf(adapter.getItemCount()));

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBS.setLayoutManager(layoutManager);
        recyclerBS.setHasFixedSize(true);
        recyclerBS.setAdapter(adapter);


        //linearLayout = new LinearLayout(getActivity().getApplicationContext());
        //linearLayout.findViewById(R.id.bottom_sheet_motorista_container);
        //bsView = inflater.inflate(R.layout.layout_bottom_sheet_motorista,linearLayout);


        //Criando Mapa
        mapView =  view.findViewById(R.id.mapMotorista);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        iniciarMapa();

        return view;
    }

    private void fabOnClick()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBottomSheet();
            }
        });
    }

    private void mostrarBottomSheet()
    {
        bsView.findViewById(R.id.bt_bs_motorista).setOnClickListener
                (new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Clicando", Toast.LENGTH_SHORT).show();
                        bsDialog.dismiss();
                    }
                }
                );
        bsDialog.show();
    }


    public void iniciarMapa ()
    {
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

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;

        recuperarLocalizacaoUsuario();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarVeiculos();
    }

    public void recuperarLocalizacaoUsuario ()
    {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng localMotorista = new LatLng(latitude, longitude);

                gMap.clear();
                gMap.addMarker
                        (
                            new MarkerOptions()
                            .position(localMotorista)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro))
                        );
                gMap.moveCamera
                        (
                            CameraUpdateFactory.newLatLngZoom(localMotorista,15)
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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates
                    (
                            LocationManager.GPS_PROVIDER,
                            10000, //tempo mínimo para atualização de localização (milisegundos)
                            10, //distância mínima para atualização de localização (metros)
                            locationListener
                    );
        }
    }

    public void recuperarVeiculos()
    {
        {

           valueEventListenerListaVeiculos = referenciaVeiculos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Veiculo veiculo = dados.getValue(Veiculo.class);
                        veiculos.add(veiculo);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public Veiculo testeVeiculo(String idUsuario, String idVeiculo, String modelo, String marca, String placa, String ano, String Crvl)
    {
        Veiculo veiculo = new Veiculo();
        veiculo.setIdUsuario(idUsuario);
        veiculo.setIdVeiculo(idVeiculo);
        veiculo.setModeloVeiculo(modelo);
        veiculo.setMarcaVeiculo(marca);
        veiculo.setPlacaVeiculo(placa);
        veiculo.setAnoVeiculo(ano);
        veiculo.setCrvlVeiculo(Crvl);

        return veiculo;
    }

    public void toastThis(String mensagem)
    {
        Toast.makeText(getActivity().getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
    }
}
