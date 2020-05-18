package com.example.travelpet.controlller.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Usuario;

public class CadastroUsuarioTipoActivity extends AppCompatActivity {

    private Usuario usuario;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_tipo);

        usuario = new Usuario();

    }

    public void botaoPassageiro( View view ){

        tipoUsuario = "donoAnimal";

        usuario.setTipoUsuario(tipoUsuario);

        Intent intent = new Intent(CadastroUsuarioTipoActivity.this, CadastroUsuarioDadosActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);

    }

    public void botaoMotorista( View view ){

        tipoUsuario = "motorista";

        usuario.setTipoUsuario(tipoUsuario);

        Intent intent = new Intent(CadastroUsuarioTipoActivity.this, CadastroUsuarioDadosActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);

    }
    /* Método para voltar com o botão do próprio aparelho
    @Override
    public void onBackPressed() {
        // Como não tem nada, não volta
    }*/
}
