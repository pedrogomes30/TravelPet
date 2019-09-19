package com.example.travelpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroNomeUsuarioActivity;
import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroTipoUsuarioActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;



    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    SignInButton BTSignIn;
    Button BTSignOut;

    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();
        BTSignIn = findViewById(R.id.sign_in_button);
        BTSignOut = findViewById(R.id.sign_out_button);
        setButtons();
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
                // O login foi feito com sucesso
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // ...
                // Pega o id do Usuario atual e armazena na variável String idUsuario
                idUsuario = user.getUid();

                /* Método inicia Activity CadastroNome...
                   Só coloquei para testes ainda será necessario avaliar se o usuario tem
                   cadastro pronto no firebase para poder indicar a Activity adequada
                   para a situação
                 */

                Intent intent = new Intent(getApplicationContext(),CadastroNomeUsuarioActivity.class);
                intent.putExtra("idUsuario",idUsuario);
                startActivity(intent);

                ToastThis("Usuário" + user.getDisplayName() + "Logado com Sucesso");

            } else {

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
                });

        ToastThis("Usuário deslogado");
        // [END auth_fui_signout]
    }

    public void abrirTelaCadastroTipoUsuario(View view){

        startActivity(new Intent(this, CadastroTipoUsuarioActivity.class));

    }


}
