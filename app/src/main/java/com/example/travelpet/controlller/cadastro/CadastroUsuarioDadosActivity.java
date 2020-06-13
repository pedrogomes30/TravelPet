package com.example.travelpet.controlller.cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.cadastro.cadastroDonoAnimal.CadastroAnimalNomeActivity;
import com.example.travelpet.controlller.cadastro.cadastroMotorista.CadastroMotoristaTermoActivity;
import com.example.travelpet.domain.CepListener;
import com.example.travelpet.domain.Util;
import com.example.travelpet.helper.MascaraCampos;
import com.example.travelpet.helper.ValidaCpf;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroUsuarioDadosActivity extends AppCompatActivity {


    private Usuario usuario;
    private DonoAnimal donoAnimal;
    private Motorista motorista;
    private Endereco endereco;
    private Util util;

    private String tipoUsuario,nome, sobrenome, telefone, cpf, fluxoDados,
                   cep, logradouro, bairro, localidade, uf;

    private TextInputEditText campoNome,campoSobrenome, campoTelefone, campoCpf,campoCep,
                              campoLogradouro, campoBairro, campoLocalidade, campoUf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados);
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        iniciarComponentes();
        getDadosTelaAnterior(); // CadastroUsuarioTipo
        MascaraCampos.mascaraTelefone(campoTelefone);
        MascaraCampos.mascaraCpf(campoCpf);
        MascaraCampos.mascaraCep(campoCep);
        campoCep.addTextChangedListener( new CepListener( this ) );

    }

    public void botaoProximo(View view) {

        getDadosDigitados();
        if (validarDados()) {

            endereco.setCep(cep);
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setLocalidade(localidade);
            endereco.setUf(uf);

            if (tipoUsuario.equals("donoAnimal")) {
                fluxoDados  =   "cadastroUsuarioDados";
                donoAnimal.setTipoUsuario(tipoUsuario);
                donoAnimal.setNome(nome);
                donoAnimal.setSobrenome(sobrenome);
                donoAnimal.setTelefone(telefone);
                donoAnimal.setCpf(cpf);
                donoAnimal.setFluxoDados(fluxoDados);

                Intent intent = new Intent(this, CadastroAnimalNomeActivity.class);
                intent.putExtra("donoAnimal", donoAnimal);
                intent.putExtra("endereco", endereco);
                startActivity(intent);

            }else if (tipoUsuario.equals("motorista")) {

                motorista.setTipoUsuario(tipoUsuario);
                motorista.setNome(nome);
                motorista.setSobrenome(sobrenome);
                motorista.setTelefone(telefone);
                motorista.setCpf(cpf);

                Intent intent = new Intent(this, CadastroMotoristaTermoActivity.class);
                intent.putExtra("motorista", motorista);
                intent.putExtra("endereco", endereco);
                startActivity(intent);
            }
        }
    }

    public void iniciarComponentes(){

        donoAnimal = new DonoAnimal();
        motorista = new Motorista();
        endereco = new Endereco();
        // Entidade que vai permitir o travamento das views
        util = new Util(this,
                R.id.editCep,
                R.id.editLogradouro,
                R.id.editBairro,
                R.id.editLocalidade,
                R.id.editUf);

        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);
        campoTelefone   =   findViewById(R.id.editTelefone);
        campoCpf        =   findViewById(R.id.editCpf);
        campoCep        =   findViewById(R.id.editCep);
        campoLogradouro =   findViewById(R.id.editLogradouro); // Rua
        campoBairro     =   findViewById(R.id.editBairro);
        campoLocalidade =   findViewById(R.id.editLocalidade); // Cidade
        campoUf         =   findViewById(R.id.editUf); //Estado
    }

    public void getDadosTelaAnterior (){
        Intent intent = getIntent();
        usuario = intent.getParcelableExtra("usuario");
        tipoUsuario = usuario.getTipoUsuario();
    }

    // Metodo para retornar a url
    public String getUriCep(){
        return "https://viacep.com.br/ws/"+campoCep.getText().toString()
                .replace("-", "")+"/json/";
    }

    // metodo que permite o acesso ao util ali encima
    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }

    // Mapeamento
    public void setDataViews(Endereco endereco){

        setField( R.id.editLogradouro, endereco.getLogradouro() );
        setField( R.id.editBairro, endereco.getBairro() );
        setField( R.id.editLocalidade, endereco.getLocalidade() );
        setField( R.id.editUf, endereco.getUf() );;
    }

    private void setField( int id, String data ) {
        ((EditText) findViewById(id)).setText( data );
    }

    public void getDadosDigitados(){

        nome        =   campoNome.getText().toString().toUpperCase();
        sobrenome   =   campoSobrenome.getText().toString().toUpperCase();
        telefone    =   campoTelefone.getText().toString();
        cpf         =   campoCpf.getText().toString();
        cep         =   campoCep.getText().toString();
        logradouro  =   campoLogradouro.getText().toString();
        bairro      =   campoBairro.getText().toString();
        localidade  =   campoLocalidade.getText().toString();
        uf          =   campoUf.getText().toString().toUpperCase();
    }

    public Boolean validarDados() {

        Boolean validado = false;

        ValidaCpf validaCpf = new ValidaCpf(cpf);

        if(!VerificaDado.isVazio(nome)){
            if(!VerificaDado.isVazio(sobrenome)){
                if(!VerificaDado.isVazio(telefone) && telefone.length() == 15){
                    if(validaCpf.isCPF()) {
                        if(!VerificaDado.isVazio(cep) && cep.length() == 9) {
                            if(!VerificaDado.isVazio(logradouro)) {
                                if(!VerificaDado.isVazio(bairro)) {
                                    if(!VerificaDado.isVazio(localidade)) {
                                        if(validarUf(uf)){

                                            validado = true;

                                        } else { toastIt("Preencha o UF corretamente"); }
                                    } else { toastIt("Preencha a Cidade"); }
                                } else { toastIt("Preencha o Bairro"); }
                            } else { toastIt("Preencha o Logradouro"); }
                        } else { toastIt("Preencha o CEP corretamente"); }
                    } else { toastIt("Preencha o CPF corretamente"); }
                } else { toastIt("Preencha o Telefone corretamente"); }
            } else { toastIt("Preencha o Sobrenome"); }
        } else { toastIt("Preencha o Nome"); }
        return validado;
    }

    public boolean validarUf(String uf){
        Boolean validado = false;
        String[] estados = getResources().getStringArray(R.array.states);
        for( int i = 0; i < estados.length; i++ ) {
            if (uf.equals(estados[i]) ) {
                validado = true;
                //break;
            }
        }
        return validado;
    }

    public void toastIt (String mensagem) {
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}

