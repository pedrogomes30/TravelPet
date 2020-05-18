package com.example.travelpet.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Motorista extends Usuario implements Parcelable {

    private byte[] fotoCNH;
    private String registroCnh;
    private String statusCadastro;

    private String fotoCnhUrl;
    private String fotoCrvlUrl;

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

    public String getStatusCadastro() {
        return statusCadastro;
    }

    public void setStatusCadastro(String statusCadastro) {
        this.statusCadastro = statusCadastro;
    }

    public String getFotoCnhUrl() {
        return fotoCnhUrl;
    }

    public void setFotoCnhUrl(String fotoCnhUrl) {
        this.fotoCnhUrl = fotoCnhUrl;
    }

    public String getFotoCrvlUrl() {
        return fotoCrvlUrl;
    }

    public void setFotoCrvlUrl(String fotoCrvlUrl) {
        this.fotoCrvlUrl = fotoCrvlUrl;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Motorista(Parcel in) {

        // Dados Classe Usuario
        idUsuario     = in.readString();
        tipoUsuario   = in.readString();
        nome          = in.readString();
        sobrenome     = in.readString();
        telefone      = in.readString();
        cpf           = in.readString();
        email         = in.readString();
        fotoPerfil    = in.createByteArray();
        fotoPerfilUrl = in.readString();

        // Dados Classe Motorista
        fotoCNH = in.createByteArray();
        registroCnh = in.readString();
        statusCadastro = in.readString();
        fotoCnhUrl = in.readString();
        fotoCrvlUrl = in.readString();
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

        // Dados Classe Motorista
        dest.writeByteArray(fotoCNH);
        dest.writeString(registroCnh);
        dest.writeString(statusCadastro);
        dest.writeString(fotoCnhUrl);
        dest.writeString(fotoCnhUrl);
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
