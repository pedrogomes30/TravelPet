package com.example.travelpet.activity.cadastro.cadastroUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroTelefoneUsuarioActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroNomeUsuario
    String idUsuario, nomeUsuario, sobrenomeUsuario;

    private TextInputEditText campoTelefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_telefone_usuario);

        // Recuperando dados passados da Activity <CadastroNomeUsuario
        Bundle dados = getIntent().getExtras();

        // Recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario           =   dados.getString("idUsuario");
        nomeUsuario         =   dados.getString("nomeUsuario");
        sobrenomeUsuario    =   dados.getString("sobrenomeUsuario");

        // Referenciando campoTelefone com o do xml
        campoTelefone = findViewById(R.id.editTelefone);

    }

    public void abrirTelaTipoUsuario(View view){

        // Recuperando textos dos campos, transformando em String e salvando nas variaveis
        String telefoneUsuario = campoTelefone.getText().toString();

        if(!telefoneUsuario.isEmpty()){

            Intent intent = new Intent(getApplicationContext(),CadastroTipoUsuarioActivity.class);

            intent.putExtra ("idUsuario",idUsuario);
            intent.putExtra ("nomeUsuario",nomeUsuario);
            intent.putExtra ("sobrenomeUsuario",sobrenomeUsuario);
            intent.putExtra ("telefoneUsuario",telefoneUsuario);

            startActivity(intent);
            //finish();

        }else{ // Se o campo Telefone estiver vazio então exibe está mensagem
        Toast.makeText(CadastroTelefoneUsuarioActivity.this,
                "Preencha o Telefone",
                Toast.LENGTH_SHORT).show();
        }

    }

}
