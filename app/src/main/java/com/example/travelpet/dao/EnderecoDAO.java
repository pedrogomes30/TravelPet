package com.example.travelpet.dao;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;

import com.example.travelpet.model.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.helper.TelaCarregamento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class EnderecoDAO {

    public EnderecoDAO() {}

    //      Metodo para salvar os dados do endereço do usuario no database do firebase
    public void salvarEnderecoRealtimeDatabase(Endereco endereco, String tipoUsuario, final int tipoSave,
                                               final ProgressDialog progressDialog){

        // Transforma a primeira letra do "tipoUsuario" em maiuscula
        tipoUsuario = tipoUsuario.substring(0,1).toUpperCase()+tipoUsuario.substring(1);

        DatabaseReference enderecoRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("enderecos"+tipoUsuario)
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        enderecoRef.setValue(endereco).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //tipoSave == 2 - Atualização de dados - ConfiguracaoFragment
                if(tipoSave == 2){
                    TelaCarregamento.pararCarregamento(progressDialog);
                    //Mensagem.mensagemAtualizarDonoAnimal(activity);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception e) {

            }
        });
    }
}
