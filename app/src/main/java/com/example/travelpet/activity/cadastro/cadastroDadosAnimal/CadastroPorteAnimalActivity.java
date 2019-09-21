package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.R;

public class CadastroPorteAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroEspecieAnimal
    String idUsuario, emailUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
           nomeAnimal, especieAnimal, racaAnimal;

    private RadioGroup radioGroupPorteAnimal;

    String porteAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_porte_animal);


        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Animal animal = intent.getParcelableExtra("animal");

        // Dados da classe Usuario
        idUsuario           =   usuario.getId();
        emailUsuario        =   usuario.getEmail();
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        // Dados da classe Animal
        nomeAnimal          =   animal.getNomeAnimal();
        especieAnimal       =   animal.getEspecieAnimal();
        racaAnimal          =   animal.getRacaAnimal();


        // Referência o id do radioGroup do xml, com a variavel tipo radioGroup
        radioGroupPorteAnimal = findViewById(R.id.radioGroupPorteAnimal);

        // Chama o método verifica tipo porte
        verificaTipoPorte(porteAnimal);

    }

    public void abrirTelaPerfilApp(View view) {


        if (porteAnimal == "Pequeno - Até 35cm" || porteAnimal == "Médio - De 36 a 49cm"
                || porteAnimal == "Grande - Acima de 50cm") {


            Usuario usuario = new Usuario();

            usuario.setId(idUsuario);
            usuario.setEmail(emailUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            usuario.salvar();

            Animal animal = new Animal();

            animal.setIdUsuario(idUsuario);
            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);
            animal.setPorteAnimal(porteAnimal);

            animal.salvarAnimal();

            Toast.makeText(CadastroPorteAnimalActivity.this,
                    "Sucesso ao cadastrar Usuário Passageiro !",
                    Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(CadastroPorteAnimalActivity.this, //onde será exibido
                    "Selecione o porte do seu Animal", // o que será exibido
                    Toast.LENGTH_SHORT).show();
        }
    }

    public  void verificaTipoPorte(String vtp) {

        // .setOnCheckedChangeListener(); = Verifica qual item foi selecionado dentro do RadioGroup
        //new RadioGroup.OnCheckedChangeListener = imstancia um objeto que dentro dele tem um metodo para recuperar o item selecionado
        radioGroupPorteAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            // Cria uma referência para a classe usuario e instância ela
            // Método para recuperar o item selecionado
            // Em "int Checked" = fica armazenado o item que foi escolhido
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

}
