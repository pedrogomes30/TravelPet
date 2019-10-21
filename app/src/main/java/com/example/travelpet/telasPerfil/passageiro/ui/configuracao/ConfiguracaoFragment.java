package com.example.travelpet.telasPerfil.passageiro.ui.configuracao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.activity.cadastro.cadastroAnimal.CadastroNomeAnimalActivity;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.example.travelpet.telasPerfil.passageiro.ui.meus.animais.ListaAnimaisFragment;
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


    private ImageButton imageButtonCamera, imageButtonGaleria;
    private TextView textViewAdicionarAnimal,textViewEditarAnimais;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private CircleImageView imageViewCircleFotoPerfil;
    private StorageReference storageReference;
    private String emailUsuario;

    // Variaveis usadas para pegar dados nulos para activity "CadastroNomeAnimalActivity"
    String nomeUsuario, sobrenomeUsuario, telefoneUsuario,tipoUsuario;
    // Variável usada para o fluxo de adicionar animais
    String fluxoDados = "perfilUsuario";

    // Variável usada no processo de pegar os dados do database
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    String nomeU,sobrenomeU,telefoneU,tipoU;

    // Variável usada no processo de trocar de Fragment
    private ListaAnimaisFragment listaAnimaisFragment;
    private ConfiguracaoFragment configuracaoFragment;

    //  Ainda precisa ser Trabalhado
    /* Código para solicitar permissão ao usuário, array de Strings
    public String [] permissoesNecessarias = new String []{
            // Definindo Permiissões
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    }; */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configuracao, container, false);

        // Recupera a referência do Storage
        storageReference    =   ConfiguracaoFirebase.getFirebaseStorage();
        emailUsuario        =   UsuarioFirebase.getEmailUsuario();

        // Validar Permissões (Ainda precisa ser trabalhado)
        // Permissao.validarPermissoes(permissoesNecessarias,this, 1);

        imageButtonCamera   =   root.findViewById(R.id.imageButtonCamera);
        imageButtonGaleria  =   root.findViewById(R.id.imageButtonGaleria);
        imageViewCircleFotoPerfil = root.findViewById(R.id.imageViewCircleFotoPerfil);
        textViewAdicionarAnimal = root.findViewById(R.id.textViewAdicionarAnimal);
        textViewEditarAnimais = root.findViewById(R.id.textViewEditarAnimais);

        //imageViewPerfil = findViewById(R.id.imageViewPerfil);
        DatabaseReference usuarios = referencia.child( "usuarios" ).child(UsuarioFirebase.getIdentificadorUsuario());

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                nomeU       =   dadosUsuario.getNome();
                sobrenomeU  =   dadosUsuario.getSobrenome();
                telefoneU   =   dadosUsuario.getTelefone();
                tipoU       =   dadosUsuario.getTipoUsuario();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Recupera dados do usuário ( usado no processo de pegar foto de perfil do usuario)
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        // Recupera a foto de perfil do usuario atual
        Uri url = usuario.getPhotoUrl();

        // Verifica a foto do usuario firebase não está vazia
        if(url != null){
            // Glide e uma biblioteca que foi inserida graças a dependencia "firebase-ui-storage"
            Glide.with(getActivity())
                    .load( url )
                    // .into = define qual imageView irá utilizar
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
                // Verifica se a intent conseguiu fazer o pedido ( que e abrir a camera)
                // getActivity(). = foi necessário colocar pois estamos dentro de um Fragment
                // e não uma Activity
                // getPackageManager() retorna uma classe um objeto packageManager responsavel
                // por obter várias informações relacionado a aplicação
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Captura a foto que foi tirada
                    // startActivityForResult = Inicia a activity, e recupera um resultado de retorno que e a foto
                    // requestCode= para saber qual ação foi executada a da camera ou da galeria
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

        textViewAdicionarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Usuario usuario = new Usuario();
                // Passando dados nulo para Activity CadastroNomeAnimal
                // Para poder enganar e passa o FluxoDados junto
                usuario.setNome(nomeUsuario);
                usuario.setSobrenome(sobrenomeUsuario);
                usuario.setTelefone(telefoneUsuario);
                usuario.setTipoUsuario(tipoUsuario);
                usuario.setFluxoDados(fluxoDados);

                Intent intent = new Intent(getActivity(), CadastroNomeAnimalActivity.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);

            }
        });
        textViewEditarAnimais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instancia do Fragment "ListaAnimaisFragment"
                listaAnimaisFragment = new ListaAnimaisFragment();
                // Configurar objeto para o Fragmento
                // getFragmentManager() = recupera o objeto que gerencia(configura) os Fragmentos
                // beginTransaction() = inicia uma transação
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, listaAnimaisFragment);
                transaction.addToBackStack(null);
                // Encerra uma transação
                transaction.commit();

            }
        });

        return root;
    }

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
            Bitmap imagem = null;

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
                if(imagem != null){
                    // Envia a imagem da camera ou galeria para o perfil nas configurações
                    imageViewCircleFotoPerfil.setImageBitmap( imagem );

                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress( Bitmap.CompressFormat.JPEG, 100, baos );
                    byte[] dadosImagem = baos.toByteArray();

                    // Salvar imagem no firebase
                    StorageReference imagemRef = storageReference
                            .child("passageiro")
                            .child(emailUsuario)
                            .child("foto de perfil")
                            .child(emailUsuario+".PERFIL.JPEG");

                    // Salvando dados da imagem método UploadTask
                    // .putBytes = passa os dados da imagem em Bytes
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    // Método para saber se o salvamento deu certo
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),
                            "Erro ao fazer upload da imagem",
                            Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                            // Configurando (atualizando) foto para pegar ela nas configurações do usuário
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            // url = pega o caminho da imagem
                            Uri url = uri.getResult();

                            // Chama o método atualizaFotoUsuario da classe UsuarioFirebase
                            // esse metodo atualiza a foto de usuário do firebase
                            UsuarioFirebase.atualizarFotoUsuario( url );

                            // Pega A url(caminho) da foto transforma em String e armazena
                            // na vriável para poder ser salva no "database"
                            String fotoUsuarioUrl = url.toString();

                            // Salvando todos os dados de novo do database
                            // para poder salvar a Url da imagem junto "FotoUsuario"
                            Usuario usuario = new Usuario();

                            usuario.setId(UsuarioFirebase.getIdentificadorUsuario());
                            usuario.setEmail(UsuarioFirebase.getEmailUsuario());
                            usuario.setNome(nomeU);
                            usuario.setSobrenome(sobrenomeU);
                            usuario.setTelefone(telefoneU);
                            usuario.setTipoUsuario(tipoU);
                            usuario.setFotoUsuarioUrl(fotoUsuarioUrl);
                            usuario.salvar();

                        }
                    });
                }
            }catch (Exception e){
                // Caso de algum erro, e possivvel visualizar no "e.printStackTrace();"
                e.printStackTrace();
            }
        }
    }
}