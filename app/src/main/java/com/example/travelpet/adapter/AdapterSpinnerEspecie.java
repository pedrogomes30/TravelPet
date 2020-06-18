package com.example.travelpet.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;

import java.util.ArrayList;

public class AdapterSpinnerEspecie extends ArrayAdapter {
    public AdapterSpinnerEspecie(Context context, ArrayList<ItemSpinnerEspecie> customList) {
        super(context, 0, customList);
    }


    //@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        }
        ItemSpinnerEspecie item = (ItemSpinnerEspecie) getItem(position);
        ImageView spinnerIV = convertView.findViewById(R.id.ivSpinnerLayout);
        TextView spinnerTV = convertView.findViewById(R.id.tvSpinnerLayout);
        if (item != null) {
            if(item.getSpinnerItemImage()== null){
                spinnerIV.setImageResource(R.drawable.ic_spinner_especie);
            }else{
                Uri fotoEspecieSpinnerUri = Uri.parse(item.getSpinnerItemImage());
                Glide.with(getContext())
                        .load( fotoEspecieSpinnerUri )
                        .into( spinnerIV);
            }
            spinnerTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout, parent, false);
        }
        ItemSpinnerEspecie item = (ItemSpinnerEspecie) getItem(position);
        ImageView dropDownIV = convertView.findViewById(R.id.ivDropDownLayout);
        TextView dropDownTV = convertView.findViewById(R.id.tvDropDownLayout);
        if (item != null) {

            if(item.getSpinnerItemImage()== null){
                dropDownIV.setImageResource(R.drawable.ic_spinner_especie);
            }else{
                Uri fotoEspecieSpinnerUri = Uri.parse(item.getSpinnerItemImage());
                Glide.with(getContext())
                        .load( fotoEspecieSpinnerUri )
                        .into( dropDownIV);
            }
            dropDownTV.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
}
