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
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroMotoristaCnhActivity extends AppCompatActivity {

    private Motorista motorista;
    private Endereco endereco;

    private TextInputEditText campoNumeroRegistroCnh;
    private String numeroRegistroCnh;
    private byte[] fotoCNH;
    private TextView campoNomeFotoCnh;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motorista_cnh);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); //CadastroMotoristaTermo

    }

    public void botaoEnviarFotoCnh (View view) {

        Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, SELECAO_GALERIA);
        }
    }

    public void botaoProximo(View view){

        getDadosDigitados();

        if(validarDados()) {

            motorista.setRegistroCnh(numeroRegistroCnh);
            motorista.setFotoCNH(fotoCNH);

            Intent intent = new Intent(this, CadastroMotoristaFotoActivity.class);
            intent.putExtra("motorista", motorista);
            intent.putExtra("endereco", endereco);
            startActivity(intent);
        }

    }

    public void iniciarComponentes(){
        campoNomeFotoCnh = findViewById(R.id.textViewNomeFotoCnh);
        campoNumeroRegistroCnh = findViewById(R.id.editNumeroRegistroCnh);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        motorista = intent.getParcelableExtra("motorista");
        endereco = intent.getParcelableExtra("endereco");
    }

    public void getDadosDigitados(){
        numeroRegistroCnh = campoNumeroRegistroCnh.getText().toString();
    }

    public Boolean validarDados () {

        Boolean validado = false;

        if(!VerificaDado.isVazio(numeroRegistroCnh) && numeroRegistroCnh.length() == 11) {
            if (fotoCNH != null) {

                validado = true;

            }else{
                Mensagem.toastIt("Envie a foto da CNH", this);
            }
        }else{
            Mensagem.toastIt("Preencha o registro da CNH corretamente", this);
        }
        return validado;
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
                    fotoCNH = baos.toByteArray();
                    // Envia o nome da imagem para o XML
                    campoNomeFotoCnh.setText(returnCursor.getString(nameIndex));

                    Toast.makeText(CadastroMotoristaCnhActivity.this,
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
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
