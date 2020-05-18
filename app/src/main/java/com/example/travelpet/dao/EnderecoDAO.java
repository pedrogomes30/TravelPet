package com.example.travelpet.dao;

import androidx.annotation.NonNull;

import com.example.travelpet.domain.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnderecoDAO {
    //private Endereco endereco ;

    public EnderecoDAO() {}

    //      Metodo para salvar os dados do endereço do usuario no database do firebase
    public void salvarEnderecoRealtimeDatabase(Endereco endereco, String tipoUsuario){

        // Transformando a primeira letra do "tipoUsuario" em maiuscula
        // para usar no metodo de salvarEnderecoDatabase
        tipoUsuario = tipoUsuario.substring(0,1).toUpperCase()+tipoUsuario.substring(1);

        DatabaseReference enderecoRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("enderecos"+tipoUsuario)
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        enderecoRef.setValue(endereco).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
