package com.example.travelpet.controlller.perfil.motorista;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.Motorista;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilMotoristaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference referencia;
    private FirebaseAuth autenticacao;
    private ImageView imgvHeader;
    private TextView nomeHeader, emailHeader;
    private NavigationView navigationView;
    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_motorista);
        Toolbar toolbar = findViewById(R.id.toolbar_motorista);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.moto_drawer_layout);
        navigationView = findViewById(R.id.moto_nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        dialog = new Dialog(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.mapMotoristaFragment, R.id.listaVeiculosFragment)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this,R.id.moto_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);

        View view  = navigationView.inflateHeaderView(R.layout.nav_header_perfil_motorista);

        imgvHeader = view.findViewById(R.id.imageViewPerfilMotorista);
        nomeHeader = view.findViewById(R.id.textNomeMotorista);
        emailHeader = view.findViewById(R.id.textEmailMotorista);

        configuraHeader();
        setOnclickTeste();
    }

    public void setOnclickTeste()
    {
        dialog.setContentView(R.layout.dialog_viagem_solicitada);
        dialog.setTitle("Viagem Solicitada");
        imgvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }




    public void configuraHeader ()
    {
        DatabaseReference motoristaRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("motorista")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        motoristaRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Motorista motorista = dataSnapshot.getValue(Motorista.class);

                String fotoPerfilUrl = motorista.getFotoPerfilUrl();
                nomeHeader.setText(motorista.getNome()+" "+motorista.getSobrenome());
                emailHeader.setText(motorista.getEmail());

                if(!fotoPerfilUrl.equals(""))
                {
                    Uri fotoUsuarioUri = Uri.parse(fotoPerfilUrl);
                    Glide.with(PerfilMotoristaActivity.this)
                            .load(fotoPerfilUrl)
                            .into(imgvHeader);
                }
                else{ imgvHeader.setImageResource(R.drawable.iconperfiloficial); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void toastThis (String mensagem)
    {
        Toast.makeText(getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.moto_logout:
                {
                    System.out.println("botão sair clickado");
                }break;

            default:{}break;
        }

        return false;
    }

}
