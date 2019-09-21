package com.example.travelpet.activity.cadastro.cadastroDadosMotorista;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelpet.R;
import com.example.travelpet.classes.Motorista;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroFotoMotoristaActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroCnhMotorista
    String idUsuario, emailUsuario, nomeUsuario,
           sobrenomeUsuario, telefoneUsuario, tipoUsuario;

    // Variavel armazena a foto da carteira de motorista
    byte[] fotoCNH;

    // Variavel armazena a foto da perfil do motorista
    byte[] fotoMotorista;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_foto_motorista);

        // Recuperando dados passados da Activity CadastroCnhMototorista
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        Motorista motorista = intent.getParcelableExtra("motorista");

        //      Armazena os dados recuperados em uma um variável String
        // Dados classe Usuario
        idUsuario           =   usuario.getId();
        emailUsuario        =   usuario.getEmail();
        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        // Dados classe Motorista
        fotoCNH             =   motorista.getFotoCNH();

    }

    // Evento executado pelo botão enviar
    public void enviarFotoMotorista (View view) {

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
                    fotoMotorista = baos.toByteArray();

                    Toast.makeText(CadastroFotoMotoristaActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    // Evento onClick ( Botão buttonProximoCNH )
    public void abrirTelaFotoCrlv(View view){

        if(fotoMotorista != null) {

            Usuario usuario = new Usuario();

            usuario.setId(idUsuario);
            usuario.setEmail(emailUsuario);
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            Motorista motorista = new Motorista();

            motorista.setFotoCNH(fotoCNH);
            motorista.setFotoPerfilMotorista(fotoMotorista);


            Intent intent = new Intent(getApplicationContext(), CadastroCrlvMotoristaActivity.class);
            intent.putExtra("usuario", usuario);
            intent.putExtra("motorista", motorista);
            startActivity(intent);



        }else{
            Toast.makeText(CadastroFotoMotoristaActivity.this,
                    "Envie a foto de Perfil",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
