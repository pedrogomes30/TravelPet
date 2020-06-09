package com.example.travelpet.controlller.perfil.passageiro;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilPassageiroActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView campoFotoUsuario ;
    private TextView  campoNomeUsuario,campoEmailUsuario ;

    private String fotoPerfilUrl, nomeUsuario, sobrenomeUsuario, emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_viagem,
                R.id.nav_meus_animais,
                R.id.nav_configuracao,
                R.id.nav_info,R.id.nav_sair)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View view = navigationView.inflateHeaderView(R.layout.nav_header_perfil_passageiro);

          campoFotoUsuario = view.findViewById(R.id.imageViewPerfil);
          campoNomeUsuario = view.findViewById(R.id.textNomeUsuario);
          campoEmailUsuario = view.findViewById(R.id.textEmail);

          getDadosDonoAnimalDatabase();

    }
    //Menu dos 3 pontos direito da tela do toolbar
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

    public void getDadosDonoAnimalDatabase(){

        DatabaseReference donoAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child( "donoAnimal")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DonoAnimal donoAnimal = dataSnapshot.getValue(DonoAnimal.class);
                fotoPerfilUrl      =   donoAnimal.getFotoPerfilUrl();
                nomeUsuario        =   donoAnimal.getNome();
                sobrenomeUsuario   =   donoAnimal.getSobrenome();
                emailUsuario       =   donoAnimal.getEmail();

                campoNomeUsuario.setText(nomeUsuario+" "+sobrenomeUsuario);
                campoEmailUsuario.setText(emailUsuario);

                if(!fotoPerfilUrl.equals("")){
                    Uri fotoUsuarioUri = Uri.parse(fotoPerfilUrl);
                    Glide.with(PerfilPassageiroActivity.this)
                            .load( fotoUsuarioUri)
                            .into( campoFotoUsuario);
                }else{
                    campoFotoUsuario.setImageResource(R.drawable.imagem_usuario);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
