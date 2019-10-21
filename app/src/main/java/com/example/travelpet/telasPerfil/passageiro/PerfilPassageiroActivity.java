package com.example.travelpet.telasPerfil.passageiro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.UsuarioFirebase;
import com.example.travelpet.helper.Permissao;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilPassageiroActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    //private CircleImageView imageViewPerfil;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();


    // Array de String para solicitar permissões
    public String [] permissoesNecessarias = new String []{
            // Definindo Permiissões
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        // Solicitando (Validar) Permissões
        Permissao.validarPermissoes(permissoesNecessarias, PerfilPassageiroActivity.this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View view = navigationView.inflateHeaderView(R.layout.nav_header_perfil_passageiro);

        final ImageView imageView = view.findViewById(R.id.imageViewPerfil);
        final TextView  textNomeUsuario = view.findViewById(R.id.textNomeUsuario);
        final TextView textEmail = view.findViewById(R.id.textEmail);

        DatabaseReference usuarios = referencia.child( "usuarios" ).child(UsuarioFirebase.getIdentificadorUsuario());

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                // Recupera a url da fotousuario em String do database
                String fotoUrl              =   dadosUsuario.getFotoUsuarioUrl();
                String nomeUsuario          =   dadosUsuario.getNome();
                String sobrenomeUsuario     =   dadosUsuario.getSobrenome();
                String emailUsuario         =   dadosUsuario.getEmail();

                textNomeUsuario.setText(nomeUsuario+" "+sobrenomeUsuario);
                textEmail.setText(emailUsuario);

                // Recupera a Referência do user do usuario atual
                FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
                // Recupera a foto de perfil da conta do gmail
                Uri fotoUsuarioGmail = usuario.getPhotoUrl();

                // Se não tiver foto no database e não tiver foto no gmail, executa
                if(fotoUrl.equals("vazio")&& fotoUsuarioGmail== null){
                    // envia a imagem padrão para o perfil
                    imageView.setImageResource(R.drawable.iconperfiloficial);

                }else if(!fotoUrl.equals("vazio")){ // Envia foto do gmail
                    Uri fotoUsuarioUrl = Uri.parse(fotoUrl);
                    Glide.with(PerfilPassageiroActivity.this)
                            .load( fotoUsuarioUrl )
                            // .into = define qual imageView irá utilizar
                            .into( imageView );

                }else { // Se não Envia a imagem que está no database
                        Glide.with(PerfilPassageiroActivity.this)
                                .load( fotoUsuarioGmail )
                                // .into = define qual imageView irá utilizar
                                .into( imageView );

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //imageView.setImageResource(R.drawable.iconperfiloficial);
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_viagem, R.id.nav_pagamento, R.id.nav_configuracao,
                R.id.nav_contato, R.id.nav_meus_animais, R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    //Método para tratamento da validação da permissão, caso não aceite
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int permissaoResultado : grantResults ){
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED ){
                Permissao.alertaValidacaoPermissao(PerfilPassageiroActivity.this);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_passageiro, menu);

        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();



    }

}
