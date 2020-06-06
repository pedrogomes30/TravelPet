package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Usuario implements Parcelable{

    protected String idUsuario;
    protected String tipoUsuario;
    protected String nome;
    protected String sobrenome;
    protected String telefone;
    protected String cpf;
    protected String email;
    protected byte[] fotoPerfil;
    protected String fotoPerfilUrl;

    // Construtor
    public Usuario() {}

    // Métodos Getter and Setter
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Exclude
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Usuario(Parcel in) {

        tipoUsuario   = in.readString();
        /*
        idUsuario     = in.readString();
        tipoUsuario   = in.readString();
        nome          = in.readString();
        sobrenome     = in.readString();
        telefone      = in.readString();
        cpf           = in.readString();
        email         = in.readString();
        fotoPerfil    = in.createByteArray();
        fotoPerfilUrl = in.readString();
         */

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(tipoUsuario);
        /*
        dest.writeString(idUsuario);
        dest.writeString(tipoUsuario);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(telefone);
        dest.writeString(cpf);
        dest.writeString(email);
        dest.writeByteArray(fotoPerfil);
        dest.writeString(fotoPerfilUrl);
         */
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

}
