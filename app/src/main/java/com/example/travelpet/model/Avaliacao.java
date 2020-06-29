package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Avaliacao implements Parcelable {
    String  idAvaliado,
            idAvaliador,
            observacao,
            tipoAvaliacao,
            idViagem,
            iddaavaliacao,
            data;
    Double  notaAvaliacao;

    public Avaliacao() {}

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

    public String getIdAvaliado() {
        return idAvaliado;
    }

    public void setIdAvaliado(String idAvaliado) {
        this.idAvaliado = idAvaliado;
    }

    public String getIdAvaliador() {
        return idAvaliador;
    }

    public void setIdAvaliador(String idAvaliador) {
        this.idAvaliador = idAvaliador;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public void setTipoAvaliacao(String tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }

    public String getIddaavaliacao() {
        return iddaavaliacao;
    }

    public void setIddaavaliacao(String iddaavaliacao) {
        this.iddaavaliacao = iddaavaliacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(Double notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }

    protected Avaliacao(Parcel in) {
        idAvaliado = in.readString();
        idAvaliador = in.readString();
        observacao = in.readString();
        tipoAvaliacao = in.readString();
        idViagem = in.readString();
        iddaavaliacao = in.readString();
        data = in.readString();
        if (in.readByte() == 0) {
            notaAvaliacao = null;
        } else {
            notaAvaliacao = in.readDouble();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAvaliado);
        dest.writeString(idAvaliador);
        dest.writeString(observacao);
        dest.writeString(tipoAvaliacao);
        dest.writeString(idViagem);
        dest.writeString(iddaavaliacao);
        dest.writeString(data);
        if (notaAvaliacao == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(notaAvaliacao);
        }
    }
}
