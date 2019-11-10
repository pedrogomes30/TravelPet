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

public class CadastroNomeAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroTipoUsuario,
    // ou do Fragment do perfil do Passageiro
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;
    // Variável para dizer de onde vem o fluxo de dado
    String fluxoDados;

    private TextInputEditText campoNomeANimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_animal);

        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();
        fluxoDados          =   usuario.getFluxoDados();

        campoNomeANimal = findViewById(R.id.editNomeAnimal);
    }
    
    public void buttonProximoNomeAnimal(View view){

       String nomeAnimal = campoNomeANimal.getText().toString().toUpperCase();;

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            Usuario usuario = new Usuario();
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setFluxoDados(fluxoDados);

            Animal animal = new Animal();
            animal.setNomeAnimal(nomeAnimal);

            Intent intent = new Intent(CadastroNomeAnimalActivity.this, CadastroEspecieAnimalActivity.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra ("animal", animal);
            startActivity(intent);

        // Se estiver vazio então envia essa mensagem
        }else{

            Toast.makeText(CadastroNomeAnimalActivity.this,
                    "Preencha o nome do animal",
                     Toast.LENGTH_SHORT).show();
        }
    }
}
