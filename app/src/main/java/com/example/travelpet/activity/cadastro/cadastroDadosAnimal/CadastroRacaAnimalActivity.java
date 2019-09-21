package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroRacaAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroEspecieAnimal
    String idUsuario, emailUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
           nomeAnimal, especieAnimal;

    private TextInputEditText campoRacaAnimal;

    String racaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_raca_animal);

        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Animal animal = intent.getParcelableExtra("animal");

        // Dados da Classe Usuario
        idUsuario           =   usuario.getId();
        emailUsuario        =   usuario.getEmail();
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        // Dados da Classe Animal
        nomeAnimal          =   animal.getNomeAnimal();
        especieAnimal       =   animal.getEspecieAnimal();

        // Referenciando campoNomeAnimal com o do xml
        campoRacaAnimal = findViewById(R.id.editRacaAnimal);

    }

    public void abrirTelaPorteEspecial(View view){

        racaAnimal = campoRacaAnimal.getText().toString().toUpperCase();

        if(!racaAnimal.isEmpty()){

            Usuario usuario = new Usuario();

            usuario.setId(idUsuario);
            usuario.setEmail(emailUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            Animal animal = new Animal();

            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);

            Intent intent = new Intent(getApplicationContext(),CadastroPorteAnimalActivity.class);

            intent.putExtra ("usuario",usuario);
            intent.putExtra ("animal",animal);

            startActivity(intent);

        }else{
            Toast.makeText(CadastroRacaAnimalActivity.this,
                    "Preencha a Raça do animal",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
