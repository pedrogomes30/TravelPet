package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.MascaraCampos;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Veiculo;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroMotoristaVeiculoActivity extends AppCompatActivity {

    private TextInputEditText modeloVeiculo, marcaVeiculo, anoVeiculo, placaVeiculo, crlvVeiculo;
    private String modeloV, marcaV, anoV, placaV, crlvV;
    private Button btProximo;

    private Motorista motorista;
    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_veiculo);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); // CadastroMotoristaFoto
        botaoProximo();
    }

    public void botaoProximo() {
        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDadosDigitados();

                if(validarDados()){

                    Veiculo veiculo = new Veiculo();
                    veiculo.setMarcaVeiculo(marcaV);
                    veiculo.setModeloVeiculo(modeloV);
                    veiculo.setAnoVeiculo(anoV);
                    veiculo.setPlacaVeiculo(placaV);
                    veiculo.setCrlvVeiculo(crlvV);

                    Intent intent = new Intent(getApplicationContext(), CadastroMotoristaCrlvActivity.class);
                    intent.putExtra("motorista", motorista);
                    intent.putExtra("endereco",endereco);
                    intent.putExtra("veiculo",veiculo);
                    startActivity(intent);
                }
            }
        });
    }

    public void iniciarComponentes(){
        modeloVeiculo   =   findViewById(R.id.etModeloVeiculo);
        marcaVeiculo    =   findViewById(R.id.etMarcaVeiculo);
        anoVeiculo      =   findViewById(R.id.etAnoVeiculo);
        placaVeiculo    =   findViewById(R.id.etPlacaVeiculo);
        crlvVeiculo     =   findViewById(R.id.etCRLVveiculo);
        btProximo       =   findViewById(R.id.btProximoVeiculo);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
    }

    public void getDadosDigitados(){
        marcaV  =   marcaVeiculo.getText().toString().toUpperCase();
        modeloV =   modeloVeiculo.getText().toString().toUpperCase();
        anoV    =   anoVeiculo.getText().toString().toUpperCase();
        placaV  =   placaVeiculo.getText().toString().toUpperCase();
        crlvV   =    crlvVeiculo.getText().toString().toUpperCase();
    }

    public Boolean validarDados (){
        Boolean validado = false;

        if (!VerificaDado.isVazio(modeloV))
        {
            if (!VerificaDado.isVazio(marcaV))
            {
                if (!VerificaDado.isVazio(anoV) && anoV.length() == 4)
                {
                    if (!VerificaDado.isVazio(placaV) && placaV.length() == 7)
                    {
                        if (!VerificaDado.isVazio(crlvV))
                        {
                            validado = true;

                        } else { Mensagem.toastIt("Preencha o CRVL", this); }
                    } else { Mensagem.toastIt("Preencha a placa corretamente", this); }
                } else { Mensagem.toastIt("Preencha o ano corretamente (Ex.: 2010)", this); }
            } else { Mensagem.toastIt("Preencha a marca", this); }
        } else { Mensagem.toastIt("Preencha a modelo", this); }

        return validado;
    }

    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
