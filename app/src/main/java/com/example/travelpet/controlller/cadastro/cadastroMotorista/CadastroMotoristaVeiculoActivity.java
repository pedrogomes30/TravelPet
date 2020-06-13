package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Veiculo;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroMotoristaVeiculoActivity extends AppCompatActivity {

    private TextInputEditText modeloVeiculo, marcaVeiculo, anoVeiculo, placaVeiculo, crvlVeiculo;
    private String modeloV, marcaV, anoV, placaV, crvlV;
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
                    veiculo.setCrvlVeiculo(crvlV);

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
        crvlVeiculo     =   findViewById(R.id.etCRVLveiculo);
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
        crvlV   =   crvlVeiculo.getText().toString().toUpperCase();
    }

    public Boolean validarDados (){
        Boolean validado = false;

        if (!VerificaDado.isVazio(marcaV))
        {
            if (!VerificaDado.isVazio(modeloV))
            {
                if (!VerificaDado.isVazio(anoV))
                {
                    if (!VerificaDado.isVazio(placaV))
                    {
                        if (!VerificaDado.isVazio(crvlV))
                        {
                            validado = true;

                        } else { Mensagem.toastIt("Preencha o CRVL", this); }
                    } else { Mensagem.toastIt("Preencha a Placa", this); }
                } else { Mensagem.toastIt("Preencha o Ano", this); }
            } else { Mensagem.toastIt("Preencha o Modelo", this); }
        } else { Mensagem.toastIt("Preencha o CRVL", this); }

        return validado;
    }

    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
