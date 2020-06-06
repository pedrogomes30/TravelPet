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
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.helper.VerificaCampo;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.TipoAnimal;

import java.util.ArrayList;

public class CadastroAnimalEspecieRacaActivity<escolha> extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    // Spinner
    private Spinner spinnerEspecieAnimal;
    ArrayList<CustomItem> listaEspecieAnimal =new ArrayList<>();;
    int width = 150;
    private String especieAnimal;

    // AutoComplete
    private String racaAnimal;
    private AutoCompleteTextView campoRacaAnimal;
    ArrayList<String> listaRacaAnimal= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_especie_raca);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");

        spinnerEspecieAnimal = findViewById(R.id.spinnerEspecieAnimal);
        CustomAdapter adapter = new CustomAdapter(this,TipoAnimal.getListaEspecie() );

        if (spinnerEspecieAnimal != null) {
            spinnerEspecieAnimal.setAdapter(adapter);
            spinnerEspecieAnimal.setOnItemSelectedListener(this);
        }

        campoRacaAnimal = findViewById(R.id.autoCompleteRacaAnimal);

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

        //listarRacas(especieAnimal);
        //listaRacaAnimal.clear();
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                TipoAnimal.getListaRaca(especieAnimal));

        campoRacaAnimal.setAdapter(adapterRaca);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void botaoProximo(View view){

        racaAnimal = campoRacaAnimal.getText().toString();

        if(!especieAnimal.equals("espécie")){

            if(!VerificaCampo.isVazio(racaAnimal)){

                animal.setEspecieAnimal(especieAnimal);
                animal.setRacaAnimal(racaAnimal);
                Intent intent = new Intent(this, CadastroAnimalPorteActivity.class);
                intent.putExtra ("donoAnimal",donoAnimal);
                intent.putExtra ("endereco",endereco);
                intent.putExtra ("animal",animal);
                startActivity(intent);

            }else{
                Toast.makeText(this,
                        "Digite a raça do seu animal",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,
                    "Selecione a Espécie do seu animal",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
