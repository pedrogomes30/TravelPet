package com.example.travelpet.controlller.cadastro.cadastroDonoAnimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.adapter.AdapterSpinnerEspecie;
import com.example.travelpet.adapter.ItemSpinnerEspecie;
import com.example.travelpet.dao.TipoAnimalDAO;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.Animal;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Endereco;

public class CadastroAnimalEspecieRacaActivity<escolha> extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private Animal animal;

    // Spinner
    private Spinner spinnerEspecieAnimal;
    int width = 150;
    private String especieAnimal;

    // AutoComplete
    private String racaAnimal;
    private AutoCompleteTextView campoRacaAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal_especie_raca);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); //CadastroAnimalNomeActivity
        setDadosSpinnerEspecie();

    }

    public void botaoProximo(View view) {

        getDadosDigitados();

        if (validarDados()) {

                animal.setEspecieAnimal(especieAnimal);
                animal.setRacaAnimal(racaAnimal);
                Intent intent = new Intent(this, CadastroAnimalPorteActivity.class);
                intent.putExtra("donoAnimal", donoAnimal);
                intent.putExtra("endereco", endereco);
                intent.putExtra("animal", animal);
                startActivity(intent);
        }
    }

    public void iniciarComponentes(){
        spinnerEspecieAnimal = findViewById(R.id.spinnerEspecieAnimal);
        campoRacaAnimal = findViewById(R.id.autoCompleteRacaAnimal);
    }

    public void getDadosTelaAnterior(){
        Intent intent = getIntent();
        donoAnimal = intent.getParcelableExtra("donoAnimal");
        endereco = intent.getParcelableExtra("endereco");
        animal = intent.getParcelableExtra("animal");
    }

    public void setDadosSpinnerEspecie(){
        AdapterSpinnerEspecie adapter = new AdapterSpinnerEspecie(this, TipoAnimalDAO.getListaEspecie() );
        if (spinnerEspecieAnimal != null) {
            spinnerEspecieAnimal.setAdapter(adapter);
            spinnerEspecieAnimal.setOnItemSelectedListener(this);
        }
    }

    // Método para receber o item escolhido no Spinner
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        try {
            LinearLayout linearLayout = findViewById(R.id.customSpinnerItemLayout);
            width = linearLayout.getWidth();
        } catch (Exception e) {
        }

        ItemSpinnerEspecie item = (ItemSpinnerEspecie) adapterView.getSelectedItem();
        especieAnimal = item.getSpinnerItemName().toLowerCase();

        //listarRacas(especieAnimal);
        //listaRacaAnimal.clear();
        ArrayAdapter<String> adapterRaca = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                TipoAnimalDAO.getListaRaca(especieAnimal));

        campoRacaAnimal.setAdapter(adapterRaca);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void getDadosDigitados(){
        racaAnimal = campoRacaAnimal.getText().toString();
    }

    public Boolean validarDados () {

        Boolean validado = false;

        if(!especieAnimal.equals("espécie")){
            if(!VerificaDado.isVazio(racaAnimal)){

                validado = true;

            }else{
                Mensagem.toastIt("Digite a raça do seu animal", this);
            }
        }else{
            Mensagem.toastIt("Selecione a Espécie do seu animal", this);
        }
        return validado;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}
