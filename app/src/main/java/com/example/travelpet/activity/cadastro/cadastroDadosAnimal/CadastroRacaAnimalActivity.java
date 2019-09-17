package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroRacaAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroEspecieAnimal
    String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
            nomeAnimal, tipoEspecieAnimal;

    private TextInputEditText campoRacaAnimal;

    String racaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_raca_animal);

        // Recuperando dados passados da Activity <CadastroEspecieAnimal
        Bundle dados = getIntent().getExtras();

        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario         =     dados.getString("idUsuario");
        nomeUsuario       =     dados.getString("nomeUsuario");
        sobrenomeUsuario  =     dados.getString("sobrenomeUsuario");
        telefoneUsuario   =     dados.getString("telefoneUsuario");
        tipoUsuario       =     dados.getString("tipoUsuario");
        nomeAnimal        =     dados.getString("nomeAnimal");
        tipoEspecieAnimal =     dados.getString("tipoEspecieAnimal");

        // Referenciando campoNomeAnimal com o do xml
        campoRacaAnimal = findViewById(R.id.editRacaAnimal);

    }

    public void abrirTelaPorteEspecial(View view){
        racaAnimal = campoRacaAnimal.getText().toString();

        if(!racaAnimal.isEmpty()){

            Intent intent = new Intent(getApplicationContext(),CadastroPorteAnimalActivity.class);

            intent.putExtra ("idUsuario",idUsuario);
            intent.putExtra ("nomeUsuario",nomeUsuario);
            intent.putExtra ("sobrenomeUsuario",sobrenomeUsuario);
            intent.putExtra ("telefoneUsuario",telefoneUsuario);
            intent.putExtra ("tipoUsuario",tipoUsuario);
            intent.putExtra ("nomeAnimal",nomeAnimal);
            intent.putExtra ("tipoEspecieAnimal",tipoEspecieAnimal);
            intent.putExtra ("racaAnimal",racaAnimal);

            startActivity(intent);
            //finish();

        }else{
            Toast.makeText(CadastroRacaAnimalActivity.this,
                    "Preencha a Raça do animal",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
