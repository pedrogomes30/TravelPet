package com.example.travelpet.dao;

import android.provider.ContactsContract;

import com.example.travelpet.model.Viagem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;

public class ViagemDAO
{
    Viagem viagem;
    public ViagemDAO ()
    {

    }

    public static String gerarPushKeyIdViagem()
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String key = referencia.push().getKey();
        return key;
    }

    public static DatabaseReference getRootViagens ()
    {
        DatabaseReference rootViagem = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("viagem");
        return rootViagem;
    }

    public void salvarViagem (Viagem viagem, final CountDownLatch latch)
    {
        DatabaseReference viagemRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("viagem").child(viagem.getIdViagem());

        viagemRef.setValue(viagem).addOnSuccessListener(new OnSuccessListener<Void>()
        {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        latch.countDown();
                    }
        }).addOnFailureListener(new OnFailureListener()
        {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        latch.countDown();
                    }
        });
    }

    public void receberViagem ()
    {
        //retorna Viagem
    }
}
