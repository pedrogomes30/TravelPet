package com.example.travelpet.controlller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //  Esconde a ActionBar
        //getSupportActionBar().hide();
        // Exibe a SplashActivity em Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // handler = Cria Splash Screen
        // postDelayed() = usado para da um tempo de delay na tela antes de ir para outra Activity
        // Runnable() = Responsabel por executar o codigo depois de 5 segundos = 5000 milesegundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        }, 2000);

    }
}
