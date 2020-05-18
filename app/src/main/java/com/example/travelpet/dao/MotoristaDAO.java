package com.example.travelpet.dao;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.model.Motorista;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MotoristaDAO {
    //private Motorista motorista;


    public MotoristaDAO() {}

    // Método para salvar os dados do usuário no firebase
    public void  salvarMotoristaRealtimeDatabase(Motorista motorista){

        DatabaseReference motoristaRefRealtime = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("motorista")
                .child(motorista.getIdUsuario());
        motoristaRefRealtime.setValue(motorista).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    // Método salva imagens no Storage, depois chama metodo pra salvar dados no storage
    public void salvarImagemMotoristaStorage(final Motorista motorista){

        // Salvando FotoCnh no Storage
        StorageReference imagemRefFotoCnh = ConfiguracaoFirebase.getFirebaseStorage()
                .child("motorista")
                .child(motorista.getIdUsuario())
                .child(motorista.getIdUsuario()+".FOTO.CNH.JPEG");
        // Método para realmente salvar no Firebase
        // putBytes = para os dados da imagem em bytes
        UploadTask uploadTaskFotoCnh = imagemRefFotoCnh.putBytes (motorista.getFotoCNH());
        uploadTaskFotoCnh.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Recuperando o edereço da imagem no Storage
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                // url = pega o caminho da imagem
                Uri url = uri.getResult();
                // converte a url/uri para String
                final String fotoCnhUrl = url.toString();

                // Salvando FotoPerfil no Storage
                StorageReference imagemRefFotoPerfil = ConfiguracaoFirebase.getFirebaseStorage()
                        .child("motorista")
                        .child(motorista.getIdUsuario())
                        .child(motorista.getIdUsuario()+".FOTO.PERFIL.JPEG");

                UploadTask uploadTaskFotoPerfil = imagemRefFotoPerfil.putBytes (motorista.getFotoPerfil());
                uploadTaskFotoPerfil.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        final String fotoPerfilUrl = url.toString();

                        motorista.setFotoCnhUrl(fotoCnhUrl);
                        motorista.setFotoPerfilUrl(fotoPerfilUrl);
                        salvarMotoristaRealtimeDatabase(motorista);

                    }
                });
            }
        });
    }
}
