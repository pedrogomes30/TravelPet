package com.example.travelpet.telasPerfil.passageiro.ui.viagem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelpet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViagemFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private MapView mapView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viagem,container,false);
        //return inflater.inflate(R.layout.fragment_viagem,container, false);
        mapView = (MapView) view.findViewById(R.id.MapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }




        mapView.getMapAsync(this);





        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}



/*
public class ViagemFragment extends Fragment {

    private ViagemViewModel viagemViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viagemViewModel =
                ViewModelProviders.of(this).get(ViagemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_viagem, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        viagemViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}

 */