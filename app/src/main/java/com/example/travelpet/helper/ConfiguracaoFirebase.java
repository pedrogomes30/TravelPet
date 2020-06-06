package com.example.travelpet.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    // Referenciando Banco de dados
    // database = é a referência
    private static FirebaseDatabase fireDB;
    private static DatabaseReference database;

    // Autenticação do Firebase
    private static FirebaseAuth auth;

    // Objeto de Autenticação do Storage Firebase
    private static StorageReference storage;


    // Retorna a Referência do FirebaseDatabase feita no curso
    public static DatabaseReference getFirebaseDatabaseReferencia(){
        // se for vazio, entra ponto inicial
        if( database == null){
            // .getReference Retorna a referencia do DatabaseReference
            // .getInstance() = Recupera a instância do Firebase utilizada para salvar dados
            // .getReference() = Volta para o nó raiz do Firebase
            // .getReference("Motorista") = nesse caso iria colocar dados aparti do nó motorista
            database = FirebaseDatabase.getInstance().getReference();
        }
        // se já tiver esse objeto n precisa configurar novamente
        return database;
    }

    // Retorna a instância do FirebaseDatabase
    public static FirebaseDatabase getFirebaseDatabase(){

        if (fireDB == null){

            fireDB = FirebaseDatabase.getInstance();
        }

        return fireDB;
    }

    // retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        // verifica se o auth já esta configurado
        if(auth == null){
            // recupera a instancia do FirebaseAuth
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
    // Método para salvar a imagem no Storage do FireBase
    public static StorageReference getFirebaseStorage(){
        // verifica se o storage já esta configurado
        if(storage == null){
            // recupera a referencia
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}


