package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Endereco;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroAnimalObservacaoActivity extends AppCompatActivity {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private TextInputEditText campoObservacaoAnimal;
    private String observacaoAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_observacao);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponenetes();
        getDadosTelaAnterior(); //CadastroAnimalPorte

    }

    public void botaoProximo(View view) {

        getDadosDigitados();
        animal.setObservacaoAnimal(observacaoAnimal);

        Intent intent = new Intent(this, CadastroAnimalFotoActivity.class);
        intent.putExtra("donoAnimal", donoAnimal);
        intent.putExtra("endereco", endereco);
        intent.putExtra("animal", animal);
        startActivity(intent);
    }

    public void iniciarComponenetes(){
        campoObservacaoAnimal = findViewById(R.id.editObservacaoAnimal);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");
    }

    public void getDadosDigitados(){
        observacaoAnimal = campoObservacaoAnimal.getText().toString();
    }

    @Override
    public  void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }


}

