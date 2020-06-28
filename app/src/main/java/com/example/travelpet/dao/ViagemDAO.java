package com.example.travelpet.dao;

import android.provider.ContactsContract;

import com.example.travelpet.model.Viagem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.DatabaseConfig;

import java.util.HashMap;
import java.util.Map;
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

    public void excluirViagem(Viagem viagem)
    {
        DatabaseReference viagemRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("viagem").child(viagem.getIdViagem());
        viagemRef.removeValue();
    }

    public void recusarViagem(String idViagem)
    {
        DatabaseReference viagemRef = ConfiguracaoFirebase
                .getFirebaseDatabaseReferencia()
                .child("viagem")
                .child(idViagem);

        Map<String,Object> viagemRefUpdates = new HashMap<>();
        viagemRefUpdates.put("idMotorista","");
        viagemRefUpdates.put("statusViagem",Viagem.CANCELADA);

        viagemRef.updateChildren(viagemRefUpdates);
    }

    public void atualizaStatusviagem (String idViagem, String status)
    {
        DatabaseReference viagemRef = ConfiguracaoFirebase
                .getFirebaseDatabaseReferencia()
                .child("viagem")
                .child(idViagem);

        Map<String,Object> viagemRefUpdates = new HashMap<>();
        viagemRefUpdates.put("statusViagem", status);
        viagemRef.updateChildren(viagemRefUpdates);
    }

    public void receberViagem ()
    {
        //retorna Viagem
    }

    public void atualizaViagem (Viagem viagem, final CountDownLatch contador)
    {
        DatabaseReference viagemRef = ConfiguracaoFirebase
                .getFirebaseDatabaseReferencia()
                .child("viagem")
                .child(viagem.getIdViagem());

        Map<String,Object> viagemRefUpdates = receberMapViagem(viagem);
        //colocar o meio milhao de variaveis aqui

        viagemRef.updateChildren(viagemRefUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        contador.countDown();
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        contador.countDown();
                    }
                });
    }

    private Map<String,Object> receberMapViagem (Viagem viagem)
    {
        Map<String, Object> mapViagem = new HashMap<>();
        mapViagem.put("idViagem",viagem.getIdViagem());
        //
        //
        //

        return mapViagem;
    }
}
