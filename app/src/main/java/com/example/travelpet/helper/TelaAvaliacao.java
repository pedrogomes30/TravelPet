package com.example.travelpet.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.R;
import com.example.travelpet.dao.AvaliacaoDAO;
import com.example.travelpet.model.Avaliacao;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class TelaAvaliacao {
    private CircleImageView campoFotoUsuario;
    private TextView campoNomeUsuario;
    private TextView campoAvaliacao;
    private ImageView campoNota1;
    private ImageView campoNota2;
    private ImageView campoNota3;
    private ImageView campoNota4;
    private ImageView campoNota5;
    private EditText campoObservação;
    private Button botaoConfirmar;
    private AlertDialog.Builder builder ;
    private AlertDialog dialog;
    private String nota;
    private String  observacao;
    private String data;

    private ProgressDialog progressDialog;

    private Activity activity;
    private Avaliacao avaliacao;
    private AvaliacaoDAO avaliacaoDAO;

    // constrututor
    public TelaAvaliacao(Activity activity) {
        this.activity = activity;
    }

    public void iniciarAvaliacao(){

        View viewDialog = activity.getLayoutInflater().inflate(R.layout.dialog_avaliacao, null);

        iniciarComponentes(viewDialog);
        funcoesBotoes();

        builder.setView(viewDialog);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void funcoesBotoes(){
        campoNota1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota = "1";
                setCoresNotas(nota);
                setTextoNotas(nota);

            }
        });

        campoNota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota = "2";
                setCoresNotas(nota);
                setTextoNotas(nota);
            }
        });

        campoNota3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota = "3";
                setCoresNotas(nota);
                setTextoNotas(nota);

            }
        });

        campoNota4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota = "4";
                setCoresNotas(nota);
                setTextoNotas(nota);

            }
        });

        campoNota5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nota = "5";
                setCoresNotas(nota);
                setTextoNotas(nota);

            }
        });

        botaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observacao = campoObservação.getText().toString();
                if(validarDados()){

                    TelaCarregamento.iniciarCarregamento(progressDialog);
                    setDadosAvalicao();
                    avaliacaoDAO.salvarAvaliacaoDatabase(activity, dialog, progressDialog, avaliacao);

                }
            }
        });
    }

    private void iniciarComponentes(View viewDialog){
        builder = new AlertDialog.Builder(activity);
        campoFotoUsuario =  viewDialog.findViewById(R.id.imageViewCircleFotoUsuario);
        campoNomeUsuario = viewDialog.findViewById(R.id.textViewNomeUsuario);
        campoAvaliacao =  viewDialog.findViewById(R.id.textViewAvaliacaoTipo);
        campoNota1 = viewDialog.findViewById(R.id.imageViewIconeNota1);
        campoNota2 = viewDialog.findViewById(R.id.imageViewIconeNota2);
        campoNota3 = viewDialog.findViewById(R.id.imageViewIconeNota3);
        campoNota4 = viewDialog.findViewById(R.id.imageViewIconeNota4);
        campoNota5 = viewDialog.findViewById(R.id.imageViewIconeNota5);
        campoObservação = viewDialog.findViewById(R.id.editTextObservacao);
        botaoConfirmar = viewDialog.findViewById(R.id.botaoConfirmar);

        avaliacao = new Avaliacao();
        avaliacaoDAO = new AvaliacaoDAO();
        progressDialog = new ProgressDialog(activity);
    }

    private void setCoresNotas(String avalicao){
        switch (avalicao){
            case"1":
                campoNota1.setColorFilter(activity.getResources().getColor(R.color.amarelo_avalicao_muito_ruim), PorterDuff.Mode.MULTIPLY );
                campoNota2.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota3.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota4.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota5.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                break;
            case"2":
                campoNota2.setColorFilter(activity.getResources().getColor(R.color.amarelo_avalicao_ruim), PorterDuff.Mode.MULTIPLY );
                campoNota1.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota3.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota4.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota5.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                break;
            case"3":
                campoNota3.setColorFilter(activity.getResources().getColor(R.color.azul_avalicao_regular), PorterDuff.Mode.MULTIPLY );
                campoNota1.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota2.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota4.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota5.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                break;
            case"4":
                campoNota4.setColorFilter(activity.getResources().getColor(R.color.verde_avalicao_bom), PorterDuff.Mode.MULTIPLY );
                campoNota1.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota2.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota3.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota5.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                break;
            case"5":
                campoNota5.setColorFilter(activity.getResources().getColor(R.color.verde_avalicao_muito_bom), PorterDuff.Mode.MULTIPLY );
                campoNota1.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota2.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota3.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                campoNota4.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY );
                break;
        }
    }

    public void setTextoNotas(String avalicao){
        switch (avalicao){
            case"1":
                campoAvaliacao.setText(R.string.muito_ruim);
                campoAvaliacao.setTextColor(activity.getResources().getColor(R.color.amarelo_avalicao_muito_ruim));
                break;
            case"2":
                campoAvaliacao.setText(R.string.ruim);
                campoAvaliacao.setTextColor(activity.getResources().getColor(R.color.amarelo_avalicao_ruim));
                break;
            case"3":
                campoAvaliacao.setText(R.string.regular);
                campoAvaliacao.setTextColor(activity.getResources().getColor(R.color.azul_avalicao_regular));
                break;
            case"4":
                campoAvaliacao.setText(R.string.bom);
                campoAvaliacao.setTextColor(activity.getResources().getColor(R.color.verde_avalicao_bom));
                break;
            case"5":
                campoAvaliacao.setText(R.string.muito_bom);
                campoAvaliacao.setTextColor(activity.getResources().getColor(R.color.verde_avalicao_muito_bom));
                break;
        }
    }

    private Boolean validarDados(){
        Boolean validado = false;
        if(nota != null){

            validado = true;

        }else{
            Mensagem.toastIt("Por favor, escolha uma nota", activity);
        }
        return validado;
    }

    private void setDadosAvalicao(){

        data = String.format("%02d/%02d/%4d",
                Calendar.getInstance().get(Calendar.DATE),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.YEAR));

        avaliacao.setNotaAvaliacao(Double.parseDouble(nota));
        avaliacao.setObservacao(observacao);
        avaliacao.setData(data);
    }
}
