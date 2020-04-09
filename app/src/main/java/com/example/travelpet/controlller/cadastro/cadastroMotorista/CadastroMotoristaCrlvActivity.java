package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCrlvActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroFotoUsuario
    private String tipoUsuario, nome, sobrenome, telefone;

    // Variaveis usadas para armazenar fotos decocumentos do motorista
    private byte[] fotoCNH, fotoPerfil, fotoCrlv;

    private TextView textViewNomeArquivo;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;

    private String statusCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_crlv);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroMotoristaFotoActivity
        Intent intent = getIntent();
        Motorista motorista = intent.getParcelableExtra("motorista");

        tipoUsuario     =   motorista.getTipoUsuario();
        nome            =   motorista.getNome();
        sobrenome       =   motorista.getSobrenome();
        telefone        =   motorista.getTelefone();
        fotoCNH         =   motorista.getFotoCNH();
        fotoPerfil      =   motorista.getFotoPerfil();

        statusCadastro  =   "Em análise";

        textViewNomeArquivo = findViewById(R.id.textViewNomeArquivoMotoristaCrvl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK){

            try {

                // Recupera local da imagem selecionada
                Uri localImagemSelecionada = data.getData();

                // Recuperando nome da imagem selecionada
                Cursor returnCursor = getContentResolver().query(localImagemSelecionada, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();

                // getContentResolver() = responsável por recupera conteúdo dentro do app android
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if ( imagem != null){

                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    // Tranforma a imagem em um array de Bytes e armazena na variável
                    fotoCrlv = baos.toByteArray();

                    // Envia o nome da imagem para o XML
                    textViewNomeArquivo.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaCrlvActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    public void botaoEnviarFotoCrvl (View view) {

        // Seleciona a foto da galeria
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    public void botaoFinalizarCadastroMotorista(View view) {

        if (fotoCrlv != null) {
            // Método salva imagens no Storage, e depois salva os dados no Database
            Motorista.salvarMotoristaStorage(nome, sobrenome, telefone, tipoUsuario, statusCadastro, fotoCNH,
                    fotoPerfil, fotoCrlv, CadastroMotoristaCrlvActivity.this,MainActivity.class);

        } else {
            Toast.makeText(CadastroMotoristaCrlvActivity.this,
                    "Envie a foto do CRVL",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
