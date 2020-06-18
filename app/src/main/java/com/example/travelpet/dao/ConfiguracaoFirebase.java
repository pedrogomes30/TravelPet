package com.example.travelpet.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    // Referenciando Banco de dados
    private static FirebaseDatabase fireDB;
    private static DatabaseReference database;
    // Autenticação do Firebase
    private static FirebaseAuth auth;
    private static StorageReference storage;

    //Nome de cada nó principal do firebase --------------------------------------------------------
    public static String donoAnimal = "donoAnimal",
                         animal     = "animais",
                         motorista  = "motorista",
                         endereco   = "enderecos",
                         enderecoDA = "enderecosDonoAnimal",
                       //enderecoMO = "enderecosMotorista",
                         tipoAnimal = "racaAnimal",
                         veiculo    = "veiculos";
                       //avaliacao  = "avaliacao",
                       //viagem     = "viagem";

    //outras variaveis chave no firebase -----------------------------------------------------------
    public static String iconeUrl = "iconeUrl"; // Url onde está salvo o icone da espécie

    //Variaveis de status da Conta------------------------------------------------------------------
    public static String donoAnimalAtivo      = "ativo",
                         donoAnimalBloqueado  = "bloqueado",

                         motoristaAprovado    = "Aprovado",
                         motoristaBloqueado   = "Bloqueado",
                         motoristaRejeitado   = "Rejeitado",
                         motoristaEmAnalise   = "Em análise",

                         veiculoAprovado      = "Aprovado",
                         veiculoBloqueado     = "Bloqueado",
                         veiculoReprovado     = "Reprovado";

    // Retorna a referencia do database
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
    // retorna referencia do storage
    public static StorageReference getFirebaseStorage(){
        // verifica se o storage já esta configurado
        if(storage == null){
            // recupera a referencia
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}


