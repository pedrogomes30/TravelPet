package com.example.travelpet.classes;

import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Animal {

    private String idUsuario;
    //private String idAnimal;
    private String nomeAnimal;
    private String tipoEspecieAnimal;
    private String racaAnimal;
    private String porte;

    public Animal() {
    }

    // Método para salvar os dados do usuário no firebase
    public void salvarAnimal(){
        // DatabaseReference = Referência do Firebase
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        // Referência DatabaseRefence para usuário
        // firebaseRef.child("usuarios") = indica o nó filho chamado usuarios
        // child(getId()) = recupera o id do nó  usuarios
        // .push(); = pega o id criado pelo nó
        DatabaseReference animais = firebaseRef.child("animais").push();//.child(getIdUsuario());

        // Configurando usuário no Firebase
        // this = pois salvara todos os dados (idUsuario, idAnimal, nomeAnimal...)
        //animais.push().setValue(animais);
        animais.setValue(this);

    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    /*@Exclude
    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

     */

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getTipoEspecieAnimal() {
        return tipoEspecieAnimal;
    }

    public void setTipoEspecieAnimal(String tipoEspecieAnimal) {
        this.tipoEspecieAnimal = tipoEspecieAnimal;
    }

    public String getRacaAnimal() {
        return racaAnimal;
    }

    public void setRacaAnimal(String racaAnimal) {
        this.racaAnimal = racaAnimal;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }
}
