package com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais;

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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.adapter.CustomAdapterEditarAnimal;
import com.example.travelpet.adapter.CustomItem;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.RacaAnimal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

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
    DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceListaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_animal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Animal");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recupera referência do database
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        // Recupera a referência do Storage
        storageReference    =   ConfiguracaoFirebase.getFirebaseStorage();

        emailUsuario        =   UsuarioFirebase.getEmailUsuario();

        // Relacionando os componentes do xml
        imageViewFotoAnimal         =   findViewById(R.id.imageViewFotoAnimal);
        editTextNomeAnimal          =   findViewById(R.id.editTextNomeAnimal);
        spinnerEspecielAnimal       =   findViewById(R.id.spinnerEspecieAnimal);
        autoCompleteRacaAnimal      =   findViewById(R.id.autoCompleteRacaAnimal);
        spinnerPorteAnimal          =   findViewById(R.id.spinnerPorteAnimal);
        editTextObservacaoAnimal    =   findViewById(R.id.editTextObservacaoAnimal);


        // Recuperar dados do animalDestinatario / escolhido
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            animalDestinatario = (Animal) bundle.getParcelable("animalSelecionado");

            // Recebendo os dados do animal escolhido na ListaAnimaisFragmet
            idAnimal = animalDestinatario.getIdAnimal();
            fotoAnimalUrl = animalDestinatario.getFotoAnimal();
            nomeAnimal = animalDestinatario.getNomeAnimal();
            especieAnimal = animalDestinatario.getEspecieAnimal();
            racaAnimal = animalDestinatario.getRacaAnimal();
            porteAnimal = animalDestinatario.getPorteAnimal();
            observacaoAnimal = animalDestinatario.getObservacaoAnimal();

            // Variavel para comparação, para saber se ouve alteração
            nomeAnimalEdit = nomeAnimal;
            especieAnimalEdit = especieAnimal;
            racaAnimalEdit = racaAnimal;
            observacaoAnimalEdit = observacaoAnimal;

            //              Enviando os dados recebidos para o XML

            // Envia a foto do animal para o XML
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

            //executa uma determinada ação durante a modificação do editText
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

        // Método Ouvinte editTextObservacaoAnimal
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
                // Recuperando os dados do BD através da classe RacaAnimal
                RacaAnimal racasAnimais = dataSnapshot.getValue(RacaAnimal.class);
                // Add cada nome das raças na lista (listaRacaAnimal)
                listaRacaAnimal.add(racasAnimais.getNomeRacaAnimal());

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            public void onCancelled( DatabaseError databaseError) {

            }
        });
        // Montando Array com a lista de raça do BD
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaRacaAnimal);
        autoCompleteRacaAnimal.setAdapter(adapterRaca);
    }

    public void botaoSalvarAnimal(View view){

        if(imagem != null ){
            String localSalvamentoAnimal = "EditarAnimalActivity";
            // Método salva a imagem no Storage, depois salva os dados no DataBase
            Animal.salvarAnimalStorage(emailUsuario,idAnimal,nomeAnimalEdit,especieAnimalEdit,racaAnimalEdit,
                    porteAnimalEdit, observacaoAnimalEdit,fotoAnimal,
         EditarAnimalActivity.this,PerfilPassageiroActivity.class,localSalvamentoAnimal);

        }else if((!nomeAnimal.equals(nomeAnimalEdit)) || (!especieAnimal.equals(especieAnimalEdit)) ||
                (!racaAnimal.equals(racaAnimalEdit)) || (!porteAnimal.equals(porteAnimalEdit)) ||
                (!observacaoAnimal.equals(observacaoAnimalEdit))){

            String localSalvamentoAnimal = "EditarAnimalActivity";
            Animal animal = new Animal();
            animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
            animal.setIdAnimal(idAnimal);
            animal.setNomeAnimal(nomeAnimalEdit);
            animal.setEspecieAnimal(especieAnimalEdit);
            animal.setRacaAnimal(racaAnimalEdit);
            animal.setPorteAnimal(porteAnimalEdit);
            animal.setObservacaoAnimal(observacaoAnimalEdit);
            animal.setFotoAnimal(fotoAnimalUrl);
            animal.salvarAnimalDatabase(EditarAnimalActivity.this, localSalvamentoAnimal);

            super.finish();
            overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
        }
    }

    public void botaoExcluirAnimal(View view){

        AlertDialog.Builder msgBox = new AlertDialog.Builder(EditarAnimalActivity.this);
        msgBox.setTitle("Excluindo...");
        msgBox.setIcon(R.drawable.ic_lixeira_24dp);
        msgBox.setMessage("Tem certeza que deseja excluir este animal?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Chama Método excluirAnimal da classe Animal
                Animal.excluirAnimal(emailUsuario, idAnimal, EditarAnimalActivity.this);
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

