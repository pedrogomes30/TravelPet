package com.example.travelpet.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

// Esse sistema de permissão surgiu aparti da versão marshmallow que e API 23
public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if( Build.VERSION.SDK_INT >= 23 ){

            List<String> listaPermissoes = new ArrayList<>();

            for (String permissao : permissoes){

                Boolean temPermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;

                if(!temPermissao) {
                    listaPermissoes.add(permissao);
                }
            }

            if( listaPermissoes.isEmpty()) {
                return true;
            }

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray( novasPermissoes );

            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode);

        }

        return true;
    }
    public static void alertaValidacaoPermissao(final Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder( activity);
        builder.setTitle("Permissão negada");
        builder.setMessage("Para utilizar o app é necessário aceitar todas as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.show();

    }
}