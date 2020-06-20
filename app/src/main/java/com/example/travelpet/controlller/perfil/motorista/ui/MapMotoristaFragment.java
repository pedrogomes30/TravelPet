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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

import com.example.travelpet.R;
import com.example.travelpet.adapter.VeiculoBinder;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DisponibilidadeMotoristaDao;
import com.example.travelpet.dao.LocalDAO;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.dao.ViagemDAO;
import com.example.travelpet.model.DisponibilidadeMotorista;
import com.example.travelpet.model.Local;
import com.example.travelpet.model.Veiculo;
import com.example.travelpet.model.Viagem;
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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class MapMotoristaFragment extends Fragment implements OnMapReadyCallback {
    
    private GoogleMap gMap;
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    //Threads
    private Thread threadAttDisponibilidade;
    private Thread checarDisponibilidade;
    private Thread threadPopulaAdapter;
    private CountDownLatch contador;

    //Daos
    private DisponibilidadeMotoristaDao disponibilidadeDAO;
    private VeiculoDAO veiculoDAO;
    private ViagemDAO viagemDAO;
    private LocalDAO localDAO;
    private AnimalDAO animalDAO;

    //BottomSheet
    private BottomSheetDialog bsDialog;
    private View bsView;
    private MultiViewAdapter adapter;
    private ListSection<Veiculo> veiculoListSection;
    private RecyclerView recyclerBS;
    private RecyclerView.LayoutManager layoutManager;

    //Outros
    private Local localPassageiro,localDestino;
    private Viagem viagemAtual;
    private Veiculo veiculoSelecionado;
    private DisponibilidadeMotorista disponibilidade;
    private CheckBox pq,med,grd;
    private ArrayList<Veiculo> listaVeiculos = new ArrayList<Veiculo>();
    private FloatingActionButton fab;
    private LatLng localMotorista;

    public MapMotoristaFragment() {}

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_motorista, container, false);

        //iniciandoDAOS
        disponibilidadeDAO =  new DisponibilidadeMotoristaDao();
        veiculoDAO = new VeiculoDAO();
        localDAO = new LocalDAO();
        animalDAO = new AnimalDAO();
        viagemDAO = new ViagemDAO();

        //Setar FindViews
        fab = view.findViewById(R.id.fab_disponibilidade);
        mapView =  view.findViewById(R.id.mapMotorista);

        //Criando Mapa

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        iniciarMapa();
        threadChecarDisponibilidade();

        return view;
    }
    //CONFIG TELA
    public void configTela (String disponibilidade)
    {
        // 1- Indisponível
        // 2- Disponível
        // 3- Em Viagem

        switch (disponibilidade)
        {
            case "indisponivel":
                {
                    configFab(disponibilidade);
                    configBs(disponibilidade);
                }break;

            case "disponivel":
                {
                    configFab(disponibilidade);
                    configBs(disponibilidade);
                }break;
            case "em_viagem":
                {
                    configFab(disponibilidade);
                }break;

            default:{}break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void configFab(String disponibilidade)
    {
        // 1- Indisponível
        // 2- Disponível
        // 3- Em Viagem

        switch (disponibilidade)
        {
            case "indisponivel":
                {
                    fab.setVisibility(View.VISIBLE);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.about_youtube_color)));
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                }break;

            case "disponivel":
                {
                    fab.setVisibility(View.VISIBLE);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));

                }break;

            case "em_viagem":
                {
                    fab.setVisibility(View.GONE);

                }break;

            default:{}break;

        }

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bsDialog.show();
                threadPopularAdapter();
            }
        });
    }
    
    private void configBs(final String dispConfig)
    {
        bsDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialogTheme);
        bsView = getActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_motorista,null);
        bsDialog.setContentView(bsView);

        //findViews
        recyclerBS = (RecyclerView) bsView.findViewById(R.id.recycler_bs_motorista);
        Button btbs1 = bsView.findViewById(R.id.bt_bs_motorista);
        Button btbs2 = bsView.findViewById(R.id.bt_bs_motorista2);
        pq = bsView.findViewById(R.id.checkbox_peq);
        med = bsView.findViewById(R.id.checkbox_med);
        grd = bsView.findViewById(R.id.checkbox_grd);

        pq.setChecked(false);
        med.setChecked(false);
        grd.setChecked(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBS.setLayoutManager(layoutManager);
        recyclerBS.setHasFixedSize(true);

        switch (dispConfig)
        {
            case "indisponivel":
            {
                btbs2.setVisibility(View.GONE);
                btbs1.setText("CONFIRMAR DISPONIBILIDADE");
                onClickBtConfirmar(btbs1);

            }break;

            case "disponivel":
            {
                btbs2.setVisibility(View.VISIBLE);
                btbs1.setText("ATUALIZAR");
                onClickBtConfirmar(btbs1);
                onClickBtCancelar(btbs2);

            }break;

            default: {}break;
        }
    }

    public void onClickBtCancelar (Button bt)
    {
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                disponibilidade.setDisponibilidade(DisponibilidadeMotorista.INDISPONIVEL);
                threadSalvarDisponibilidade();
                bsDialog.dismiss();
            }
        });
    }

    public void onClickBtConfirmar(Button bt)
    {
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(localMotorista != null)
                {
                    disponibilidade.setLatitudeMotorista(localMotorista.latitude);
                    disponibilidade.setLongitudeMotorista(localMotorista.longitude);

                    if ( pq.isChecked() || med.isChecked() || grd.isChecked())
                    {
                        disponibilidade.setPorteAnimalPequeno(String.valueOf(pq.isChecked()));
                        disponibilidade.setPorteAnimalMedio(String.valueOf(med.isChecked()));
                        disponibilidade.setPorteAnimalGrande(String.valueOf(grd.isChecked()));

                        if(veiculoListSection.getSelectedItems().size() > 0)
                        {
                            veiculoSelecionado  = veiculoListSection.getSelectedItems().get(0);

                            disponibilidade.setIdVeiculo(veiculoSelecionado.getIdVeiculo());
                            disponibilidade.setDisponibilidade(DisponibilidadeMotorista.DISPONIVEL);

                            threadSalvarDisponibilidade();
                            bsDialog.dismiss();
                        }
                        else
                        {
                            toastThis("Selecione ao menos um Veículo");
                        }

                    }
                    else
                    {
                        toastThis(" Selecione ao menos um porte de animal !!! ");
                    }
                }
                else
                {
                    toastThis("Aguarde o aplicativo recuperar a sua Localização");
                }
            }
        });
    }

    public void configuraMultiViewAdapter()
    {
        //cria adapter
        adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new VeiculoBinder());

        //Instancia o listSection
        veiculoListSection = new ListSection<>();
        veiculoListSection.setSelectionMode(Mode.SINGLE);
        veiculoListSection.addAll(listaVeiculos);

        //add o listSection ao adapter
        adapter.addSection(veiculoListSection);
        adapter.setSelectionMode(Mode.SINGLE);

        recyclerBS.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Threads
    public void threadPopularAdapter()
    {
        threadPopulaAdapter = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                listaVeiculos.clear();
                listaVeiculos = veiculoDAO.receberVeiculosLiberados(contador);

                try
                {contador.await();}
                catch (InterruptedException e)
                { e.printStackTrace();}

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        configuraMultiViewAdapter();
                    }
                });
            }
        });
        threadPopulaAdapter.start();
    }

    private void threadSalvarDisponibilidade()
    {
        threadAttDisponibilidade = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                disponibilidadeDAO.salvarDisponibilidade(disponibilidade, contador);

                try
                {contador.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        configTela(disponibilidade.getDisponibilidade());
                    }
                });
            }
        });

        threadAttDisponibilidade.start();

    }

    private void threadChecarDisponibilidade()
    {
        checarDisponibilidade = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                disponibilidade = new DisponibilidadeMotorista();
                disponibilidade = disponibilidadeDAO.receberDisponibilidade(contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        switch (disponibilidade.getDisponibilidade())
                        {
                            case "indisponivel":
                            {
                                configTela(disponibilidade.getDisponibilidade());
                            }break;

                            case "disponivel":
                            {
                                configTela(disponibilidade.getDisponibilidade());
                            }break;

                            case "em_viagem":
                            {
                                configTela(disponibilidade.getDisponibilidade());
                            }break;

                            default:{}break;
                        }
                    }
                });
            }
        });
        checarDisponibilidade.start();
    }

    //MAPA
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

    public void recuperarLocalizacaoUsuario ()
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                localMotorista = new LatLng(latitude, longitude);

                gMap.clear();
                gMap.addMarker
                        (
                            new MarkerOptions()
                            .position(localMotorista)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro))
                        );
                gMap.moveCamera
                        (
                            CameraUpdateFactory.newLatLngZoom(localMotorista,16)
                        );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {

            }

            @Override
            public void onProviderEnabled(String s)
            {

            }

            @Override
            public void onProviderDisabled(String s)
            {

            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates
                    (
                            LocationManager.GPS_PROVIDER,
                            2000, //tempo mínimo para atualização de localização (milisegundos)
                            10, //distância mínima para atualização de localização (metros)
                            locationListener
                    );
        }
    }

    public void toastThis(String mensagem)
    {
        Toast.makeText(getActivity().getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
    }
}
