package com.example.travelpet.classes;

import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    private String id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String tipoUsuario;

    public Usuario() {
    }

    // Método para salvar os dados do usuário no firebase
    public void salvar(){

        System.out.println("Veio até aqui");
        FirebaseDatabase firedb = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference firebaseRef = firedb.getReference("usuarios");
        DatabaseReference usuarios = firebaseRef.push();//.child(getIdUsuario());
        System.out.println("Passou");


        // Configurando usuário no Firebase
        // this = pois salvara todos os dados (id,nome,email,telefone,tipo)
        // excessão vai ser a senha pois já temos ela salva, e não e interessante
        // que se tenha uma senha que fique sendo visualizada sempre
        usuarios.setValue(this);

    }

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


}
