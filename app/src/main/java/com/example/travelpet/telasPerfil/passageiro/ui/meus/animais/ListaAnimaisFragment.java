package com.example.travelpet.telasPerfil.passageiro.ui.meus.animais;

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
import com.example.travelpet.activity.adapter.ListaAnimaisAdapter;
import com.example.travelpet.activity.cadastro.cadastroAnimal.CadastroNomeAnimalActivity;
import com.example.travelpet.activity.classes.Animal;
import com.example.travelpet.activity.classes.Usuario;
import com.example.travelpet.activity.config.ConfiguracaoFirebase;
import com.example.travelpet.activity.config.UsuarioFirebase;
import com.example.travelpet.activity.helper.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaAnimaisFragment extends Fragment {

    private RecyclerView recyclerViewListaAnimais;
    private ListaAnimaisAdapter adapter;
    private ArrayList<Animal> listaAnimais = new ArrayList<>();
    private DatabaseReference usuariosRef;

    private ValueEventListener valueEventListenerListaAnimais;

    // Variaveis usadas para pegar dados nulos para activity "CadastroNomeAnimalActivity"
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;
    // Variável usada para o fluxo de adicionar animais
    private String fluxoDados = "perfilUsuario";

    public static ListaAnimaisFragment newInstance() {
        return new ListaAnimaisFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_animais_fragment, container, false);

        // Configurando Evento de clique do FloatingActionButton
        FloatingActionButton adicionarAnimal = view.findViewById(R.id.adicionarAnimal);
        adicionarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario();
                // Passando dados nulo para Activity CadastroNomeAnimal
                // Para poder enganar e passa o FluxoDados junto
                usuario.setNome(nomeUsuario);
                usuario.setSobrenome(sobrenomeUsuario);
                usuario.setTelefone(telefoneUsuario);
                usuario.setTipoUsuario(tipoUsuario);
                usuario.setFluxoDados(fluxoDados);

                Intent intent = new Intent(getActivity(), CadastroNomeAnimalActivity.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });

        recyclerViewListaAnimais = view.findViewById(R.id.recyclerViewListaAnimais);
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("animais").child(UsuarioFirebase.getIdentificadorUsuario());

        adapter = new ListaAnimaisAdapter( listaAnimais, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
                    Intent intent = new Intent(getActivity(),EditarAnimalActivity.class);
                    intent.putExtra("animalSelecionado", animalSelecionado);
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            }
            )
        );
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
        usuariosRef.removeEventListener( valueEventListenerListaAnimais );
    }

    @Override
    public void onResume() {
        super.onResume();
        listaAnimais.clear();
    }

    public void recuperarAnimais(){
        valueEventListenerListaAnimais = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Animal animal = dados.getValue(Animal.class);
                    listaAnimais.add ( animal );

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
