package com.example.travelpet.dao;

import com.example.travelpet.model.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;

public class LocalDAO
{
    private Local local;
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

    public Local getLocal (String id, final CountDownLatch contador)
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("local").child(id);
        referencia.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                local = snapshot.getValue(Local.class);
                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                contador.countDown();
            }
        });

        try {contador.await();}
        catch (InterruptedException e)
        {e.printStackTrace();}

        return local;
    }

    public void excluirLocal(Local localDestino)
    {
        DatabaseReference referenciaLocal = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("local").child(localDestino.getIdLocal());
        referenciaLocal.removeValue();
    }
}
