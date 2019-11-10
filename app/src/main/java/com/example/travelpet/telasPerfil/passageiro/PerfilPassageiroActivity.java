package com.example.travelpet.telasPerfil.passageiro;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.activity.MainActivity;
import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroNomeUsuarioActivity;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.example.travelpet.helper.Permissao;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilPassageiroActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference referencia;
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurações iniciais
        referencia = FirebaseDatabase.getInstance().getReference();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

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
                // Recupera dados do usuário no database para exibir no NavigationDrawer
                String fotoUrl              =   dadosUsuario.getFotoUsuarioUrl();
                String nomeUsuario          =   dadosUsuario.getNome();
                String sobrenomeUsuario     =   dadosUsuario.getSobrenome();
                String emailUsuario         =   dadosUsuario.getEmail();

                textNomeUsuario.setText(nomeUsuario+" "+sobrenomeUsuario);
                textEmail.setText(emailUsuario);

                // Recupera a Referência do user do usuario atual
                FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
                // Recupera a foto de perfil da conta do gmail
                Uri fotoUsuarioEmail = usuario.getPhotoUrl();

                // Se não tiver foto no database e não tiver foto no gmail, executa
                if(fotoUrl.equals("vazio")&& fotoUsuarioEmail== null){
                    // envia a imagem padrão para o perfil
                    imageView.setImageResource(R.drawable.iconperfiloficial);

                }else if(!fotoUrl.equals("vazio")){ // Envia a a foto do database
                    Uri fotoUsuarioUrl = Uri.parse(fotoUrl);
                    Glide.with(PerfilPassageiroActivity.this)
                            .load( fotoUsuarioUrl )
                            // .into = define qual imageView irá utilizar
                            .into( imageView );

                }else { // Envia a a foto da conta do gmail
                        Glide.with(PerfilPassageiroActivity.this)
                                .load( fotoUsuarioEmail )
                                // .into = define qual imageView irá utilizar
                                .into( imageView );

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    //Menu dos 3 pontos direito da tela do toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_passageiro, menu);

        return true;

    }
    // Ação ao clicar em algum item do Menu dos 3 pontos do Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                // Caixa de diálogo
                AlertDialog.Builder msgBox = new AlertDialog.Builder(PerfilPassageiroActivity.this);
                msgBox.setTitle("Saindo...");
                msgBox.setMessage("Tem certeza que deseja sair desta conta ?");
                msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthUI.getInstance()
                                .signOut(PerfilPassageiroActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                } );
                        startActivity(new Intent(PerfilPassageiroActivity.this, MainActivity.class));
                        finish();
                    }
                });
                msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                msgBox.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    // Método voltar com  o botão do próprio aparelho
    @Override
    public void onBackPressed() {
        // como não tem nada ele não volta
    }

}
