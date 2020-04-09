package com.example.travelpet.controlller.cadastro.cadastroAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;

public class CadastroAnimalPorteActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroAnimalEspecieRaca
    private String tipoUsuario, nome, sobrenome, telefone,
                   nomeAnimal, especieAnimal, racaAnimal;

    private String fluxoDados;

    private RadioGroup radioGroupPorteAnimal;

    private String porteAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_porte);

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

        // Referência o id do radioGroup do xml, com a variavel tipo radioGroup
        radioGroupPorteAnimal = findViewById(R.id.radioGroupPorteAnimal);

        // Chama o método verifica tipo porte
        verificaTipoPorte(porteAnimal);

    }

    public void botaoProximoAnimalPorte(View view) {

        if (porteAnimal == "Pequeno - Até 35cm" || porteAnimal == "Médio - De 36 a 49cm"
                || porteAnimal == "Grande - Acima de 50cm") {

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

            Intent intent = new Intent(CadastroAnimalPorteActivity.this, CadastroAnimalObservacaoActivity.class);
            intent.putExtra ("donoAnimal",donoAnimal);
            intent.putExtra ("animal",animal);
            startActivity(intent);

        }else{
            Toast.makeText(CadastroAnimalPorteActivity.this,
                    "Selecione o porte do seu Animal",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void verificaTipoPorte(String vtp) {

        // .setOnCheckedChangeListener(); = Verifica qual item foi selecionado dentro do RadioGroup
        //new RadioGroup.OnCheckedChangeListener = imstancia um objeto que dentro dele tem um metodo para recuperar o item selecionado
        radioGroupPorteAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            // Método para recuperar o item selecionado
            // Em "int checkedId" = fica armazenado o item que foi escolhido
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonPequeno) {
                    porteAnimal = "Pequeno - Até 35cm";
                } else if (checkedId == R.id.radioButtonMedio) {
                    porteAnimal = "Médio - De 36 a 49cm";
                } else if (checkedId == R.id.radioButtonGrande) {
                    porteAnimal = "Grande - Acima de 50cm";
                }else {
                    porteAnimal = null;
                }

            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
