package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.EnderecoDAO;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.helper.UsuarioFirebase;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroAnimalFotoActivity extends AppCompatActivity {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private DonoAnimalDAO donoAnimalDAO;
    private EnderecoDAO enderecoDAO;
    private AnimalDAO animalDAO;

    private String fluxoDados;

    private CircleImageView campoFotoAnimal;
    private byte[] fotoAnimal;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_foto);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        donoAnimalDAO = new DonoAnimalDAO();
        enderecoDAO = new EnderecoDAO();
        animalDAO = new AnimalDAO();
        progressDialog = new ProgressDialog(CadastroAnimalFotoActivity.this);

        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");

        fluxoDados = donoAnimal.getFluxoDados();

        campoFotoAnimal = findViewById(R.id.circleImageViewFotoAnimal);

    }

    public void botaoCamera(View view){

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(i,SELECAO_CAMERA);
        }
    }

    public void botaoGaleria(View view){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_GALERIA);
        }
    }

    public void botaoFinalizar(View view) {

        int tipoSave = 1; // tipoSave = 1 / CADASTRO

        if(fotoAnimal != null && fluxoDados.equals("cadastroUsuarioDados")){

            TelaCarregamento.iniciarCarregamento(progressDialog);

            setarDadosCadastroUsuario();

            donoAnimalDAO.salvarDonoAnimalRealtimeDatabase(donoAnimal, progressDialog, tipoSave,
                    this);

            enderecoDAO.salvarEnderecoRealtimeDatabase(endereco, donoAnimal.getTipoUsuario(), tipoSave,
                    progressDialog);
            animal.setFotoAnimal(fotoAnimal);

            animalDAO.salvarAnimalStorage(animal, progressDialog, tipoSave,this);


        }else if(fotoAnimal != null && fluxoDados.equals("listaAnimais")){

            TelaCarregamento.iniciarCarregamento(progressDialog);

            setarDadosAdicionarAnimal();

            animalDAO.salvarAnimalStorage(animal, progressDialog, tipoSave,this);

        }else{
            Toast.makeText(this,
                    "Envie a foto do seu Animal ",
                    Toast.LENGTH_SHORT).show();

        }
    }

    public void setarDadosCadastroUsuario(){
        // Classe DonoAnimal
        donoAnimal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimal.setEmail(UsuarioFirebase.getEmailUsuario());
        donoAnimal.setFotoPerfilUrl(UsuarioFirebase.getFotoEmailUsuario());

        // Classe Animal
        animal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        animal.setIdAnimal(animalDAO.gerarPushKeyIdAnimal());
        animal.setFotoAnimal(fotoAnimal);
    }

    public void setarDadosAdicionarAnimal(){
        // Classe Animal
        animal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        animal.setIdAnimal(animalDAO.gerarPushKeyIdAnimal());
        animal.setFotoAnimal(fotoAnimal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Teste resultCode
        // se for igual ao RESULT_OK, deu td ok
        if( resultCode == RESULT_OK){
            // null = por que pode receber dados de dois lugares , camera ou galeria
            Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:

                        imagem = (Bitmap) data.getExtras().get("data");

                        break;
                    case SELECAO_GALERIA:

                        Uri localImagemSelecionada = data.getData();

                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if(imagem != null){

                    campoFotoAnimal.setImageBitmap( imagem );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress( Bitmap.CompressFormat.JPEG, 100, baos );
                    fotoAnimal = baos.toByteArray();

                    Toast.makeText(CadastroAnimalFotoActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
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



