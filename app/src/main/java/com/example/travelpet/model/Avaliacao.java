package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Avaliacao implements Parcelable {
    private String  avaliado,
                    avaliador,
                    observacao,
                    tipoPerfil,
                    data;

    private Double notaAvaliacao;

    public Avaliacao() {}

    public String getAvaliado() {
        return avaliado;
    }

    public void setAvaliado(String avaliado) {
        this.avaliado = avaliado;
    }

    public String getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public Double getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(Double notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // Métodos Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avaliado);
        dest.writeString(avaliador);
        dest.writeString(observacao);
        dest.writeString(tipoPerfil);
        dest.writeString(data);
        if (notaAvaliacao == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(notaAvaliacao);
        }
    }
    protected Avaliacao(Parcel in) {
        avaliado = in.readString();
        avaliador = in.readString();
        observacao = in.readString();
        tipoPerfil = in.readString();
        data = in.readString();
        if (in.readByte() == 0) {
            notaAvaliacao = null;
        } else {
            notaAvaliacao = in.readDouble();
        }
    }

    public static final Creator<Avaliacao> CREATOR = new Creator<Avaliacao>() {
        @Override
        public Avaliacao createFromParcel(Parcel in) {
            return new Avaliacao(in);
        }

        @Override
        public Avaliacao[] newArray(int size) {
            return new Avaliacao[size];
        }
    };
}
