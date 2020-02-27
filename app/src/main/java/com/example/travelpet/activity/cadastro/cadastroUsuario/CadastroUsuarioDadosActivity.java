package com.example.travelpet.activity.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.activity.cadastro.cadastroAnimal.CadastroAnimalNomeActivity;
import com.example.travelpet.activity.cadastro.cadastroMotorista.CadastroMotoristaTermoActivity;
import com.example.travelpet.classes.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroUsuarioDadosActivity extends AppCompatActivity {


    private String tipoUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario ;
    private String fluxoDados;

    // Váriaveis usadas para referênciar dados dos campos do nome e sobrenome do xml
    private TextInputEditText campoNome,campoSobrenome, campoTelefone;

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

        /* Criando mascara para o campo de nome
        SimpleMaskFormatter smfNome = new SimpleMaskFormatter("LLL");
        MaskTextWatcher mtwNome = new MaskTextWatcher(campoNome, smfNome);
        // Referenciando que esse campo tem mascara
        campoNome.addTextChangedListener(mtwNome);
        */

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

                    // Cria referência a classe Usuario
                    Usuario usuario = new Usuario();

                    //Envia dados para a classe Usuario
                    usuario.setNome(nomeUsuario);
                    usuario.setSobrenome(sobrenomeUsuario);
                    usuario.setTelefone(telefoneUsuario);
                    usuario.setTipoUsuario(tipoUsuario);
                    usuario.setFluxoDados(fluxoDados);

                    if(tipoUsuario.equals("passageiro")) {
                    Intent intent = new Intent(this, CadastroAnimalNomeActivity.class);
                    // Cria uma chave para armazenar os arquivos que serão passados pela activity
                    intent.putExtra("usuario", usuario);
                    // Inicia Acitivity indica acima referênciado na intent
                    startActivity(intent);


                    }else if(tipoUsuario.equals("motorista")){

                        Intent intent = new Intent(this, CadastroMotoristaTermoActivity.class);
                        intent.putExtra("usuario", usuario);
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
