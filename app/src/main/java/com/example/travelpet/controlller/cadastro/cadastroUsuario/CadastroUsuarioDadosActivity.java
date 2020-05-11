package com.example.travelpet.controlller.cadastro.cadastroUsuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelpet.R;
import com.example.travelpet.controlller.cadastro.cadastroAnimal.CadastroAnimalNomeActivity;
import com.example.travelpet.controlller.cadastro.cadastroMotorista.CadastroMotoristaTermoActivity;
import com.example.travelpet.domain.CepListener;
import com.example.travelpet.domain.Endereco;
import com.example.travelpet.domain.Util;
import com.example.travelpet.model.DonoAnimal;
import com.example.travelpet.model.Motorista;
import com.example.travelpet.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroUsuarioDadosActivity extends AppCompatActivity {


    private String tipoUsuario, nome, sobrenome, telefone, cpf,
            cep, logradouro, bairro, localidade, uf;
    private String fluxoDados;

    // Váriaveis usadas para referênciar dados dos campos do nome e sobrenome do xml
    private TextInputEditText campoNome,campoSobrenome, campoTelefone, campoCpf,
            campoCep, campoLogradouro, campoBairro, campoLocalidade, campoUf;

    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_dados);

        // Efeito de Transição
        // 1ª (R.anim.activity_filho_entrando)= animação que vai executar pra activity que ta estrando
        // 2ª = Animação que vai executar pra activity que tiver saindo
        overridePendingTransition(R.anim.activity_filho_entrando, R.anim.activity_pai_saindo);

        // Recuperando dados da Activity (CadastroTipoUsuario)
        Intent intent = getIntent();
        Usuario usuario = intent.getParcelableExtra("usuario");
        tipoUsuario = usuario.getTipoUsuario();

        // Variável para referênciar fluxo de dados
        fluxoDados = "cadastroUsuario";

        campoNome       =   findViewById(R.id.editNomeUsuario);
        campoSobrenome  =   findViewById(R.id.editSobrenomeUsuario);
        campoTelefone   =   findViewById(R.id.editTelefone);
        campoCpf        =   findViewById(R.id.editCpf);
        campoCep        =   findViewById(R.id.editCep);
        campoCep.addTextChangedListener( new CepListener( this ) ); // Listener ouvinte

        campoLogradouro =   findViewById(R.id.editLogradouro); // Rua
        campoBairro     =   findViewById(R.id.editBairro);
        campoLocalidade =   findViewById(R.id.editLocalidade); // Cidade
        campoUf     =   findViewById(R.id.editUf); //Estado

        // Entidade que vai permitir o travamento das views
        util = new Util(this,
                R.id.editCep,
                R.id.editLogradouro,
                R.id.editBairro,
                R.id.editLocalidade,
                R.id.editUf);

        // Mascara para o usuario so digitar nímero, e só 11 numeros no campoTelefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(campoTelefone, smf);
        campoTelefone.addTextChangedListener(mtw);
    }

    // Metodo para retornar a url
    public String getUriZipCode(){
        return "https://viacep.com.br/ws/"+campoCep.getText()+"/json/";
        //return "https://viacep.com.br/ws/"+etZipCode.getText()+"/json/";
    }

    // metodo que permite o acesso ao util ali encima
    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }

    // Mapeamento
    public void setDataViews(Endereco address){
        setField( R.id.editLogradouro, address.getLogradouro() );
        setField( R.id.editBairro, address.getBairro() );
        setField( R.id.editLocalidade, address.getLocalidade() );
        setField( R.id.editUf, address.getUf() );
        //setSpinner( R.id.sp_state, R.array.states, address.getUf() );
    }

    private void setField( int id, String data ) {
        ((EditText) findViewById(id)).setText( data );
    }

    // Evento de clique do botão PrcoximoNomeUsuario
    public void botaoProximoUsuarioDados(View view){

        // Recuperando textos dos campos, transformando em String e salvando nas variaveis
        nome        =   campoNome.getText().toString().toUpperCase();
        sobrenome   =   campoSobrenome.getText().toString().toUpperCase();
        telefone    =   campoTelefone.getText().toString();
        cpf         =   campoCpf.getText().toString();
        cep         =   campoCep.getText().toString();
        logradouro  =   campoLogradouro.getText().toString();
        bairro      =   campoBairro.getText().toString();
        localidade  =   campoLocalidade.getText().toString();
        uf          =   campoUf.getText().toString().toUpperCase();

        //Verificando se não estiver vazio
        if(!nome.isEmpty()){
            if(!sobrenome.isEmpty()){
                if(!telefone.isEmpty() && telefone.length() == 15){
                    if(!cpf.isEmpty() && cpf.length() == 11) {
                        if(!cep.isEmpty() && cep.length() == 8) {
                            if(!logradouro.isEmpty()) {
                                if(!bairro.isEmpty()) {
                                    if(!localidade.isEmpty()) {
                                        if(!uf.isEmpty() && uf.length() == 2) {

                                            Endereco endereco = new Endereco();
                                            endereco.setCep(cep);
                                            endereco.setLogradouro(logradouro);
                                            endereco.setBairro(bairro);
                                            endereco.setLocalidade(localidade);
                                            endereco.setUf(uf);

                                            if (tipoUsuario.equals("donoAnimal")) {

                                                DonoAnimal donoAnimal = new DonoAnimal();
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

                                            } else if (tipoUsuario.equals("motorista")) {

                                                Motorista motorista = new Motorista();
                                                motorista.setTipoUsuario(tipoUsuario);
                                                motorista.setNome(nome);
                                                motorista.setSobrenome(sobrenome);
                                                motorista.setTelefone(telefone);

                                                Intent intent = new Intent(this, CadastroMotoristaTermoActivity.class);
                                                intent.putExtra("motorista", motorista);
                                                intent.putExtra("endereco", endereco);
                                                startActivity(intent);
                                            }
                                        }else{
                                            Toast.makeText(CadastroUsuarioDadosActivity.this,
                                                    "Preencha o UF corretamente",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(CadastroUsuarioDadosActivity.this,
                                                "Preencha a Cidade",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(CadastroUsuarioDadosActivity.this,
                                            "Preencha o Bairro",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(CadastroUsuarioDadosActivity.this,
                                        "Preencha o Logradouro",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(CadastroUsuarioDadosActivity.this,
                                    "Preencha o CEP corretamente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroUsuarioDadosActivity.this,
                                "Preencha o CPF corretamente",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{ // Se tefefoneUsuario estiver vazio então exibe está mensagem
                    Toast.makeText(CadastroUsuarioDadosActivity.this,
                            "Preencha o Telefone corretamente",
                            Toast.LENGTH_SHORT).show();
                }
            }else{ // Se o campo Sobrenome estiver vazio então exibe está mensagem
                Toast.makeText(CadastroUsuarioDadosActivity.this,
                        "Preencha o Sobrenome",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroUsuarioDadosActivity.this,
                    "Preencha o Nome",
                    Toast.LENGTH_SHORT).show();
        }
    }
    // Metodo chamado quando clica no botão voltar do aparelho
    @Override
    public void finish() {
        super.finish();
        // Efeito de voltar para activity anterior
        overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
    }
}