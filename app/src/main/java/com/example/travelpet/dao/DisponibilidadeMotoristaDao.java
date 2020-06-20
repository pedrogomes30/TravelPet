package com.example.travelpet.dao;

import android.location.Location;
import android.provider.ContactsContract;

import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DisponibilidadeMotorista;
import com.example.travelpet.model.Local;
import com.example.travelpet.model.Usuario;
import com.example.travelpet.model.Viagem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import mva2.adapter.ListSection;

public class DisponibilidadeMotoristaDao
{
    private DisponibilidadeMotorista disponibilidade;
    private ArrayList<DisponibilidadeMotorista> motoristasDisponiveis = new ArrayList<>();

    public DisponibilidadeMotoristaDao () { }

    public void criarDisponibilidade (DisponibilidadeMotorista disponibilidade)
    {
        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference disponibilidadeRef = fireDB.getReference().child("disponibilidadeMotorista").child(disponibilidade.getIdMotorista());

        disponibilidadeRef.setValue(disponibilidade).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                System.out.println( "<<<<<< " +" Sucesso " + " >>>>>>");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                System.out.println( "<<<<<< " + e.toString() + " >>>>>>");
            }
        });
    }

    public void salvarDisponibilidade (DisponibilidadeMotorista disponibilidade, final CountDownLatch contador)
    {
        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference disponibilidadeRef = fireDB.getReference().child("disponibilidadeMotorista").child(disponibilidade.getIdMotorista());

        disponibilidadeRef.setValue(disponibilidade)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        contador.countDown();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        contador.countDown();
                    }
                });

        try
        {contador.await();}
        catch (InterruptedException e)
        {e.printStackTrace();}
    }

    public DisponibilidadeMotorista receberDisponibilidade(final CountDownLatch latch)
    {
        disponibilidade = new DisponibilidadeMotorista();
        DatabaseReference referenciaDisponibilidade = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("disponibilidadeMotorista")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        referenciaDisponibilidade.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    DisponibilidadeMotorista disp = dataSnapshot.getValue(DisponibilidadeMotorista.class);
                    disponibilidade = disp;

                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println( "<<<<<<<<<<<<<<<<<<<<< return:" + disponibilidade.getDisponibilidade()+" >>>>>>>>>>>>>>>>>");
        return disponibilidade;
    }

    public ArrayList<DisponibilidadeMotorista> queryMotoristasDisponiveis
            (final Local localOrigem,
             final CountDownLatch contador,
             final float distanciaMax,
             final ArrayList<Animal> animaisSelecionados)
    {
        disponibilidade = new DisponibilidadeMotorista();
        motoristasDisponiveis = new ArrayList<>();
        DatabaseReference referenciaDisp = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        Query queryDisp = referenciaDisp.child("disponibilidadeMotorista").orderByChild("disponibilidade").equalTo("disponivel");

        queryDisp.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dados : dataSnapshot.getChildren())
                {
                    disponibilidade = dados.getValue(DisponibilidadeMotorista.class);
                    if (checarDistancia(localOrigem,distanciaMax))
                    {
                        if (checarPortes(animaisSelecionados))
                        {
                            motoristasDisponiveis.add(disponibilidade);
                        }
                    }
                }
                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        //contador.await();
        return motoristasDisponiveis;
    }

    private boolean checarDistancia (Local localOrigem, float distanciaMax)
    {
        Location donoAnimal = new Location("donoAnimal");
        Location motorista  = new Location("motorista");
        float distancia = 0;

        donoAnimal.setLatitude(localOrigem.getLatitude());
        donoAnimal.setLongitude(localOrigem.getLatitude());

        motorista.setLatitude(disponibilidade.getLatitudeMotorista());
        motorista.setLongitude(disponibilidade.getLongitudeMotorista());

        distancia = donoAnimal.distanceTo(motorista);

        if(distancia < distanciaMax)
        {
            return true;
        }

        return false;
    }

    public boolean checarPortes (ArrayList<Animal> animaisSelecionados )
    {
        ArrayList<String> portesSelecionados = new ArrayList<>();
        for (int i= 0; i<animaisSelecionados.size(); i++)
        {
            portesSelecionados.add(animaisSelecionados.get(i).getPorteAnimal());
        }

        for (int i =0; i<portesSelecionados.size(); i++)
        {
            if(disponibilidade.getPorteAnimalPequeno()=="false" && portesSelecionados.get(i) == "Pequeno - Até 35cm")
            { return false;}

            if(disponibilidade.getPorteAnimalMedio() == "false" && portesSelecionados.get(i) == "Médio - De 36 a 49cm")
            { return false; }

            if(disponibilidade.getPorteAnimalGrande() == "false" && portesSelecionados.get(i) == "Grande - Acima de 50cm")
            { return false; }

        }
        return true;
    }


    }


