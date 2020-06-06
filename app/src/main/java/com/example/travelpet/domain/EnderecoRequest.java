package com.example.travelpet.domain;

import android.os.AsyncTask;

import com.example.travelpet.controlller.cadastro.cadastroUsuario.CadastroUsuarioDadosActivity;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class EnderecoRequest extends AsyncTask<Void, Void, Endereco> {
    // Referencia fraca
    private WeakReference<CadastroUsuarioDadosActivity> activity;

    // Construtor
    public EnderecoRequest(CadastroUsuarioDadosActivity activity ){
        this.activity = new WeakReference<CadastroUsuarioDadosActivity>( activity );
    }

    @Override // Serve para travar antes da requisição
    protected void onPreExecute() {
        super.onPreExecute();
        // if( activity.get() != null ){ = verifica se a activity não foi destruida,
        // para ver se a referencia ainda existe
        if( activity.get() != null ){
              // para travar os campos que os cep for preencher antes da requisição
              activity.get().lockFields( true );
        }
    }

    @Override
    protected Endereco doInBackground(Void... voids) {
        try {
            // Acesso ao JsonRequest
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );

            Gson gson = new Gson();
            // Address.class = classe pojo classe que vai ser mapeada
            return gson.fromJson( jsonString, Endereco.class );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override   // Para destravar
    protected void onPostExecute(Endereco address) {
        super.onPostExecute(address);

        if( activity.get() != null ){
            // para destravar
            activity.get().lockFields( false );

            // verificação para ter problema com pointException(não sei se escreve assim)
            if( address != null ){
                activity.get().setDataViews( address );
            }
        }
    }
}
