package com.example.travelpet.activity.cadastro.cadastroDadosAnimal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.R;

public class CadastroPorteAnimalActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroEspecieAnimal
    String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario,
           nomeAnimal, tipoEspecieAnimal, racaAnimal;

    private RadioGroup radioGroupPorteAnimal;

    String porteAnimal;

    //UsuarioFirebase usuarioRef = new UsuarioFirebase();
    //String idUsuario  = usuarioRef.getIdentificadorUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_porte_animal);

        // Recuperando dados passados da Activity <CadastroEspecieAnimal
        Bundle dados = getIntent().getExtras();

        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario         =     dados.getString("idUsuario");
        nomeUsuario       =     dados.getString("nomeUsuario");
        sobrenomeUsuario  =     dados.getString("sobrenomeUsuario");
        telefoneUsuario   =     dados.getString("telefoneUsuario");
        tipoUsuario       =     dados.getString("tipoUsuario");
        nomeAnimal        =     dados.getString("nomeAnimal");
        tipoEspecieAnimal =     dados.getString("tipoEspecieAnimal");
        racaAnimal        =     dados.getString("racaAnimal");

        // Referência o id do radioGroup do xml, com a variavel tipo radioGroup
        radioGroupPorteAnimal = findViewById(R.id.radioGroupPorteAnimal);

        // Chama o método verifica tipo especie
        verificaTipoPorte(porteAnimal);

    }

    public void abrirTelaPerfilApp(View view) {


        if (porteAnimal == "Pequeno - Até 35cm" || porteAnimal == "Médio - De 36 a 49cm"
                || porteAnimal == "Grande - Acima de 50cm") {

            // Cria uma referência para a classe Animal e instância ela
            Animal animal = new Animal();

            // Envia a dados para classe Animal
            animal.setIdUsuario(idUsuario);
            animal.setNomeAnimal(nomeAnimal);
            animal.setTipoEspecieAnimal(tipoEspecieAnimal);
            animal.setRacaAnimal(racaAnimal);
            animal.setPorte(porteAnimal);

            // Chama método salvar da classe Animal, responsável por salvar no firebase
            animal.salvarAnimal();


            Usuario usuario = new Usuario();

            usuario.setId(idUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            usuario.salvar();

            Toast.makeText(CadastroPorteAnimalActivity.this,
                    "Sucesso ao cadastrar Usuário Passageiro !",
                    Toast.LENGTH_SHORT).show();

            /*
            Intent intent = new Intent(getApplicationContext(),TesteFinalActivity.class);

            intent.putExtra("idUsuario",idUsuario);
            intent.putExtra("nomeUsuario",nomeUsuario);
            intent.putExtra("sobrenomeUsuario",sobrenomeUsuario);
            intent.putExtra("telefoneUsuario",telefoneUsuario);
            intent.putExtra("tipoUsuario",tipoUsuario);
            intent.putExtra("nomeAnimal",nomeAnimal);
            intent.putExtra("tipoEspecieAnimal",tipoEspecieAnimal);
            intent.putExtra("racaAnimal",racaAnimal);
            intent.putExtra("porteAnimal",porteAnimal);
                startActivity(intent);
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
