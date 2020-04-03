package com.example.travelpet.controlller.cadastro.cadastroAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroAnimalNomeActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroUsuarioTipo,
    // ou do Fragment do perfil do Passageiro
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;
    // Variável para dizer de onde vem o fluxo de dado
    private String fluxoDados;

    private TextInputEditText campoNomeANimal;

    private TextView textViewTeste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_nome);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        DonoAnimal donoAnimal = intent.getParcelableExtra("donoAnimal");

        nomeUsuario         =   donoAnimal.getNome();
        sobrenomeUsuario    =   donoAnimal.getSobrenome();
        telefoneUsuario     =   donoAnimal.getTelefone();
        tipoUsuario         =   donoAnimal.getTipoUsuario();
        fluxoDados          =   donoAnimal.getFluxoDados();

        campoNomeANimal = findViewById(R.id.editNomeAnimal);

        textViewTeste = findViewById(R.id.textViewTeste);
        textViewTeste.setText(fluxoDados);

    }
    
    public void buttonProximoNomeAnimal(View view){

       String nomeAnimal = campoNomeANimal.getText().toString().toUpperCase();;

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            DonoAnimal donoAnimal = new DonoAnimal();
            donoAnimal.setNome(nomeUsuario);
            donoAnimal.setSobrenome(sobrenomeUsuario);
            donoAnimal.setTelefone(telefoneUsuario);
            donoAnimal.setTipoUsuario(tipoUsuario);
            donoAnimal.setFluxoDados(fluxoDados);

            Animal animal = new Animal();
            animal.setNomeAnimal(nomeAnimal);

            Intent intent = new Intent(CadastroAnimalNomeActivity.this, CadastroAnimalEspecieRacaActivity.class);
            intent.putExtra("donoAnimal",donoAnimal);
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
