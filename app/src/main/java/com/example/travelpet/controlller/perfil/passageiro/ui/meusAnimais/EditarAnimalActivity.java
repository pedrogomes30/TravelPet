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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.adapter.CustomAdapterEditarAnimal;
import com.example.travelpet.adapter.CustomItem;
import com.example.travelpet.controlller.perfil.passageiro.PerfilPassageiroActivity;
import com.example.travelpet.dao.AnimalDAO;
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

    private Animal animal;
    private AnimalDAO animalDAO;

    private String nomeAnimal, especieAnimal, racaAnimal,
                   porteAnimal, observacaoAnimal, fotoAnimalUrl;

    private String nomeAnimalEdit, especieAnimalEdit, racaAnimalEdit,
                   porteAnimalEdit, observacaoAnimalEdit;

    // Foto Animal
    private CircleImageView campoFotoAnimal;
    private Bitmap imagem = null;
    private byte[] fotoAnimal;
    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;


    private EditText campotNomeAnimal, campotObservacaoAnimal;

    // Spinner Especie
    private Spinner campoSpinnerEspecielAnimal;
    ArrayList<CustomItem> customList;

    // Raça Animal
    private AutoCompleteTextView campoAutoCompleteRacaAnimal;
    ArrayList<String> listaRacaAnimal = new ArrayList<>();

    // Spinner Porte
    private Spinner campoSpinnerPorteAnimal;
    private List<String> listaPorteAnimal = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_animal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Animal");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        animalDAO = new AnimalDAO();

        // Relacionando os componentes do xml
        campoFotoAnimal             =   findViewById(R.id.imageViewFotoAnimal);
        campotNomeAnimal            =   findViewById(R.id.editTextNomeAnimal);
        campoSpinnerEspecielAnimal  =   findViewById(R.id.spinnerEspecieAnimal);
        campoAutoCompleteRacaAnimal =   findViewById(R.id.autoCompleteRacaAnimal);
        campoSpinnerPorteAnimal     =   findViewById(R.id.spinnerPorteAnimal);
        campotObservacaoAnimal      =   findViewById(R.id.editTextObservacaoAnimal);


        // Recuperar dados do animalDestinatario / escolhido
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            animal = bundle.getParcelable("animalSelecionado");

            //animalDestinatario = (Animal) bundle.getParcelable("animalSelecionado");

            // Recebendo os dados do animal escolhido na ListaAnimaisFragmet
            nomeAnimal       =  animal.getNomeAnimal();
            especieAnimal    =  animal.getEspecieAnimal();
            racaAnimal       =  animal.getRacaAnimal();
            porteAnimal      =  animal.getPorteAnimal();
            observacaoAnimal =  animal.getObservacaoAnimal();
            fotoAnimalUrl    =  animal.getFotoAnimalUrl();

            // Variavel para comparação, para saber se ouve alteração
            especieAnimalEdit = especieAnimal;

            //              Enviando os dados recebidos para o XML
            // Envia nomeAnimal, para exibir no arquivo XML
            campotNomeAnimal.setText(nomeAnimal);
            // Envia o tipo da racaAnimal, para o XML
            campoAutoCompleteRacaAnimal.setText(racaAnimal);
            //Envia observacaoAnimal para exibir no arquivo XML
            campotObservacaoAnimal.setText(observacaoAnimal);

            // Envia a foto do animal para o XML
            if(fotoAnimalUrl != null){
                Uri fotoAnimalUri = Uri.parse(fotoAnimalUrl);
                Glide.with(EditarAnimalActivity.this)
                        .load(fotoAnimalUri)
                        .into(campoFotoAnimal);
            }else{
                campoFotoAnimal.setImageResource(R.drawable.iconperfilanimal);
            }

            //Montando o arrayList SpinnerEspecieAnimal, para colocar no XML
            customList = getCustomList(especieAnimalEdit);
            CustomAdapterEditarAnimal adapter = new CustomAdapterEditarAnimal(this, customList);
            if (campoSpinnerEspecielAnimal != null) {
                campoSpinnerEspecielAnimal.setAdapter(adapter);
                campoSpinnerEspecielAnimal.setOnItemSelectedListener(this);
            }

            // Inserindo dados ao array do Spinner no XML, baseado no porte recebido do database
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
            campoSpinnerPorteAnimal.setAdapter(adapterPorte);

        }

    }

    // Evento de clique do botão camera
    public void botaoCamera(View view){

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verifica se a intent conseguiu fazer o pedido ( que e abrir a camera)
        if (i.resolveActivity(getPackageManager()) != null) {
            // Captura a foto que foi tirada
            // startActivityForResult = Inicia a activity, e recupera um resultado de retorno que e a foto
            startActivityForResult(i,SELECAO_CAMERA);
        }
    }
    // Evento do clique do botão galeria
    public void botaoGaleria(View view) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_GALERIA);
        }
    }

    public void botaoSalvar(View view){

        nomeAnimalEdit       = campotNomeAnimal.getText().toString();
        racaAnimalEdit       = campoAutoCompleteRacaAnimal.getText().toString();
        porteAnimalEdit      = campoSpinnerPorteAnimal.getSelectedItem().toString();
        observacaoAnimalEdit = campotObservacaoAnimal.getText().toString();

        // tipoLocalSave = 2 - EditarAnimalAcitivity
        int tipoLocalSave = 2;

        if(fotoAnimal != null ){

            animal.setNomeAnimal(nomeAnimalEdit);
            animal.setEspecieAnimal(especieAnimalEdit);
            animal.setRacaAnimal(racaAnimalEdit);
            animal.setPorteAnimal(porteAnimalEdit);
            animal.setObservacaoAnimal(observacaoAnimalEdit);
            animal.setFotoAnimal(fotoAnimal);
            //       Funcionando, mas Ainda em fase de analise
            animalDAO.salvarImagemAnimalStorage(animal, tipoLocalSave,
                                 EditarAnimalActivity.this,
                                             PerfilPassageiroActivity.class);

        }else if((!nomeAnimal.equals(nomeAnimalEdit)) || (!especieAnimal.equals(especieAnimalEdit)) ||
                (!racaAnimal.equals(racaAnimalEdit)) || (!porteAnimal.equals(porteAnimalEdit)) ||
                (!observacaoAnimal.equals(observacaoAnimalEdit))){

            animal.setNomeAnimal(nomeAnimalEdit);
            animal.setEspecieAnimal(especieAnimalEdit);
            animal.setRacaAnimal(racaAnimalEdit);
            animal.setPorteAnimal(porteAnimalEdit);
            animal.setObservacaoAnimal(observacaoAnimalEdit);
            //       Funcionando, mas ainda em fase de analise
            animalDAO.salvarAnimalRealtimeDatabase(animal, tipoLocalSave,
                                       EditarAnimalActivity.this,
                                                   PerfilPassageiroActivity.class);

        }
    }

    public void botaoExcluir(View view){

        animalDAO.contarAnimais();
        AlertDialog.Builder msgBox = new AlertDialog.Builder(EditarAnimalActivity.this);
        msgBox.setTitle("Excluindo...");
        msgBox.setIcon(R.drawable.ic_lixeira_24dp);
        msgBox.setMessage("Tem certeza que deseja excluir este animal?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                animalDAO.excluirAnimal(animal,EditarAnimalActivity.this);

            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        msgBox.show();
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
                    campoFotoAnimal.setImageBitmap(imagem);
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

    // Método pegar o item selecionado no SpinnerEspecieAnimal
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
    public void onNothingSelected(AdapterView<?> adapterView) {}

    // Método Listar campoRaçaAnimal autoComplete depedendo da especie do SpinnerEsopecieAnimal
    public  void listarRacas(String lr) {

        // Limpa a lista
        listaRacaAnimal.clear();

        //              Colocando os nomes das raças do banco de dados em uma lista
        DatabaseReference databaseReferenceListaAnimal = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child("racaAnimal")
                .child(especieAnimalEdit);
        databaseReferenceListaAnimal.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Recuperando os dados do BD através da classe RacaAnimal
                RacaAnimal racasAnimais = dataSnapshot.getValue(RacaAnimal.class);
                // Add cada nome das raças na lista (listaRacaAnimal)
                listaRacaAnimal.add(racasAnimais.getNomeRacaAnimal());
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved( DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled( DatabaseError databaseError) {}
        });
        // Montando Array com a lista de raça do BD
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaRacaAnimal);
        campoAutoCompleteRacaAnimal.setAdapter(adapterRaca);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}

