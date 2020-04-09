package com.example.travelpet.controlller.cadastro.cadastroAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroAnimalObservacaoActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroAnimalPorte
    private String tipoUsuario,nome, sobrenome, telefone,
                   nomeAnimal, especieAnimal, racaAnimal, porteAnimal;

    private String fluxoDados;

    private TextInputEditText campoObservacaoAnimal;
    private String observacaoAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_observacao);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        DonoAnimal donoAnimal = intent.getParcelableExtra("donoAnimal");
        Animal animal = intent.getParcelableExtra("animal");

        // Dados DonoAnimal
        tipoUsuario  =   donoAnimal.getTipoUsuario();
        nome         =   donoAnimal.getNome();
        sobrenome    =   donoAnimal.getSobrenome();
        telefone     =   donoAnimal.getTelefone();
        fluxoDados   =   donoAnimal.getFluxoDados();

        // Dados Animal
        nomeAnimal          =   animal.getNomeAnimal();
        especieAnimal       =   animal.getEspecieAnimal();
        racaAnimal          =   animal.getRacaAnimal();
        porteAnimal         =   animal.getPorteAnimal();

        campoObservacaoAnimal = findViewById(R.id.editObservacaoAnimal);

    }

    public void botaoProximoAnimalObservacao(View view) {

        observacaoAnimal = campoObservacaoAnimal.getText().toString();

        // Verifica se não esta vazia
        if(!observacaoAnimal.isEmpty()) {

            DonoAnimal donoAnimal = new DonoAnimal();
            donoAnimal.setTipoUsuario(tipoUsuario);
            donoAnimal.setNome(nome);
            donoAnimal.setSobrenome(sobrenome);
            donoAnimal.setTelefone(telefone);
            donoAnimal.setFluxoDados(fluxoDados);

            Animal animal = new Animal();
            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);
            animal.setPorteAnimal(porteAnimal);
            animal.setObservacaoAnimal(observacaoAnimal);

            Intent intent = new Intent(CadastroAnimalObservacaoActivity.this, CadastroAnimalFotoActivity.class);
            intent.putExtra("donoAnimal",donoAnimal);
            intent.putExtra ("animal", animal);
            startActivity(intent);


            // Se estiver vazio então envia essa mensagem
        }else{

            Toast.makeText(CadastroAnimalObservacaoActivity.this,
                    "Preencha o campo Observações",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }

}

