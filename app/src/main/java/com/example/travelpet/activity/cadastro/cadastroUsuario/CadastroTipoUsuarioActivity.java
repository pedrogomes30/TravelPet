package com.example.travelpet.activity.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.activity.cadastro.cadastroAnimal.CadastroNomeAnimalActivity;
import com.example.travelpet.activity.cadastro.cadastroMotorista.CadastroTermoMotoristaActivity;
import com.example.travelpet.classes.Usuario;

public class CadastroTipoUsuarioActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroTelefoneUsuario
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario;
    String fluxoDados = "cadastroUsuario";
    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo_usuario);

        // Recuperando dados passados da Activity CadastroTelefoneUsuario
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();

    }

    // Evento de onClick do botão "Passageiro"
    public void abrirTelaCadastroNomeAnimal( View view ){

        tipoUsuario = "passageiro";

        Usuario usuario = new Usuario();

        usuario.setNome(nomeUsuario);
        usuario.setSobrenome(sobrenomeUsuario);
        usuario.setTelefone(telefoneUsuario);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setFluxoDados(fluxoDados);

        Intent intent = new Intent(CadastroTipoUsuarioActivity.this, CadastroNomeAnimalActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);

    }

    // Evento de onClick do botão "Motorista"
    public void abrirTelaCadastroTermoMotorista( View view ){

        tipoUsuario = "motorista";

        Usuario usuario = new Usuario();

        usuario.setNome(nomeUsuario);
        usuario.setSobrenome(sobrenomeUsuario);
        usuario.setTelefone(telefoneUsuario);
        usuario.setTipoUsuario(tipoUsuario);

        Intent intent = new Intent(CadastroTipoUsuarioActivity.this, CadastroTermoMotoristaActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);

    }
}
