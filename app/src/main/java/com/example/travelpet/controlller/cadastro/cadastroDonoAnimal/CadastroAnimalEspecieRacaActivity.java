package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.adapter.CustomAdapter;
import com.example.travelpet.adapter.CustomItem;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.RacaAnimal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CadastroAnimalEspecieRacaActivity<escolha> extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    // Spinner
    private Spinner spinnerEspecieAnimal;
    ArrayList<CustomItem> customList;
    int width = 150;
    private String especieAnimal;

    // AutoComplete
    private String racaAnimal;
    private AutoCompleteTextView campoRacaAnimal;
    ArrayList<String> listaRacaAnimal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_especie_raca);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados passados da Activity <CadastroNomeAnimal
        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");

        // referenciando spinner, do xml
        spinnerEspecieAnimal = findViewById(R.id.spinnerEspecieAnimal);
        customList = getCustomList();
        CustomAdapter adapter = new CustomAdapter(this, customList);

        if (spinnerEspecieAnimal != null) {
            spinnerEspecieAnimal.setAdapter(adapter);
            spinnerEspecieAnimal.setOnItemSelectedListener(this);
        }

        // referenciando campo AutoComplete, do xml
        campoRacaAnimal = findViewById(R.id.autoCompleteRacaAnimal);

    }

    //          Spinner
    private ArrayList<CustomItem> getCustomList() {

        customList = new ArrayList<>();
        customList.add(new CustomItem("Espécie", R.drawable.ic_especie_spinner));
        customList.add(new CustomItem("Ave", R.drawable.ic_ave_spinner));
        customList.add(new CustomItem("Cachorro", R.drawable.ic_cachorro_spinner));
        customList.add(new CustomItem("Gato", R.drawable.ic_gato_spinner));
        customList.add(new CustomItem("Réptil", R.drawable.ic_reptil_spinner));
        customList.add(new CustomItem("Roedor", R.drawable.ic_roedor_spinner));
        return customList;
    }
    // Método para receber o item escolhido no Spinner
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        try {
            LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
            width = linearLayout.getWidth();
        } catch (Exception e) {
        }

        CustomItem item = (CustomItem) adapterView.getSelectedItem();
        especieAnimal = item.getSpinnerItemName().toLowerCase();

        if(especieAnimal.equals("réptil")){
            especieAnimal = "reptil";
        }

        listarRacas(especieAnimal);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void listarRacas(String lr) {

        // Limpa a lista
        listaRacaAnimal.clear();

        //              Colocando os nomes das raças do banco de dados em uma lista de acordo com a especia escolhida
        DatabaseReference especieRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                                        .child("racaAnimal")
                                        .child(especieAnimal);

        especieRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Pegando os dados do BD através da classe RacaAnimal
                RacaAnimal racasAnimais = dataSnapshot.getValue(RacaAnimal.class);
                // Add cada nome das raças na lista (listaRacaAnimal
                listaRacaAnimal.add(racasAnimais.getNomeRacaAnimal());

            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved( DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled( DatabaseError databaseError) {}
        });
        // Montando Array com a lista de raça do BD
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaRacaAnimal);
        campoRacaAnimal.setAdapter(adapterRaca);
    }

    public void botaoProximo(View view){

        racaAnimal = campoRacaAnimal.getText().toString();

        if(!especieAnimal.equals("espécie")){

            if(!racaAnimal.isEmpty()){
                if(especieAnimal.equals("reptil")){
                    especieAnimal = "réptil";
                }

                animal.setEspecieAnimal(especieAnimal);
                animal.setRacaAnimal(racaAnimal);

                Intent intent = new Intent(CadastroAnimalEspecieRacaActivity.this, CadastroAnimalPorteActivity.class);
                intent.putExtra ("donoAnimal",donoAnimal);
                intent.putExtra ("endereco",endereco);
                intent.putExtra ("animal",animal);
                startActivity(intent);

            }else{
                Toast.makeText(CadastroAnimalEspecieRacaActivity.this,
                        "Digite a raça do seu animal",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroAnimalEspecieRacaActivity.this,
                    "Selecione a Espécie do seu animal",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
