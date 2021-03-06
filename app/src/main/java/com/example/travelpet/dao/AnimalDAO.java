package com.example.travelpet.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.R;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.TelaCarregamento;
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

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AnimalDAO {

    private int quantidadeAnimais; // contar animais
    private ArrayList<Animal> listaAnimal = new ArrayList<>();
    private Animal animal;

    public AnimalDAO() {}

    public static String gerarPushKeyIdAnimal(){
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String chave = referencia.push().getKey();
        return chave;
    }


    //      Método para salvar os dados do animal no firebase
    public void salvarAnimalRealtimeDatabase(Animal animal, final ProgressDialog progressDialog,
                                             final int tipoSave,
                                             final Activity activity)
    {

        DatabaseReference animalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.animal)
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal());
        animalRef.setValue(animal).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid) {

                TelaCarregamento.pararCarregamento(progressDialog);

                // tipoSave == 1 - (cadastrarUsuario/Animal) - CadastroAnimalFotoActivity ou ListaAnimaisFragment
                if(tipoSave == 1)
                {
                    Mensagem.mensagemCadastrarDados(activity);
                }

                // tipoSave == 2 - atualizar os dados do animal -  EditarAnimalActivity
                if(tipoSave == 2)
                {
                    Mensagem.mensagemAtualizarAnimal(activity);
                }

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });
    }

    // Método salvar a foto e os dados do animal / função salvar e atualizar
    public void salvarAnimalStorage(final Animal animal,
                                          final ProgressDialog progressDialog,
                                          final int tipoSave,
                                          final Activity activity){

        StorageReference animalStorageRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child(ConfiguracaoFirebase.animal)
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
                salvarAnimalRealtimeDatabase(animal, progressDialog,tipoSave, activity);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public int contarAnimais () {

        DatabaseReference animalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.animal)
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

    public void  excluirAnimal(Animal animal, Activity activity) {

        quantidadeAnimais = contarAnimais();

        if (quantidadeAnimais == 1) {

            Mensagem.mensagemImpedirExcluirAnimal(activity);

        }else {

            excluirAnimalRealTimeDatabase(animal);
            excluirAnimalStorage(animal, activity);

        }
    }

    public void excluirAnimalRealTimeDatabase (Animal animal)
    {
        ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.animal)
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {}
                });
    }

    public void excluirAnimalStorage (Animal animal, final Activity activity) {

        StorageReference animalStorageRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child(ConfiguracaoFirebase.animal)
                .child(animal.getIdUsuario())
                .child(animal.getIdAnimal())
                .child(animal.getIdAnimal() + ".FOTO.PERFIL.JPEG");

        animalStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Animal excluído com sucesso", Toast.LENGTH_SHORT).show();
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
            }
        });
    }

    public Animal receberAnimalPorId (String idAnimal, String idDonoAnimal , final CountDownLatch contador)
    {
        animal = new Animal();
        DatabaseReference referenciaAnimal = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.animal)
                .child(idDonoAnimal)
                .child(idAnimal);

        referenciaAnimal.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                animal = dataSnapshot.getValue(Animal.class);
                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        try {contador.await();}
        catch (InterruptedException e)
        { e.printStackTrace();}

        return animal;
    }


    public ArrayList<Animal> receberListaAnimal (final CountDownLatch contador)
    {
        DatabaseReference referenciaAnimais = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.animal)
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        referenciaAnimais.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    Animal animal = data.getValue(Animal.class);
                    listaAnimal.add(animal);
                }
                contador.countDown();
                System.out.println("lista animais após o for: "+ listaAnimal.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                contador.countDown();
            }

        });

        return listaAnimal;
    }


}
