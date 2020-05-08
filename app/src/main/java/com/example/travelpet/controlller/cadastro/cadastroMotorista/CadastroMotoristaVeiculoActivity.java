package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroMotoristaVeiculoActivity extends AppCompatActivity {

    //private String

    private TextInputEditText modeloVeiculo, marcaVeiculo, anoVeiculo, placaVeiculo, crvlVeiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_veiculo);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        modeloVeiculo   =   findViewById(R.id.etModeloVeiculo);
        marcaVeiculo    =   findViewById(R.id.etMarcaVeiculo);
        anoVeiculo      =   findViewById(R.id.etAnoVeiculo);
        placaVeiculo    =   findViewById(R.id.etPlacaVeiculo);
        crvlVeiculo     =   findViewById(R.id.etCRVLveiculo);


    }
}
