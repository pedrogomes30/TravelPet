package com.example.travelpet.classes;

import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        System.out.println("Veio até aqui");
        FirebaseDatabase firedb = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference firebaseRef = firedb.getReference("animais");
        DatabaseReference animais = firebaseRef.push();//.child(getIdUsuario());
        System.out.println("Passou");

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
