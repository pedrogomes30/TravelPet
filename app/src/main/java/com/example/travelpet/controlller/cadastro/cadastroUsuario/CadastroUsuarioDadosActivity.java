package com.example.travelpet.controlller.cadastro.cadastroUsuario;

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
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.domain.Util;
import com.example.travelpet.helper.MascaraCampos;
import com.example.travelpet.helper.ValidaCpf;
import com.example.travelpet.helper.VerificaCampo;
import com.example.travelpet.model.DonoAnimal;
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

        donoAnimal = new DonoAnimal();
        motorista = new Motorista();
        endereco = new Endereco();

        // Recuperando dados da Activity (CadastroTipoUsuario)
        Intent intent = getIntent();
        usuario = intent.getParcelableExtra("usuario");
        tipoUsuario = usuario.getTipoUsuario();
        fluxoDados  =   "cadastroUsuarioDados";

        // campoTextView.requestFocus();
        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);
        campoTelefone   =   findViewById(R.id.editTelefone);
        campoCpf        =   findViewById(R.id.editCpf);
        campoCep        =   findViewById(R.id.editCep);
        campoCep.addTextChangedListener( new CepListener( this ) ); // Listener ouvinte
        campoLogradouro =   findViewById(R.id.editLogradouro); // Rua
        campoBairro     =   findViewById(R.id.editBairro);
        campoLocalidade =   findViewById(R.id.editLocalidade); // Cidade
        campoUf         =   findViewById(R.id.editUf); //Estado


        MascaraCampos.mascaraTelefone(campoTelefone);
        MascaraCampos.mascaraCpf(campoCpf);
        MascaraCampos.mascaraCep(campoCep);

        // Entidade que vai permitir o travamento das views
        util = new Util(this,
                R.id.editCep,
                R.id.editLogradouro,
                R.id.editBairro,
                R.id.editLocalidade,
                R.id.editUf);

    }

    // Metodo para retornar a url
    public String getUriZipCode(){
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

    public void botaoProximo(View view) {

        recuperarDadosDigitados();
        //Verificando se não estiver vazio
        if (validarCampos()) {

            endereco.setCep(cep);
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setLocalidade(localidade);
            endereco.setUf(uf);

            if (tipoUsuario.equals("donoAnimal")) {

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

    public void recuperarDadosDigitados(){

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

    public Boolean validarCampos () {

        Boolean validado = false;

        ValidaCpf validaCpf = new ValidaCpf(cpf);

        if(!VerificaCampo.isVazio(nome)){
            if(!VerificaCampo.isVazio(sobrenome)){
                if(!VerificaCampo.isVazio(telefone) && telefone.length() == 15){
                    if(validaCpf.isCPF()) {
                        if(!VerificaCampo.isVazio(cep) && cep.length() == 9) {
                            if(!VerificaCampo.isVazio(logradouro)) {
                                if(!VerificaCampo.isVazio(bairro)) {
                                    if(!VerificaCampo.isVazio(localidade)) {
                                        if(validarUf(uf)){

                                            validado = true;

                                        } else { ToastIt("Preencha o UF corretamente"); }
                                    } else { ToastIt("Preencha a Cidade"); }
                                } else { ToastIt("Preencha o Bairro"); }
                            } else { ToastIt("Preencha o Logradouro"); }
                        } else { ToastIt("Preencha o CEP corretamente"); }
                    } else { ToastIt("Preencha o CPF corretamente"); }
                } else { ToastIt("Preencha o Telefone corretamente"); }
            } else { ToastIt("Preencha o Sobrenome"); }
        } else { ToastIt("Preencha o Nome"); }
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

    public void ToastIt (String mensagem) {
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}

