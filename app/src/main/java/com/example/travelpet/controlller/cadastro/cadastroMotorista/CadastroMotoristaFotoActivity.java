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
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Motorista;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaFotoActivity extends AppCompatActivity {

    // Variaveis usadas para pegar dados da Activity CadastroCnhMotorista
    private String  tipoUsuario, nome, sobrenome, telefone,
                    cep, logradouro, bairro, localidade, uf;

    // Variaveis usadas para armazenar fotos decocumentos do motorista
    private byte[] fotoCNH, fotoPerfil;

    private TextView textViewNomeArquivo;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_foto);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity CadastroCnhMototorista
        Intent intent = getIntent();
        Motorista motorista = intent.getParcelableExtra("motorista");
        Endereco endereco = intent.getParcelableExtra("endereco");

        tipoUsuario  =   motorista.getTipoUsuario();
        nome         =   motorista.getNome();
        sobrenome    =   motorista.getSobrenome();
        telefone     =   motorista.getTelefone();
        fotoCNH      =   motorista.getFotoCNH();

        cep          =   endereco.getCep();
        logradouro   =   endereco.getLogradouro();
        bairro       =   endereco.getBairro();
        localidade   =   endereco.getLocalidade();
        uf           =   endereco.getUf();

        textViewNomeArquivo = findViewById(R.id.textViewNomeArquivoMotoristaFoto);

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
                    fotoPerfil = baos.toByteArray();

                    // Envia o nome da imagem para o XML
                    textViewNomeArquivo.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaFotoActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    public void botaoEnviarFotoPerfil (View view) {

        // Seleciona a foto da galeria
        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    public void botaoProximoMotoristaFoto(View view){

        if(fotoPerfil != null) {
            System.out.println(fotoPerfil);
            Motorista motorista = new Motorista();
            motorista.setTipoUsuario(tipoUsuario);
            motorista.setNome(nome);
            motorista.setSobrenome(sobrenome);
            motorista.setTelefone(telefone);
            motorista.setFotoCNH(fotoCNH);
            motorista.setFotoPerfil(fotoPerfil);

            Endereco endereco = new Endereco();
            endereco.setCep(cep);
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setLocalidade(localidade);
            endereco.setUf(uf);

            Intent intent = new Intent(CadastroMotoristaFotoActivity.this, CadastroMotoristaVeiculoActivity.class);
            intent.putExtra("motorista", motorista);
            intent.putExtra("endereco",endereco);
            startActivity(intent);

        }else{
            Toast.makeText(CadastroMotoristaFotoActivity.this,
                    "Envie a foto de Perfil",
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
