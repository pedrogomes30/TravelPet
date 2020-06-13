package com.example.travelpet.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelpet.adapter.ItemSpinnerEspecie;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TipoAnimalDAO {

    public static ArrayList<ItemSpinnerEspecie> getListaEspecie() {

        final ArrayList<ItemSpinnerEspecie> listaEspecie = new ArrayList<>();
        listaEspecie.add(new ItemSpinnerEspecie("espécie", null));

        DatabaseReference especieRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("racaAnimal");
        especieRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                listaEspecie.add(new ItemSpinnerEspecie(dataSnapshot.getKey(), dataSnapshot.child("iconeUrl").getValue(String.class)));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return listaEspecie;
    }

    public static ArrayList<String> getListaRaca(final String especieAnimal) {

        final ArrayList<String> listaRacaAnimal = new ArrayList<>();
        DatabaseReference racaAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("racaAnimal").child(especieAnimal);
        racaAnimalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    listaRacaAnimal.add(dados.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return listaRacaAnimal;
    }

}
