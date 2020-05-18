package com.example.travelpet.dao;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.R;
import com.example.travelpet.controlller.cadastro.cadastroDonoAnimal.CadastroAnimalFotoActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Animal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnimalDAO {
    //private Animal animal;
    //private String fotoAnimalUrl;
    //private int tipoLocalSave; // RealtimeDatabase
    //private String mensagem;
    int quantidadeAnimais; // contar animais

    public AnimalDAO() {}

    public static String gerarPushKeyIdAnimal(){
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String chave = referencia.push().getKey();
        return chave;
    }

    //      Método para salvar os dados do animal no firebase
    public void salvarAnimalRealtimeDatabase(Animal animal, final int tipoLocalSave,
                                             final Activity activityAtual,
                                             final Class<PerfilPassageiroActivity> activitySeguinte){

        DatabaseReference animalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("animais")
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal());
        animalRef.setValue(animal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // tipoLocalSave == 1 - CadastroAnimalFotoActivity ou ListaAnimaisFragment
                if(tipoLocalSave == 1){
                    Toast.makeText(activityAtual,
                            "Cadastro realizado com sucesso",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activityAtual, activitySeguinte);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activityAtual.startActivity(intent);
                }

                // tipoLocalSave == 2 = EditarAnimalActivity
                if(tipoLocalSave == 2){
                    Toast.makeText(activityAtual,
                            "Atualização feita com Sucesso",
                            Toast.LENGTH_SHORT).show();
                    activityAtual.finish();
                    activityAtual.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    /*
    public String salvarAnimal (byte[] fotoAnimal , Animal animal, int tipoLocalSave)
    {
        //this.animal = anim;
        mensagem = salvarFotoAnimalStorage(fotoAnimal, animal, tipoLocalSave);

        return mensagem;
    } */

    // Método salvar a foto e os dados do animal / função salvar e atualizar
    public void salvarImagemAnimalStorage(final Animal animal, final int tipoLocalSave,
                                          final Activity activityAtual,
                                          final Class<PerfilPassageiroActivity> activitySeguinte){

        StorageReference animalStorageRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child("animais")
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal())
                .child(animal.getIdAnimal()+".FOTO.PERFIL.JPEG");

        UploadTask uploadTask = animalStorageRef.putBytes(animal.getFotoAnimal());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                Uri url = uri.getResult();
                String fotoAnimalUrl = url.toString();

                animal.setFotoAnimalUrl(fotoAnimalUrl);
                salvarAnimalRealtimeDatabase(animal, tipoLocalSave, activityAtual, activitySeguinte);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public int contarAnimais () {

        DatabaseReference animalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("animais")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        animalRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                 quantidadeAnimais = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }

        });
        return quantidadeAnimais;
    }

    public void  excluirAnimal(Animal animal, Activity activityAtual) {

        quantidadeAnimais = contarAnimais();

        if (quantidadeAnimais <= 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activityAtual);

            builder.setTitle("Você Possui 1 animal");
            builder.setIcon(R.drawable.ic_atencao_laranja_24dp);
            builder.setMessage("Não e possível excluir com apenas 1 animal cadastrado");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else {

            excluirAnimalRealTimeDatabase(animal);
            excluirImagemAnimalStorage(animal, activityAtual);

        }
    }

    public void excluirAnimalRealTimeDatabase (Animal animal)
    {
        ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("animais")
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {}
                });
    }

    public void excluirImagemAnimalStorage (Animal animal, final Activity activityAtual) {

        StorageReference animalStorageRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child("animais")
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal())
                .child(animal.getIdAnimal() + ".FOTO.PERFIL.JPEG");

        animalStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                activityAtual.finish();
                activityAtual.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
            }
        });

    }


}
