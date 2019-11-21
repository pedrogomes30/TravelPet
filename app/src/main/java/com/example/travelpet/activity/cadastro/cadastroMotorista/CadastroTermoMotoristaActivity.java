package com.example.travelpet.activity.cadastro.cadastroMotorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.classes.Motorista;
import com.example.travelpet.classes.Usuario;

public class CadastroTermoMotoristaActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroDadosUsuario
    private String  nomeUsuario, sobrenomeUsuario, telefoneUsuario, tipoUsuario;

    private CheckBox checkBoxTermos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_termo_motorista);

        // Efeito de Transição
        // 1ª (R.anim.activity_filho_entrando)= animação que vai executar pra activity que ta estrando
        // 2ª = Animação que vai executar pra activity que tiver saindo
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);


        // Recuperando dados passados da Activity CadastroTipoUsuario
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        // Armazena os dados recuperados em uma variável String
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        checkBoxTermos = findViewById(R.id.checkBoxTermos);

    }

    // Evento onClick ( Botão ProximoTermo )
    public void buttonProximoTermoMotorista(View view){

        // isChecked(); = verifica se o check box esta marcado, enviando true ou false , se ele esta marcado ou não
        if ( checkBoxTermos.isChecked()){

            Usuario usuario = new Usuario();

            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            //      Enviando dados para a Activity CadastroEspecie
            Intent intent = new Intent(CadastroTermoMotoristaActivity.this, CadastroCnhMotoristaActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);


        }else{ // Se não tiver marcado o checkBoxTermos, exibe essa mensagem
            Toast.makeText(CadastroTermoMotoristaActivity.this, //onde será exibido
                    "Aceite os termos para prosseguir", // o que será exibido
                    Toast.LENGTH_SHORT).show(); // quanto tempo. show = monstrar executa
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
