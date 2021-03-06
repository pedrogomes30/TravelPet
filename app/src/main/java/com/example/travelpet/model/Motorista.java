package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;


public class Motorista extends Usuario implements Parcelable {

    private byte[] fotoCNH;
    private String registroCnh;
    private String fotoCnhUrl;
    private String fotoCrlvUrl;

    // Construtor
    public Motorista() {}

    @Exclude
    public byte[] getFotoCNH() {
        return fotoCNH;
    }

    public void setFotoCNH(byte[] fotoCNH) {
        this.fotoCNH = fotoCNH;
    }

    public String getRegistroCnh() {
        return registroCnh;
    }

    public void setRegistroCnh(String registroCnh) {
        this.registroCnh = registroCnh;
    }

    public String getFotoCnhUrl() {
        return fotoCnhUrl;
    }

    public void setFotoCnhUrl(String fotoCnhUrl) {
        this.fotoCnhUrl = fotoCnhUrl;
    }

    public String getFotoCrlvUrl() {
        return fotoCrlvUrl;
    }

    public void setFotoCrlvUrl(String fotoCrlvUrl) {
        this.fotoCrlvUrl = fotoCrlvUrl;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Motorista(Parcel in) {

        // Dados herdados da classe Usuario
        idUsuario      = in.readString();
        tipoUsuario    = in.readString();
        nome           = in.readString();
        sobrenome      = in.readString();
        telefone       = in.readString();
        cpf            = in.readString();
        email          = in.readString();
        fotoPerfil     = in.createByteArray();
        fotoPerfilUrl  = in.readString();
        statusConta    = in.readString();

        // Dados Classe Motorista
        fotoCNH        = in.createByteArray();
        registroCnh    = in.readString();
        fotoCnhUrl     = in.readString();
        fotoCrlvUrl    = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // Dados Classe Usuario
        dest.writeString(idUsuario);
        dest.writeString(tipoUsuario);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(telefone);
        dest.writeString(cpf);
        dest.writeString(email);
        dest.writeByteArray(fotoPerfil);
        dest.writeString(fotoPerfilUrl);
        dest.writeString(statusConta);

        // Dados Classe Motorista
        dest.writeByteArray(fotoCNH);
        dest.writeString(registroCnh);
        dest.writeString(fotoCnhUrl);
        dest.writeString(fotoCrlvUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Motorista> CREATOR = new Creator<Motorista>() {
        @Override
        public Motorista createFromParcel(Parcel in) {
            return new Motorista(in);
        }

        @Override
        public Motorista[] newArray(int size) {
            return new Motorista[size];
        }
    };
}
