package com.example.travelpet.model;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.controlller.cadastro.cadastroMotorista.CadastroMotoristaCrlvActivity;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Motorista extends Usuario implements Parcelable {

    private byte[] fotoCNH;
    private byte[] fotoPerfil;
    private byte[] fotoCrlv;
    private String statusCadastro;

    private String fotoCnhUrl;
    private String fotoPerfilUrl;
    private String fotoCrvlUrl;
    // Construtor
    public Motorista() {
    }

    public byte[] getFotoCNH() {
        return fotoCNH;
    }

    public void setFotoCNH(byte[] fotoCNH) {
        this.fotoCNH = fotoCNH;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public byte[] getFotoCrlv() {
        return fotoCrlv;
    }

    public void setFotoCrlv(byte[] fotoCrlv) {
        this.fotoCrlv = fotoCrlv;
    }

    public String getStatusCadastro() {
        return statusCadastro;
    }

    public void setStatusCadastro(String statusCadastro) {
        this.statusCadastro = statusCadastro;
    }

    public String getFotoCnhUrl() {
        return fotoCnhUrl;
    }

    public void setFotoCnhUrl(String fotoCnhUrl) {
        this.fotoCnhUrl = fotoCnhUrl;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getFotoCrvlUrl() {
        return fotoCrvlUrl;
    }

    public void setFotoCrvlUrl(String fotoCrvlUrl) {
        this.fotoCrvlUrl = fotoCrvlUrl;
    }

    // Método salva imagens no Storage, depois chama metodo pra salvar dados no storage
    public static void salvarMotoristaStorage(final String nome, final String sobrenome, final String telefone,
                                  final String tipoUsuario, final String statusCadastro, final byte[] fotoCNH,
                                  final byte[] fotoPerfil, final byte[] fotoCrlv, final Activity activityAtual,
                                  final Class<MainActivity> activitySeguinte){


        // Salvando FotoCnh no Storage
        StorageReference imagemRefFotoCnh = ConfiguracaoFirebase.getFirebaseStorage()
                .child("motorista")
                .child(UsuarioFirebase.getEmailUsuario())
                .child(UsuarioFirebase.getEmailUsuario()+".FOTO.CNH.JPEG");
        // Método para realmente salvar no Firebase
        // putBytes = para os dados da imagem em bytes
        UploadTask uploadTaskFotoCnh = imagemRefFotoCnh.putBytes (fotoCNH);
        uploadTaskFotoCnh.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Recuperando o edereço da imagem no Storage
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                // url = pega o caminho da imagem
                Uri url = uri.getResult();
                // converte a url/uri para String
                final String fotoCnhUrl = url.toString();

                // Salvando FotoPerfil no Storage
                StorageReference imagemRefFotoPerfil = ConfiguracaoFirebase.getFirebaseStorage()
                        .child("motorista")
                        .child(UsuarioFirebase.getEmailUsuario())
                        .child(UsuarioFirebase.getEmailUsuario()+".FOTO.PERFIL.JPEG");

                UploadTask uploadTaskFotoPerfil = imagemRefFotoPerfil.putBytes (fotoPerfil);
                uploadTaskFotoPerfil.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        final String fotoPerfilUrl = url.toString();

                        // Salvando FotoCrvl no Storage
                        StorageReference imagemRefFotoCrvl= ConfiguracaoFirebase.getFirebaseStorage()
                                .child("motorista")
                                .child(UsuarioFirebase.getEmailUsuario())
                                .child(UsuarioFirebase.getEmailUsuario()+".FOTO.CRVL.JPEG");

                        UploadTask uploadTaskFotoCrvl= imagemRefFotoCrvl.putBytes (fotoCrlv);
                        uploadTaskFotoCrvl.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                Uri url = uri.getResult();

                                String fotoCrvlUrl = url.toString();

                                // Envia os dados para a classe
                                Motorista motorista = new Motorista();
                                motorista.setId(UsuarioFirebase.getIdentificadorUsuario());
                                motorista.setEmail(UsuarioFirebase.getEmailUsuario());
                                motorista.setNome(nome);
                                motorista.setSobrenome(sobrenome);
                                motorista.setTelefone(telefone);
                                motorista.setTipoUsuario(tipoUsuario);
                                motorista.setStatusCadastro(statusCadastro);

                                motorista.setFotoCnhUrl(fotoCnhUrl);
                                motorista.setFotoPerfilUrl(fotoPerfilUrl);
                                motorista.setFotoCrvlUrl(fotoCrvlUrl);

                                // Emite um local de salvamento so para passar como parametro no metodo salvar
                                String localSalvamento = "CadastroMotoristaCrlvActivity";

                                // Chama método para salvar os dados no Database
                                motorista.salvarUsuarioDatabase(activityAtual, localSalvamento);

                                // Depois que salva todos os dados chama essa caixa de dialogo
                                AlertDialog.Builder builder = new AlertDialog.Builder(activityAtual);

                                builder.setTitle("Cadastro realizado com sucesso");
                                builder.setMessage("Seu cadastro foi realizado com sucesso, agora iremos avaliar seus dados" +
                                        " a análise pode levar até 7 dias útéis");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(activityAtual, activitySeguinte);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        activityAtual.startActivity(intent);
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }// Falha fotoCrvl
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activityAtual,
                                        "Erro ao salvar foto da CRVL",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } // Falha fotoPerfil
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activityAtual,
                                "Erro ao salvar foto de perfil",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } // Falha fotoCnh
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activityAtual,
                        "Erro ao salvar foto da CNH",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Motorista(Parcel in) {

        id = in.readString();
        nome = in.readString();
        sobrenome = in.readString();
        telefone = in.readString();
        tipoUsuario = in.readString();
        email = in.readString();
        fotoUsuarioUrl = in.readString();

        //idUsuario = in.readString();
        fotoCNH = in.createByteArray();
        fotoPerfil = in.createByteArray();
        fotoCrlv = in.createByteArray();
        statusCadastro = in.readString();

        fotoCnhUrl = in.readString();
        fotoPerfilUrl = in.readString();
        fotoCrvlUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(idUsuario);
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(telefone);
        dest.writeString(tipoUsuario);
        dest.writeString(email);
        dest.writeString(fotoUsuarioUrl);

        dest.writeByteArray(fotoCNH);
        dest.writeByteArray(fotoPerfil);
        dest.writeByteArray(fotoCrlv);
        dest.writeString(statusCadastro);

        dest.writeString(fotoCnhUrl);
        dest.writeString(fotoPerfilUrl);
        dest.writeString(fotoCnhUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Motorista> CREATOR = new Creator<Motorista>() {
        @Override
        public Motorista createFromParcel(Parcel in) {
            return new Motorista(in);
        }

        @Override
        public Motorista[] newArray(int size) {
            return new Motorista[size];
        }
    };
}
