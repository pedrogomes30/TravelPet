package com.example.travelpet.dao;

import android.location.Location;
import android.provider.ContactsContract;
import android.util.Log;

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

import java.text.DecimalFormat;
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

    public DatabaseReference receberDisponibilidaReferencia(DisponibilidadeMotorista referencia)
    {
        DatabaseReference dbref = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("disponibilidadeMotorista")
                .child(referencia.getIdMotorista());
        return dbref;
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

    public DisponibilidadeMotorista queryMotoristaDisponivel
            (final Local localOrigem,
             final CountDownLatch contador,
             final float distanciaMax,
             final ArrayList<Animal> animaisSelecionados,
             final ArrayList<String> motoristasCancelados)
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

                    if(checarPortes(animaisSelecionados, disponibilidade))
                    {
                        showInTerminal("checarPortes : OK");
                        if (checarMotoristasCancelados(motoristasCancelados))
                        {
                            showInTerminal("checarMotoristasCancelados : OK");
                            if (checarDistancia(localOrigem,distanciaMax))
                            {
                                showInTerminal("checarDistancia : OK");
                                motoristasDisponiveis.add(disponibilidade);
                            }
                            else
                            {
                                showInTerminal("Motorista muito distante");
                            }
                        }
                        else
                        {
                            showInTerminal("motorista Cancelado");
                        }
                    }
                    else
                    {
                        showInTerminal("animais não são compativeis");
                    }
                }
                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                showInTerminal("nenhum motorista disponível");
                contador.countDown();
            }
        });

        try {contador.await();}
        catch (InterruptedException e)
        {e.printStackTrace();}

        if (motoristasDisponiveis.size() > 0 )
        {
            retornaMotoristaMaisProximo(localOrigem);
            showInTerminal("Devolvendo "+disponibilidade.getIdMotorista());
            return disponibilidade;
        }
        else
        {
            return null;
        }
    }

    private void retornaMotoristaMaisProximo (Local localOrigem)
    {
        if(motoristasDisponiveis.size() == 1)
        {
            disponibilidade = motoristasDisponiveis.get(0);
            showInTerminal("apenas 1 motorista");
        }

        else if (motoristasDisponiveis.size() > 1)
        {
            float menorDistancia = 0;
            int motoristaMaisProximo = 0;
            Location origem = new Location("Origem");
            origem.setLongitude(localOrigem.getLongitude());
            origem.setLatitude(localOrigem.getLatitude());

            for (int i = 0; i < motoristasDisponiveis.size(); i++)
            {
                Location motorista = new Location("Motorista");
                motorista.setLatitude(motoristasDisponiveis.get(i).getLatitudeMotorista());
                motorista.setLongitude(motoristasDisponiveis.get(i).getLongitudeMotorista());
                float distancia = origem.distanceTo(motorista);
                showInTerminal("Distancia do Motorista ["+i+"]= " + distancia);

                if (menorDistancia == 0) {
                    menorDistancia = origem.distanceTo(motorista);
                    motoristaMaisProximo = i;
                }

                else
                {
                    if (menorDistancia > distancia)
                    {
                        showInTerminal("menor distancia = " + Math.round(distancia));
                        menorDistancia = distancia;
                        motoristaMaisProximo = i;
                    }
                }
            }
            disponibilidade = motoristasDisponiveis.get(motoristaMaisProximo);
        }

        else
        {
            showInTerminal("nã há motorists próximos");
        }
    }

    private boolean checarMotoristasCancelados(ArrayList<String> motoristasCancelados)
    {
        for (int i = 0; i< motoristasCancelados.size();i++)
        {
            if (disponibilidade.getIdMotorista().equals(motoristasCancelados.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    private boolean checarDistancia (Local localOrigem, float distanciaMax)
    {
        Location donoAnimal = new Location("donoAnimal");
        Location motorista  = new Location("motorista");

        float distancia = 0;

        donoAnimal.setLatitude(localOrigem.getLatitude());
        donoAnimal.setLongitude(localOrigem.getLongitude());
        showInTerminal("latitude donoAnimal = " + String.valueOf(donoAnimal.getLatitude()));
        showInTerminal("longitude donoAnimal = " + String.valueOf(donoAnimal.getLongitude()));

        motorista.setLatitude(disponibilidade.getLatitudeMotorista());
        motorista.setLongitude(disponibilidade.getLongitudeMotorista());
        showInTerminal("latitude motorista = " + String.valueOf(motorista.getLatitude()));
        showInTerminal("latitude motorista = " + String.valueOf(motorista.getLongitude()));

        distancia = donoAnimal.distanceTo(motorista);
        //distanceTo retorna em metros, pra obter em kilometros precisa dividir por 1000

        int teste = (int) distancia;
        int teste2 = (int) distanciaMax;

        //DecimalFormat df = new DecimalFormat("0.0"); para km


        showInTerminal("distancia em int =" + teste);
        showInTerminal("distanciaMAX em int =" + teste2);

        showInTerminal("distancia em Float =" + distancia);
        showInTerminal("distanciaMAX em Float=" + distanciaMax);


        if(distancia < distanciaMax)
        {
            return true;
        }

        return false;
    }

    public boolean checarPortes (ArrayList<Animal> animaisSelecionados, DisponibilidadeMotorista disponivel)
    {
        DisponibilidadeMotorista dispPortes = disponivel;

        ArrayList<String> portesSelecionados = new ArrayList<>();
        for (int i= 0; i< animaisSelecionados.size(); i++)
        {
            portesSelecionados.add(animaisSelecionados.get(i).getPorteAnimal());
        }

        for (int i =0; i<portesSelecionados.size(); i++)
        {
            if(dispPortes.getPorteAnimalPequeno().equals("false") && portesSelecionados.get(i).equals("pequeno"))
            { showInTerminal("porte Pequeno Imcompatível");
                return false;
            }

            if(dispPortes.getPorteAnimalMedio().equals("false") && portesSelecionados.get(i).equals("medio"))
            {
                showInTerminal("porte Medio Imcompatível");
                return false;
            }

            if(dispPortes.getPorteAnimalGrande().equals("false") && portesSelecionados.get(i).equals("grande"))
            { showInTerminal("porte Grande Imcompatível");
                return false;
            }
        }
        return true;
    }


    public void showInTerminal (String mensagem)
    {
        System.out.println(mensagem);
    }

    }

