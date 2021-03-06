package com.example.travelpet.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.EventListener;
import java.util.concurrent.CountDownLatch;

public class DonoAnimalDAO {

    private DonoAnimal donoAnimal;

    public DonoAnimalDAO() {}

    public void salvarDonoAnimalRealtimeDatabase(DonoAnimal donoAnimal, final ProgressDialog progressDialog,
                                                 final int tipoSave, final Activity activity){

        DatabaseReference donoAnimalRefRealtime = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.donoAnimal)
                .child(donoAnimal.getIdUsuario());
        donoAnimalRefRealtime.setValue(donoAnimal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // tipoSave == 2 - Atualizar dados DonoAnimal - ConfiguracaoFragment
                if(tipoSave == 2){
                    TelaCarregamento.pararCarregamento(progressDialog);
                    Mensagem.mensagemAtualizarDonoAnimal(activity);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //----------------------------------------------------------------------------------------------
    public void salvarImagemDonoAnimalStorage(final DonoAnimal donoAnimal, final ProgressDialog progressDialog,
                                              final int tipoLocalSave, final Activity activity){
        StorageReference donoAnimalRefStorage = ConfiguracaoFirebase.getFirebaseStorage()
                .child(ConfiguracaoFirebase.donoAnimal)
                .child(donoAnimal.getIdUsuario())
                .child(donoAnimal.getIdUsuario()+".FOTO.PERFIL.JPEG");

        UploadTask uploadTask = donoAnimalRefStorage.putBytes(donoAnimal.getFotoPerfil());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                Uri url = uri.getResult();
                String fotoPerfilUrl = url.toString();

                donoAnimal.setFotoPerfilUrl(fotoPerfilUrl);
                salvarDonoAnimalRealtimeDatabase(donoAnimal, progressDialog, tipoLocalSave, activity);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //----------------------------------------------------------------------------------------------

    public DonoAnimal receberPerfil(String id, final CountDownLatch contador)
    {
        donoAnimal = null;
        DatabaseReference dbrefence = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("donoAnimal").child(id);
        dbrefence.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                DonoAnimal dAnimal = dataSnapshot.getValue(DonoAnimal.class);
                donoAnimal = dAnimal;

                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { contador.countDown();}
        });

        try {contador.await();}
        catch (InterruptedException e)
        {e.printStackTrace();}


        return donoAnimal;
    }


}
