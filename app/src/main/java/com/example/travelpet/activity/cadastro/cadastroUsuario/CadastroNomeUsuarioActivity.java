package com.example.travelpet.activity.cadastro.cadastroUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.example.travelpet.classes.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroNomeUsuarioActivity extends AppCompatActivity {

    // Variáveis usadas para armazenar dados da MainActivity
    String idUsuario, emailUsuario;

    /// Váriaveis usadas para armazenar dados dos campos do nome e sobrenome do xml
    String nomeUsuario, sobrenomeUsuario;

    // Váriaveis usadas para referênciar dados dos campos do nome e sobrenome do xml
    private TextInputEditText campoNome,campoSobrenome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_usuario);

        // Recuperando dados passados da MainActivity
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        // Armazena os dados recuperados em uma um variável String
        idUsuario       =   usuario.getId();
        emailUsuario    =   usuario.getEmail();

        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);

        /* Métorna obsoleto, pois depedendo do tipo de letra
           ele pode pode digitar + ou - letras no campo
        // Criando marcara para o campo de telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("Lllllllllllllllllllllllllllllll");
        MaskTextWatcher mtw = new MaskTextWatcher(campoNome, smf);
        // Referenciando que esse campo tem mascara
        campoNome.addTextChangedListener(mtw);

         */
    }

    // Evento de clique do botão PrcoximoNomeUsuario
    public void abrirTelaTelefoneUsuario(View view){

        // Recuperando textos dos campos, transformando em String e salvando nas variaveis
        nomeUsuario         =   campoNome.getText().toString().toUpperCase();;
        sobrenomeUsuario    =   campoSobrenome.getText().toString().toUpperCase();;

        //Verificando se não estiver vazio
        if(!nomeUsuario.isEmpty()){
            if(!sobrenomeUsuario.isEmpty()){

                // Cria referência a classe Usuario
                Usuario usuario = new Usuario();

                //Envia dados para a classe Usuario
                usuario.setId(idUsuario);
                usuario.setEmail(emailUsuario);
                usuario.setNome(nomeUsuario);
                usuario.setSobrenome(sobrenomeUsuario);

                //      Enviando dados para a Activity CadastroTelefoneActivity
                Intent intent = new Intent(getApplicationContext(), CadastroTelefoneUsuarioActivity.class);

                    // Cria uma chave para armazenar os arquivos que serão passados pela activity
                intent.putExtra("usuario",usuario);

                // Inicia Acitivity indica acima referênciado na intent
                startActivity(intent);


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
}
