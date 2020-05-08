package com.example.travelpet.dao;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.example.travelpet.model.Veiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;

public class VeiculoDAO {

    private Veiculo veiculo;
    int tipo;
    String resultado;

    public VeiculoDAO (Veiculo veiculo, int tipo )
    {
        this.veiculo = veiculo;
        this.tipo = tipo;
    }

    public static String gerarPushKeyIdVeiculo()
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String chave = referencia.push().getKey();
        return chave;
    }

    //Metodo para salvar o veículo no firebase
    public String salvarVeiculoDatabase () {

        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference veiculosRef = fireDB.getReference().child("veiculos").child(veiculo.getIdUsuario()).child(veiculo.getIdVeiculo());

        veiculosRef.setValue(veiculo).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                //tipo 1 - adicionar veiculo
                //tipo 2 - editar veiculo
                if(tipo == 1){resultado = "Sucesso ao cadastrar veiculo";} else
                if(tipo == 2){resultado = "Atualização feita com Sucesso";}
            }
        }

        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //tipo 1 - adicionar veiculo
                //tipo 2 - editar veiculo
                if(tipo == 1){resultado = "Erro ao salvar dados do veiculo";} else
                if(tipo == 2){resultado = "Erro ao atualizard dados do veiculo";}
            }
        });

        return resultado;
    }

public String salvarVeiculoStorage ()
{



}


public void salvaImagemFirebase (UploadTask.TaskSnapshot snapshot)
{

    Task<Uri> uri = snapshot.getStorage().getDownloadUrl();
    while(!uri.isComplete());
    Uri url = uri.getResult();
    String fotoVeiculoUrl = url.toString();


}



}
