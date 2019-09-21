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

public class CadastroNomeAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroTipoUsuario
     String idUsuario, emailUsuario, nomeUsuario,
            sobrenomeUsuario, telefoneUsuario,tipoUsuario;

    private TextInputEditText campoNomeANimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_animal);

        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        idUsuario           =   usuario.getId();
        emailUsuario        =   usuario.getEmail();
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        campoNomeANimal = findViewById(R.id.editNomeAnimal);

    }
    
    public void abriTelaCadastroEspecieAnimal(View view){

       String nomeAnimal = campoNomeANimal.getText().toString().toUpperCase();;

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            Usuario usuario = new Usuario();

            usuario.setId(idUsuario);
            usuario.setEmail(emailUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            Animal animal = new Animal();

            animal.setNomeAnimal(nomeAnimal);

            Intent intent = new Intent(getApplicationContext(), CadastroEspecieAnimalActivity.class);
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
