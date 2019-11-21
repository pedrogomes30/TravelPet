package com.example.travelpet.activity.cadastro.cadastroAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroRacaAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroEspecieAnimal
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
            nomeAnimal, especieAnimal;

    private TextInputEditText campoRacaAnimal;

    private String racaAnimal;

    private String fluxoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_raca_animal);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Animal animal = intent.getParcelableExtra("animal");

        // Dados da Classe Usuario
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();
        fluxoDados          =   usuario.getFluxoDados();

        // Dados da Classe Animal
        nomeAnimal          =   animal.getNomeAnimal();
        especieAnimal       =   animal.getEspecieAnimal();

        // Referenciando campoNomeAnimal com o do xml
        campoRacaAnimal = findViewById(R.id.editRacaAnimal);

    }

    public void buttonProximoRacaAnimal(View view){

        racaAnimal = campoRacaAnimal.getText().toString().toUpperCase();

        if(!racaAnimal.isEmpty()){

            Usuario usuario = new Usuario();

            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setFluxoDados(fluxoDados);

            Animal animal = new Animal();

            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);

            Intent intent = new Intent(CadastroRacaAnimalActivity.this,CadastroPorteAnimalActivity.class);
            intent.putExtra ("usuario",usuario);
            intent.putExtra ("animal",animal);
            startActivity(intent);


        }else{
            Toast.makeText(CadastroRacaAnimalActivity.this,
                    "Preencha a Raça do animal",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
