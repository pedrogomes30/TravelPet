package com.example.travelpet.controlller.cadastro.cadastroMotorista;

import android.app.ProgressDialog;
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
import com.example.travelpet.dao.EnderecoDAO;
import com.example.travelpet.dao.MotoristaDAO;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.dao.UsuarioFirebase;
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

    private ProgressDialog progressDialog;

    private TextView campoNomeFotoCrvl;

    private byte[] fotoCrvl;

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
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
        veiculo = intent.getParcelableExtra("veiculo");

        statusCadastro  =   "Em análise";

        campoNomeFotoCrvl = findViewById(R.id.textViewNomeFotoCrvl);
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

            TelaCarregamento.iniciarCarregamento(progressDialog);
            //          Funcionando, mas ainda em fase de analise
            int tipoSave = 1; // tipo = 1 - Cadastro de dados
            enderecoDAO.salvarEnderecoRealtimeDatabase(endereco, motorista.getTipoUsuario(),
                                                       tipoSave, progressDialog );

            veiculo.setFotoCrvl(fotoCrvl);
            veiculoDAO.salvarVeiculo(veiculo);

            motorista.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
            motorista.setEmail(UsuarioFirebase.getEmailUsuario());
            motorista.setStatusCadastro(statusCadastro);
            motoristaDAO.salvarImagemMotoristaStorage(motorista, this,
                                                     tipoSave, progressDialog);


        } else {
            Toast.makeText(this,
                    "Envie a foto do CRVL",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK){

            try {

                Uri localImagemSelecionada = data.getData();

                Cursor returnCursor = getContentResolver().query(localImagemSelecionada, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();

                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if ( imagem != null){

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    fotoCrvl = baos.toByteArray();

                    // Envia o nome da imagem para o XML
                    campoNomeFotoCrvl.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace(); // Caso ocorra um erro podemos ver aqui
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
