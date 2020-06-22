package com.example.travelpet.helper;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class RecuperaFoto {


    public static void getCamera(Activity activity, Integer SELECAO_CAMERA){

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(i,SELECAO_CAMERA);
        }
    }

    public static void getGaleria(Activity activity,Integer SELECAO_GALERIA ){
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(activity.getPackageManager()) != null){
            activity.startActivityForResult(i, SELECAO_GALERIA);
        }
    }

}
