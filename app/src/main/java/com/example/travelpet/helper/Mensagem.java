package com.example.travelpet.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.R;
import com.example.travelpet.controlller.LoginActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais.EditarAnimalActivity;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.Animal;

public class Mensagem {

    public Mensagem() {}

    // Usado na hora de cadastrar DonoAnimal, e também cadastrar um novo Animal
    public static void mensagemCadastrarDados(Activity activity){

        Toast.makeText(activity,
                "Cadastro realizado com sucesso",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, PerfilPassageiroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
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

    public static void mensagemExcluirAnimal(final Activity activity, final Animal animal){
        final AnimalDAO animalDAO = new AnimalDAO();
        animalDAO.contarAnimais();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Excluindo...");
        builder.setIcon(R.drawable.ic_lixeira);
        builder.setMessage("Tem certeza que deseja excluir este animal?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                animalDAO.excluirAnimal(animal, activity);

            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builder.show();
    }

    public static void mensagemImpedirExcluirAnimal(Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Você Possui 1 animal");
        builder.setIcon(R.drawable.ic_atencao_);
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

    public static void mensagemCadastrarMotorista(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Cadastro realizado com sucesso");
        builder.setMessage("Agora iremos avaliar seus dados, a análise pode levar até 7 dias útéis");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Mensagem de validarLogin---------------------------------------------------------------------

    //Usado para conta bloqueada do donoAnimal e no motorista
    public static  void mensagemContaBloqueada(Activity activity){
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

    // Usado para motorista
    public static void mensagemContaEmAnalise(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder( activity);
        builder.setTitle("Em análise...");
        builder.setMessage("Estamos avaliando seus dados, prazo máximo de 7 dias após o cadastro");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    // Usado para motorista
    public static void mensagemContaReprovada(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder( activity);
        builder.setTitle("Conta reprovada");
        builder.setMessage("Seus dados não estão de acordo com a exigência da Travel Pet, entre em contato conosco para mais informações");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();

    }

    //----------------------------------------------------------------------------------------------
    public static void toastIt (String mensagem, Activity activity) {
        Toast.makeText(activity,mensagem,Toast.LENGTH_SHORT).show();
    }


}
