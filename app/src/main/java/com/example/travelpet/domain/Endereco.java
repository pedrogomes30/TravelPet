package com.example.travelpet.domain;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by viniciusthiengo on 03/01/17.
 */
/*
Address
 Classe para responder no caso ao retorno do ViaCep
 classe POJO, que vai ter que mapear / mapeamento utilizando o json
  */
public class Endereco implements Parcelable {
    //public static final int REQUEST_ZIP_CODE_CODE = 566;
    //public static final String ZIP_CODE_KEY = "zip_code_key";

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    // Construtor
    public Endereco() {
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    /*     Metodo para salvar os dados do endereço do usuario no database do firebase
    public void salvarEnderecoDatabase(String tipoUsuario){
        // Transformando a primeira letra do "tipoUsuario" em maiuscula
        // para usar no metodo de salvarEnderecoDatabase
        tipoUsuario = tipoUsuario.substring(0,1).toUpperCase()+tipoUsuario.substring(1);

        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference enderecosRef = fireDB.getReference().child("enderecos"+tipoUsuario)
                                                              .child(UsuarioFirebase.getIdentificadorUsuario());

        enderecosRef.setValue(this).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    } */

    // Métodos Necessarios para usar a Interface Parcelable
    public Endereco(Parcel in) {

        cep = in.readString();
        logradouro = in.readString();
        complemento = in.readString();
        bairro = in.readString();
        localidade = in.readString();
        uf = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(cep);
        dest.writeString(logradouro);
        dest.writeString(complemento);
        dest.writeString(bairro);
        dest.writeString(localidade);
        dest.writeString(uf);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };
}
