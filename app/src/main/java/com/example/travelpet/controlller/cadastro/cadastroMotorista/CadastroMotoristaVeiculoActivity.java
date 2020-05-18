package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelpet.R;
import com.example.travelpet.domain.Endereco;
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

        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");

        modeloVeiculo   =   findViewById(R.id.etModeloVeiculo);
        marcaVeiculo    =   findViewById(R.id.etMarcaVeiculo);
        anoVeiculo      =   findViewById(R.id.etAnoVeiculo);
        placaVeiculo    =   findViewById(R.id.etPlacaVeiculo);
        crvlVeiculo     =   findViewById(R.id.etCRVLveiculo);
        btProximo       =   findViewById(R.id.btProximoVeiculo);

        botaoProximoCadVeiculo();
    }

    public void botaoProximoCadVeiculo ()
    {
        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marcaV  =   marcaVeiculo.getText().toString().toUpperCase();
                modeloV =   modeloVeiculo.getText().toString().toUpperCase();
                anoV    =   anoVeiculo.getText().toString().toUpperCase();
                placaV  =   placaVeiculo.getText().toString().toUpperCase();
                crvlV   =   crvlVeiculo.getText().toString().toUpperCase();

                if(verificaCampos() == "completo")
                {
                    Veiculo veiculo = new Veiculo();
                    veiculo.setMarcaVeiculo(marcaV);
                    veiculo.setModeloVeiculo(modeloV);
                    veiculo.setAnoVeiculo(anoV);
                    veiculo.setPlacaVeiculo(placaV);
                    veiculo.setCrvlVeiculo(crvlV);

                    Intent intent = new Intent(CadastroMotoristaVeiculoActivity.this, CadastroMotoristaCrlvActivity.class);
                    intent.putExtra("motorista", motorista);
                    intent.putExtra("endereco",endereco);
                    intent.putExtra("veiculo",veiculo);
                    startActivity(intent);
                }
            }
        });
    }


    public String verificaCampos ()
    {
        String verificado = "incompleto";

        if (!marcaV.isEmpty())
        {
            if (!modeloV.isEmpty())
            {
                if (!anoV.isEmpty())
                {
                    if (!placaV.isEmpty())
                    {
                        if (!crvlV.isEmpty())
                        {
                            verificado = "completo";
                        } else { ToastIt("Preencha o CRVL"); }
                    } else { ToastIt("Preencha a Placa"); }
                } else { ToastIt("Preencha o Ano"); }
            } else { ToastIt("Preencha o Modelo"); }
        } else { ToastIt("Preencha o CRVL"); }

        return verificado;
    }


    public void ToastIt (String mensagem)
    {
        Toast.makeText(getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
