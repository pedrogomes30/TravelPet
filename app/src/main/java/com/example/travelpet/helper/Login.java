package com.example.travelpet.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.controlller.cadastro.cadastroDonoAnimal.CadastroAnimalEspecieRacaActivity;
import com.example.travelpet.controlller.cadastro.cadastroUsuario.CadastroUsuarioDadosActivity;
import com.example.travelpet.controlller.cadastro.cadastroUsuario.CadastroUsuarioTipoActivity;
import com.example.travelpet.controlller.perfil.motorista.PerfilMotoristaActivity;
import com.example.travelpet.controlller.perfil.motorista.TestePerfilMotoristaActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.model.Motorista;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Login {

    public static void  logarUsuario(final Activity activityAtual){

        DatabaseReference donoAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("donoAnimal")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // dataSnapshot.exists()= verifica se existe o usuário no database
                if(dataSnapshot.exists()) {

                    activityAtual.startActivity(new Intent(activityAtual, PerfilPassageiroActivity.class));
                    activityAtual.finish();

                }else{

                    DatabaseReference motoristaRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                            .child("motorista")
                            .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
                    motoristaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {

                                Motorista motorista = dataSnapshot.getValue(Motorista.class);
                                String tipoUsuario = motorista.getTipoUsuario();
                                String statusCadastroMotorista = motorista.getStatusCadastro();

                                if (tipoUsuario.equals("motorista")) {

                                    if (statusCadastroMotorista.equals("Em análise")){

                                        AlertDialog.Builder builder = new AlertDialog.Builder( activityAtual);
                                        builder.setTitle("Em análise...");
                                        builder.setMessage("Estamos avaliando seus dados, prazo máximo de 7 dias após o cadastro");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    } else if(statusCadastroMotorista.equals("Reprovado")){

                                        AlertDialog.Builder builder = new AlertDialog.Builder( activityAtual);
                                        builder.setTitle("Dados reprovados");
                                        builder.setMessage("Seus dados não estão de acordo com a exigência da Travel Pet");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }else if(statusCadastroMotorista.equals("Aprovado")){

                                        activityAtual.startActivity(new Intent(activityAtual, PerfilMotoristaActivity.class));
                                        activityAtual.finish();
                                    }

                                }
                            }else{
                                activityAtual.startActivity(new Intent(activityAtual, CadastroUsuarioTipoActivity.class));
                                activityAtual.finish();
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