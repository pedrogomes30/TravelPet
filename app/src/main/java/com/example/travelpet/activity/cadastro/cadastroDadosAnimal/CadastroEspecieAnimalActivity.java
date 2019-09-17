package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.travelpet.R;

public class CadastroEspecieAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroNomeAnimal
    String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
                   nomeAnimal;

    private RadioGroup radioGroupTipoEspecie;

    String tipoEspecieAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_especie_animal);

        // Recuperando dados passados da Activity <CadastroNomeAnimal
        Bundle dados = getIntent().getExtras();


        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario         =     dados.getString("idUsuario");
        nomeUsuario       =     dados.getString("nomeUsuario");
        sobrenomeUsuario  =     dados.getString("sobrenomeUsuario");
        telefoneUsuario   =     dados.getString("telefoneUsuario");
        tipoUsuario       =     dados.getString("tipoUsuario");
        nomeAnimal        =     dados.getString("nomeAnimal");

        // Referência o id do radioGroup do xml, com a variavel tipo radioGroup
        radioGroupTipoEspecie = findViewById(R.id.radioGroupTipoEspecie);

        // Chama o método verifica tipo especie
        verificaTipoEspecie(tipoEspecieAnimal);

    }

    public void abrirTelaCadastroRacaAnimal(View view){

        if(tipoEspecieAnimal =="cachorro" || tipoEspecieAnimal == "gato" || tipoEspecieAnimal == "roedor"
                || tipoEspecieAnimal == "ave" || tipoEspecieAnimal == "réptil"){

            //      Enviando dados para a Activity CadastroRacaAnimal
            Intent intent = new Intent(getApplicationContext(), CadastroRacaAnimalActivity.class);

            // Envia dados atravez de uma chave nomeada, que é passada no 1º parâmetro
            // no 2º parâmetro e passado o dado
            intent.putExtra ("idUsuario",idUsuario);
            intent.putExtra ("nomeUsuario",nomeUsuario);
            intent.putExtra ("sobrenomeUsuario",sobrenomeUsuario);
            intent.putExtra ("telefoneUsuario",telefoneUsuario);
            intent.putExtra ("tipoUsuario",tipoUsuario);
            intent.putExtra ("nomeAnimal",nomeAnimal);
            intent.putExtra ("tipoEspecieAnimal",tipoEspecieAnimal);

            startActivity(intent);
            //finish();

        }else{ // Se o tipoEspecie estiver vazio então exibe está mensagem
            Toast.makeText(CadastroEspecieAnimalActivity.this, //onde será exibido
                    "Escolha uma opção de Espécie do seu Animal", // o que será exibido
                    Toast.LENGTH_SHORT).show(); // quanto tempo. show = monstrar executa
        }

    }

    // Método verificar tipo da espécie
    public  void verificaTipoEspecie(String vte) {

        // .setOnCheckedChangeListener(); =Verifica qual item foi selecionado dentro do RadioGroup
        //new RadioGroup.OnCheckedChangeListener = imstancia um objeto que dentro dele tem um metodo para recuperar o item selecionado
        radioGroupTipoEspecie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            // Cria uma referência para a classe usuario e instância ela
            // Método para recuperar o item selecionado
            // Em "int Checked" = fica armazenado o item que foi escolhido
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCachorro) {
                    tipoEspecieAnimal = "cachorro";
                } else if (checkedId == R.id.radioButtonGato) {
                    tipoEspecieAnimal = "gato";
                } else if (checkedId == R.id.radioButtonRoedor) {
                    tipoEspecieAnimal = "roedor";
                }else if (checkedId == R.id.radioButtonAve) {
                    tipoEspecieAnimal = "ave";
                }else if (checkedId == R.id.radioButtonReptil) {
                    tipoEspecieAnimal = "réptil";
                }else {
                    tipoEspecieAnimal = null;
                }
            }
        });
    }
}
