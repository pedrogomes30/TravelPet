package com.example.travelpet.helper;

import android.app.ProgressDialog;

import com.example.travelpet.R;

public class TelaCarregamento {

    public static void iniciarCarregamento(ProgressDialog progressDialog){

        // Show Dialog
        progressDialog.show();
        // Set Contet View
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        // Set Transparent Background
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

   public static void pararCarregamento(ProgressDialog progressDialog){

       progressDialog.dismiss();
    }

}
