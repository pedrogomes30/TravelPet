package com.example.travelpet.activity.cadastro.cadastroUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroNomeUsuarioActivity extends AppCompatActivity {

    private TextInputEditText campoNome,campoSobrenome;

    String idUsuario;
    // Variaveis usadas para textos dos campos e passar para outras Activitys
    String nomeUsuario, sobrenomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_nome_usuario);

        // Recuperando dados passados da Activity <MainActivity
        Bundle dados = getIntent().getExtras();

        // recupera dados atrávez da key (chave) passada, , e armazena em uma nova variável
        idUsuario = dados.getString("idUsuario");


        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);

    }

    // Evento de clique do botão PrcoximoNomeUsuario
    public void abrirTelaTelefoneUsuario(View view){

        // Recuperando textos dos campos, transformando em String e salvando nas variaveis
        nomeUsuario = campoNome.getText().toString();
        sobrenomeUsuario = campoSobrenome.getText().toString();

        //Verificando se não estiver vazio
        if(!nomeUsuario.isEmpty()){
            if(!sobrenomeUsuario.isEmpty()){

                //      Enviando dados para a Activity CadastroTelefoneActivity
                Intent intent = new Intent(getApplicationContext(), CadastroTelefoneUsuarioActivity.class);

                // Envia dados através de uma chave nomeada, que é passada no 1º parâmetro
                // no 2º parâmetro e passado o dado
                intent.putExtra ("idUsuario",idUsuario);
                intent.putExtra ("nomeUsuario",nomeUsuario);
                intent.putExtra ("sobrenomeUsuario",sobrenomeUsuario);

                // Inicia Acitivity indica acima na intent
                startActivity(intent);
                //finish();


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
