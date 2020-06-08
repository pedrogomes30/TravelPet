package com.example.travelpet.controlller.perfil.passageiro.ui.meusAnimais;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.dao.AnimalDAO;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.helper.VerificaCampo;
import com.example.travelpet.model.Animal;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarAnimalActivity extends AppCompatActivity{

    private Animal animal;
    private AnimalDAO animalDAO;
    private ProgressDialog progressDialog;

    private String nomeAnimal, especieAnimal, racaAnimal,
                   porteAnimal, observacaoAnimal, fotoAnimalUrl;

    private String porteAnimalEdit, observacaoAnimalEdit;

    private CircleImageView campoFotoAnimal;
    private TextView campoNome, campoEspecie, campoRaca;
    private EditText campotObservacaoAnimal;

    private byte[] fotoAnimal;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;


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
        progressDialog = new ProgressDialog(EditarAnimalActivity.this);

        // Relacionando os componentes do xml
        campoFotoAnimal             =   findViewById(R.id.imageViewFotoAnimal);
        campoNome                   =   findViewById(R.id.textViewNomeAnimal);
        campoEspecie                =   findViewById(R.id.textViewEspecieAnimal);
        campoRaca                   =   findViewById(R.id.textViewRacaAnimal);
        campoSpinnerPorteAnimal     =   findViewById(R.id.spinnerPorteAnimal);
        campotObservacaoAnimal      =   findViewById(R.id.editTextObservacaoAnimal);

        getDadosAnimalSelecionado();
        setDadosAnimalSelecionado();

    }

    public void botaoCamera(View view){

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_CAMERA);
        }
    }

    public void botaoGaleria(View view) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i,SELECAO_GALERIA);
        }
    }

    public void botaoSalvar(View view){

        getDadosEditados();
        salvarAlteracoes();

    }

    public void botaoExcluir(View view){

        animalDAO.contarAnimais();
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarAnimalActivity.this);
        builder.setTitle("Excluindo...");
        builder.setIcon(R.drawable.ic_lixeira_24dp);
        builder.setMessage("Tem certeza que deseja excluir este animal?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                animalDAO.excluirAnimal(animal, EditarAnimalActivity.this);

            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void getDadosAnimalSelecionado(){
    // Recuperar dados do animalDestinatario / escolhido
    Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            animal = bundle.getParcelable("animalSelecionado");

            nomeAnimal = animal.getNomeAnimal();
            especieAnimal = animal.getEspecieAnimal();
            racaAnimal = animal.getRacaAnimal();
            porteAnimal = animal.getPorteAnimal();
            observacaoAnimal = animal.getObservacaoAnimal();
            fotoAnimalUrl = animal.getFotoAnimalUrl();

        }
    }

    public void setDadosAnimalSelecionado(){
        // Envia a foto do animal para o XML
        if (fotoAnimalUrl != null) {
            Uri fotoAnimalUri = Uri.parse(fotoAnimalUrl);
            Glide.with(EditarAnimalActivity.this)
                    .load(fotoAnimalUri)
                    .into(campoFotoAnimal);
        } else {
            campoFotoAnimal.setImageResource(R.drawable.iconperfilanimal);
        }

        campoNome.setText(nomeAnimal);
        campoEspecie.setText(especieAnimal);
        campoRaca.setText(racaAnimal);
        campotObservacaoAnimal.setText(observacaoAnimal);


        // Inserindo dados ao array do Spinner no XML, baseado no porte recebido do database
        if (porteAnimal.equals("Pequeno - Até 35cm")) {
            listaPorteAnimal.add("Pequeno - Até 35cm");
            listaPorteAnimal.add("Médio - De 36 a 49cm");
            listaPorteAnimal.add("Grande - Acima de 50cm");

        } else if (porteAnimal.equals("Médio - De 36 a 49cm")) {
            listaPorteAnimal.add("Médio - De 36 a 49cm");
            listaPorteAnimal.add("Pequeno - Até 35cm");
            listaPorteAnimal.add("Grande - Acima de 50cm");

        } else if (porteAnimal.equals("Grande - Acima de 50cm")) {
            listaPorteAnimal.add("Grande - Acima de 50cm");
            listaPorteAnimal.add("Pequeno - Até 35cm");
            listaPorteAnimal.add("Médio - De 36 a 49cm");

        }
        ArrayAdapter<String> adapterPorte = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaPorteAnimal);
        campoSpinnerPorteAnimal.setAdapter(adapterPorte);
    }

    public void getDadosEditados(){

        porteAnimalEdit      = campoSpinnerPorteAnimal.getSelectedItem().toString();
        observacaoAnimalEdit = campotObservacaoAnimal.getText().toString();
    }

    public void salvarAlteracoes(){
        // tipoLocalSave = 2 - EditarAnimalAcitivity
        int tipoSave = 2;

        if(fotoAnimal != null){

            TelaCarregamento.iniciarCarregamento(progressDialog);

            if(!VerificaCampo.isMesmoValor(porteAnimal, porteAnimalEdit)){
                animal.setPorteAnimal(porteAnimalEdit);
            }
            if(!VerificaCampo.isMesmoValor(observacaoAnimal, observacaoAnimalEdit)){
                animal.setObservacaoAnimal(observacaoAnimalEdit);
            }

            animal.setFotoAnimal(fotoAnimal);
            animalDAO.salvarAnimalStorage(animal, progressDialog, tipoSave,
                    EditarAnimalActivity.this);

        }else if(!VerificaCampo.isMesmoValor(porteAnimal, porteAnimalEdit) ||
                !VerificaCampo.isMesmoValor(observacaoAnimal, observacaoAnimalEdit)){

            TelaCarregamento.iniciarCarregamento(progressDialog);

            animal.setPorteAnimal(porteAnimalEdit);
            animal.setObservacaoAnimal(observacaoAnimalEdit);
            animalDAO.salvarAnimalRealtimeDatabase(animal,progressDialog, tipoSave,
                    EditarAnimalActivity.this);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK) {
            Bitmap imagem = null;
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}

