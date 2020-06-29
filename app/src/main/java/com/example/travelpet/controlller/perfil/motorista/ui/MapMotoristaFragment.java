package com.example.travelpet.controlller.perfil.motorista.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.adapter.VeiculoBinder;
import com.example.travelpet.dao.DisponibilidadeMotoristaDao;
import com.example.travelpet.dao.LocalDAO;
import com.example.travelpet.dao.MotoristaDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.dao.ViagemDAO;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.DisponibilidadeMotorista;
import com.example.travelpet.model.Local;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Veiculo;
import com.example.travelpet.model.Viagem;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class MapMotoristaFragment extends Fragment implements OnMapReadyCallback {
    
    private GoogleMap gMap;
    private MapView mapView;
    private Marker marcadorPassageiro, marcadorDestino, marcadorMotorista;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private SettingsClient settingsClient;
    private LocationCallback callbackAcaminho;
    private LocationCallback callbackEmViagem;
    private LocationCallback callbackLocalizacaoMotorista;

    private CountDownLatch contador, contadorPerfil, contadorDisponibilidade;

    private ChildEventListener listenerViagem;

    //Daos e References
    private DatabaseReference viagensRef;
    private DisponibilidadeMotoristaDao disponibilidadeDAO;
    private MotoristaDAO motoristaDAO;
    private VeiculoDAO veiculoDAO;
    private ViagemDAO viagemDAO;
    private LocalDAO localDAO;

    //BottomSheet
    private BottomSheetDialog bsDialog;
    private View bsView;
    private MultiViewAdapter adapter;
    private ListSection<Veiculo> veiculoListSection;
    private RecyclerView recyclerBS;
    private RecyclerView.LayoutManager layoutManager;

    private Local localPassageiro,localDestino;
    private Dialog dialogSolicitacaoViagem;
    private Viagem viagemAtual;
    private LatLng localMotorista;
    private Button btInteragirViagem;

    //Outros
    private int contadorViagem;
    private int contadorListenerLocalizacao;
    private Motorista meuPerfil;

    private DisponibilidadeMotorista disponibilidade;
    private CheckBox pq,med,grd;
    private ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
    private Veiculo veiculoSelecionado;
    private FloatingActionButton fab;


    public MapMotoristaFragment() {}

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_motorista, container, false);

        //iniciandoDAOS
        motoristaDAO = new MotoristaDAO();
        disponibilidadeDAO =  new DisponibilidadeMotoristaDao();
        veiculoDAO = new VeiculoDAO();
        localDAO = new LocalDAO();
        viagemDAO = new ViagemDAO();

        //Setar FindViews
        fab = view.findViewById(R.id.fab_disponibilidade);
        btInteragirViagem = view.findViewById(R.id.btInteragirViagem);
        mapView =  view.findViewById(R.id.mapMotorista);

        //Criando Mapa
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        threadIniciarPerfil();
        threadRecuperarDisponibilidade();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        iniciarMapa();
    }

    private void threadIniciarPerfil()
    {
        showInTerminal("threadIniciarPerfil", "Iniciando a recuperação do Perfil do Motorista");
        Thread threadIniciarPerfil = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                meuPerfil = motoristaDAO.receberPerfil(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()),contador);

                try { contador.await();}
                catch (InterruptedException e) { e.printStackTrace(); }

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showInTerminal("threadIniciarPerfil/runOnUi...","Motorista Recebido :" + meuPerfil.getNome());
                    }
                });
            }
        });
        threadIniciarPerfil.start();
    }

    private void threadRecuperarDisponibilidade()
    {
        //Atualiza a tela
        showInTerminal("threadRecuperarDisponibilidade","recuperando a disponibilidade do motorista ");
        Thread threadRecuperarDisponibilidade = new Thread(new Runnable() {
            @Override
            public void run() {
                contadorDisponibilidade = new CountDownLatch(1);
                disponibilidade = disponibilidadeDAO.receberDisponibilidade(contadorDisponibilidade);

                try {contadorDisponibilidade.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                if(veiculoSelecionado == null)
                {
                    contadorDisponibilidade = new CountDownLatch(1);
                    veiculoSelecionado = veiculoDAO.getVeiculo(disponibilidade.getIdVeiculo(),disponibilidade.getIdMotorista(),contadorDisponibilidade);

                    try {contadorDisponibilidade.await();}
                    catch (InterruptedException e)
                    {e.printStackTrace();}
                }

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (disponibilidade != null)
                        {
                            //Atualiza a tela
                            configTela(disponibilidade.getDisponibilidade());
                            configListeners(disponibilidade.getDisponibilidade());
                        }
                    }
                });
            }
        });
        threadRecuperarDisponibilidade.start();
    }

    private void threadAtualizarDisponibilidade()
    {
        showInTerminal("threadSalvarDisponibilidade", "atualizando a disponibilidade do motorista");
        Thread threadAtualizarDisponibilidade = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                disponibilidadeDAO.salvarDisponibilidade(disponibilidade, contador);

                try
                {
                    contador.await();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        configTela(disponibilidade.getDisponibilidade());
                        configListeners(disponibilidade.getDisponibilidade());
                        if (disponibilidade.getDisponibilidade().equals(DisponibilidadeMotorista.PREPARANDO_VIAGEM)) {
                            preparaDialogSolicitacaoViagem();
                        }
                    }
                });
            }
        });
        threadAtualizarDisponibilidade.start();
    }

    //MAPA
    private void iniciarMapa()
    {
        try
        { MapsInitializer.initialize(requireActivity().getApplicationContext()); }
        catch (Exception e)
        { e.printStackTrace(); }
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
                showInTerminal("getLastLocation","onSucess");
                if (location != null)
                {
                    showInTerminal("getLastLocation/if","location != null" );
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    localMotorista = new LatLng(latitude, longitude);

                    gMap.clear();
                    gMap.addMarker(new MarkerOptions().position(localMotorista).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localMotorista,19));
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10* 1000);// intervalo de tempo para buscar localizacao em milisegundos;
        locationRequest.setFastestInterval(5 * 1000);// intervalo de tempo para receber localização de outros apps que estejam utilizando o mesmo recurso
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builderlocationsSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
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

            }
        });
    }

    //CONFIG TELA
    private void configTela(String disponibilidade)
    {
        showInTerminal("configTela","Configurando a tela para "+ disponibilidade);
        configFab(disponibilidade);//ok
        configBs(disponibilidade); //ok?
        configBtInteragirViagem(disponibilidade);
    }

    @SuppressLint("RestrictedApi")
    private void configFab(String disponibilidade)
    {
        showInTerminal("configFab","configurando para disponibilidade: "+disponibilidade);
        switch (disponibilidade)
        {
            case DisponibilidadeMotorista.INDISPONIVEL:
            {
                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonVermelhoPadrao)));
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                showInTerminal("configFab","Mostra Fab Indisponivel");
            }
            break;

            case DisponibilidadeMotorista.DISPONIVEL:
            {
                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonVerdePadrao)));
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
                showInTerminal("configFab","Mostra Fab disponivel");

            }
            break;

            case DisponibilidadeMotorista.EM_VIAGEM:
            case DisponibilidadeMotorista.PREPARANDO_VIAGEM:
            case DisponibilidadeMotorista.A_CAMINHO:
            {
                if (fab.isShown())
                {
                    fab.setVisibility(View.GONE);
                    showInTerminal("configFab","remove Fab");
                }
            }
            break;

            default: {}
            break;
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

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void configBs(final String disponibilidade)
    {
        Button btbs1, btbs2;
        if (disponibilidade.equals(DisponibilidadeMotorista.INDISPONIVEL ) || disponibilidade.equals(DisponibilidadeMotorista.DISPONIVEL ))
        {
            bsDialog = new BottomSheetDialog(requireActivity(),R.style.BottomSheetDialogTheme);
            bsView = requireActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_motorista,null);
            bsDialog.setContentView(bsView);

            pq = bsView.findViewById(R.id.checkbox_peq);
            med = bsView.findViewById(R.id.checkbox_med);
            grd = bsView.findViewById(R.id.checkbox_grd);
            recyclerBS = bsView.findViewById(R.id.recycler_bs_motorista);

            pq.setChecked(false);
            med.setChecked(false);
            grd.setChecked(false);

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerBS.setLayoutManager(layoutManager);
            recyclerBS.setHasFixedSize(true);

            btbs1 = bsView.findViewById(R.id.bt_bs_motorista);
            btbs2 = bsView.findViewById(R.id.bt_bs_motorista2);

            showInTerminal("configBS","para disponibilidade = "+disponibilidade);
            switch (disponibilidade)
            {
                case DisponibilidadeMotorista.INDISPONIVEL:
                {
                    btbs2.setVisibility(View.GONE);
                    btbs1.setText("CONFIRMAR DISPONIBILIDADE");
                    onClickBtConfirmarBS(btbs1);
                    showInTerminal("configBS","BsRemovido!!");

                }break;

                case DisponibilidadeMotorista.DISPONIVEL:
                {
                    btbs2.setVisibility(View.VISIBLE);
                    btbs1.setText("ATUALIZAR DISPONIBILIDADE");
                    onClickBtConfirmarBS(btbs1);
                    onClickBtCancelarBs(btbs2);
                    showInTerminal("configBS","BsAdicionado!!");

                }break;

                default: {}break;
            }
        }
    }

    private void configMultiViewAdapter()
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

    private void onClickBtCancelarBs(Button bt)
    {
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                disponibilidade.setDisponibilidade(DisponibilidadeMotorista.INDISPONIVEL);
                threadAtualizarDisponibilidade();
                bsDialog.dismiss();
            }
        });
    }

    private void onClickBtConfirmarBS(Button bt)
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

                            threadAtualizarDisponibilidade();
                            bsDialog.dismiss();
                            addListenerViagem();
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

    //Threads
    private void threadPopularAdapter()
    {
        Thread threadPopulaAdapter = new Thread(new Runnable() {
            @Override
            public void run() {
                contador = new CountDownLatch(1);
                listaVeiculos.clear();
                listaVeiculos = veiculoDAO.receberVeiculosLiberados(contador);

                try {
                    contador.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configMultiViewAdapter();
                    }
                });
            }
        });
        threadPopulaAdapter.start();
    }

    @SuppressLint("SetTextI18n")
    private void configBtInteragirViagem(String disponibilidade)
    { showInTerminal("configBtInteragirViagem","configurando para disponibilidade: "+ disponibilidade);
        switch (disponibilidade)
        {
            case DisponibilidadeMotorista.DISPONIVEL:
            case DisponibilidadeMotorista.INDISPONIVEL:
            case DisponibilidadeMotorista.PREPARANDO_VIAGEM:
            {
                if(btInteragirViagem.isShown())
                {
                    btInteragirViagem.setVisibility(View.GONE);
                    showInTerminal("configBtInteragirViagem/remover","botão removido!!!");
                }
            }break;

            case DisponibilidadeMotorista.A_CAMINHO:
                {
                    btInteragirViagem.setBackground(getResources().getDrawable(R.drawable.button_vermelho_seletor));
                    btInteragirViagem.setText("Cancelar a viagem");
                    btInteragirViagem.setVisibility(View.VISIBLE);
                    btInteragirViagem.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            recusarViagem();
                        }
                    });
                    showInTerminal("configBtInteragirViagem","Botão Adicionado !!!");
                }break;

            case DisponibilidadeMotorista.EM_VIAGEM:
            {
                btInteragirViagem.setBackground(getResources().getDrawable(R.drawable.button_verde_seletor));
                btInteragirViagem.setText("Finalizar Viagem");
                showInTerminal("configBtInteragirViagem","Botão Alterado");
            }break;

            default:{}break;
        }
    }

    private void configListeners(String disponibilidade)
    {
        showInTerminal("configListener","configura para disponibilidade: "+disponibilidade );
        switch (disponibilidade)
        {
            case DisponibilidadeMotorista.DISPONIVEL:
                {
                    addListenerViagem();
                    setCallbackLocalizacaoMotorista();
                }break;

            case DisponibilidadeMotorista.INDISPONIVEL:
                {
                    showInTerminal("configLister/indisponivel","passou aqui");
                    setCallbackLocalizacaoMotorista();
                    removeListenerViagem();
                }break;

            case DisponibilidadeMotorista.PREPARANDO_VIAGEM:
                {
                    removeListenerViagem();
                    setCallbackLocalizacaoMotorista();

                }break;

            case DisponibilidadeMotorista.A_CAMINHO:
                {
                    removeListenerViagem();
                    removeCallbacks(callbackLocalizacaoMotorista, "callbackLocalizacaoMotorista");
                    setCallbackAcaminho();
                }break;

            case DisponibilidadeMotorista.EM_VIAGEM:
                {
                    System.out.println("<<<<<<<<<<<<<<< EM_VIAGEM >>>>>>>>>>>>>>>>>");
                    removeCallbacks(callbackAcaminho,"callbackAcaminho");
                    setCallbackEmViagem();
                    removeListenerViagem();
                }break;
            default:{}break;
        }
    }

    private void addListenerViagem()
    {
        showInTerminal("addListenerViagem","adicionado");
        contadorViagem =0;
        viagensRef = ViagemDAO.getRootViagens();
        listenerViagem = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s){ }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                String seuid = meuPerfil.getIdUsuario();

                //if ("idMotorista".equals(dataSnapshot.child().getKey()) && seuid.equals(dataSnapshot.getValue(String.class)))
                if (seuid.equals(dataSnapshot.child("idMotorista").getValue(String.class)) && contadorViagem == 0)
                {
                    contadorViagem++;
                    showInTerminal("ListenerViagem/onChildChanged","executou ["+contadorViagem+"] vezes");
                    viagemAtual = dataSnapshot.getValue(Viagem.class);
                    disponibilidade.setDisponibilidade(DisponibilidadeMotorista.PREPARANDO_VIAGEM);
                    threadAtualizarDisponibilidade();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        viagensRef.addChildEventListener(listenerViagem);
    }

    private void removeListenerViagem()
    {
        if(viagensRef != null)
        {
            viagensRef.removeEventListener(listenerViagem);
            showInTerminal("removeListenerViagem","removendo Listener");
        }
    }

    private void setCallbackLocalizacaoMotorista ()
    {
        showInTerminal("setCallbackLocalizacaoMotorista","adicionado");
        callbackLocalizacaoMotorista = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Location location = locationResult.getLastLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                localMotorista = new LatLng(latitude, longitude);

                gMap.clear();
                gMap.addMarker(new MarkerOptions().position(localMotorista).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localMotorista,19));
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {}
        };

        client.requestLocationUpdates(locationRequest, callbackLocalizacaoMotorista, null);
    }

    private void setCallbackAcaminho ()
    {
        showInTerminal("callback a caminho","Executou");
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        callbackAcaminho = new LocationCallback()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                gMap.clear();
                Location location = locationResult.getLastLocation();
                localMotorista = new LatLng(location.getLatitude(),location.getLongitude());
                LatLng latlngPassageiro = new LatLng(localPassageiro.getLatitude(), localPassageiro.getLongitude());

                disponibilidadeDAO.atualizarLatLong(localMotorista, disponibilidade);

                gMap.addMarker(new MarkerOptions().position(localMotorista).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                marcadorPassageiro = gMap.addMarker( new MarkerOptions().position(latlngPassageiro).title("Local de embarque").icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario_passageiro)));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localMotorista,19));

                if(distanciaMotorista(latlngPassageiro)<30)
                {
                    btInteragirViagem.setBackground(getResources().getDrawable(R.drawable.button_verde_seletor));
                    btInteragirViagem.setText("Dono de Animal a bordo");
                    btInteragirViagem.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            btInteragirViagem.setVisibility(View.GONE);
                            disponibilidade.setDisponibilidade(DisponibilidadeMotorista.EM_VIAGEM);
                            threadAtualizarDisponibilidade();
                            viagemDAO.atualizaStatusviagem(viagemAtual.getIdViagem(),Viagem.EM_ANDAMENTO);
                        }
                    });
                }

                else
                    {
                        btInteragirViagem.setBackground(getResources().getDrawable(R.drawable.button_vermelho_seletor));
                        btInteragirViagem.setText("Cancelar a viagem");
                    }

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {}
        };

        client.requestLocationUpdates(locationRequest,callbackAcaminho,null);
    }

    private void setCallbackEmViagem()
    {
        showInTerminal("callback e viagem","Executou");
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        callbackEmViagem = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                LatLng minhalatLng = locationToLatLong(locationResult.getLastLocation());
                LatLng destinoLatLng = locationToLatLong(localDestino.getLocation());
                showInTerminal("CallbackEmViagem latLong=", destinoLatLng.toString());


                marcadorDestino = gMap.addMarker(new MarkerOptions().position(destinoLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_usuario)));
                marcadorMotorista = gMap.addMarker(new MarkerOptions().position(minhalatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localMotorista,19));

                if (distanciaMotorista(destinoLatLng)<30)
                {
                    btInteragirViagem.setVisibility(View.VISIBLE);
                    btInteragirViagem.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //finaliza a viagem
                        }
                    });
                }
                else
                {
                    btInteragirViagem.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability){}
        };

        client.requestLocationUpdates(locationRequest, callbackAcaminho, null);

    }

    private LatLng locationToLatLong(Location location)
    {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

    private void removeCallbacks (LocationCallback callback, String nomeCallBack)
    {
        if(client != null && callback != null)
        {
            showInTerminal("RemoveCallbacks"," removendo callback = " );
            client.removeLocationUpdates(callback);
        }
    }

    private void preparaDialogSolicitacaoViagem()
    {
        dialogSolicitacaoViagem = new Dialog(requireActivity());
        dialogSolicitacaoViagem.setContentView(R.layout.dialog_viagem_solicitada);
        dialogSolicitacaoViagem.setCanceledOnTouchOutside(false);

        Button btAceitar, btRecusar;
        final TextView tvDonoAnimal, animal1, animal2, animal3, descricao;
        final CircleImageView ciDonoAnimal, ciAnimal1, ciAnimal2,ciAnimal3;
        LinearLayout layoutAnimal2, layoutAnimal3;

        tvDonoAnimal = dialogSolicitacaoViagem.findViewById(R.id.tv_donoanimal);
        animal1 = dialogSolicitacaoViagem.findViewById(R.id.tv_cianimal1);
        animal2 = dialogSolicitacaoViagem.findViewById(R.id.tv_cianimal2);
        animal3 = dialogSolicitacaoViagem.findViewById(R.id.tv_cianimal3);
        descricao = dialogSolicitacaoViagem.findViewById(R.id.tv_descricaoanimal);
        ciDonoAnimal = dialogSolicitacaoViagem.findViewById(R.id.ci_donoanimal);
        ciAnimal1 = dialogSolicitacaoViagem.findViewById(R.id.ci_animal1);
        ciAnimal2 = dialogSolicitacaoViagem.findViewById(R.id.ci_animal2);
        ciAnimal3 = dialogSolicitacaoViagem.findViewById(R.id.ci_animal3);
        layoutAnimal2 = dialogSolicitacaoViagem.findViewById(R.id.layout_cianimal2);
        layoutAnimal3 = dialogSolicitacaoViagem.findViewById(R.id.layout_cianimal3);
        btAceitar = dialogSolicitacaoViagem.findViewById(R.id.bt_aceitarViagem);
        btRecusar = dialogSolicitacaoViagem.findViewById(R.id.bt_recusarViagem);

        tvDonoAnimal.setText(viagemAtual.getNomeDonoAnimal());
        setGlide(viagemAtual.getFotoDonoAnimalUrl(),ciDonoAnimal);

        animal1.setText(viagemAtual.getNomeAnimal1());
        setGlide(viagemAtual.getFotoAnimalUrl1(), ciAnimal1);
        ciAnimal1.setBorderColor(getResources().getColor(R.color.colorButtonVerdePadrao));
        ciAnimal1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ciAnimal1.setBorderColor(getResources().getColor(R.color.colorButtonVerdePadrao));
                ciAnimal2.setBorderColor(getResources().getColor(R.color.white));
                ciAnimal3.setBorderColor(getResources().getColor(R.color.white));
                descricao.setText(viagemAtual.getObservacaoAnimal1());
            }
        });

        if(viagemAtual.getIdAnimal2() != null)
        {
            animal2.setText(viagemAtual.getNomeAnimal2());
            setGlide(viagemAtual.getFotoAnimalUrl2(), ciAnimal2);
            layoutAnimal2.setVisibility(View.VISIBLE);
            ciAnimal2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ciAnimal1.setBorderColor(getResources().getColor(R.color.white));
                    ciAnimal2.setBorderColor(getResources().getColor(R.color.colorButtonVerdePadrao));
                    ciAnimal3.setBorderColor(getResources().getColor(R.color.white));
                    descricao.setText(viagemAtual.getObservacaoAnimal2());
                }
            });
        }
        if(viagemAtual.getIdAnimal3() != null)
        {
            animal3.setText(viagemAtual.getNomeAnimal3());
            setGlide(viagemAtual.getFotoAnimalUrl3(), ciAnimal3);
            layoutAnimal3.setVisibility(View.VISIBLE);
            ciAnimal3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ciAnimal1.setBorderColor(getResources().getColor(R.color.white));
                    ciAnimal2.setBorderColor(getResources().getColor(R.color.white));
                    ciAnimal3.setBorderColor(getResources().getColor(R.color.colorButtonVerdePadrao));
                    descricao.setText(viagemAtual.getObservacaoAnimal3());
                }
            });
        }

        descricao.setText(viagemAtual.getObservacaoAnimal1());
        OnClickDialogSolicitarViagem(btAceitar, btRecusar);

        dialogSolicitacaoViagem.show();
        showInTerminal("DialogFragment em exibição","Disponibilidade Atual: "+disponibilidade.getDisponibilidade());
    }

    private void OnClickDialogSolicitarViagem (Button btAceitar, Button btRecusar)
    {
        btAceitar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aceitarViagem();
                dialogSolicitacaoViagem.dismiss();
            }
        });

        btRecusar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(viagemAtual!= null)
                {
                    recusarViagem();
                }
                dialogSolicitacaoViagem.dismiss();
            }
        });
    }

    private void aceitarViagem()
    {
        viagemAtual.setNomeMotorista(meuPerfil.getNome());
        viagemAtual.setFotoMotoristaUrl(meuPerfil.getFotoPerfilUrl());
        viagemAtual.setIdVeiculo(veiculoSelecionado.getIdVeiculo());
        viagemAtual.setMarcaVeiculo(veiculoSelecionado.getMarcaVeiculo());
        viagemAtual.setModeloVeiculo(veiculoSelecionado.getModeloVeiculo());
        viagemAtual.setPlacaVeiculo(veiculoSelecionado.getPlacaVeiculo());
        viagemAtual.setStatusViagem(Viagem.AGUARDANDO_MOTORISTA);

        Thread threadAceitarViagem = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                contador = new CountDownLatch(1);
                localPassageiro = localDAO.getLocal(viagemAtual.getIdOrigem(), contador);

                try {contador.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                contador = new CountDownLatch(1);
                localDestino = localDAO.getLocal(viagemAtual.getIdDestino(),contador);

                try {contador.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                contador = new CountDownLatch(1);
                //setar itens da viagem
                String tipoAvaliacao = "donoAnimal";
                viagemDAO.salvarViagem(viagemAtual, contador, getActivity(), tipoAvaliacao);

                try {contador.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        disponibilidade.setDisponibilidade(DisponibilidadeMotorista.A_CAMINHO);
                        threadAtualizarDisponibilidade();
                    }
                });
            }
        });
        threadAceitarViagem.start();
    }

    private void recusarViagem()
    {
        viagemDAO.recusarViagem(viagemAtual.getIdViagem());
        disponibilidade.setDisponibilidade(DisponibilidadeMotorista.DISPONIVEL);
        threadAtualizarDisponibilidade();
        viagemAtual = null;
    }

    private float distanciaMotorista(LatLng destino)
    {
        float distancia;
        Location locationMotorista = new Location("Location Motorista");
        locationMotorista.setLongitude(localMotorista.longitude);
        locationMotorista.setLatitude(localMotorista.latitude);
        Location locationDestino = new Location("Location Destino");
        locationDestino.setLatitude(destino.latitude);
        locationDestino.setLongitude(destino.longitude);
        distancia = locationMotorista.distanceTo(locationDestino);
        return distancia;
    }

    private void setGlide(String url, CircleImageView view)
    {
        Uri uri = Uri.parse(url);
        Glide.with(requireActivity()).load(uri).into(view);
    }

    private void toastThis(String mensagem)
    {
        Toast.makeText(getActivity(),mensagem,Toast.LENGTH_SHORT).show();
    }

    private void showInTerminal(String Local,String mensagem)
    {
        System.out.println("<<< Motorista: <" + Local + "> (" + mensagem +") >>>");
    }
}
