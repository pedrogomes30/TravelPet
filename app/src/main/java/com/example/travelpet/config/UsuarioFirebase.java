package com.example.travelpet.config;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.travelpet.activity.cadastro.cadastroDadosMotorista.CadastroTermoMotoristaActivity;
import com.example.travelpet.activity.cadastro.cadastroUsuario.CadastroNomeUsuarioActivity;
import com.example.travelpet.activity.teste.TesteMotoristaActivity;
import com.example.travelpet.activity.teste.TestePassageiroActivity;
import com.example.travelpet.classes.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UsuarioFirebase {

    // Métedo recupera o usuário atual e retorna onjeto inteiro
    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // retorna objeto inteiro
        return usuario.getCurrentUser();

    }
    // Método recupera o id do usuário atual
    public static  String getIdentificadorUsuario(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser().getUid();
    }
    /*    Teste redirecionar usuario na hora que loga para uma determinada Activity
    //        Ainda precisa ser trabalhado

    public static void redirecionaUsuarioLogado(final Activity activity) {

        DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios")
                .child(getIdentificadorUsuario());
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                String tipoUsuario = usuario.getTipoUsuario();
                //String idUsuario = usuario.getId();


                if(tipoUsuario.equals("MOTORISTA")){
                    activity.startActivity(new Intent(activity, TesteMotoristaActivity.class));
                }if(tipoUsuario.equals("PASSAGEIRO")){
                    activity.startActivity(new Intent(activity, TestePassageiroActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

     */

}


