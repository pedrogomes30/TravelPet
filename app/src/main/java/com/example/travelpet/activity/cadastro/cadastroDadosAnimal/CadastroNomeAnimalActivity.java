package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroNomeAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroTipoUsuario
     String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;

    private TextInputEditText campoNomeANimal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_animal);

        // Recuperando dados passados da Activity <CadastroTipoUsuario
        Bundle dados = getIntent().getExtras();


        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario         =     dados.getString("idUsuario");
        nomeUsuario       =     dados.getString("nomeUsuario");
        sobrenomeUsuario  =     dados.getString("sobrenomeUsuario");
        telefoneUsuario   =     dados.getString("telefoneUsuario");
        tipoUsuario       =     dados.getString("tipoUsuario");

        // Referenciando campoNomeAnimal com o do xml
        campoNomeANimal = findViewById(R.id.editNomeAnimal);

    }

    public void abriTelaCadastroEspecieAnimal(View view){

       String nomeAnimal = campoNomeANimal.getText().toString();

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            Intent intent = new Intent(getApplicationContext(), CadastroEspecieAnimalActivity.class);

            intent.putExtra ("idUsuario", idUsuario);
            intent.putExtra ("nomeUsuario", nomeUsuario);
            intent.putExtra ("sobrenomeUsuario", sobrenomeUsuario);
            intent.putExtra ("telefoneUsuario", telefoneUsuario);
            intent.putExtra ("tipoUsuario", tipoUsuario);
            intent.putExtra ("nomeAnimal", nomeAnimal);

            startActivity(intent);
            //finish();

        // Se estiver vazio então envia essa mensagem
        }else{

            Toast.makeText(CadastroNomeAnimalActivity.this,
                    "Preencha o nome do animal",
                     Toast.LENGTH_SHORT).show();

        }

    }

}
