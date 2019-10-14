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

public class CadastroPorteAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroEspecieAnimal
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
           nomeAnimal, especieAnimal, racaAnimal;
    String fluxoDados;
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
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();
        fluxoDados          =   usuario.getFluxoDados();

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

            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setFluxoDados(fluxoDados);

            Animal animal = new Animal();

            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);
            animal.setPorteAnimal(porteAnimal);

            Intent intent = new Intent(CadastroPorteAnimalActivity.this,CadastroFotoAnimalActivity.class);

            intent.putExtra ("usuario",usuario);
            intent.putExtra ("animal",animal);

            startActivity(intent);

            /*
            Usuario usuario = new Usuario();

            usuario.setId(UsuarioFirebase.getIdentificadorUsuario());
            usuario.setEmail(UsuarioFirebase.getEmailUsuario());
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            usuario.salvar();

            Animal animal = new Animal();

            animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
            animal.setNomeAnimal(nomeAnimal);
            animal.setEspecieAnimal(especieAnimal);
            animal.setRacaAnimal(racaAnimal);
            animal.setPorteAnimal(porteAnimal);

            animal.salvarAnimal();

            Toast.makeText(CadastroPorteAnimalActivity.this,
                    "Sucesso ao cadastrar Usuário Passageiro !",
                    Toast.LENGTH_SHORT).show();

             */

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
