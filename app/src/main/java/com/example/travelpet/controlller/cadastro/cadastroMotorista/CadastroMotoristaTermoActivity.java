package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;

public class CadastroMotoristaTermoActivity extends AppCompatActivity {

    // Variaveis usadas para recuperar dados da Activity CadastroDadosUsuario
    private String  tipoUsuario, nome, sobrenome, telefone,
                    cep, logradouro, bairro, localidade, uf;

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
        Motorista motorista = intent.getParcelableExtra("motorista");
        Endereco endereco = intent.getParcelableExtra("endereco");

        // Dados Motorista
        tipoUsuario  =   motorista.getTipoUsuario();
        nome         =   motorista.getNome();
        sobrenome    =   motorista.getSobrenome();
        telefone     =   motorista.getTelefone();

        // Dados Endereco
        cep          =   endereco.getCep();
        logradouro   =   endereco.getLogradouro();
        bairro       =   endereco.getBairro();
        localidade   =   endereco.getLocalidade();
        uf           =   endereco.getUf();

        checkBoxTermos = findViewById(R.id.checkBoxTermos);

    }

    public void botaoProximoMotoristaTermo(View view){

        // isChecked(); = verifica se o check box esta marcado, enviando true ou false , se ele esta marcado ou não
        if ( checkBoxTermos.isChecked()){

            Motorista motorista = new Motorista();
            motorista.setTipoUsuario(tipoUsuario);
            motorista.setNome(nome);
            motorista.setSobrenome(sobrenome);
            motorista.setTelefone(telefone);

            Endereco endereco = new Endereco();
            endereco.setCep(cep);
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setLocalidade(localidade);
            endereco.setUf(uf);

            //      Enviando dados para a Activity CadastroEspecie
            Intent intent = new Intent(CadastroMotoristaTermoActivity.this, CadastroMotoristaCnhActivity.class);
            intent.putExtra("motorista", motorista);
            intent.putExtra("endereco",endereco);
            startActivity(intent);


        }else{
            Toast.makeText(CadastroMotoristaTermoActivity.this,
                    "Aceite os termos para prosseguir",
                    Toast.LENGTH_SHORT).show();
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
