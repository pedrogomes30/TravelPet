package com.example.travelpet.controlller.perfil.motorista.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DisponibilidadeMotoristaDao;
import com.example.travelpet.dao.LocalDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.dao.ViagemDAO;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.DisponibilidadeMotorista;
import com.example.travelpet.model.Local;
import com.example.travelpet.model.Veiculo;
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
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder builderlocationsSettingsRequest;
    private SettingsClient settingsClient;

    //Threads
    private Thread threadAttDisponibilidade;
    private Thread checarDisponibilidade;
    private Thread threadPopulaAdapter;
    private CountDownLatch contador;

    private ChildEventListener listenerViagem;

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

    private Dialog dialogSolicitacaoViagem;

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
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        threadChecarDisponibilidade();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        iniciarMapa();
    }

    private void addListenerViagem()
    {
        DatabaseReference referenciaViagem = ViagemDAO.getRootViagens();
        //Escutando nó viagem;
        //Query queryViagem = referenciaViagem
        referenciaViagem.addChildEventListener( listenerViagem = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                String seuid = Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario());
                System.out.println("seu id = " + seuid);

                if ("idMotorista".equals(dataSnapshot.getKey()) && seuid.equals(dataSnapshot.getValue()))
                {
                    viagemAtual = dataSnapshot.getValue(Viagem.class);
                    disponibilidade.setDisponibilidade(DisponibilidadeMotorista.PREPARANDO_VIAGEM);
                    threadSalvarDisponibilidade();
                    preparaDialogSolicitacaoViagem();

                    System.out.println("dentro do if== "+dataSnapshot.toString());
                    System.out.println("Key = "+ dataSnapshot.getKey());
                    System.out.println(dataSnapshot.child("idMotorista").toString());
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                if (location != null)
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
                                    CameraUpdateFactory.newLatLngZoom(localMotorista,19)
                            );
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                System.out.println("falha ao recuperar localização");
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
                localMotorista = new LatLng(latitude, longitude);

                gMap.clear();
                gMap.addMarker(new MarkerOptions().position(localMotorista).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_carro)));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localMotorista,19));
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {}
        };

        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    //CONFIG TELA
    private void configTela(String disponibilidade)
    {
        showInTerminal("<< Configurando a tela para"+disponibilidade+" >>");
        switch (disponibilidade)
        {
            case DisponibilidadeMotorista.INDISPONIVEL:
            case DisponibilidadeMotorista.DISPONIVEL:
                {
                    configFab(disponibilidade);
                    configBs(disponibilidade);
                }break;

            case DisponibilidadeMotorista.EM_VIAGEM:
            case DisponibilidadeMotorista.PREPARANDO_VIAGEM:
                {
                    configFab(disponibilidade);

                }break;

            default:{}break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void configFab(String disponibilidade)
    {
        switch (disponibilidade)
        {
            case DisponibilidadeMotorista.INDISPONIVEL:
                {
                    fab.setVisibility(View.VISIBLE);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonVermelhoPadrao)));
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                }break;

            case DisponibilidadeMotorista.DISPONIVEL:
                {
                    fab.setVisibility(View.VISIBLE);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonVerdePadrao)));
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));

                }break;

            case DisponibilidadeMotorista.EM_VIAGEM:
            case DisponibilidadeMotorista.PREPARANDO_VIAGEM:
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
    
    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void configBs(final String dispConfig)
    {
        bsDialog = new BottomSheetDialog(requireActivity(),R.style.BottomSheetDialogTheme);
        bsView = requireActivity().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_motorista,null);
        bsDialog.setContentView(bsView);

        //findViews
        recyclerBS = bsView.findViewById(R.id.recycler_bs_motorista);
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
            case DisponibilidadeMotorista.INDISPONIVEL:
            {
                btbs2.setVisibility(View.GONE);
                btbs1.setText("CONFIRMAR DISPONIBILIDADE");
                onClickBtConfirmar(btbs1);

            }break;

            case DisponibilidadeMotorista.DISPONIVEL:
            {
                btbs2.setVisibility(View.VISIBLE);
                btbs1.setText("ATUALIZAR DISPONIBILIDADE");
                onClickBtConfirmar(btbs1);
                onClickBtCancelar(btbs2);

            }break;

            default: {}break;
        }
    }

    private void configuraMultiViewAdapter()
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

    private void onClickBtCancelar(Button bt)
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

    private void onClickBtConfirmar(Button bt)
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

                requireActivity().runOnUiThread(new Runnable()
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
                disponibilidade = disponibilidadeDAO.receberDisponibilidade(contador);

                try { contador.await();}
                catch (InterruptedException e)
                {e.printStackTrace();}

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(disponibilidade != null)
                        {
                            //Atualiza a tela
                            configTela(disponibilidade.getDisponibilidade());
                        }
                    }
                });
            }
        });
        checarDisponibilidade.start();
    }





    private void preparaDialogSolicitacaoViagem()
    {
        dialogSolicitacaoViagem = new Dialog(requireActivity());
        dialogSolicitacaoViagem.setContentView(R.layout.dialog_viagem_solicitada);
        dialogSolicitacaoViagem.setCanceledOnTouchOutside(false);

        Button btconfirmar, btCancelar;
        TextView tvDonoAnimal, animal1, animal2, animal3, descricao;
        CircleImageView ciDonoAnimal, ciAnimal1, ciAnimal2,ciAnimal3;
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
        btconfirmar = dialogSolicitacaoViagem.findViewById(R.id.bt_aceitarViagem);
        btCancelar = dialogSolicitacaoViagem.findViewById(R.id.bt_recusarViagem);

        tvDonoAnimal.setText(viagemAtual.getNomeDonoAnimal());
        setGlide(viagemAtual.getFotoDonoAnimalUrl(),ciDonoAnimal);

        animal1.setText(viagemAtual.getNomeAnimal1());
        setGlide(viagemAtual.getFotoAnimalUrl1(), ciAnimal1);

        if(viagemAtual.getIdAnimal2() != null)
        {
            animal2.setText(viagemAtual.getNomeAnimal2());
            setGlide(viagemAtual.getFotoAnimalUrl2(), ciAnimal2);
            layoutAnimal2.setVisibility(View.VISIBLE);
        }
        if(viagemAtual.getIdAnimal3() != null)
        {
            animal3.setText(viagemAtual.getNomeAnimal3());
            setGlide(viagemAtual.getFotoAnimalUrl3(), ciAnimal3);
            layoutAnimal3.setVisibility(View.VISIBLE);
        }

        descricao.setText(viagemAtual.getObservacaoAnimal1());

        dialogSolicitacaoViagem.show();
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

    private void showInTerminal(String message)
    {
        System.out.println(message);
    }
}
