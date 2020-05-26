package com.example.travelpet.controlller.perfil.motorista;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.travelpet.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PerfilMotoristaActivity extends AppCompatActivity {

    private DatabaseReference referencia;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_motorista);
        Toolbar toolbar = findViewById(R.id.toolbar_motorista);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.moto_drawer_layout);
        NavigationView navigationView = findViewById(R.id.moto_drawer_layout);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.perfil_motorista, menu);
        return true;
    }
}
