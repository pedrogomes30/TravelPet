package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.travelpet.R;
import com.example.travelpet.adapter.ListaVeiculosAdapter;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.helper.RecyclerItemClickListener;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.Veiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListaVeiculosFragment extends Fragment {

    private RecyclerView recyclerViewVeiculos;
    private ListaVeiculosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Veiculo> listaVeiculos = new ArrayList<Veiculo>() ;
    private DatabaseReference usuariosRef;
    private FloatingActionButton floatAddVeiculo;
    private VeiculoDAO veiculodao;
    private ListaVeiculosViewModel veiculosViewModel;
    private ValueEventListener valueEventListenerListaVeiculos;
    DatabaseReference referenciaVeiculos;


    public ListaVeiculosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_veiculos, container, false);

        floatAddVeiculo = view.findViewById(R.id.floatAddVeiculo);
        recyclerViewVeiculos = view.findViewById(R.id.recyclerViewListaVeiculos);



        //listaVeiculos.add(testeVeiculo("123456","652314","Fusca","Volkswagem","PNK-9645","1980","123456789"));
        //listaVeiculos.add(testeVeiculo("123456","789521","Gol","Volkswagem","PXF-6541","1994","78945613"));
        //listaVeiculos.add(testeVeiculo("123456","159753","Astra","Volkswagem","PLJ-6432","2010","91738246"));
        //adapter.notifyDataSetChanged();

        referenciaVeiculos = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        adapter = new ListaVeiculosAdapter(getActivity(),listaVeiculos);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewVeiculos.setLayoutManager(layoutManager);
        recyclerViewVeiculos.setHasFixedSize(true);
        recyclerViewVeiculos.setAdapter(adapter);

        //Setar Onclicks
        OnclickAddVeiculo();
        setaRecyclerOnClick();

        return view;
    }

    private void OnclickAddVeiculo() {
        floatAddVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chamar Fragment de edição
                Navigation.findNavController(view).navigate(R.id.action_addVeiculo);
            }
        });
    }

    private void recuperarVeiculos()
    {
        {
            valueEventListenerListaVeiculos = referenciaVeiculos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot)
                {
                    listaVeiculos.clear();
                    for( DataSnapshot dados: dataSnapshot.getChildren() )
                    {
                        Veiculo veiculo = dados.getValue(Veiculo.class);
                        listaVeiculos.add ( veiculo );

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarVeiculos();
    }

    @Override
    public void onResume() {
        super.onResume();
        listaVeiculos.clear();
    }

    private void setaRecyclerOnClick ()
    {
        recyclerViewVeiculos.addOnItemTouchListener( new RecyclerItemClickListener(getActivity(), recyclerViewVeiculos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Veiculo veiculoSelecionado = listaVeiculos.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("veiculo",veiculoSelecionado);
                Navigation.findNavController(view).navigate(R.id.action_exibirVeiculo,bundle);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }
}