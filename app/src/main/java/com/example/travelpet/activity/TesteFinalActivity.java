package com.example.travelpet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.travelpet.R;

public class TesteFinalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroEspecieAnimal
    String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
            nomeAnimal, tipoEspecieAnimal, racaAnimal, porteAnimal;

    private TextView campoIdUsuario, campoNomeUsuario,campoSobrenome,campoTelefone,
                     campoNomeAnimal,campoEspecie, campoRaca, campoPorte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_final);

        Bundle dados = getIntent().getExtras();

        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario         =     dados.getString("idUsuario");
        nomeUsuario       =     dados.getString("nomeUsuario");
        sobrenomeUsuario  =     dados.getString("sobrenomeUsuario");
        telefoneUsuario   =     dados.getString("telefoneUsuario");
        tipoUsuario       =     dados.getString("tipoUsuario");
        nomeAnimal        =     dados.getString("nomeAnimal");
        tipoEspecieAnimal =     dados.getString("tipoEspecieAnimal");
        racaAnimal        =     dados.getString("racaAnimal");
        porteAnimal       =     dados.getString("porteAnimal");

        campoIdUsuario = findViewById(R.id.textViewIdUsuario);
        campoNomeUsuario = findViewById(R.id.textViewNomeUsuario);
        campoSobrenome = findViewById(R.id.textViewSobrenomeUsuario);
        campoTelefone = findViewById(R.id.textViewTelefone);
        campoNomeAnimal = findViewById(R.id.textViewNomeAnimal);
        campoEspecie = findViewById(R.id.textViewEspecie);
        campoRaca = findViewById(R.id.textViewRaca);
        campoPorte = findViewById(R.id.textViewPorte);

        campoIdUsuario.setText(idUsuario);
        campoNomeUsuario.setText(nomeUsuario);
        campoSobrenome.setText(sobrenomeUsuario);
        campoTelefone.setText(telefoneUsuario);
        campoNomeAnimal.setText(nomeAnimal);
        campoEspecie.setText(tipoEspecieAnimal);
        campoRaca.setText(racaAnimal);
        campoPorte.setText(porteAnimal);
    }
}
