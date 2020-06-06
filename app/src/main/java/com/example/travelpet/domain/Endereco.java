package com.example.travelpet.domain;

import android.os.Parcel;
import android.os.Parcelable;

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

    // Métodos Necessarios para usar a Interface Parcelable
    public Endereco(Parcel in) {

        cep = in.readString();
        logradouro = in.readString();
        bairro = in.readString();
        localidade = in.readString();
        uf = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(cep);
        dest.writeString(logradouro);
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
