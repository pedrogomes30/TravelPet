package com.example.travelpet.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.R;
import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;

public class Mensagem {

    public Mensagem() {}

    // Usado na hora de cadastrar DonoAnimal, e também cadastrar um novo Animal
    public static void mensagemCadastrarDados(Activity activity){

        //contexto = contexto.getApplicationContext();
        Toast.makeText(activity,
                "Cadastro realizado com sucesso",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, PerfilPassageiroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void mensagemCadastrarMotorista(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Cadastro realizado com sucesso");
        builder.setMessage("Agora iremos avaliar seus dados, a análise pode levar até 7 dias útéis");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void mensagemAtualizarDonoAnimal(Activity activity){

        Toast.makeText(activity,
                "Atualização realizada com Sucesso",
                Toast.LENGTH_SHORT).show();
    }

    public static void mensagemAtualizarAnimal(Activity activity){

        Toast.makeText(activity,
                "Atualização realizada com Sucesso",
                Toast.LENGTH_SHORT).show();
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }

    public static void mensagemImpedirExcluirAnimal(Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Você Possui 1 animal");
        builder.setIcon(R.drawable.ic_atencao_laranja_24dp);
        builder.setMessage("Não e possível excluir com apenas 1 animal cadastrado");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void mensagemDeslogarUsuario(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Saindo...");
        builder.setMessage("Tem certeza que deseja sair desta conta ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Login.deslogarUsuario(activity);
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builder.show();
    }

}
