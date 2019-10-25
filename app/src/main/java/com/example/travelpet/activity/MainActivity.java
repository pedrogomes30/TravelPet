package com.example.travelpet.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.activity.cadastro.cadastroAnimal.CadastroNomeAnimalActivity;
import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroNomeUsuarioActivity;
import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroTipoUsuarioActivity;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.example.travelpet.helper.Permissao;
import com.example.travelpet.telasPerfil.motorista.TestePerfilMotoristaActivity;
import com.example.travelpet.telasPerfil.passageiro.PerfilPassageiroActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    SignInButton BTSignIn;
    Button BTSignOut;
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,fotoUsuarioUrl,email;

    // Array de String para solicitar permissões
    public String [] permissoesNecessarias = new String []{
            // Definindo Permiissões
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias,MainActivity.this,1);

        nomeUsuario = "nulo";
        sobrenomeUsuario = "nulo";
        telefoneUsuario = "nulo";
        tipoUsuario = "nulo";
        fotoUsuarioUrl = "nulo";

        // Tira a ActionBar
        //getSupportActionBar().hide();


        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();
        BTSignIn = findViewById(R.id.sign_in_button);
        BTSignOut = findViewById(R.id.sign_out_button);
        setButtons();

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                } );

    }

    public void setButtons()
    {
        BTSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignInIntent();
            }
        });

        BTSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


    }

    public void ToastThis (String mensagem)
    {
        Toast.makeText(this, mensagem,Toast.LENGTH_LONG).show();
        //Mostra algo na tela, usado para testes
    }

    public void createSignInIntent()
    {
        // Escolhe os provedores de login
        List<AuthUI.IdpConfig> providers = Arrays.asList
                (
                    new AuthUI.IdpConfig.GoogleBuilder().build()
                    //new AuthUI.IdpConfig.EmailBuilder().build(),
                    //new AuthUI.IdpConfig.PhoneBuilder().build(),
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    //new AuthUI.IdpConfig.TwitterBuilder().build()
                );

        // Cria e lança a sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //startActivity(new Intent(MainActivity.this, PerfilPassageiroActivity.class));
                //ToastThis("Usuário" + user.getDisplayName() + "Logado com Sucesso");

                FirebaseUserMetadata metadata = FirebaseAuth.getInstance().getCurrentUser().getMetadata();
                // Verficiando se é a primeira vez que o o usuário entra
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    // Se for a primeira vez que o usuário entra, entao executa

                    /* Como e preciso saber se  o tipoUsuario e nulo ou não, para saber em
                    qual activity ele vai entrar, então mandei Strings com valor "nulo",
                    e salvei no database do firebase antes mesmo de termina o cadastro ,
                    assim se o usuario não termina o cadastro por algum motivo
                    fica um cadastro com valores nulos no database, e assim posso fazer a verificação
                    se o tipóUsuario e "motorista" , "passsageiro" ou "nulo", sem dar erro */
                    nomeUsuario = "nulo";
                    sobrenomeUsuario = "nulo";
                    telefoneUsuario = "nulo";
                    tipoUsuario = "nulo";
                    fotoUsuarioUrl = "nulo";
                    email = "nulo";
                    Usuario usuario = new Usuario();

                    usuario.setId(UsuarioFirebase.getIdentificadorUsuario());
                    usuario.setTipoUsuario(tipoUsuario);

                    usuario.salvar();

                    startActivity(new Intent(MainActivity.this, CadastroNomeUsuarioActivity.class));
                    finish();

                    } else {
                    UsuarioFirebase usuarioFirebase = new UsuarioFirebase();
                    // Método para Recuperar dados do usuario do database
                    DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().getReference()
                            .child("usuarios")
                            .child(usuarioFirebase.getIdentificadorUsuario());
                    usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            // Recuperando o tipo do usuário
                            String tipoU = usuario.getTipoUsuario();

                            if(tipoU.equals("motorista")){

                                startActivity(new Intent(getApplicationContext(), TestePerfilMotoristaActivity.class));
                                finish();
                            }else if(tipoU.equals("passageiro")){

                                startActivity(new Intent(getApplicationContext(), PerfilPassageiroActivity.class));
                                finish();

                            }else if (tipoU.equals("nulo")){

                                startActivity(new Intent(getApplicationContext(), CadastroNomeUsuarioActivity.class));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    }

            }else{

                ToastThis(response.getError().toString());
                // Login falhou. Se a resposta for null o usuário
                // cancelou com o  back button. De outra maneira veja:
                // response.getError().getErrorCode() e lide com o erro.
                // ...
            }
        }
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                   }
                } );

        ToastThis("Usuário deslogado");
        // [END auth_fui_signout]
    }

    public void abrirTelaCadastroTipoUsuario(View view){

        startActivity(new Intent(this, CadastroTipoUsuarioActivity.class));

    }
    //Método para tratamento da validação da permissão, caso não aceite
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for ( int permissaoResultado : grantResults ){
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED ){
                Permissao.alertaValidacaoPermissao(MainActivity.this);
            }
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}
