package com.example.travelpet.telasPerfil.passageiro.ui.meusAnimais;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.activity.MainActivity;
import com.example.travelpet.adapter.CustomAdapterEditarAnimal;
import com.example.travelpet.adapter.CustomItem;
import com.example.travelpet.classes.Animal;
import com.example.travelpet.classes.RacaAnimal;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.example.travelpet.config.UsuarioFirebase;
import com.example.travelpet.telasPerfil.passageiro.PerfilPassageiroActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarAnimalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Puxar dados dados Firebase
    private String emailUsuario;
    private String idAnimal,fotoAnimalUrl, nomeAnimal, especieAnimal,
            racaAnimal, porteAnimal, observacaoAnimal;

    // Váriaveis usados na imagem do animal
    private CircleImageView imageViewFotoAnimal;
    private String fotoAnimalUri;
    // Váriavel para verificação, se a foto do Animal foi alterada
    private Bitmap imagem = null;
    private byte[] fotoAnimal;
    private StorageReference storageReference;
    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    // Nome Animal
    private EditText editTextNomeAnimal;
    private String nomeAnimalEdit;

    // Spinner Especie
    private Spinner spinnerEspecielAnimal;
    private String especieAnimalEdit;
    ArrayList<CustomItem> customList;

    // Raça Animal
    private AutoCompleteTextView autoCompleteRacaAnimal;
    private  String racaAnimalEdit;
    ArrayList<String> listaRacaAnimal = new ArrayList<>();

    // Spinner Porte
    private Spinner spinnerPorteAnimal;
    private String porteAnimalEdit;
    private List<String> listaPorteAnimal = new ArrayList<String>();

    // Observação Animal
    private EditText editTextObservacaoAnimal;
    private String observacaoAnimalEdit;

    private Animal animalDestinatario;

    // Variáveis para recolher dados editados

    // Variável utilizada no processo de excluir Animal
    // Variável utilizada no processo de excluir Animal
    DatabaseReference databaseReference;
    private DatabaseReference  databaseReferenceListaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_animal);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurações iniciais
        imageViewFotoAnimal         =   findViewById(R.id.imageViewFotoAnimal);
        editTextNomeAnimal          =   findViewById(R.id.editTextNomeAnimal);
        spinnerEspecielAnimal       =   findViewById(R.id.spinnerEspecieAnimal);
        autoCompleteRacaAnimal      =   findViewById(R.id.autoCompleteRacaAnimal);
        spinnerPorteAnimal          =   findViewById(R.id.spinnerPorteAnimal);
        editTextObservacaoAnimal    =   findViewById(R.id.editTextObservacaoAnimal);



        // Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            animalDestinatario = (Animal) bundle.getParcelable("animalSelecionado");

            // Colocando dados na variaveis para poder salvar no firebase as alterações
            idAnimal = animalDestinatario.getIdAnimal();
            fotoAnimalUrl = animalDestinatario.getFotoAnimal();
            nomeAnimal = animalDestinatario.getNomeAnimal();
            especieAnimal = animalDestinatario.getEspecieAnimal();
            racaAnimal = animalDestinatario.getRacaAnimal();
            porteAnimal = animalDestinatario.getPorteAnimal();
            observacaoAnimal = animalDestinatario.getObservacaoAnimal();


            // Para comparação na hora de salvar
            nomeAnimalEdit = nomeAnimal;
            especieAnimalEdit = especieAnimal;
            racaAnimalEdit = racaAnimal;
            observacaoAnimalEdit = observacaoAnimal;

            //              Enviando os dados recebidos para o XML

            // Pegando Foto do Animal do BD e colocando no xml
            if(fotoAnimalUrl != null){
                Uri url = Uri.parse(fotoAnimalUrl);
                Glide.with(EditarAnimalActivity.this)
                        .load(url)
                        .into(imageViewFotoAnimal);
            }else{
                imageViewFotoAnimal.setImageResource(R.drawable.iconperfilanimal);
            }

            // Envia nomeAnimal, para exibir no arquivo XML
            editTextNomeAnimal.setText(nomeAnimal);

            //Montando o arrayList SpinnerEspecieAnimal, para colocar no XML
            customList = getCustomList(especieAnimalEdit);
            CustomAdapterEditarAnimal adapter = new CustomAdapterEditarAnimal(this, customList);
            if (spinnerEspecielAnimal != null) {
                spinnerEspecielAnimal.setAdapter(adapter);
                spinnerEspecielAnimal.setOnItemSelectedListener(this);
            }

            // Envia o tipo da racaAnimal, para o XML
            autoCompleteRacaAnimal.setText(racaAnimal);

            // Inserindo dados ao array do Spinner, baseado no porte recebido do database
            // e colocando no XML
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
            ArrayAdapter<String> adapterPorte = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaPorteAnimal);
            spinnerPorteAnimal.setAdapter(adapterPorte);

            //Envia observacaoAnimal para exibir no arquivo XML
            editTextObservacaoAnimal.setText(observacaoAnimal);

        }

        //              MÉTODOS OUVINTES

        // Método Ouvinte editTextNomeAnimal
        editTextNomeAnimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override //executa uma determinada ação durante a modificação do editText
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                nomeAnimalEdit = editTextNomeAnimal.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Método ouvinte autoCompleteRacaAnimal
        autoCompleteRacaAnimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //executa uma determinada ação durante a modificação do editText
                racaAnimalEdit = autoCompleteRacaAnimal.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Método Ouvinte porteAnimal (Spinner)
        AdapterView.OnItemSelectedListener escolhaPorte = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Armazena a mudança do porte do animal
                porteAnimalEdit = spinnerPorteAnimal.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spinnerPorteAnimal.setOnItemSelectedListener(escolhaPorte);

        // Método Ouvinte editTextNomeAnimal
        editTextObservacaoAnimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override //executa uma determinada ação durante a modificação do editText
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                observacaoAnimalEdit = editTextObservacaoAnimal.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
    // Evento do clique do botão galeria
    public void buttonGaleriaAnimal(View view) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_GALERIA);
        }
    }
    // Método para pegar a foto da Câmera ou Galeria
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

                if (imagem != null) {
                    imageViewFotoAnimal.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    fotoAnimal = baos.toByteArray();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Método para criar a lista SpinnerEspecieAnimal
    private ArrayList<CustomItem> getCustomList(String ea) {

        customList = new ArrayList<>();

        if(especieAnimalEdit.equals("ave")){
            customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
            customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
            customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
            customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));
            customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));

        }else if(especieAnimalEdit.equals("cachorro")){
            customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
            customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
            customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
            customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));
            customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));

        }else if(especieAnimalEdit.equals("gato")){
            customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
            customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
            customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
            customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));
            customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));

        }else if(especieAnimalEdit.equals("réptil")){
            customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));
            customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
            customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
            customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
            customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));

        }else if(especieAnimalEdit.equals("roedor")){
            customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));
            customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
            customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
            customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
            customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));

        }
        return customList;

    }
    // Método para ouvir o SpinnerEspecieAnimal
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
            //width = linearLayout.getWidth();
        }catch (Exception e) {
        }
        //customSpinner.setDropDownWidth(width);
        CustomItem item = (CustomItem) adapterView.getSelectedItem();
        especieAnimalEdit = item.getSpinnerItemName().toLowerCase();

        if(especieAnimalEdit.equals("réptil")){
            especieAnimalEdit = "reptil";
        }

        listarRacas(especieAnimalEdit);

        if(especieAnimalEdit.equals("reptil")){
            especieAnimalEdit = "réptil";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Método Listar campoRaçaAnimal autoComplete depedendo da especie do SpinnerEsopecieAnimal
    public  void listarRacas(String lr) {

        // Limpa a lista
        listaRacaAnimal.clear();

        //              Colocando os nomes das raças do banco de dados em uma lista
        databaseReferenceListaAnimal = ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("racaAnimal").child(especieAnimalEdit);
        databaseReferenceListaAnimal.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Pegando os dados do BD através da classe RacaAnimal
                RacaAnimal racasAnimais = dataSnapshot.getValue(RacaAnimal.class);
                // Add cada nome das raças na lista (listaRacaAnimal
                listaRacaAnimal.add(racasAnimais.getNomeRacaAnimal());

            }

            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }


            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot,  String s) {

            }

            public void onCancelled( DatabaseError databaseError) {

            }
        });
        // Montando Array com a lista de raça do BD
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaRacaAnimal);
        autoCompleteRacaAnimal.setAdapter(adapterRaca);
    }

    // Método onClick do botão Salvar
    public void salvarDadosAnimal(View view){
        // Salvar imagem no firebase
        if(imagem != null ){

            StorageReference imagemRef = storageReference
                    .child("animais")
                    .child(emailUsuario)
                    .child(idAnimal)
                    .child(idAnimal+".FOTO.PERFIL.JPEG");

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
                    enviarDadosAnimalDatabase();

                }
            });
        }
        else if((!nomeAnimal.equals(nomeAnimalEdit)) || (!especieAnimal.equals(especieAnimalEdit)) ||
                (!racaAnimal.equals(racaAnimalEdit)) || (!porteAnimal.equals(porteAnimalEdit)) ||
                (!observacaoAnimal.equals(observacaoAnimalEdit))){

            enviarDadosAnimalDatabase();
        }
    }
    // Método para auxiliar no botão Salvar
    public void enviarDadosAnimalDatabase (){
        Animal animal = new Animal();

        animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
        animal.setIdAnimal(idAnimal);
        animal.setNomeAnimal(nomeAnimalEdit);
        animal.setEspecieAnimal(especieAnimalEdit);
        animal.setRacaAnimal(racaAnimalEdit);
        animal.setPorteAnimal(porteAnimalEdit);
        animal.setFotoAnimal(fotoAnimalUrl);
        animal.setObservacaoAnimal(observacaoAnimalEdit);
        animal.salvarAnimal();

        Toast.makeText(EditarAnimalActivity.this,
                "Alteração feita com sucesso!",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditarAnimalActivity.this, PerfilPassageiroActivity.class));
        finish();
    }

    // Método onClick do botão Excluir
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
                        "animais/"+emailUsuario+"/"+idAnimal+"/"+idAnimal+".FOTO.PERFIL.JPEG");

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
                        startActivity(new Intent(EditarAnimalActivity.this, PerfilPassageiroActivity.class));

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }



}

