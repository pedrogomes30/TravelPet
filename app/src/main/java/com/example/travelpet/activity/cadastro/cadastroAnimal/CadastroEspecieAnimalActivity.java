package com.example.travelpet.activity.cadastro.cadastroAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.Usuario;

public class CadastroEspecieAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroNomeAnimal
    String  nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
            nomeAnimal;

    String fluxoDados;

    private RadioGroup radioGroupTipoEspecie;

    String especieAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_especie_animal);

        // Recuperando dados passados da Activity <CadastroNomeAnimal
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Animal animal = intent.getParcelableExtra("animal");

        // Dados da Classe Usuario
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();
        fluxoDados          =   usuario.getFluxoDados();

        // Dados da Classe Animal
        nomeAnimal          =   animal.getNomeAnimal();


        // Referência o id do radioGroup do xml, com a variavel tipo radioGroup
        radioGroupTipoEspecie = findViewById(R.id.radioGroupTipoEspecie);

        // Chama o método verifica tipo especie
        verificaTipoEspecie(especieAnimal);

    }

    public void abrirTelaCadastroRacaAnimal(View view){

        if(especieAnimal =="CACHORRO" || especieAnimal == "GATO" || especieAnimal == "ROEDOR"
                || especieAnimal == "AVE" || especieAnimal == "REPTIL"){

            Usuario usuario = new Usuario();

            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setFluxoDados(fluxoDados);

            Animal animal = new Animal();

            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);

            Intent intent = new Intent(CadastroEspecieAnimalActivity.this, CadastroRacaAnimalActivity.class);

            intent.putExtra ("usuario",usuario);
            intent.putExtra ("animal",animal);

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
                    especieAnimal = "CACHORRO";
                } else if (checkedId == R.id.radioButtonGato) {
                    especieAnimal = "GATO";
                } else if (checkedId == R.id.radioButtonRoedor) {
                    especieAnimal = "ROEDOR";
                }else if (checkedId == R.id.radioButtonAve) {
                    especieAnimal = "AVE";
                }else if (checkedId == R.id.radioButtonReptil) {
                    especieAnimal = "REPTIL";
                }else {
                    especieAnimal = null;
                }
            }
        });
    }
}
