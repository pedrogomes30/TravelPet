package com.example.travelpet.activity.cadastro.cadastroMotorista;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.activity.MainActivity;
import com.example.travelpet.classes.Motorista;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCrlvActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroFotoUsuario
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario, tipoUsuario;

    // Variavel armazena a foto da carteira de motorista
    private byte[] fotoCNH;

    // Variavel armazena a foto da perfil do motorista
    private byte[] fotoMotorista;

    // Variavel armazena a foto do documento do veículo
    private byte[] fotoCrlv;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;

    // Variável armazena a referência do Sotorage
    private StorageReference storageReference;

    private String statusCadastroMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_crlv);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroFotoUsuario
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Motorista motorista = intent.getParcelableExtra("motorista");

        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        fotoCNH             =   motorista.getFotoCNH();
        fotoMotorista       =   motorista.getFotoPerfilMotorista();
        statusCadastroMotorista = "Em análise";

        // Recuperando Referência do Storage do Firebase
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
    }
    // Evento executado pelo botão enviar
    public void enviarCrlvMotorista (View view) {

        // Seleciona a foto da galeria
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK){

            try {

                // Recupera local da imagem selecionada
                Uri localImagemSelecionada = data.getData();

                // getContentResolver() = responsável por recupera conteúdo dentro do app android
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if ( imagem != null){

                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    // Tranforma a imagem em um array de Bytes e armazena na variável
                    fotoCrlv = baos.toByteArray();

                    Toast.makeText(CadastroMotoristaCrlvActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    // Evento Botão buttonFinalizarCadastroMotorista
    public void buttonFinalizarCadastroMotorista(View view){

        if ( fotoCrlv != null ) {

            Motorista motorista = new Motorista();

            motorista.setId(UsuarioFirebase.getIdentificadorUsuario());
            motorista.setEmail(UsuarioFirebase.getEmailUsuario());
            motorista.setNome(nomeUsuario);
            motorista.setSobrenome(sobrenomeUsuario);
            motorista.setTelefone(telefoneUsuario);
            motorista.setTipoUsuario(tipoUsuario);
            motorista.setStatusCadastro(statusCadastroMotorista);

            // Chama método salvar da classe Usuário, responsável por salvar no database do firebase
            motorista.salvar();

             // Chama método para salvar as fotos no storage do firebase
            salvarFotoCNH();
            salvarFotoPerfilMotorista();
            salvarFotoCrlv();

            AlertDialog.Builder builder = new AlertDialog.Builder( CadastroMotoristaCrlvActivity.this);
            builder.setTitle("Cadastro realizado com sucesso");
            builder.setMessage("Seu cadastro foi realizado com sucesso, agora iremos avaliar seus dados"+
                               " a análise pode levar até 7 dias útéis");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(CadastroMotoristaCrlvActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


            Toast.makeText(CadastroMotoristaCrlvActivity.this,
                    "Sucesso ao cadastrar Usuário Motorista!",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CadastroMotoristaCrlvActivity.this,
                    "Envie a foto do CRVL",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //          Métodos para salvamento no FireBase, de cada imagem passadas das Activitys

    // Método de salvamento FotoCNG ( Activity EnviarCNH )
    public void salvarFotoCNH(){

        // Salvar Imagem no FireBase cada child e uma pasta criada, no ultimo e nome da imagem
        StorageReference imagemRef = storageReference
                .child("motorista")
                .child(UsuarioFirebase.getEmailUsuario())
                .child("documentos de validacao")
                .child(UsuarioFirebase.getEmailUsuario()+".CNH.JPEG");


        // Método para realmente salvar no Firebase
        // putBytes = para os dados da imagem em bytes
        UploadTask uploadTask = imagemRef.putBytes (fotoCNH);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CadastroMotoristaCrlvActivity.this,
                        "Erro ao fazer upload da imagem CNH",
                        Toast.LENGTH_SHORT).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    // Método de salvamento FotoPerfilMotorista
    public void salvarFotoPerfilMotorista(){

        StorageReference imagemRef = storageReference
                .child("motorista")
                .child(UsuarioFirebase.getEmailUsuario())
                .child("documentos de validacao")
                .child(UsuarioFirebase.getEmailUsuario()+".Perfil.JPEG");

        UploadTask uploadTask = imagemRef.putBytes (fotoMotorista);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CadastroMotoristaCrlvActivity.this,
                        "Erro ao fazer upload da imagem Foto de Pefil",
                        Toast.LENGTH_SHORT).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    // Método de salvemento FotoDccumentoVeiculo
    public void salvarFotoCrlv(){

        StorageReference imagemRef = storageReference
                .child("motorista")
                .child(UsuarioFirebase.getEmailUsuario())
                .child("documentos de validacao")
                .child(UsuarioFirebase.getEmailUsuario()+".CRVL.JPEG");

        UploadTask uploadTask = imagemRef.putBytes (fotoCrlv);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CadastroMotoristaCrlvActivity.this,
                        "Erro ao fazer upload da imagem CRLV",
                        Toast.LENGTH_SHORT).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
