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

    // Variaveis usadas para pegar dados da Activity CadastroUsuarioDados,
    // ou do Fragment ListaAnimaisFragment
    private String tipoUsuario, nome, sobrenome, telefone;
    // Variável receber de onde vem o fluxo dos dados
    private String fluxoDados;

    private TextInputEditText campoNomeANimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_nome);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        DonoAnimal donoAnimal = intent.getParcelableExtra("donoAnimal");

        tipoUsuario  =   donoAnimal.getTipoUsuario();
        nome         =   donoAnimal.getNome();
        sobrenome    =   donoAnimal.getSobrenome();
        telefone     =   donoAnimal.getTelefone();
        fluxoDados   =   donoAnimal.getFluxoDados();

        campoNomeANimal = findViewById(R.id.editNomeAnimal);

    }
    
    public void botaoProximoAnimalNome(View view){

       String nomeAnimal = campoNomeANimal.getText().toString();

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            DonoAnimal donoAnimal = new DonoAnimal();
            donoAnimal.setTipoUsuario(tipoUsuario);
            donoAnimal.setNome(nome);
            donoAnimal.setSobrenome(sobrenome);
            donoAnimal.setTelefone(telefone);
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
