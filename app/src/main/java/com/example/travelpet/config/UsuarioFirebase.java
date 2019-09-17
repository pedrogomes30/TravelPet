package com.example.travelpet.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFirebase {

    // Métedo recupera o usuário atual e retorna onjeto inteiro
    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // retorna objeto inteiro
        return usuario.getCurrentUser();

    }
    // Método recupera o id do usuário atual
    public static  String getIdentificadorUsuario(){

        return getUsuarioAtual().getUid();
    }
    /*      Teste redirecionar usuario na hora que loga para uma determinada Activity
            Ainda precisa ser trabalhado

    public static void redirecionaUsuarioLogado(Activity activity){
        DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios")
                .child(getIdentificadorUsuario());
        String idAtual = usuariosRef.toString();
        String id = getIdentificadorUsuario();

        if(idAtual.equals(id)){
            activity.startActivity(new Intent (activity, TesteCheioActivity.class));
        }else{
            activity.startActivity(new Intent (activity, CadastroTipoUsuarioActivity.class));
        }
     )*/

}


