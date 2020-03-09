package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Motorista extends Usuario implements Parcelable {
    // Pega referência do Sotorage
    //private StorageReference storageReference;
    //private String idUsuario;
    private byte[] fotoCNH;
    private byte[] fotoPerfilMotorista;
    private byte[] fotoCrlv;
    private String statusCadastro;


    // Construtor
    public Motorista() {
    }

    // Métodos Getter and Setter
    /*
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    } */

    public byte[] getFotoCNH() {
        return fotoCNH;
    }

    public void setFotoCNH(byte[] fotoCNH) {
        this.fotoCNH = fotoCNH;
    }

    public byte[] getFotoPerfilMotorista() {
        return fotoPerfilMotorista;
    }

    public void setFotoPerfilMotorista(byte[] fotoPerfilMotorista) {
        this.fotoPerfilMotorista = fotoPerfilMotorista;
    }

    public byte[] getFotoCrlv() {
        return fotoCrlv;
    }

    public void setFotoCrlv(byte[] fotoCrlv) {
        this.fotoCrlv = fotoCrlv;
    }

    public String getStatusCadastro() {
        return statusCadastro;
    }

    public void setStatusCadastro(String statusCadastro) {
        this.statusCadastro = statusCadastro;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Motorista(Parcel in) {
        //idUsuario = in.readString();
        fotoCNH = in.createByteArray();
        fotoPerfilMotorista = in.createByteArray();
        fotoCrlv = in.createByteArray();
        statusCadastro = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(idUsuario);
        dest.writeByteArray(fotoCNH);
        dest.writeByteArray(fotoPerfilMotorista);
        dest.writeByteArray(fotoCrlv);
        dest.writeString(statusCadastro);
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
