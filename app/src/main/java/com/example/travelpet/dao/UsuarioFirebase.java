package com.example.travelpet.dao;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFirebase {

    // Métedo recupera o usuário atual com tds os dados
    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // retorna objeto inteiro
        return usuario.getCurrentUser();
    }

    // Método recupera o id do usuário atual
    public static String getIdentificadorUsuario(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser().getUid();
    }

    public static String getEmailUsuario(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser().getEmail();
    }

    public static String getFotoEmailUsuario(){
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri fotoUsuarioEmail = usuario.getPhotoUrl();
        String fotoPerfilUrl;
        if(fotoUsuarioEmail != null){
            fotoPerfilUrl = fotoUsuarioEmail.toString();
        }else{
            fotoPerfilUrl = "";
        }
        return fotoPerfilUrl;
    }

    public static void deslogarUsuario(final Activity activityAtual){

        AuthUI.getInstance()
                .signOut(activityAtual)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                } );

    }

}


