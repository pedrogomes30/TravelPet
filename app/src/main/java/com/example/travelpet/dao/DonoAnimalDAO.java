package com.example.travelpet.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonoAnimalDAO {

    public DonoAnimalDAO() {}

    // Método para salvar os dados do usuário no firebase
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

    // Método salva primeiro a foto do usuario no storage e depois salvar os dados no database
    public void salvarImagemDonoAnimalStorage(final DonoAnimal donoAnimal, final ProgressDialog progressDialog,
                                              final int tipoLocalSave, final Activity activity){
        // Salvar imagem no firebase
        StorageReference donoAnimalRefStorage = ConfiguracaoFirebase.getFirebaseStorage()
                .child(ConfiguracaoFirebase.donoAnimal)
                .child(donoAnimal.getIdUsuario())
                .child(donoAnimal.getIdUsuario()+".FOTO.PERFIL.JPEG");

        UploadTask uploadTask = donoAnimalRefStorage.putBytes(donoAnimal.getFotoPerfil());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Configurando (atualizando) foto para pegar ela nas configurações do usuário
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                // url = pega o caminho da imagem
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

}
