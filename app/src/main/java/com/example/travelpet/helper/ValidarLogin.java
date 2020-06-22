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
            public void onDataChange(@NonNull DataSnapshot donoAnimal_DS) {

                if(donoAnimal_DS.exists())
                {
                    DonoAnimal donoAnimal = donoAnimal_DS.getValue(DonoAnimal.class);
                    String statusContaDonoAnimal = donoAnimal.getStatusConta();

                    if(statusContaDonoAnimal.equals(ConfiguracaoFirebase.donoAnimalAtivo))
                    {
                        activity.startActivity(new Intent(activity, PerfilPassageiroActivity.class));
                        activity.finish();
                    }
                    else if(statusContaDonoAnimal.equals(ConfiguracaoFirebase.donoAnimalBloqueado))
                    {
                        Mensagem.mensagemContaBloqueada(activity);
                    }

                }
                else
                    {
                    DatabaseReference motoristaRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                            .child(ConfiguracaoFirebase.motorista)
                            .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
                    motoristaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot motorista_DS) {

                            if(motorista_DS.exists()) {
                                Motorista motorista = motorista_DS.getValue(Motorista.class);
                                String statusContaMotorista = motorista.getStatusConta();

                                if (statusContaMotorista.equals(ConfiguracaoFirebase.motoristaAprovado)){
                                    activity.startActivity(new Intent(activity, PerfilMotoristaActivity.class));
                                    activity.finish();

                                }else if(statusContaMotorista.equals(ConfiguracaoFirebase.motoristaEmAnalise)){
                                    Mensagem.mensagemContaEmAnalise(activity);

                                }else if(statusContaMotorista.equals(ConfiguracaoFirebase.motoristaRejeitado)){
                                    Mensagem.mensagemContaReprovada(activity);

                                }else if (statusContaMotorista.equals(ConfiguracaoFirebase.motoristaBloqueado)){
                                    Mensagem.mensagemContaBloqueada(activity);
                                }
                            }else{
                                activity.startActivity(new Intent(activity, CadastroUsuarioTipoActivity.class));
                                activity.finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError motorista_DE) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError donoAnimal_DE) {}
        });
    }

}

