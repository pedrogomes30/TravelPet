package com.example.travelpet.dao;

import com.example.travelpet.model.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;

public class LocalDAO
{
    public LocalDAO(){}

    public static String gerarPushKeyIdLocal()
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String pushKey = referencia.push().getKey();
        return pushKey;
    }

    public void salvarLocal (Local local, final CountDownLatch latch)
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("local").child(local.getIdLocal());

        referencia.setValue(local).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                latch.countDown();
            }
        }). addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                System.out.println("falha na gravação");
                latch.countDown();
            }
        });
    }




}
