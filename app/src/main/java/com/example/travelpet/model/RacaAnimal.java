package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RacaAnimal implements Parcelable{

    //private String idRacaAnimal;
    private String nomeRacaAnimal;

    public RacaAnimal() {
    }

    /*
    public String getIdRacaAnimal() {
        return idRacaAnimal;
    }

    public void setIdRacaAnimal(String idRacaAnimal) {
        this.idRacaAnimal = idRacaAnimal;
    }

     */

    public String getNomeRacaAnimal() {
        return nomeRacaAnimal;
    }

    public void setNomeRacaAnimal(String nomeRacaAnimal) {
        this.nomeRacaAnimal = nomeRacaAnimal;
    }


    // Métodos Necessarios para usar a Interface Parcelable
    protected RacaAnimal(Parcel in) {
        //idRacaAnimal = in.readString();
        nomeRacaAnimal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(idRacaAnimal);
        dest.writeString(nomeRacaAnimal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RacaAnimal> CREATOR = new Parcelable.Creator<RacaAnimal>() {
        @Override
        public RacaAnimal createFromParcel(Parcel in) {
            return new RacaAnimal(in);
        }

        @Override
        public RacaAnimal[] newArray(int size) {
            return new RacaAnimal[size];
        }
    };

}
