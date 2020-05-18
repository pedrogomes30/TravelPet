package com.example.travelpet.dao;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonoAnimalDAO {
    //private DonoAnimal donoAnimal;
    //private byte[] fotoUsuario;


    public DonoAnimalDAO() {}

    // Método para salvar os dados do usuário no firebase
    public void  salvarDonoAnimalRealtimeDatabase(DonoAnimal donoAnimal){

        DatabaseReference donoAnimalRefRealtime = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("donoAnimal")
                .child(donoAnimal.getIdUsuario());
        donoAnimalRefRealtime.setValue(donoAnimal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    /*
    public void salvarDonoAnimal (byte[] fotoU, DonoAnimal donoA)
    {
        this.donoAnimal = donoA;
        this.fotoUsuario = fotoU;
        donoAnimal.setId(UsuarioFirebase.getIdentificadorUsuario());
        salvarFotoDonoAnimalStorage(fotoUsuario);
    } */

    // Método salva primeiro a foto do usuario no storage e depois salvar os dados no database
    public void salvarImagemDonoAnimalStorage(byte[] fotoUsuario, final DonoAnimal donoAnimal){
        // Salvar imagem no firebase
        StorageReference donoAnimalRefStorage = ConfiguracaoFirebase.getFirebaseStorage()
                .child("donoAnimal")
                .child(donoAnimal.getIdUsuario())
                .child(donoAnimal.getIdUsuario()+".FOTO.PERFIL.JPEG");


        UploadTask uploadTask = donoAnimalRefStorage.putBytes(fotoUsuario);
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
                salvarDonoAnimalRealtimeDatabase(donoAnimal);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
