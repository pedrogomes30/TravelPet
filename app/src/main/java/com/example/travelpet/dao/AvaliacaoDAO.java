package com.example.travelpet.dao;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.model.Avaliacao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class AvaliacaoDAO {

    private static String gerarIdViagem(){
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String chave = referencia.push().getKey();
        return chave;
    }
    //      Metodo para salvar os dados do endereço do usuario no database do firebase
    public void salvarAvaliacaoDatabase(final Activity activity, final AlertDialog dialog,
                                                       final ProgressDialog progressDialog , Avaliacao avaliacao){


        DatabaseReference avaliacaoRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("avaliacao")
                .child("idAvaliado") // Recuperar o id Do Avaliado
                .child(gerarIdViagem());
        avaliacaoRef.setValue(avaliacao).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                    TelaCarregamento.pararCarregamento(progressDialog);
                    Mensagem.mensagemConclusaoAvaliacao(activity);
                    dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e) {

            }
        });
    }
}
