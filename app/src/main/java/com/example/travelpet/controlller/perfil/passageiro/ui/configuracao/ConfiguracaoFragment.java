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
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Usuario;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais.ListaAnimaisFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ConfiguracaoFragment extends Fragment {

    private CircleImageView imageViewCircleFotoPerfil;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private EditText editTextNomeUsuario, editTextSobrenomeUsuario;
    private ImageView imageViewAtualizarNomeUsuario,imageViewAtualizarSobrenomeUsuario;
    private Button buttonSalvarConfiguracao, buttonSair;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private Bitmap imagem = null;
    private byte[] fotoUsuario;


    private StorageReference storageReference;
    private String email;

    // Variável usada no processo de pegar os dados do database
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private String nome,sobrenome,telefone,tipoUsuario, fotoUsuarioUrl;
    private String nomeEdit, sobrenomeEdit, localSalvamentoUsuario;

    // Variável usada no processo de trocar de Fragment
    private ListaAnimaisFragment listaAnimaisFragment;
    private ConfiguracaoFragment configuracaoFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configuracao, container, false);

        // Recupera a referência do Storage
        storageReference    =   ConfiguracaoFirebase.getFirebaseStorage();
        email               =   UsuarioFirebase.getEmailUsuario();

        imageViewCircleFotoPerfil = root.findViewById(R.id.imageViewCircleFotoPerfil);
        imageButtonCamera   =   root.findViewById(R.id.imageButtonCamera);
        imageButtonGaleria  =   root.findViewById(R.id.imageButtonGaleria);
        editTextNomeUsuario = root.findViewById(R.id.editTextNomeUsuario);
        editTextSobrenomeUsuario = root.findViewById(R.id.editTextSobrenomeUsuario);
        imageViewAtualizarNomeUsuario = root.findViewById(R.id.imageViewAtualizarNomeUsuario);
        imageViewAtualizarSobrenomeUsuario = root.findViewById(R.id.imageViewAtualizarSobrenomeUsuario);
        buttonSalvarConfiguracao = root.findViewById(R.id.buttonSalvarConfiguracao);
        buttonSair = root.findViewById(R.id.buttonSair);

        DatabaseReference usuarios = referencia.child( "usuarios" ).child(UsuarioFirebase.getIdentificadorUsuario());

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                // Necessario pegar os dados de novo para salvar junto com a foto, nome ou sobrenome alterado de novo
                nome              =   dadosUsuario.getNome();
                sobrenome         =   dadosUsuario.getSobrenome();
                telefone          =   dadosUsuario.getTelefone();
                tipoUsuario       =   dadosUsuario.getTipoUsuario();
                fotoUsuarioUrl    =   dadosUsuario.getFotoUsuarioUrl();

                // Enviando o nome e sobrenome do Usuário para o xml da fragment
                editTextNomeUsuario.setText(nome);
                editTextSobrenomeUsuario.setText(sobrenome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Recupera dados do usuário ( usado no processo de pegar foto de perfil do usuario)
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        // Recupera a foto de perfil do usuario atual
        Uri fotoUsuarioEmail = usuario.getPhotoUrl();

        // Verifica a foto do usuario firebase não está vazia
        if(fotoUsuarioEmail != null){
            // Glide e uma biblioteca que foi inserida graças a dependencia "firebase-ui-storage"
            Glide.with(getActivity())
                    .load( fotoUsuarioEmail )
                    .into( imageViewCircleFotoPerfil );

        }else{// caso esteja vazio
            // Envia imagem padrão para a foto de perfil em configurações
            imageViewCircleFotoPerfil.setImageResource(R.drawable.iconperfiloficial);
        }

        // Evento de clique do botão camera
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
        buttonSalvarConfiguracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nomeEdit = editTextNomeUsuario.getText().toString().toUpperCase();
                sobrenomeEdit = editTextSobrenomeUsuario.getText().toString().toUpperCase();
                localSalvamentoUsuario = "ConfiguracaoFragmet";
                // Verificando se a imagem não está vazia
                if(imagem != null){

                    DonoAnimal.atualizarDonoAnimal(email,nomeEdit, sobrenomeEdit, telefone,
                                                   tipoUsuario, fotoUsuario, getActivity(), localSalvamentoUsuario);

                }else if(!nome.equals(nomeEdit) || !sobrenome.equals(sobrenomeEdit)){

                    DonoAnimal donoAnimal = new DonoAnimal();
                    donoAnimal.setId(UsuarioFirebase.getIdentificadorUsuario());
                    donoAnimal.setEmail(email);
                    donoAnimal.setNome(nomeEdit);
                    donoAnimal.setSobrenome(sobrenomeEdit);
                    donoAnimal.setTelefone(telefone);
                    donoAnimal.setTipoUsuario(tipoUsuario);
                    donoAnimal.setFotoUsuarioUrl(fotoUsuarioUrl);
                    donoAnimal.salvar(getActivity(), localSalvamentoUsuario);
               }
            }
        });

        buttonSair.setOnClickListener(new View.OnClickListener() {
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
                    imageViewCircleFotoPerfil.setImageBitmap(imagem);

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