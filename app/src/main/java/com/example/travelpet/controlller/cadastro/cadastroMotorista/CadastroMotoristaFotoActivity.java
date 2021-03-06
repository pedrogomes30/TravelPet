package com.example.travelpet.controlller.cadastro.cadastroMotorista;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.RecuperaFoto;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaFotoActivity extends AppCompatActivity {

    private Motorista motorista;
    private Endereco endereco;

    private TextView campoNomeFotoMotorista;
    private byte[] fotoPerfil;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_foto);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); //CadastroMotoristaCnh

    }

    public void botaoEnviarFotoPerfil (View view) {

        RecuperaFoto.getGaleria(this, SELECAO_GALERIA);
    }

    public void botaoProximo(View view){

        if(validarDados()) {

            motorista.setFotoPerfil(fotoPerfil);

            Intent intent = new Intent(this, CadastroMotoristaVeiculoActivity.class);
            intent.putExtra("motorista", motorista);
            intent.putExtra("endereco",endereco);
            startActivity(intent);
        }
    }

    public void iniciarComponentes(){
        campoNomeFotoMotorista = findViewById(R.id.textViewNomeFotoMotorista);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
    }

    public Boolean validarDados () {

        Boolean validado = false;

        if(fotoPerfil != null) {
            validado = true;
        }else{
            Mensagem.toastIt("Envie a foto de Perfil", this);
        }
        return validado;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK){

            try {

                Uri localImagemSelecionada = data.getData();

                // Recuperando nome da imagem selecionada
                Cursor returnCursor = getContentResolver().query(localImagemSelecionada, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();

                // getContentResolver() = responsável por recupera conteúdo dentro do app android
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if ( imagem != null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.WEBP, 50, baos);
                    fotoPerfil = baos.toByteArray();
                    campoNomeFotoMotorista.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaFotoActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
