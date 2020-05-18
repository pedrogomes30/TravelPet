package com.example.travelpet.dao;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Usuario;
import com.example.travelpet.model.Veiculo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class VeiculoDAO
{

    private Veiculo veiculo;
    int tipo,quantidade;
    String resultado,fotoURL;
    ArrayList<Veiculo> veiculos;

    public VeiculoDAO (){}

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
    public String salvarVeiculoRealtimeDatabase (Veiculo veiculo)
    {

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
                if(tipo == 2){resultado = "Erro ao atualizar dados do veiculo";}
            }
        });

        return resultado;
    }

    public String salvarVeiculo (Veiculo veic)
    {
        this.veiculo = veic;
        veiculo.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        veiculo.setIdVeiculo(gerarPushKeyIdVeiculo());
        veiculo.setFotoCRVLurl(salvaImagemCRVL(veiculo.getFotoCrvl()));
        String foto = salvarVeiculoRealtimeDatabase(veiculo);

        return foto;
    }

    public String salvaImagemCRVL (byte[] foto )
    {
        StorageReference reference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("veiculos")
                .child(veiculo.getIdUsuario())
                .child(veiculo.getIdVeiculo())
                .child(veiculo.getIdVeiculo() + ".FOTO.CRVL.JPEG");

        UploadTask uploadTask = reference.putBytes(foto);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot)
            {
                Task<Uri> uri = snapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                fotoURL = "vai que vai";
            }
        });
        fotoURL= reference.getDownloadUrl().toString();
        System.out.println(fotoURL);
        return fotoURL;
    }

    public ArrayList<Veiculo> receberVeiculos ()
    {
        veiculos = new ArrayList<Veiculo>();
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        referencia.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot dados : dataSnapshot.getChildren())
                {
                    veiculos.add(dados.getValue(Veiculo.class));
                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
        });
        return veiculos;
    }

    public int contarVeiculos ()
    {

        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        referencia.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                quantidade = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        return quantidade;
    }

    public String excluirVeiculo(Veiculo veiculo)
    {
        if (contarVeiculos() <= 1)
        {
            excluirVeiculoStorage(veiculo);
            excluirVeiculoRealTime(veiculo);
        }
        else
        {
            resultado = "Você só possui 1 Veículo ";
        }

        return resultado;
    }

    public String excluirVeiculoStorage (Veiculo veiculo)
    {

        StorageReference veiculoStorage = ConfiguracaoFirebase.getFirebaseStorage()
                .child("veiculos")
                .child(veiculo.getIdUsuario())
                .child(veiculo.getIdVeiculo())
                .child(veiculo.getIdVeiculo()+".FOTO.CRVL.JPEG");

        veiculoStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                resultado = "CRVL excluido Com Sucesso !!!!!!!!";
            }
        });
        return resultado;
    }

    public String excluirVeiculoRealTime (final Veiculo veiculo)
    {
        ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(UsuarioFirebase.getIdentificadorUsuario())
                .child(veiculo.getIdVeiculo())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        resultado = "veiculo " + veiculo.getIdVeiculo() + " Excluido com Sucesso!!!";
                    }
                });
        return  resultado;
    }

}
