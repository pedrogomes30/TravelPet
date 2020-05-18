package com.example.travelpet.model;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonoAnimal extends Usuario implements Parcelable {

    private String fluxoDados;

    public DonoAnimal() {}

    @Exclude // Com isso não será salvo o fluxo dados no banco de dados
    public String getFluxoDados() {
        return fluxoDados;
    }

    public void setFluxoDados(String fluxoDados) {
        this.fluxoDados = fluxoDados;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    public DonoAnimal(Parcel in) {

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

        // Dados Classe DonoAnimal
        fluxoDados = in.readString();

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

        // Dados Classe DonoAnimal
        dest.writeString(fluxoDados);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DonoAnimal> CREATOR = new Creator<DonoAnimal>() {
        @Override
        public DonoAnimal createFromParcel(Parcel in) {
            return new DonoAnimal(in);
        }

        @Override
        public DonoAnimal[] newArray(int size) {
            return new DonoAnimal[size];
        }
    };

}
