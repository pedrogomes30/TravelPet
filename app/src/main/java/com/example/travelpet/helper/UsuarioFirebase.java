package com.example.travelpet.helper;

import android.net.Uri;

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

}


