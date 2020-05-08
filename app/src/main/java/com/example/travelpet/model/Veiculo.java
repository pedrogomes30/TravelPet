package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Veiculo implements Parcelable {


    //private String corVeiculo;
    //private String capacidadeAnimal;

    private String marcaVeiculo;
    private String modeloVeiculo;
    private String crvlVeiculo;
    private String anoVeiculo;
    private String placaVeiculo;
    private String idUsuario;

    private String fotoCRVL;
    private String idVeiculo;

    public Veiculo () {}


    public String getIdUsuario(){return idUsuario; }

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

    public String getCrvlVeiculo() {return crvlVeiculo;}

    public void setCrvlVeiculo(String crvlVeiculo) {this.crvlVeiculo = crvlVeiculo;}

    public String getFotoCRVL() {return fotoCRVL;}

    public void setFotoCRVL(String fotoCRVL) {this.fotoCRVL = fotoCRVL;}

    //public String getCapacidadeAnimal() {return capacidadeAnimal;}

   //public void setCapacidadeAnimal(String capacidadeAnimal) {this.capacidadeAnimal = capacidadeAnimal;}

    //public String getCorVeiculo() {return corVeiculo;}

    //public void setCorVeiculo(String corVeiculo) {this.corVeiculo = corVeiculo;}

    protected Veiculo(Parcel in) {
        idUsuario = in.readString();
        idVeiculo = in.readString();
        marcaVeiculo = in.readString();
        modeloVeiculo = in.readString();
        anoVeiculo = in.readString();
        placaVeiculo = in.readString();
        crvlVeiculo = in.readString();
        fotoCRVL = in.readString();
        //corVeiculo = in.readString();
        //capacidadeAnimal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idUsuario);
        parcel.writeString(idVeiculo);
        parcel.writeString(marcaVeiculo);
        parcel.writeString(modeloVeiculo);
        parcel.writeString(anoVeiculo);
        parcel.writeString(placaVeiculo);
        parcel.writeString(crvlVeiculo);
        parcel.writeString(fotoCRVL);
        //parcel.writeString(corVeiculo);
        //parcel.writeString(capacidadeAnimal);
    }

    @Override
    public int describeContents() {return 0;}

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
