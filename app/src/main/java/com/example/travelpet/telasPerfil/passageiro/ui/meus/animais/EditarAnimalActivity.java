package com.example.travelpet.telasPerfil.passageiro.ui.meus.animais;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.telasPerfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.classes.Animal;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarAnimalActivity extends AppCompatActivity {

    // Váriaveis representando recursos do xml
    private CircleImageView imageViewFotoAnimal;
    private EditText editTextNomeAnimal;
    private TextView textViewEspecieEdit,textViewRacaEdit;
    private Spinner spinnerPorteAnimal;

    private Animal animalDestinatario;
    private List<String> listaPorteAnimal = new ArrayList<String>();
    // Variáveis para recolher dados editados
    private String porteAnimalEdit,nomeAnimalEdit;

    // Váriavel para verificação, se a foto do Animal foi alterada
    private Bitmap imagem = null;

    private byte[] fotoAnimal;
    private StorageReference storageReference;

    private String emailUsuario;

    private String idAnimal,nomeAnimal, especieAnimal, racaAnimal,porteAnimal,fotoAnimalUrl;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    // Variável utilizada no processo de excluir Animal
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_animal);

        // Recupera referência do database
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        // Recupera a referência do Storage
        storageReference    =   ConfiguracaoFirebase.getFirebaseStorage();

        emailUsuario        =   UsuarioFirebase.getEmailUsuario();


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Animal");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        // Configurando seta voltar do toolbar (ativando)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurações iniciais
        imageViewFotoAnimal         =   findViewById(R.id.imageViewFotoAnimal);
        editTextNomeAnimal          =   findViewById(R.id.editTextNomeAnimal);
        textViewEspecieEdit         =   findViewById(R.id.textViewEspecieEdit);
        textViewRacaEdit            =   findViewById(R.id.textViewRacaEdit);
        spinnerPorteAnimal          =   findViewById(R.id.spinnerPorteAnimal);

        // Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            animalDestinatario = (Animal) bundle.getParcelable("animalSelecionado");
            // Recebendo dados do database para exibir no arquivo xml
            editTextNomeAnimal.setText(animalDestinatario.getNomeAnimal());
            textViewEspecieEdit.setText(animalDestinatario.getEspecieAnimal());
            textViewRacaEdit.setText(animalDestinatario.getRacaAnimal());

            // Colocando dados na variaveis para poder salvar no firebase as alterações
            idAnimal = animalDestinatario.getIdAnimal();
            nomeAnimal = animalDestinatario.getNomeAnimal();
            especieAnimal = animalDestinatario.getEspecieAnimal();
            racaAnimal = animalDestinatario.getRacaAnimal();
            porteAnimal = animalDestinatario.getPorteAnimal();
            fotoAnimalUrl = animalDestinatario.getFotoAnimal();

            // para comparação comn "nomeAnimal"
            nomeAnimalEdit = animalDestinatario.getNomeAnimal();

            // ouvindote para o nome
            editTextNomeAnimal.addTextChangedListener(new TextWatcher() {
                @Override //
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //executa uma determinada ação antes da modificação do editText
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    //executa uma determinada ação durante a modificação do editText
                    nomeAnimalEdit = editTextNomeAnimal.getText().toString().toUpperCase();;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //aqui você executa uma determinada ação depois da modificação do editText
                }
            });

            String fotoAnimal = animalDestinatario.getFotoAnimal();
            if(fotoAnimal != null){
                Uri url = Uri.parse(fotoAnimal);
                Glide.with(EditarAnimalActivity.this)
                        .load(url)
                        .into(imageViewFotoAnimal);
            }else{
                imageViewFotoAnimal.setImageResource(R.drawable.iconperfilanimal);
            }

            // Inserindo dados ao array do Spinner, baseado no porte recebido do database
            if(porteAnimal.equals("Pequeno - Até 35cm")){
                listaPorteAnimal.add("Pequeno - Até 35cm");
                listaPorteAnimal.add("Médio - De 36 a 49cm");
                listaPorteAnimal.add("Grande - Acima de 50cm");

            }else if(porteAnimal.equals("Médio - De 36 a 49cm")){
                listaPorteAnimal.add("Médio - De 36 a 49cm");
                listaPorteAnimal.add("Pequeno - Até 35cm");
                listaPorteAnimal.add("Grande - Acima de 50cm");


            }else if (porteAnimal.equals("Grande - Acima de 50cm")){
                listaPorteAnimal.add("Grande - Acima de 50cm");
                listaPorteAnimal.add("Pequeno - Até 35cm");
                listaPorteAnimal.add("Médio - De 36 a 49cm");

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaPorteAnimal);
            spinnerPorteAnimal.setAdapter(adapter);
        }
        // Método fica ouvindo a mudança no porte do animal
        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Armazena a mudança do porte do animal
                porteAnimalEdit = spinnerPorteAnimal.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinnerPorteAnimal.setOnItemSelectedListener(escolha);


    }


    // Evento de clique do botão camera
        public void buttonCameraAnimal(View view){

            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Verifica se a intent conseguiu fazer o pedido ( que e abrir a camera)
            if (i.resolveActivity(getPackageManager()) != null) {
                // Captura a foto que foi tirada
                // startActivityForResult = Inicia a activity, e recupera um resultado de retorno que e a foto
                startActivityForResult(i,SELECAO_CAMERA);
            }
        }

        public void buttonGaleriaAnimal(View view) {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i,SELECAO_GALERIA);
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK) {

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                // Verificando se a imagem não está vazia
                if (imagem != null) {
                    imageViewFotoAnimal.setImageBitmap(imagem);
                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    fotoAnimal = baos.toByteArray();
                }
            }catch (Exception e){
                // Caso de algum erro, e possivvel visualizar no "e.printStackTrace();"
                e.printStackTrace();
            }
        }
    }
     public void salvarDadosAnimal(View view){
         // Salvar imagem no firebase
         if(imagem != null ){

            StorageReference imagemRef = storageReference
                 .child("animais")
                 .child(emailUsuario)
                 .child(idAnimal)
                 .child(emailUsuario +"."+idAnimal+"."+especieAnimal+"."+racaAnimal+".FOTO.PERFIL.JPEG");

            UploadTask uploadTask = imagemRef.putBytes(fotoAnimal);

             // Método para saber se o salvamento deu certo
             // caso de erro
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                 Toast.makeText(EditarAnimalActivity.this,
                         "Erro ao fazer upload da imagem",
                         Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Método para recupera o caminho(url) da foto, quando salvar ela no storage
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    // url = recupera o caminho da imagem
                    Uri url = uri.getResult();
                    // transforma a url para String, e armazena na variável
                    fotoAnimalUrl = url.toString();

                    // Método para salvar animal
                    // foi feito aqui por causa do método que pega o caminho da url da foto
                    Animal animal = new Animal();

                    animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
                    animal.setIdAnimal(idAnimal);
                    animal.setNomeAnimal(nomeAnimalEdit);
                    animal.setEspecieAnimal(especieAnimal);
                    animal.setRacaAnimal(racaAnimal);
                    animal.setPorteAnimal(porteAnimalEdit);
                    animal.setFotoAnimal(fotoAnimalUrl);
                    animal.salvarAnimal();

                    Toast.makeText(EditarAnimalActivity.this,
                         "Alteração feitaa com sucesso!",
                         Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditarAnimalActivity.this,PerfilPassageiroActivity.class));
                    finish();

                }
            });
         }
             else if((!porteAnimal.equals(porteAnimalEdit)) || (!nomeAnimal.equals(nomeAnimalEdit))){
                Animal animal = new Animal();

                animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
                animal.setIdAnimal(idAnimal);
                animal.setNomeAnimal(nomeAnimalEdit);
                animal.setEspecieAnimal(especieAnimal);
                animal.setRacaAnimal(racaAnimal);
                animal.setPorteAnimal(porteAnimalEdit);
                animal.setFotoAnimal(fotoAnimalUrl);
                animal.salvarAnimal();

                Toast.makeText(EditarAnimalActivity.this,
                     "Alteração feita com sucesso!",
                     Toast.LENGTH_SHORT).show();
             startActivity(new Intent(EditarAnimalActivity.this,PerfilPassageiroActivity.class));
             finish();

             }
     }
     public void excluirAnimal(View view){
        // Caixa de diálogo
         AlertDialog.Builder msgBox = new AlertDialog.Builder(EditarAnimalActivity.this);
         msgBox.setTitle("Excluindo...");
         msgBox.setIcon(R.drawable.ic_lixeira_24dp);
         msgBox.setMessage("Tem certeza que deseja excluir este animal?");
         // Evento de clique do botão sim
         msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

                 // Recuperando o caminho da foto no storage de acordo com animal escolhido
                 StorageReference imagemReferencia = storageReference.child(
                         "animais/"+emailUsuario+"/"+idAnimal+"/"+idAnimal+"."+".FOTO.PERFIL.JPEG");

                 // Caso consiga exccluir, executa
                 imagemReferencia.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         // Exclui o nó do animal do database também
                         databaseReference.child("animais")
                                 .child(UsuarioFirebase.getIdentificadorUsuario())
                                 .child(idAnimal).removeValue();

                         Toast.makeText(EditarAnimalActivity.this,
                                 "Sucesso ao remover Animal!",
                                 Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(EditarAnimalActivity.this,PerfilPassageiroActivity.class));

                     }
                     // Caso de erro
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(EditarAnimalActivity.this,
                                 "Erro ao remover Animal!",
                                 Toast.LENGTH_SHORT).show();
                     }
                 });
             }
         });
         msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
         msgBox.show();
     }
}

