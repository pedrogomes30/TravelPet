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
                .child(ConfiguracaoFirebase.tipoAnimal);
        especieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    listaEspecie.add(new ItemSpinnerEspecie(data.getKey(), data
                            .child(ConfiguracaoFirebase.iconeUrl)
                            .getValue(String.class)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return listaEspecie;
    }

    public static ArrayList<String> getListaRaca(final String especieAnimal) {

        final ArrayList<String> listaRacaAnimal = new ArrayList<>();
        DatabaseReference racaAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.tipoAnimal).child(especieAnimal);
        racaAnimalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    listaRacaAnimal.add(dados.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listaRacaAnimal;
    }

}
