package com.example.travelpet.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Exclude;

public class Animal implements Parcelable {

    private String idUsuario;
    private String nomeAnimal;
    private String especieAnimal;
    private String racaAnimal;
    private String porteAnimal;

    // Construtor
    public Animal() {
    }


    //      Métodos getter and setter

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getEspecieAnimal() {
        return especieAnimal;
    }

    public void setEspecieAnimal(String especieAnimal) {
        this.especieAnimal = especieAnimal;
    }

    public String getRacaAnimal() {
        return racaAnimal;
    }

    public void setRacaAnimal(String racaAnimal) {
        this.racaAnimal = racaAnimal;
    }

    public String getPorteAnimal() {
        return porteAnimal;
    }

    public void setPorteAnimal(String porteAnimal) {
        this.porteAnimal = porteAnimal;
    }

    //      Método para salvar os dados do animal no firebase
    public void salvarAnimal(){
        // DatabaseReference = Referência do Firebase
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        // Referência DatabaseRefence para animais
        // firebaseRef.child("animais") = indica o nó filho chamado animais
        // .push(); = pega o id criado pelo nó
        DatabaseReference animais = firebaseRef.child("animais").child(getIdUsuario()).push();

        // Configurando usuário no Firebase
        // this = pois salvara todos os dados de uma vez (idUsuario, idAnimal, nomeAnimal...)
        animais.setValue(this);

    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Animal(Parcel in) {
        idUsuario = in.readString();
        nomeAnimal = in.readString();
        especieAnimal = in.readString();
        racaAnimal = in.readString();
        porteAnimal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUsuario);
        dest.writeString(nomeAnimal);
        dest.writeString(especieAnimal);
        dest.writeString(racaAnimal);
        dest.writeString(porteAnimal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

}
