package com.example.travelpet.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.travelpet.R;
import com.example.travelpet.model.Veiculo;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class VeiculoBinder extends ItemBinder<Veiculo, VeiculoBinder.VeiculoViewHolder> {


    @Override
    public VeiculoViewHolder createViewHolder(ViewGroup parent) {
        return new VeiculoViewHolder(inflate(parent,R.layout.adapterlistaveiculos_bs));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Veiculo;
    }

    @Override
    public void bindViewHolder(VeiculoViewHolder holder, Veiculo item) {
        holder.modelo.setText(item.getModeloVeiculo());
        holder.marca.setText(item.getMarcaVeiculo());
        holder.placa.setText(item.getPlacaVeiculo());

        int bgColor = ContextCompat.getColor(holder.modelo.getContext(), R.color.colorButtonAzulPadrao);
        int bgpadrao = ContextCompat.getColor(holder.modelo.getContext(), R.color.white);

        if (holder.isItemSelected()== true)
        {
            holder.cardview.setCardBackgroundColor(bgColor);
            holder.cardview.setCardElevation(16);
        }
        else
        {
            holder.cardview.setCardBackgroundColor(bgpadrao);
            holder.cardview.setCardElevation(2);
        }
    }

    public static  class VeiculoViewHolder extends ItemViewHolder<Veiculo>
 {
     TextView modelo,placa,marca;
     CardView cardview;

     public VeiculoViewHolder(View itemView) {
         super(itemView);

         modelo = itemView.findViewById(R.id.tv_modeloveiculo_bs);
         marca = itemView.findViewById(R.id.tv_marcaveiculo_bs);
         placa = itemView.findViewById(R.id.tv_placaveiculo_bs);
         cardview =itemView.findViewById(R.id.card_adapter_motorista_bs);

         itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 toggleItemSelection();
             }
         });
     }
 }
}
