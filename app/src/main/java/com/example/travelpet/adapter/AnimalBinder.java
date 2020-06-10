package com.example.travelpet.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.travelpet.R;
import com.example.travelpet.model.Animal;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class AnimalBinder extends ItemBinder<Animal, AnimalBinder.AnimalViewHolder>
{

    @Override
    public AnimalBinder.AnimalViewHolder createViewHolder(ViewGroup parent)
    {
        return new AnimalViewHolder(inflate(parent, R.layout.adapterlistaanimais_bs));
    }

    @Override
    public boolean canBindData(Object item) { return item instanceof Animal; }

    @Override
    public void bindViewHolder(AnimalBinder.AnimalViewHolder holder, Animal item)
    {
        holder.nomeAnimal.setText(item.getNomeAnimal());
        holder.fotoAnimal.setImageResource(R.drawable.iconperfilanimal);

        int corClick    =   ContextCompat.getColor(holder.nomeAnimal.getContext(),R.color.colorApp);
        int colorApp    =   ContextCompat.getColor(holder.nomeAnimal.getContext(),R.color.white);



            if (holder.isItemSelected() == true)
            {
                holder.cardView.setBackgroundColor(corClick);
                holder.cardView.setCardElevation(16);
            }
            else
            {
                holder.cardView.setBackgroundColor(colorApp);
                holder.cardView.setCardElevation(4);
            }

    }

    public static class AnimalViewHolder extends ItemViewHolder<Animal>
    {
        TextView nomeAnimal;
        CircleImageView fotoAnimal;
        CardView cardView;

        public AnimalViewHolder (View itemView)
        {
            super(itemView);

            nomeAnimal = itemView.findViewById(R.id.tv_nomeanimal_bs);
            fotoAnimal = itemView.findViewById(R.id.ci_animal_bs);
            cardView = itemView.findViewById(R.id.cardview_animal_bs);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleItemSelection();
                }
            });
        }
    }
}
