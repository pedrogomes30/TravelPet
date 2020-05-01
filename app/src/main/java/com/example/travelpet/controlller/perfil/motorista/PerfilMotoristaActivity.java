package com.example.travelpet.controlller.perfil.motorista;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.travelpet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PerfilMotoristaActivity extends AppCompatActivity {
    private DatabaseReference referencia;
    private FirebaseAuth autenticacao;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_motorista);
        //toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
           //NavigationView navigationView = findViewById()
    }
}
