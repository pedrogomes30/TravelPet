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
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCnhActivity extends AppCompatActivity {

    // Variaveis usadas para armazenar dados da Activity CadastroTermoMotorista
    private String  nomeUsuario, sobrenomeUsuario, telefoneUsuario, tipoUsuario;

    // Variavel armazena a foto da carteira de motorista CNH
    private byte[] fotoCNH;

    private TextView textViewNomeArquivo;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_cnh);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroTermoMotorista
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");

        nomeUsuario         =   usuario.getNome();
        sobrenomeUsuario    =   usuario.getSobrenome();
        telefoneUsuario     =   usuario.getTelefone();
        tipoUsuario         =   usuario.getTipoUsuario();

        textViewNomeArquivo = findViewById(R.id.textViewNomeArquivoMotoristaCnh);

    }

    // Evento executado pelo botão enviar foto
    public void enviarFotoCnh (View view) {

        // ACTION_PICK = Permite que escolha uma foto em um local especifico
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI = caminho padrão onde fica armazenado as fotos no celular
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // resolveActivity = Verifica se existe a galeria de fotos ( o software de galeria)
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    // Capturando imagem de retorno
    // requestCode = saber se e SELECAO_GALERIA definido no começo
    // resultCode = código de resultado para saber se deu certo ou não a execução do onActivityResult
    // Intent data = dados retornados , no caso a imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Testando resultCode
        // se resultCode == RESULT_OK = então os dados foram recuperados normalmente,se ouver ou não erro
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
                    fotoCNH = baos.toByteArray();
                    // Envia o nome da imagem para o XML
                    textViewNomeArquivo.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaCnhActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    // Evento onClick ( Botão buttonProximoCNH )
    public void buttonProximoCnhMotorista(View view){

        if(fotoCNH != null) {

            Usuario usuario = new Usuario();
            usuario.setNome(nomeUsuario);
            usuario.setSobrenome(sobrenomeUsuario);
            usuario.setTelefone(telefoneUsuario);
            usuario.setTipoUsuario(tipoUsuario);

            Motorista motorista = new Motorista();
            motorista.setFotoCNH(fotoCNH);

            Intent intent = new Intent(CadastroMotoristaCnhActivity.this, CadastroMotoristaFotoActivity.class);
            intent.putExtra("usuario", usuario);
            intent.putExtra("motorista", motorista);
            startActivity(intent);

        }else{
            Toast.makeText(CadastroMotoristaCnhActivity.this,
                    "Envie a foto da CNH",
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
