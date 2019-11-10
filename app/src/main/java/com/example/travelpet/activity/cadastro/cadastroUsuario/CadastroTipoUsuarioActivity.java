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

    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo_usuario);

    }

    // Evento de onClick do botão "Passageiro"
    public void buttonPassageiro( View view ){

        tipoUsuario = "passageiro";

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuario);

        Intent intent = new Intent(CadastroTipoUsuarioActivity.this, CadastroNomeUsuarioActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);

    }

    // Evento de onClick do botão "Motorista"
    public void buttonMotorista( View view ){

        tipoUsuario = "motorista";

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuario);

        Intent intent = new Intent(CadastroTipoUsuarioActivity.this, CadastroNomeUsuarioActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }
    // Método para voltar com o botão do próprio aparelho
    @Override
    public void onBackPressed() {
        // Como não tem nada, não volta
    }
}
