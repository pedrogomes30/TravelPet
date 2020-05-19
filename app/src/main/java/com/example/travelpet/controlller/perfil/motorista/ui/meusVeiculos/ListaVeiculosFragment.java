package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelpet.R;
import com.example.travelpet.adapter.ListaAnimaisAdapter;
import com.example.travelpet.adapter.ListaVeiculosAdapter;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.helper.RecyclerItemClickListener;
import com.example.travelpet.model.Veiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class ListaVeiculosFragment extends Fragment {

    private RecyclerView recyclerViewVeiculos;
    private ListaVeiculosAdapter adapter;
    private ArrayList<Veiculo> listaVeiculos;
    private DatabaseReference usuariosRef;
    private FloatingActionButton floatAddVeiculo;
    private VeiculoDAO veiculodao;

    public ListaVeiculosFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_lista_veiculos, container, false);

        floatAddVeiculo  = view.findViewById(R.id.floatAddVeiculo);
        recyclerViewVeiculos =view.findViewById(R.id.recyclerViewListaVeiculos);

        //Setando Conexões Firebase
        veiculodao = new VeiculoDAO();
        listaVeiculos = veiculodao.receberVeiculos();

        //Setando RecyclerView
        adapter = new ListaVeiculosAdapter(getContext(),listaVeiculos);
        recyclerViewVeiculos.setAdapter(adapter);
        recyclerViewVeiculos.setLayoutManager(new LinearLayoutManager(getContext()));

        //Setar Onclicks
        OnclickAddVeiculo();

        return view;
    }

    public void OnclickAddVeiculo ()
    {
        floatAddVeiculo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //chamar Fragment de edição
            }
        });
    }

}
