package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;

public class CadastroMotoristaTermoActivity extends AppCompatActivity {

    private Motorista motorista;
    private Endereco endereco;

    private CheckBox checkBoxTermos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_termo);

        // Efeito de Transição
        // 1ª (R.anim.activity_filho_entrando)= animação que vai executar pra activity que ta estrando
        // 2ª = Animação que vai executar pra activity que tiver saindo
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroTipoUsuario
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");

        checkBoxTermos = findViewById(R.id.checkBoxTermos);

    }

    public void botaoProximo(View view){

        // isChecked(); = verifica se o check box esta marcado, enviando true ou false , se ele esta marcado ou não
        if ( checkBoxTermos.isChecked()){

            //      Enviando dados para a Activity CadastroEspecie
            Intent intent = new Intent(this, CadastroMotoristaCnhActivity.class);
            intent.putExtra("motorista", motorista);
            intent.putExtra("endereco", endereco);
            startActivity(intent);


        }else{
            Toast.makeText(this,
                     "Aceite os termos para prosseguir",
                           Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }

}
