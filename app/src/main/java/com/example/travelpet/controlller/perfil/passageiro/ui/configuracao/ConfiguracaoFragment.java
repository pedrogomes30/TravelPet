package com.example.travelpet.controlller.perfil.passageiro.ui.configuracao;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.controlller.MainActivity;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais.ListaAnimaisFragment;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ConfiguracaoFragment extends Fragment {

    private DonoAnimal    donoAnimal;
    private DonoAnimalDAO donoAnimalDAO;

    private CircleImageView campoFotoPerfil;
    private EditText        campoNome, campoSobrenome;
    private ImageButton     botaoCamera, botaoGaleria;
    private Button          botaoSalvar, botaoSair;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private Bitmap imagem = null;
    private byte[] fotoUsuario;

    // Variável usada no processo de pegar os dados do database
    private String nome,sobrenome, fotoPerfilUrl;
    private String nomeEdit, sobrenomeEdit;

    // Variável usada no processo de trocar de Fragment
    private ListaAnimaisFragment listaAnimaisFragment;
    private ConfiguracaoFragment configuracaoFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_configuracao, container, false);

        donoAnimalDAO = new DonoAnimalDAO();

        campoFotoPerfil     =   root.findViewById(R.id.imageViewCircleFotoPerfil);
        campoNome           =   root.findViewById(R.id.editTextNomeUsuario);
        campoSobrenome      =   root.findViewById(R.id.editTextSobrenomeUsuario);
        botaoCamera         =   root.findViewById(R.id.imageButtonCamera);
        botaoGaleria        =   root.findViewById(R.id.imageButtonGaleria);
        botaoSalvar         =   root.findViewById(R.id.botaoSalvar);
        botaoSair           =   root.findViewById(R.id.botaoSair);

        DatabaseReference donoAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child( "donoAnimal" )
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donoAnimal = dataSnapshot.getValue(DonoAnimal.class);
                // Necessario pegar os dados de novo para salvar junto com a foto, nome ou sobrenome alterado de novo
                nome            =   donoAnimal.getNome();
                sobrenome       =   donoAnimal.getSobrenome();
                fotoPerfilUrl   =   donoAnimal.getFotoPerfilUrl();

                //          Enviando os dados para o layout XML
                if(!fotoPerfilUrl.equals("")){

                    Uri fotoPerfilUri = Uri.parse(fotoPerfilUrl);
                    Glide.with(getActivity()).load( fotoPerfilUri ).into( campoFotoPerfil );

                }else{
                    campoFotoPerfil.setImageResource(R.drawable.iconperfiloficial);
                }

                campoNome.setText(nome);
                campoSobrenome.setText(sobrenome);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        //              Configurando função dos botões
        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
        botaoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_GALERIA);

                }
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nomeEdit      = campoNome.getText().toString().toUpperCase();
                sobrenomeEdit = campoSobrenome.getText().toString().toUpperCase();

                // Verificando se a imagem não está vazia
                if(fotoUsuario != null){

                    donoAnimal.setNome(nomeEdit);
                    donoAnimal.setSobrenome(sobrenomeEdit);
                    donoAnimalDAO.salvarImagemDonoAnimalStorage(fotoUsuario, donoAnimal);
                    fotoUsuario = null;

                    Toast.makeText(getActivity(),
                            "Atualização  feita com sucesso",
                            Toast.LENGTH_SHORT).show();


                }else if(!nome.equals(nomeEdit) || !sobrenome.equals(sobrenomeEdit)){

                    donoAnimal.setNome(nomeEdit);
                    donoAnimal.setSobrenome(sobrenomeEdit);
                    donoAnimalDAO.salvarDonoAnimalRealtimeDatabase(donoAnimal);

                    Toast.makeText(getActivity(),
                            "Atualização feita com sucesso",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Caixa de diálogo
                AlertDialog.Builder msgBox = new AlertDialog.Builder(getContext());
                msgBox.setTitle("Saindo...");
                msgBox.setMessage("Tem certeza que deseja sair desta conta ?");
                msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthUI.getInstance()
                                .signOut(getContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                } );
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    }
                });
                msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                msgBox.show();
            }
        });

        return root;
    }

    //          Método para verificar de onde será pego a foto, da camera ou galeria
    /* Capturando (Recuperando a imagem, sobre-escrevendo o método
    // requestCode = saber se e SELECAO_GALERIA definido no começo
    // resultCode = código de resultado para saber se deu certo ou não a execução do onActivityResult
    // Intent data = dados retornados , no caso a imagem */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Teste resultCode
        // se for igual ao RESULT_OK, deu td ok
        if( resultCode == RESULT_OK){
            // null = por que pode receber dados de dois lugares , camera ou galeria
            //Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        // data = parâmetro refênciado la encima no "onActivityResult"
                        // getExtras() = recupera recursos extras
                        // get("data"); = recebe uma String ("data) que o dado retornado
                        // (Bitmap) = e um cash para Bitmap, referenciando que o tipo da variável
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        // Recupera o local da imagem selecionada
                        // data.getData(); = local da imagem
                        Uri localImagemSelecionada = data.getData();
                        // getActivity(). = foi necessário usar pois estamos dentro de um Fragment
                        // é não uma Activity
                        // getContentResolver() = responsável por recupera conteúdo dentro do app android
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }
                // Verificando se a imagem não está vazia
                if(imagem != null) {
                    // Envia a imagem para o XML
                    campoFotoPerfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    // Converte a imagem para um array de byts
                    fotoUsuario = baos.toByteArray();
                }

                }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}