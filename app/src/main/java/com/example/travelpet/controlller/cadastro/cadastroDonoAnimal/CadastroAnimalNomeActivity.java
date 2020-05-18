package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroAnimalNomeActivity extends AppCompatActivity {


    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private TextInputEditText campoNomeANimal;
    private String nomeAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_nome);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        animal = new Animal();

        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");

        campoNomeANimal = findViewById(R.id.editNomeAnimal);
    }
    
    public void botaoProximo(View view){

       nomeAnimal = campoNomeANimal.getText().toString();

        // Verifica se não esta vazia
        if(!nomeAnimal.isEmpty()) {

            animal.setNomeAnimal(nomeAnimal);

            Intent intent = new Intent(CadastroAnimalNomeActivity.this, CadastroAnimalEspecieRacaActivity.class);
            intent.putExtra("donoAnimal",donoAnimal);
            intent.putExtra("endereco",endereco);
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
