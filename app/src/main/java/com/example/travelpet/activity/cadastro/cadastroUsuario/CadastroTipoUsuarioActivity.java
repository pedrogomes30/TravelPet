package com.example.travelpet.activity.cadastro.cadastroUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelpet.activity.cadastro.cadastroDadosAnimal.CadastroNomeAnimalActivity;
import com.example.travelpet.R;

public class CadastroTipoUsuarioActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroTelefoneUsuario
    String idUsuario, nomeUsuario, sobrenomeUsuario, telefoneUsuario;

    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo_usuario);

        // Recuperando dados passados da Activity <CadastroTelefoneUsuario
        Bundle dados = getIntent().getExtras();

        // recupera dados atrávez da key (chave) passada, e armazena em uma nova variável
        idUsuario           =   dados.getString("idUsuario");
        nomeUsuario         =   dados.getString("nomeUsuario");
        sobrenomeUsuario    =   dados.getString("sobrenomeUsuario");
        telefoneUsuario     =   dados.getString("telefoneUsuario");

    }

    // Evento de onClick do botão "Passageiro"
    public void passageiroAbrirTelaCadastroUsuario( View view ){


        tipoUsuario = "passageiro";

        Intent intent = new Intent(getApplicationContext(), CadastroNomeAnimalActivity.class);

        // Envia dados atravez de uma chave nomeada, que é passada no 1º parâmetro
        // no 2º parâmetro e passado o dado
        intent.putExtra ("idUsuario",idUsuario);
        intent.putExtra ("nomeUsuario",nomeUsuario);
        intent.putExtra ("sobrenomeUsuario",sobrenomeUsuario);
        intent.putExtra ("telefoneUsuario",telefoneUsuario);
        intent.putExtra ("tipoUsuario",tipoUsuario);


        startActivity(intent);
        //finish();

    }

    /* Evento de onClick do botão "Motorista"
   public void motoristaAbrirTelaCadastroMotorista( View view ){

        tipoUsuario = "motorista";

    }*/

}
