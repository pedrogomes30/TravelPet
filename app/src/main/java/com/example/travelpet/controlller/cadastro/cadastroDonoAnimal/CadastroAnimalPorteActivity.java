package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;

public class CadastroAnimalPorteActivity extends AppCompatActivity {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private RadioGroup radioGroupPorteAnimal;
    private String porteAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_porte);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");

        radioGroupPorteAnimal = findViewById(R.id.radioGroupPorteAnimal);

        verificaTipoPorte(porteAnimal);

    }

    public void botaoProximo(View view) {

        if (porteAnimal == "Pequeno - Até 35cm" || porteAnimal == "Médio - De 36 a 49cm"
                || porteAnimal == "Grande - Acima de 50cm") {

            animal.setPorteAnimal(porteAnimal);

            Intent intent = new Intent(CadastroAnimalPorteActivity.this, CadastroAnimalObservacaoActivity.class);
            intent.putExtra ("donoAnimal",donoAnimal);
            intent.putExtra ("endereco",endereco);
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
