package com.example.travelpet.activity.cadastro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travelpet.classes.Usuario;
import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private TextInputEditText campoNome,campoSobrenome,campoTelefone,campoSenha,
            campoConfirmarSenha;

    // Variável recebe o valor da Activity CadastroTipoUsuario
    private String tipoUsuario;

    // Variável recebe os valores dos campos do do activity_cadastro_usuario.xml
    private String nomeUsuario, sobrenomeUsuario, telefoneUsuario, senhaUsuario,
            confirmarSenhaUsuario;;

    // Ainda precisa ser trabalhada
    //private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        // Recuperando dado passa da Activity CadastroTipoUsuario
        Bundle dados = getIntent().getExtras();

        // recupera dados atráves da key (chave) passada, e armazena em uma nova variável
        tipoUsuario = dados.getString("tipoUsuario");

        // Inicializando componentes, pegando referência de cada componente do arquivo xml
        campoNome = findViewById(R.id.editNomeUsuario);
        campoSobrenome = findViewById(R.id.editSobrenomeUsuario);
        campoTelefone = findViewById(R.id.editTelUsuario);


    }

    // Evento onClick (Botão Proximo)
    public void ValidarCadastro(View view) {

        // Recuperando textos dos campos do xml, transformando em String e salvando nas variaveis
        nomeUsuario = campoNome.getText().toString();
        sobrenomeUsuario = campoSobrenome.getText().toString();
        telefoneUsuario = campoTelefone.getText().toString();


        //Verificando se não estiver vazio
        if(!nomeUsuario.isEmpty()){//Verifica o nome
            if(!sobrenomeUsuario.isEmpty()){//Verifica o Sobrenome
                if(!telefoneUsuario.isEmpty()){//Verifica o Telefone

                          // Cria uma referência para a classe usuario e instância ela
                            Usuario usuario = new Usuario();

                            // chamando metodo para autenticar e salvar no firebase
                            //cadastrarUsuario( usuario );


                }else{ // Se o campo Telefone estiver vazio então exibe está mensagem
                        Toast.makeText(CadastroUsuarioActivity.this,
                                "Preencha o Telefone",
                                Toast.LENGTH_SHORT).show();
                }
            }else{ // Se o campo Sobrenome estiver vazio então exibe está mensagem
                Toast.makeText(CadastroUsuarioActivity.this,
                        "Preencha o Sobrenome",
                        Toast.LENGTH_SHORT).show();
            }
        }else{ // Se o campo nome estiver vazio então exibe está mensagem
            Toast.makeText(CadastroUsuarioActivity.this,
                    "Preencha o Nome",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

