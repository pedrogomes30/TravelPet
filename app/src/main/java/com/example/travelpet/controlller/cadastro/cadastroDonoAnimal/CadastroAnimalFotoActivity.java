package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.EnderecoDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.helper.Permissao;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroAnimalFotoActivity extends AppCompatActivity {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    private DonoAnimalDAO donoAnimalDAO;
    private EnderecoDAO enderecoDAO;
    private AnimalDAO animalDAO;

    private String fotoPerfilUrl,fluxoDados;

    private CircleImageView campoFotoAnimal;
    private byte[] fotoAnimal;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_foto);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        donoAnimalDAO = new DonoAnimalDAO();
        enderecoDAO = new EnderecoDAO();
        animalDAO = new AnimalDAO();

        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");


        fluxoDados = donoAnimal.getFluxoDados();

        // Pegando foto do Email do Usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri fotoUsuarioEmail = usuario.getPhotoUrl();
        if(fotoUsuarioEmail != null){
            fotoPerfilUrl = fotoUsuarioEmail.toString();
        }else{
            fotoPerfilUrl = "";
        }

        campoFotoAnimal = findViewById(R.id.circleImageViewFotoAnimal);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                Permissao.alertaValidacaoPermissao(CadastroAnimalFotoActivity.this);
            }
        }
    }
    public void botaoCamera(View view){

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verifica se a intent conseguiu fazer o pedido ( que e abrir a camera)
        // getActivity(). = foi necessário colocar pois estamos dentro de um Fragment
        // e não uma Activity
        // getPackageManager() retorna uma classe um objeto packageManager responsavel
        // por obter várias informações relacionado a aplicação
        if (i.resolveActivity(getPackageManager()) != null) {
            // Captura a foto que foi tirada
            // startActivityForResult = Inicia a activity, e recupera um resultado de retorno que e a foto
            // requestCode= para saber qual ação foi executada a da camera ou da galeria
            //startActivityForResult(i, SELECAO_CAMERA );
            startActivityForResult(i,SELECAO_CAMERA);
        }
    }

    public void botaoGaleria(View view){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_GALERIA);
        }
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
                        // data = parâmetro refênciado la encima no "onActivityResult"
                        // getExtras() = recupera recursos extras
                        // get("data"); = recebe uma String ("data) que e o dado retornado
                        // (Bitmap) = e um cash para Bitmap, referenciando que e o tipo da variável
                        imagem = (Bitmap) data.getExtras().get("data");

                    break;
                    case SELECAO_GALERIA:
                        // Recupera o local da imagem selecionada
                        // data.getData(); = local da imagem
                        Uri localImagemSelecionada = data.getData();
                        // getContentResolver() = responsável por recupera conteúdo dentro do app android
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                    break;
                }
                // Verificando se a imagem não está vazia
                if(imagem != null){
                    // Envia a imagem da camera ou galeria para o imagemView do xml
                    campoFotoAnimal.setImageBitmap( imagem );

                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress( Bitmap.CompressFormat.JPEG, 100, baos );
                    fotoAnimal = baos.toByteArray();

                    Toast.makeText(CadastroAnimalFotoActivity.this,
                            "Sucesso ao selececionar a imagem",
                            Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                // Caso de algum erro, e possivvel visualizar no "e.printStackTrace();"
                e.printStackTrace();
            }
        }
    }

    public void botaoFinalizar(View view) {

        int tipoLocalSave = 1; // cadastro

        if (fotoAnimal != null && fluxoDados.equals("cadastroUsuario")) {

            donoAnimal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
            donoAnimal.setEmail(UsuarioFirebase.getEmailUsuario());
            donoAnimal.setFotoPerfilUrl(fotoPerfilUrl);
            donoAnimalDAO.salvarDonoAnimalRealtimeDatabase(donoAnimal);

            enderecoDAO.salvarEnderecoRealtimeDatabase(endereco, donoAnimal.getTipoUsuario());

            //          Funcionando, mas ainda em fase de analise
            animal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
            animal.setIdAnimal(animalDAO.gerarPushKeyIdAnimal());
            animal.setFotoAnimal(fotoAnimal);
            animalDAO.salvarImagemAnimalStorage(animal, tipoLocalSave,
                                    CadastroAnimalFotoActivity.this,
                                                PerfilPassageiroActivity.class);

        }else if(fotoAnimal != null && fluxoDados.equals("perfilUsuario")){

            //          Funcionando, mas ainda em fase de analise
            animal.setIdUsuario(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
            animal.setIdAnimal(animalDAO.gerarPushKeyIdAnimal());
            animal.setFotoAnimal(fotoAnimal);
            animalDAO.salvarImagemAnimalStorage(animal, tipoLocalSave,
                                   CadastroAnimalFotoActivity.this,
                                               PerfilPassageiroActivity.class);

        }
        else{

            Toast.makeText(CadastroAnimalFotoActivity.this,
                    "Envie a foto do seu Animal ",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}



