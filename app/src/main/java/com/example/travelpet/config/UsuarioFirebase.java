package com.example.travelpet.config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelpet.classes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {



    // Métedo recupera o usuário atual com tds os dados
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
    public static  String getEmailUsuario(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser().getEmail();
    }
    // salvando a foto no usuário do firebase
    public static  boolean atualizarFotoUsuario(Uri url){

        try {

            FirebaseUser user = getUsuarioAtual();
            // configurando usuário atual
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            // updateProfile() = Atualiza perfil do usuário ele requer que tenha
            // um UserProfileChangeRequest = traduzindo = Pedido de mudança do perfil do usuário
            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Verifica se não tiver obtido sucesso
                    if(!task.isSuccessful()){
                        // Log.d = para vizualizar caso de algum erro, log com "d" de Debug
                        Log.d("Perfil","Erro ao atualizar foto de perfil.");
                    }
                }
            });
            // por padrão "return true"
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /*
       Teste redirecionar usuario na hora que loga para uma determinada Activity
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


