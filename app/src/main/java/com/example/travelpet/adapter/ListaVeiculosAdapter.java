package com.example.travelpet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.travelpet.R;
import com.example.travelpet.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaVeiculosAdapter extends RecyclerView.Adapter<ListaVeiculosAdapter.MyViewHolder>
{
    ArrayList<Veiculo> listaVeiculos = new ArrayList<Veiculo>();
    LayoutInflater layoutInflater;

    public ListaVeiculosAdapter () {}

    public ListaVeiculosAdapter (Context context, ArrayList<Veiculo> veiculos)
    {
        layoutInflater = LayoutInflater.from(context);
        this.listaVeiculos = veiculos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapterlistaveiculos,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Veiculo veiculo = listaVeiculos.get(position);

        holder.tvModelo.setText(veiculo.getModeloVeiculo());
        holder.tvPlaca.setText(veiculo.getPlacaVeiculo());
        holder.tvMarca.setText(veiculo.getMarcaVeiculo());
    }

    @Override
    public int getItemCount() {
        return listaVeiculos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvModelo,tvPlaca,tvMarca;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvModelo = itemView.findViewById(R.id.tv_adapterVeiculoModelo);
            tvPlaca = itemView.findViewById(R.id.tv_adapterVeiculoPlaca);
            tvMarca = itemView.findViewById(R.id.tv_adapterVeiculoMarca);

        }
    }
}
