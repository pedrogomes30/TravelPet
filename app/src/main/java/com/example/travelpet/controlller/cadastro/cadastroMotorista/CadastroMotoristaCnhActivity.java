package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Motorista;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCnhActivity extends AppCompatActivity {

    private Motorista motorista;
    private Endereco endereco;

    // Variavel armazena a foto da carteira de motorista CNH
    private byte[] fotoCNH;

    private TextView campoNomeFotoCnh;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;

    private TextInputEditText campoNumeroRegistroCnh;
    private String numeroRegistroCnh;
    private Button btProximo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_cnh);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroTermoMotorista
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");

        btProximo = findViewById(R.id.botaoProximo_cadCNH);
        campoNomeFotoCnh = findViewById(R.id.textViewNomeFotoCnh);

        campoNumeroRegistroCnh = findViewById(R.id.editNumeroRegistroCnh);

        botaoProximoOnclick();

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
                    campoNomeFotoCnh.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaCnhActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    public void botaoEnviarFotoCnh (View view) {

        // ACTION_PICK = Permite que escolha uma foto em um local especifico
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI = caminho padrão onde fica armazenado as fotos no celular
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // resolveActivity = Verifica se existe a galeria de fotos ( o software de galeria)
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    /*
    public void botaoProximo(View view){
        numeroRegistroCnh = campoNumeroRegistroCnh.getText().toString();

        if(!numeroRegistroCnh.isEmpty() && numeroRegistroCnh.length() == 11) {
            if (fotoCNH != null) {

                motorista.setRegistroCnh(numeroRegistroCnh);
                motorista.setFotoCNH(fotoCNH);

                Intent intent = new Intent(CadastroMotoristaCnhActivity.this, CadastroMotoristaFotoActivity.class);
                intent.putExtra("motorista", motorista);
                intent.putExtra("endereco", endereco);
                startActivity(intent);

            }else{
                Toast.makeText(CadastroMotoristaCnhActivity.this,
                        "Envie a foto da CNH",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroMotoristaCnhActivity.this,
                    "Preencha o registro da CNH corretamente",
                    Toast.LENGTH_SHORT).show();
        }
    }
    */

    public void botaoProximoOnclick()
    {
        btProximo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                numeroRegistroCnh = campoNumeroRegistroCnh.getText().toString();

                if(!numeroRegistroCnh.isEmpty() && numeroRegistroCnh.length() == 11) {
                    if (fotoCNH != null) {

                        motorista.setRegistroCnh(numeroRegistroCnh);
                        motorista.setFotoCNH(fotoCNH);

                        Intent intent = new Intent(getApplicationContext(), CadastroMotoristaFotoActivity.class);
                        intent.putExtra("motorista", motorista);
                        intent.putExtra("endereco", endereco);
                        startActivity(intent);

                    }else{
                        Toast.makeText(CadastroMotoristaCnhActivity.this,
                                "Envie a foto da CNH",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroMotoristaCnhActivity.this,
                            "Preencha o registro da CNH corretamente",
                            Toast.LENGTH_SHORT).show();
                }
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
