package com.example.travelpet.controlller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.ValidarLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private  Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;

        //  Esconde a ActionBar
        //getSupportActionBar().hide();
        // Exibe a SplashActivity em Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // handler = Cria Splash Screen
        // postDelayed() = usado para da um tempo de delay na tela antes de ir para outra Activity
        // Runnable() = Responsabel por executar o codigo depois de 5 segundos = 5000 milesegundos
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
                if (fireUser != null)
                {
                    ValidarLogin.logarUsuario(activity);
                }
                else
                {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                }
            }
        }, 2000);

    }
}
