package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Endereco;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroAnimalNomeActivity extends AppCompatActivity {


    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private TextInputEditText campoNomeAnimal;
    private String nomeAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_nome);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); //CadastroUsuarioDadosActivity
    }

    public void botaoProximo(View view){

        getDadosDigitados();

        if(validarDados()) {

            animal.setNomeAnimal(nomeAnimal);

            Intent intent = new Intent(this, CadastroAnimalEspecieRacaActivity.class);
            intent.putExtra("donoAnimal",donoAnimal);
            intent.putExtra("endereco",endereco);
            intent.putExtra ("animal", animal);
            startActivity(intent);

        }
    }

    public void iniciarComponentes(){
        animal = new Animal();
        campoNomeAnimal = findViewById(R.id.editNomeAnimal);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
    }

    public void getDadosDigitados(){
        nomeAnimal = campoNomeAnimal.getText().toString();
    }

    public Boolean validarDados () {

        Boolean validado = false;

        if(!VerificaDado.isVazio(nomeAnimal)){

            validado = true;
        } else {
            Mensagem.toastIt("Preencha o nome do animal", this);
        }
        return validado;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
