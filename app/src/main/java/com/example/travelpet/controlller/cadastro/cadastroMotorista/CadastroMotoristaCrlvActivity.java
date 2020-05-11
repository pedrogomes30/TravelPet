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
import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Veiculo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCrlvActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroFotoUsuario


    // Variaveis usadas para armazenar fotos decocumentos do motorista
    private byte[] fotoCNH, fotoPerfil, fotoCrlv;

    private TextView textViewNomeArquivo;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;

    private String statusCadastro;

    private Motorista motorista;
    private Veiculo veiculo;
    private VeiculoDAO veiculoDAO;
    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_crlv);
        veiculoDAO =  new VeiculoDAO();

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroMotoristaFotoActivity
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
        veiculo = intent.getParcelableExtra("veiculo");

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
                   motorista.setFotoCrlv(baos.toByteArray());
                    veiculo.setFotoCrvl(baos.toByteArray());

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

        if (motorista.getFotoCrlv() != null) {
            // tipoUsuarioNomeNo = atrobuto para auxiliar no nome do database

            endereco.salvarEnderecoDatabase(CadastroMotoristaCrlvActivity.this, motorista.getTipoUsuario());

            // Método salva imagens no Storage, e depois salva os dados no Database
            Motorista.salvarMotoristaStorage(motorista, CadastroMotoristaCrlvActivity.this, MainActivity.class);

            veiculoDAO.salvarVeiculo(veiculo);

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
