package com.example.travelpet.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.classes.Animal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// Aula 302 - Listando Contatos
public class ListaAnimaisAdapter extends RecyclerView.Adapter<ListaAnimaisAdapter.MyViewHolder> {

    // Variavel para receber a lista do adapter, e passar como parametro no construtor
    private List<Animal> listaAnimais;
    private Context context;

    public ListaAnimaisAdapter(List<Animal> listaA, Context c) {
        this.listaAnimais = listaA;
        this.context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_lista_animais, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        // Recuperando dados da classe animal
        Animal animal = listaAnimais.get( position );

        holder.textNomeAnimal.setText("Nome: " + animal.getNomeAnimal() );
        holder.textEspecieAnimal.setText("Espécie: " + animal.getEspecieAnimal());
        holder.textPorteAnimal.setText("Porte: " + animal.getPorteAnimal());
        holder.textRacaAnimal.setText("Raça: " + animal.getRacaAnimal());

        if( animal.getFotoAnimal() != null ){
            Uri uri = Uri.parse(animal.getFotoAnimal());
            Glide.with( context ).load(uri).into(holder.imageFotoAnimalLista);
        }else{
            holder.imageFotoAnimalLista.setImageResource( R.drawable.iconperfilanimal);
        }
    }

    @Override
    public int getItemCount() {
        // retorna o tamanho da lista dos Animais
        return listaAnimais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageFotoAnimalLista;
        TextView textNomeAnimal,textId,textEspecieAnimal,textPorteAnimal,textRacaAnimal;

        // Construtor do MyViewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFotoAnimalLista = itemView.findViewById(R.id.imageFotoAnimalLista);
            textNomeAnimal = itemView.findViewById(R.id.textNomeAnimal);
           // textId = itemView.findViewById(R.id.textId);
            textEspecieAnimal = itemView.findViewById(R.id.textEspecieAnimal);
            textPorteAnimal = itemView.findViewById(R.id.textPorteAnimal);
            textRacaAnimal = itemView.findViewById(R.id.textRacaAnimal);

        }
    }

}
