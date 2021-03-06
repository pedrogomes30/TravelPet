package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Endereco;

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

        iniciarComponentes();
        getDadosTelaAnterior(); //CadastroAnimalEspecieRaca
        verificarPorte(porteAnimal);

    }

    public void botaoProximo(View view) {

        if (validarDados()) {

            animal.setPorteAnimal(porteAnimal);

            Intent intent = new Intent(this, CadastroAnimalObservacaoActivity.class);
            intent.putExtra ("donoAnimal",donoAnimal);
            intent.putExtra ("endereco",endereco);
            intent.putExtra ("animal",animal);
            startActivity(intent);

        }
    }

    public void iniciarComponentes(){
        radioGroupPorteAnimal = findViewById(R.id.radioGroupPorteAnimal);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");
    }

    public void verificarPorte(String vp) {

        // .setOnCheckedChangeListener(); = Verifica qual item foi selecionado dentro do RadioGroup
        //new RadioGroup.OnCheckedChangeListener = imstancia um objeto que dentro dele tem um metodo para recuperar o item selecionado
        radioGroupPorteAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            // Método para recuperar o item selecionado
            // Em "int checkedId" = fica armazenado o item que foi escolhido
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonPequeno) {
                    porteAnimal = "pequeno";
                } else if (checkedId == R.id.radioButtonMedio) {
                    porteAnimal = "medio";
                } else if (checkedId == R.id.radioButtonGrande) {
                    porteAnimal = "grande";
                }else {
                    porteAnimal = null;
                }
            }
        });
    }

    public Boolean validarDados () {

        Boolean validado = false;

        if (porteAnimal == "pequeno" || porteAnimal == "medio"
                || porteAnimal == "grande") {

            validado = true;

        }else{
            Mensagem.toastIt("Selecione o porte do seu Animal", this);
        }

        return validado;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
