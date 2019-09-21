package com.example.travelpet.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario implements Parcelable {

    String id;
    String nome;
    String sobrenome;
    String telefone;
    String tipoUsuario;
    String email;

    // Construtor
    public Usuario() {
    }

    // Métodos Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    // Método para salvar os dados do usuário no firebase
    public void salvar(){
        // DatabaseReference = Referência do Firebase
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        // Referência DatabaseRefence para usuário
        // firebaseRef.child("usuarios") = indica o nó filho chamado usuarios
        // child(getId()) = recupera o id do nó  usuarios
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(getId());

        // Configurando usuário no Firebase
        // this = pois salvara todos os dados (id,nome,email,telefone,tipo)
        // excessão vai ser a senha pois já temos ela salva, e não e interessante
        // que se tenha uma senha que fique sendo visualizada sempre
        usuarios.setValue(this);

    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Usuario(Parcel in) {
        id = in.readString();
        nome = in.readString();
        sobrenome = in.readString();
        telefone = in.readString();
        tipoUsuario = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(telefone);
        dest.writeString(tipoUsuario);
        dest.writeString(email);
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
