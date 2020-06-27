package com.example.travelpet.helper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TelaTransportePet {

    private CircleImageView campoIconeCaixa;
    private TextView campoDescricao;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ImageView botaoSetaEsquerda, botaoSetaDireita;
    private Button botaoOK;
    private ImageView iconeCirculoDireto, iconeCirculoEsquerdo;


    public TelaTransportePet(Activity activity) {
        this.activity = activity;
    }

    public void iniciarTelaTransporte(){

        View viewDialog = activity.getLayoutInflater().inflate(R.layout.dialog_transporte_pet, null);

        iniciarComponentes(viewDialog);
        configIniciais(viewDialog);
        funcoesBotoes();

    }

    private void funcoesBotoes(){

        botaoSetaDireita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoIconeCaixa.setImageResource(R.drawable.ic_grades);
                campoDescricao.setText(R.string.porte_grande);
                botaoSetaEsquerda.setVisibility(View.VISIBLE);
                botaoSetaDireita.setVisibility(View.INVISIBLE);
                botaoOK.setVisibility(View.VISIBLE);
                iconeCirculoDireto.setImageResource(R.drawable.ic_circulo_cheio);
                iconeCirculoEsquerdo.setImageResource(R.drawable.ic_circulo_vazio);
            }
        });
        botaoSetaEsquerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campoIconeCaixa.setImageResource(R.drawable.ic_caixa_transporte);
                campoDescricao.setText(R.string.porte_pequeno_medio);
                botaoSetaEsquerda.setVisibility(View.INVISIBLE);
                botaoSetaDireita.setVisibility(View.VISIBLE);
                botaoOK.setVisibility(View.INVISIBLE);
                iconeCirculoEsquerdo.setImageResource(R.drawable.ic_circulo_cheio);
                iconeCirculoDireto.setImageResource(R.drawable.ic_circulo_vazio);
            }
        });
        botaoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void iniciarComponentes(View viewDialog){
        builder = new AlertDialog.Builder(activity);
        campoIconeCaixa = viewDialog.findViewById(R.id.iv_icone_caixa);
        campoDescricao = viewDialog.findViewById(R.id.tv_descricao);
        botaoSetaEsquerda = viewDialog.findViewById(R.id.iv_seta_esquerda);
        botaoSetaDireita = viewDialog.findViewById(R.id.iv_seta_direita);
        botaoOK = viewDialog.findViewById(R.id.botao_ok);
        iconeCirculoDireto = viewDialog.findViewById(R.id.iv_icone_circulo_vazio);
        iconeCirculoEsquerdo = viewDialog.findViewById(R.id.iv_icone_circulo_cheio);
    }

    public void configIniciais(View viewDialog){
        builder.setView(viewDialog);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        botaoSetaEsquerda.setVisibility(View.INVISIBLE);
        botaoOK.setVisibility(View.INVISIBLE);
    }

}
