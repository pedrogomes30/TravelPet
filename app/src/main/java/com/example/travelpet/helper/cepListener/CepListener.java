package com.example.travelpet.helper.cepListener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.travelpet.controlller.cadastro.CadastroUsuarioDadosActivity;


public class CepListener implements TextWatcher {
    private Context context;

    public CepListener(Context context ){
        this.context = context;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String zipCode = editable.toString();

        if( editable.length() == 9 ){
            new EnderecoRequest( (CadastroUsuarioDadosActivity) context ).execute();
        }
    }
}
