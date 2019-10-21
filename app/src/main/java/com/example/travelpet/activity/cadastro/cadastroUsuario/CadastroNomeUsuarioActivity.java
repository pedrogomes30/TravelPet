package com.example.travelpet.activity.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.activity.classes.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroNomeUsuarioActivity extends AppCompatActivity {

    /// Váriaveis usadas para armazenar dados dos campos do nome e sobrenome do xml
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario ;

    // Váriaveis usadas para referênciar dados dos campos do nome e sobrenome do xml
    private TextInputEditText campoNome,campoSobrenome, campoTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_usuario);

        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);
        campoTelefone   =   findViewById(R.id.editTelefone);

        // Criando mascara para o campo de nome
        SimpleMaskFormatter smfNome = new SimpleMaskFormatter("llllllllllllllllllllllllllllllll");
        MaskTextWatcher mtwNome = new MaskTextWatcher(campoNome, smfNome);
        // Referenciando que esse campo tem mascara
        campoNome.addTextChangedListener(mtwNome);

        // Criando marcara para o campo de telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(campoTelefone, smf);
        // Referenciando que esse campo tem mascara
        campoTelefone.addTextChangedListener(mtw);
    }

    // Evento de clique do botão PrcoximoNomeUsuario
    public void abrirTelaTelefoneUsuario(View view){

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

                //      Enviando dados para a Activity CadastroTelefoneActivity
                Intent intent = new Intent(CadastroNomeUsuarioActivity.this, CadastroTipoUsuarioActivity.class);

                // Cria uma chave para armazenar os arquivos que serão passados pela activity
                intent.putExtra("usuario",usuario);

                // Inicia Acitivity indica acima referênciado na intent
                startActivity(intent);

                }else{ // Se tefefoneUsuario estiver vazio então exibe está mensagem
                    Toast.makeText(CadastroNomeUsuarioActivity.this,
                            "Preencha o telefone corretamente",
                            Toast.LENGTH_SHORT).show();
                }
            }else{ // Se o campo Sobrenome estiver vazio então exibe está mensagem
                Toast.makeText(CadastroNomeUsuarioActivity.this,
                        "Preencha o Sobrenome",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroNomeUsuarioActivity.this,
                    "Preencha o Nome",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /* Método para não volta para a tela anterior
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
     */
}
