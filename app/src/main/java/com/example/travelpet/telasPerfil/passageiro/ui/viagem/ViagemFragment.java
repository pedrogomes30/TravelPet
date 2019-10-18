package com.example.travelpet.telasPerfil.passageiro.ui.viagem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.travelpet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

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

        /*
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */



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