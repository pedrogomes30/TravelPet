package com.example.travelpet.domain;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.travelpet.controlller.cadastro.cadastroUsuario.CadastroUsuarioDadosActivity;

//import br.com.thiengo.marketplaceapp.SignUpActivity;
// substitui o de cima por esse caminho de baixo

/**
 * Created by viniciusthiengo on 03/01/17.
 */

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

        if( editable.length() == 8 ){
            new EnderecoRequest( (CadastroUsuarioDadosActivity) context ).execute();
        }
    }
}
