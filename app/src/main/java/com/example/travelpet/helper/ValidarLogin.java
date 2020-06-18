package com.example.travelpet.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.controlller.cadastro.CadastroUsuarioTipoActivity;
import com.example.travelpet.controlller.cadastro.cadastroMotorista.CadastroMotoristaVeiculoActivity;
import com.example.travelpet.controlller.perfil.motorista.PerfilMotoristaActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Motorista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ValidarLogin {

    public static void  logarUsuario(final Activity activity){

        DatabaseReference donoAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child(ConfiguracaoFirebase.donoAnimal)
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    DonoAnimal donoAnimal = dataSnapshot.getValue(DonoAnimal.class);
                    String statusContaDonoAnimal = donoAnimal.getStatusConta();

                    if(statusContaDonoAnimal.equals(ConfiguracaoFirebase.donoAnimalAtivo)){
                        activity.startActivity(new Intent(activity, PerfilPassageiroActivity.class));
                        activity.finish();

                    }else if(statusContaDonoAnimal.equals(ConfiguracaoFirebase.donoAnimalBloqueado)){
                        AlertDialog.Builder builder = new AlertDialog.Builder( activity);
                        builder.setTitle("Conta bloqueada");
                        builder.setMessage("Foi detectado comportamento inadequado, entre em contato conosco para mais informações");
                        builder.setCancelable(false);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        builder.show();
                    }
                }else{
                    DatabaseReference motoristaRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                            .child(ConfiguracaoFirebase.motorista)
                            .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
                    motoristaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()) {
                                Motorista motorista = dataSnapshot.getValue(Motorista.class);
                                String statusContaMotorista = motorista.getStatusConta();

                                if (statusContaMotorista.equals(ConfiguracaoFirebase.motoristaAprovado)){
                                    activity.startActivity(new Intent(activity, PerfilMotoristaActivity.class));
                                    activity.finish();

                                }else if(statusContaMotorista.equals(ConfiguracaoFirebase.motoristaEmAnalise)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder( activity);
                                    builder.setTitle("Em análise...");
                                    builder.setMessage("Estamos avaliando seus dados, prazo máximo de 7 dias após o cadastro");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                                    builder.show();

                                }else if(statusContaMotorista.equals(ConfiguracaoFirebase.motoristaRejeitado)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder( activity);
                                    builder.setTitle("Conta rejeitada");
                                    builder.setMessage("Seus dados não estão de acordo com a exigência da Travel Pet, entre em contato conosco para mais informações");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                                    builder.show();

                                }else if (statusContaMotorista.equals(ConfiguracaoFirebase.motoristaBloqueado)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder( activity);
                                    builder.setTitle("Conta bloqueada");
                                    builder.setMessage("Foi detectado comportamento inadequado, entre em contato conosco para mais informações");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                                    builder.show();
                                }
                            }else{
                                activity.startActivity(new Intent(activity, CadastroUsuarioTipoActivity.class));
                                activity.finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}

