package com.example.travelpet.controlller.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.cadastro.cadastroAnimal.CadastroAnimalNomeActivity;
import com.example.travelpet.controlller.cadastro.cadastroMotorista.CadastroMotoristaTermoActivity;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroUsuarioDadosActivity extends AppCompatActivity {


    private String tipoUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario ;
    private String fluxoDados;

    // Váriaveis usadas para referênciar dados dos campos do nome e sobrenome do xml
    private TextInputEditText campoNome,campoSobrenome, campoTelefone;

    private TextView textViewTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados);

        // Efeito de Transição
        // 1ª (R.anim.activity_filho_entrando)= animação que vai executar pra activity que ta estrando
        // 2ª = Animação que vai executar pra activity que tiver saindo
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados da Activity (CadastroTipoUsuario)
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        tipoUsuario = usuario.getTipoUsuario();

        // Variável para referênciar fluxo de dados
        fluxoDados = "cadastroUsuario";

        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);
        campoTelefone   =   findViewById(R.id.editTelefone);

        textViewTipo    =   findViewById(R.id.textViewTipo);
        textViewTipo.setText(tipoUsuario);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(campoTelefone, smf);
        campoTelefone.addTextChangedListener(mtw);
    }

    // Evento de clique do botão PrcoximoNomeUsuario
    public void buttonProximoDadosUsuario(View view){

        // Recuperando textos dos campos, transformando em String e salvando nas variaveis
        nomeUsuario         =   campoNome.getText().toString().toUpperCase();
        sobrenomeUsuario    =   campoSobrenome.getText().toString().toUpperCase();
        telefoneUsuario     =   campoTelefone.getText().toString();

        //Verificando se não estiver vazio
        if(!nomeUsuario.isEmpty()){
            if(!sobrenomeUsuario.isEmpty()){
                if(!telefoneUsuario.isEmpty() && telefoneUsuario.length() == 15){

                    if(tipoUsuario.equals("passageiro")) {

                        // Cria referência a classe Usuario
                        DonoAnimal donoAnimal = new DonoAnimal();

                        //Envia dados para a classe Usuario
                        donoAnimal.setNome(nomeUsuario);
                        donoAnimal.setSobrenome(sobrenomeUsuario);
                        donoAnimal.setTelefone(telefoneUsuario);
                        donoAnimal.setTipoUsuario(tipoUsuario);
                        donoAnimal.setFluxoDados(fluxoDados);

                        Intent intent = new Intent(this, CadastroAnimalNomeActivity.class);
                        // Cria uma chave para armazenar os arquivos que serão passados pela activity
                        intent.putExtra("donoAnimal", donoAnimal);
                        // Inicia Acitivity indica acima referênciado na intent
                        startActivity(intent);

                    }else if(tipoUsuario.equals("motorista")){
                        // Cria referência a classe Usuario
                        Motorista  motorista = new Motorista();

                        //Envia dados para a classe Usuario
                        motorista.setNome(nomeUsuario);
                        motorista.setSobrenome(sobrenomeUsuario);
                        motorista.setTelefone(telefoneUsuario);
                        motorista.setTipoUsuario(tipoUsuario);


                        Intent intent = new Intent(this, CadastroMotoristaTermoActivity.class);
                        intent.putExtra("motorista", motorista);
                        startActivity(intent);

                    }

                }else{ // Se tefefoneUsuario estiver vazio então exibe está mensagem
                    Toast.makeText(CadastroUsuarioDadosActivity.this,
                            "Preencha o telefone corretamente",
                            Toast.LENGTH_SHORT).show();
                }
            }else{ // Se o campo Sobrenome estiver vazio então exibe está mensagem
                Toast.makeText(CadastroUsuarioDadosActivity.this,
                        "Preencha o Sobrenome",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroUsuarioDadosActivity.this,
                    "Preencha o Nome",
                    Toast.LENGTH_SHORT).show();
        }
    }
    // Chamado quando clica no botão voltar do aparelho
    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
