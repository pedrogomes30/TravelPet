package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Veiculo implements Parcelable {


    //private String corVeiculo;
    //private String capacidadeAnimal;
    //disponibilidade
    //

    private byte [] fotoCrvl;
    private String marcaVeiculo;
    private String modeloVeiculo;
    private String crlvVeiculo;
    private String anoVeiculo;
    private String placaVeiculo;
    private String idUsuario;
    private String status;

    private String fotoCRVLurl;
    private String idVeiculo;

    public Veiculo ()
    {
        setStatus("Bloqueado");
    }

    public String getIdUsuario(){ return idUsuario; }

    public void setIdUsuario (String idUsuario) {this.idUsuario = idUsuario ;}

    public String getIdVeiculo() {return idVeiculo;}

    public void setIdVeiculo(String idVeiculo) {this.idVeiculo = idVeiculo;}

    public String getMarcaVeiculo() {return marcaVeiculo;}

    public void setMarcaVeiculo(String marcaVeiculo) {this.marcaVeiculo = marcaVeiculo;}

    public String getModeloVeiculo() {return modeloVeiculo;}

    public void setModeloVeiculo(String modeloVeiculo) {this.modeloVeiculo = modeloVeiculo;}

    public String getAnoVeiculo() {return anoVeiculo;}

    public void setAnoVeiculo(String anoVeiculo) {this.anoVeiculo = anoVeiculo;}

    public String getPlacaVeiculo() {return placaVeiculo;}

    public void setPlacaVeiculo(String placaVeiculo) {this.placaVeiculo = placaVeiculo;}

    public String getCrlvVeiculo() {return crlvVeiculo;}

    public String getStatus ()  {return status;}

    public void setStatus (String status) {this.status = status;}

    public void setCrlvVeiculo(String crvlVeiculo) {this.crlvVeiculo = crlvVeiculo;}

    public String getFotoCRVLurl() { return fotoCRVLurl; }

    public void setFotoCRVLurl(String fotoCRVLurl) { this.fotoCRVLurl = fotoCRVLurl; }

    @Exclude
    public byte[] getFotoCrvl() { return fotoCrvl;}

    public void setFotoCrvl(byte[] fotoCrvl) { this.fotoCrvl = fotoCrvl; }


    //public String getCapacidadeAnimal() {return capacidadeAnimal;}

    //public void setCapacidadeAnimal(String capacidadeAnimal) {this.capacidadeAnimal = capacidadeAnimal;}

    //public String getCorVeiculo() {return corVeiculo;}

    //public void setCorVeiculo(String corVeiculo) {this.corVeiculo = corVeiculo;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(fotoCrvl);
        parcel.writeString(marcaVeiculo);
        parcel.writeString(modeloVeiculo);
        parcel.writeString(crlvVeiculo);
        parcel.writeString(anoVeiculo);
        parcel.writeString(placaVeiculo);
        parcel.writeString(idUsuario);
        parcel.writeString(fotoCRVLurl);
        parcel.writeString(idVeiculo);
        parcel.writeString(status);
    }

    protected Veiculo(Parcel in) {
        fotoCrvl = in.createByteArray();
        marcaVeiculo = in.readString();
        modeloVeiculo = in.readString();
        crlvVeiculo = in.readString();
        anoVeiculo = in.readString();
        placaVeiculo = in.readString();
        idUsuario = in.readString();
        fotoCRVLurl = in.readString();
        idVeiculo = in.readString();
        status = in.readString();
    }

    public static final Creator<Veiculo> CREATOR = new Creator<Veiculo>() {
        @Override
        public Veiculo createFromParcel(Parcel in) {
            return new Veiculo(in);
        }

        @Override
        public Veiculo[] newArray(int size) {
            return new Veiculo[size];
        }
    };
}
