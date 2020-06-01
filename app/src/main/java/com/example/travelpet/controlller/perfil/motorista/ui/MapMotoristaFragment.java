package com.example.travelpet.controlller.perfil.motorista.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.travelpet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapMotoristaFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap gMap;
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public MapMotoristaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_motorista, container, false);

        //Criando Mapa
        mapView =  view.findViewById(R.id.mapMotorista);
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

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        recuperarLocalizacaoUsuario();
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
}
