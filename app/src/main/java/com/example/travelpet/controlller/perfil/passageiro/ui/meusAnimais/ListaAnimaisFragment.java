package com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelpet.R;
import com.example.travelpet.controlller.cadastro.cadastroDonoAnimal.CadastroAnimalNomeActivity;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.helper.RecyclerItemClickListener;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaAnimaisFragment extends Fragment {

    private RecyclerView recyclerViewListaAnimais;
    private AdapterListaAnimais adapter;
    private ArrayList<Animal> listaAnimais = new ArrayList<>();
    private DatabaseReference animalRef;
    private ValueEventListener valueEventListenerListaAnimais;
    private FloatingActionButton btAdicionarAnimal;
    RecyclerView.LayoutManager layoutManager;

    public static ListaAnimaisFragment newInstance() {
        return new ListaAnimaisFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_animais, container, false);

        iniciarComponentes(view);
        iniciarRecyclerView();
        botaoAdicionarAnimal();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarAnimais();
    }

    @Override
    public void onStop(){
        super.onStop();
        animalRef.removeEventListener( valueEventListenerListaAnimais );
    }

    @Override
    public void onResume() {
        super.onResume();
        listaAnimais.clear();
    }

    public void iniciarComponentes(View view){

        recyclerViewListaAnimais = view.findViewById(R.id.recyclerViewListaAnimais);
        btAdicionarAnimal = view.findViewById(R.id.adicionarAnimal);
        animalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("animais")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        adapter = new AdapterListaAnimais( listaAnimais, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    public void iniciarRecyclerView(){

        recyclerViewListaAnimais.setLayoutManager( layoutManager );
        recyclerViewListaAnimais.setHasFixedSize( true );
        recyclerViewListaAnimais.setAdapter( adapter );
        recyclerViewListaAnimais.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaAnimais,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                                Animal animalSelecionado = listaAnimais.get(position);
                                Intent intent = new Intent(getActivity(), EditarAnimalActivity.class);
                                intent.putExtra("animalSelecionado", animalSelecionado);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {}
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
                        })
        );
    }

    public void botaoAdicionarAnimal(){

        btAdicionarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fluxoDados = "listaAnimais";
                DonoAnimal donoAnimal = new DonoAnimal();
                donoAnimal.setFluxoDados(fluxoDados);

                Intent intent = new Intent(getActivity(), CadastroAnimalNomeActivity.class);
                intent.putExtra("donoAnimal", donoAnimal);
                startActivity(intent);
            }
        });
    }

    public void recuperarAnimais(){

        valueEventListenerListaAnimais = animalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Animal animal = dados.getValue(Animal.class);
                    listaAnimais.add ( animal );


                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}
