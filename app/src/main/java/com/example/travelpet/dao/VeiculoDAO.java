package com.example.travelpet.dao;

import android.net.Uri;

import com.example.travelpet.helper.Base64Custom;
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
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;

public class VeiculoDAO
{

    private Veiculo veiculo;
    int tipo,quantidade;
    String resultado,fotoURL;
    private ArrayList<Veiculo> veiculos = new ArrayList<>();

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

    public void salvarVeiculo (Veiculo veic)
    {
        String url;
        this.veiculo = veic;
        veiculo.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        systemOUT("idUsuario salvarVeiculo() =" + veiculo.getIdUsuario());
        veiculo.setIdVeiculo(gerarPushKeyIdVeiculo());
        systemOUT("idVeiculo salvarVeiculo() =" + veiculo.getIdVeiculo());
        salvaImagemCRVL(veiculo.getFotoCrvl());

    }

    public String salvaImagemCRVL (byte[] foto )
    {
        StorageReference reference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("veiculos")
                .child(veiculo.getIdUsuario())
                .child(veiculo.getIdVeiculo())
                .child(veiculo.getIdVeiculo() + ".FOTO.CRVL.JPEG");

        systemOUT("idUsuario salvaImagemCRVL ="+veiculo.getIdUsuario());
        systemOUT("idVeiculo salvaImagemCRVL ="+veiculo.getIdVeiculo());

        UploadTask uploadTask = reference.putBytes(foto);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot)
            {
                Task<Uri> uri = snapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                fotoURL= uri.getResult().toString();
                veiculo.setFotoCRVLurl(fotoURL);
                salvarVeiculoRealtimeDatabase(veiculo);
                System.out.println("uri da foto CRVL: " + fotoURL);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                fotoURL = "vai que vai";
            }
        });

        return fotoURL;
    }

    public ArrayList<Veiculo> receberVeiculos (final CountDownLatch latch)
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        referencia.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dados : dataSnapshot.getChildren())
                {
                    veiculo = dados.getValue(Veiculo.class);
                    veiculos.add(veiculo);
                }
                latch.countDown();
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    latch.countDown();
                }
        });

        return veiculos;
    }

    public ArrayList<Veiculo> receberVeiculosLiberados (final CountDownLatch latch)
    {
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));

        referencia.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dados : dataSnapshot.getChildren())
                {
                    veiculo = dados.getValue(Veiculo.class);
                    if (veiculo.getStatus().equals(Veiculo.LIBERADO))
                    {
                        veiculos.add(veiculo);
                    }
                }
                latch.countDown();
                System.out.println("lista veiculos apos o for =" + veiculos.size()+" ///////////////////////////////////////");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                latch.countDown();
            }
        });

        return veiculos;
    }

    public int contarVeiculos ()
    {

        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(veiculo.getIdUsuario());
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

    public Veiculo getVeiculo (String idVeiculo, String idMotorista, final CountDownLatch contador)
    {

        DatabaseReference veiculoRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("veiculos").child(idMotorista).child(idVeiculo);
        veiculoRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                veiculo = snapshot.getValue(Veiculo.class);
                contador.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                contador.countDown();
            }
        });

        try {contador.await();}
        catch (InterruptedException e)
        {e.printStackTrace();}

        return veiculo;
    }

    public String excluirVeiculo(final Veiculo veiculo)
    {

        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(veiculo.getIdUsuario());
        referencia.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getChildrenCount() > 1)
                {
                    excluirVeiculoRealTime(veiculo);
                    excluirVeiculoStorage(veiculo);
                    resultado = "Veículo = "+veiculo.getIdVeiculo()+" excluido com sucesso";

                }
                else
                {
                    resultado = "Você só possui 1 Veículo ";

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                resultado = "teste";

            }
        });


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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Imagem não encontrada");
            }
        });
        return resultado;

    }

    public String excluirVeiculoRealTime (final Veiculo veiculo)
    {

        ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("veiculos")
                .child(veiculo.getIdUsuario())
                .child(veiculo.getIdVeiculo())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        resultado = "veiculo " + veiculo.getIdVeiculo() + " Excluido com Sucesso!!!";
                    }
                }
                ).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        resultado =e.getMessage().toString();
                    }
                });

        return  resultado;
    }

    public void systemOUT (String mensagem )
    {
        System.out.println(mensagem);
    }
}
