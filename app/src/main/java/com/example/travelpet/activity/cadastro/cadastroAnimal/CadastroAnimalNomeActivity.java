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

public class CadastroAnimalNomeActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroUsuarioTipo,
    // ou do Fragment do perfil do Passageiro
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;
    // Variável para dizer de onde vem o fluxo de dado
    private String fluxoDados;

    private TextInputEditText campoNomeANimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_nome);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

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

            Intent intent = new Intent(CadastroAnimalNomeActivity.this, CadastroAnimalEspecieRacaActivity.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra ("animal", animal);
            startActivity(intent);


        // Se estiver vazio então envia essa mensagem
        }else{

            Toast.makeText(CadastroAnimalNomeActivity.this,
                    "Preencha o nome do animal",
                     Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
