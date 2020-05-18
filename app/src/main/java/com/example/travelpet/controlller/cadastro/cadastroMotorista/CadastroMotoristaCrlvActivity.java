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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.dao.EnderecoDAO;
import com.example.travelpet.dao.MotoristaDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Veiculo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCrlvActivity extends AppCompatActivity {


    private Motorista motorista;
    private Veiculo veiculo;
    private Endereco endereco;

    private MotoristaDAO motoristaDAO;
    private EnderecoDAO enderecoDAO;
    private VeiculoDAO veiculoDAO;

    private TextView campoNomeFotoCrvl;

    private byte[] fotoCrvl;

    // requestCode = SELECAO_GALERIA = e um codigo para ser passado no requestCode
    private static final int SELECAO_GALERIA = 200;

    private String statusCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_crlv);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        motoristaDAO = new MotoristaDAO();
        enderecoDAO  = new EnderecoDAO();
        veiculoDAO   = new VeiculoDAO();

        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
        veiculo = intent.getParcelableExtra("veiculo");

        statusCadastro  =   "Em análise";

        campoNomeFotoCrvl = findViewById(R.id.textViewNomeFotoCrvl);
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

                    //motorista.setFotoCrlv(baos.toByteArray());
                    //veiculo.setFotoCrvl(baos.toByteArray());
                    // fotoAnimal = baos.toByteArray();
                    fotoCrvl = baos.toByteArray();

                    // Envia o nome da imagem para o XML
                    campoNomeFotoCrvl.setText(returnCursor.getString(nameIndex));

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

    public void botaoFinalizar(View view) {

        if (fotoCrvl != null) {
            //          Funcionando, mas ainda em fase de analise

            enderecoDAO.salvarEnderecoRealtimeDatabase(endereco, motorista.getTipoUsuario());

            veiculo.setFotoCrvl(fotoCrvl);
            veiculoDAO.salvarVeiculo(veiculo);

            motorista.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
            motorista.setEmail(UsuarioFirebase.getEmailUsuario());
            motorista.setStatusCadastro(statusCadastro);
            motoristaDAO.salvarImagemMotoristaStorage(motorista);

            AlertDialog.Builder builder = new AlertDialog.Builder(CadastroMotoristaCrlvActivity.this);
            builder.setTitle("Cadastro realizado com sucesso");
            builder.setMessage("Agora iremos avaliar seus dados a análise pode levar até 7 dias útéis");
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
