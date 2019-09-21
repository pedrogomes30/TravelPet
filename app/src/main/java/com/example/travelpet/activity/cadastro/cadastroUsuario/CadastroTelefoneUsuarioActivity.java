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

public class CadastroTelefoneUsuarioActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroNomeUsuario
    String idUsuario,emailUsuario, nomeUsuario, sobrenomeUsuario;

    private TextInputEditText campoTelefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_telefone_usuario);

        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        idUsuario           =   usuario.getId();
        emailUsuario        =   usuario.getEmail();
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();


        campoTelefone = findViewById(R.id.editTelefone);

        // Criando marcara para o campo de telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(campoTelefone, smf);
        // Referenciando que esse campo tem mascara
        campoTelefone.addTextChangedListener(mtw);
    }

    public void abrirTelaTipoUsuario(View view){


        String telefoneUsuario = campoTelefone.getText().toString();

        // Se tefefoneUsuario não for vazio e tiver o tamanho igual a 15 então executa
        if(!telefoneUsuario.isEmpty() && telefoneUsuario.length() == 15){

            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            usuario.setEmail(emailUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);


            Intent intent = new Intent(getApplicationContext(),CadastroTipoUsuarioActivity.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);

        }else{ // Se tefefoneUsuario estiver vazio então exibe está mensagem
        Toast.makeText(CadastroTelefoneUsuarioActivity.this,
                "Preencha o telefone corretamente",
                Toast.LENGTH_SHORT).show();
        }

    }

}
