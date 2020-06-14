package com.example.travelpet.controlller.perfil.passageiro.ui.configuracao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.travelpet.R;
import com.example.travelpet.dao.DonoAnimalDAO;
import com.example.travelpet.dao.EnderecoDAO;
import com.example.travelpet.model.Endereco;
import com.example.travelpet.domain.Util;
import com.example.travelpet.helper.Base64Custom;
import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.helper.MascaraCampos;
import com.example.travelpet.helper.Mensagem;
import com.example.travelpet.helper.TelaCarregamento;
import com.example.travelpet.dao.UsuarioFirebase;
import com.example.travelpet.helper.VerificaDado;
import com.example.travelpet.model.DonoAnimal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ConfiguracaoFragment extends Fragment {

    private DonoAnimal donoAnimal;
    private Endereco endereco;
    private DonoAnimalDAO donoAnimalDAO;
    private EnderecoDAO enderecoDAO;
    private ProgressDialog progressDialog;

    private String nome,sobrenome, cpf, fotoPerfilUrl, telefone,
                   cep, logradouro, bairro, localidade, uf;

    private String telefoneEdit, cepEdit, logradouroEdit,
                   bairroEdit, localidadeEdit, ufEdit;

    private CircleImageView campoFotoPerfil;
    private TextView campoNome, campoCpf;
    private TextInputEditText campoTelefone,campoCep,campoLogradouro,
                              campoBairro, campoLocalidade, campoUf;
    private ImageButton     btCamera, btGaleria;
    private Button          btSalvar;

    // Variáveis usadas para especificar o requestCode
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private byte[] fotoPerfil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        iniciarComponentes(view);
        getDadosDonoAnimalDatabase();
        getDadosEnderecoDatabase();
        MascaraCampos.mascaraTelefone(campoTelefone);
        MascaraCampos.mascaraCep(campoCep);
        botaoCamera();
        botaoGaleria();
        botaoSalvar();

        return view;
    }

    public void botaoCamera(){

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
    }

    public void botaoGaleria(){
        btGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
    }

    public void botaoSalvar(){
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDadosEditados();
                salvarAlteracoes();
            }
        });
    }

    public void iniciarComponentes(View view){
        donoAnimalDAO = new DonoAnimalDAO();
        enderecoDAO = new EnderecoDAO();
        progressDialog = new ProgressDialog(getActivity());
        campoFotoPerfil     =   view.findViewById(R.id.imageViewCircleFotoPerfil);
        campoNome           =   view.findViewById(R.id.textViewNomeUsuario);
        campoCpf            =   view.findViewById(R.id.textViewCpfUsuario);
        campoTelefone       =   view.findViewById(R.id.editTelefone);
        campoCep            =   view.findViewById(R.id.editCep);
        //campoCep.addTextChangedListener( new CepListener( getActivity() ) );
        campoLogradouro     =   view.findViewById(R.id.editLogradouro); // rua
        campoBairro         =   view.findViewById(R.id.editBairro);
        campoLocalidade     =   view.findViewById(R.id.editLocalidade); // cidade
        campoUf             =   view.findViewById(R.id.editUf);
        btCamera            =   view.findViewById(R.id.imageButtonCamera);
        btGaleria           =   view.findViewById(R.id.imageButtonGaleria);
        btSalvar            =   view.findViewById(R.id.botaoSalvar);
    }

    public void getDadosDonoAnimalDatabase(){
        DatabaseReference donoAnimalRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child( "donoAnimal" )
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        donoAnimalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                donoAnimal = dataSnapshot.getValue(DonoAnimal.class);
                fotoPerfilUrl   =   donoAnimal.getFotoPerfilUrl();
                nome            =   donoAnimal.getNome();
                sobrenome       =   donoAnimal.getSobrenome();
                cpf             =   donoAnimal.getCpf();
                telefone        =   donoAnimal.getTelefone();


                //          Enviando os dados para o layout XML
                if(!fotoPerfilUrl.equals("")){

                    Uri fotoPerfilUri = Uri.parse(fotoPerfilUrl);
                    Glide.with(getActivity()).load( fotoPerfilUri ).into( campoFotoPerfil );
                }else{
                    campoFotoPerfil.setImageResource(R.drawable.imagem_usuario);
                }

                campoNome.setText(nome+" "+sobrenome);
                campoCpf.setText(cpf);
                campoTelefone.setText(telefone);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void getDadosEnderecoDatabase(){

        DatabaseReference enderecoRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia()
                .child( "enderecosDonoAnimal" )
                .child(Base64Custom.codificarBase64(UsuarioFirebase.getEmailUsuario()));
        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                endereco = dataSnapshot.getValue(Endereco.class);
                cep         =   endereco.getCep();
                logradouro  =   endereco.getLogradouro();
                bairro      =   endereco.getBairro();
                localidade  =   endereco.getLocalidade();
                uf          =   endereco.getUf();

                campoCep.setText(cep);
                campoLogradouro.setText(logradouro);
                campoBairro.setText(bairro);
                campoLocalidade.setText(localidade);
                campoUf.setText(uf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void getDadosEditados(){

        telefoneEdit    =   campoTelefone.getText().toString();
        cepEdit         =   campoCep.getText().toString();
        logradouroEdit  =   campoLogradouro.getText().toString();
        bairroEdit      =   campoBairro.getText().toString();
        localidadeEdit  =   campoLocalidade.getText().toString();
        ufEdit          =   campoUf.getText().toString().toUpperCase();
    }

    public void salvarAlteracoes(){

        int tipoSave = 2; // tipoLocalSave = 2 - Atualizar dados DonoAnimal

        if(fotoPerfil != null){
            if(validarCampos()) {
                TelaCarregamento.iniciarCarregamento(progressDialog);

                if ((!VerificaDado.isMesmoValor(cep, cepEdit)) ||
                    (!VerificaDado.isMesmoValor(logradouro, logradouroEdit)) ||
                    (!VerificaDado.isMesmoValor(bairro, bairroEdit)) ||
                    (!VerificaDado.isMesmoValor(localidade, localidadeEdit)) ||
                    (!VerificaDado.isMesmoValor(uf, ufEdit))) {

                    endereco.setCep(cepEdit);
                    endereco.setLogradouro(logradouroEdit);
                    endereco.setBairro(bairroEdit);
                    endereco.setLocalidade(localidadeEdit);
                    endereco.setUf(ufEdit);
                    enderecoDAO.salvarEnderecoRealtimeDatabase(endereco,
                            donoAnimal.getTipoUsuario(),
                            tipoSave = 0, progressDialog);

                }

                if (!VerificaDado.isMesmoValor(telefone, telefoneEdit)) {
                    donoAnimal.setTelefone(telefoneEdit);
                }

                donoAnimal.setFotoPerfil(fotoPerfil);
                donoAnimalDAO.salvarImagemDonoAnimalStorage(donoAnimal, progressDialog,
                        tipoSave = 2, getActivity());
                fotoPerfil = null;

            }
        }else if((!VerificaDado.isMesmoValor(cep,cepEdit) ||
                  !VerificaDado.isMesmoValor(logradouro,logradouroEdit) ||
                  !VerificaDado.isMesmoValor(bairro, bairroEdit) ||
                  !VerificaDado.isMesmoValor(localidade,localidadeEdit) ||
                  !VerificaDado.isMesmoValor(uf,ufEdit))){

            if(validarCampos()) {

                TelaCarregamento.iniciarCarregamento(progressDialog);

                if (!VerificaDado.isMesmoValor(telefone, telefoneEdit)) {

                    donoAnimal.setTelefone(telefoneEdit);
                    donoAnimalDAO.salvarDonoAnimalRealtimeDatabase(donoAnimal, progressDialog,
                            tipoSave = 0, getActivity());
                }
                endereco.setCep(cepEdit);
                endereco.setLogradouro(logradouroEdit);
                endereco.setBairro(bairroEdit);
                endereco.setLocalidade(localidadeEdit);
                endereco.setUf(ufEdit);
                enderecoDAO.salvarEnderecoRealtimeDatabase(endereco, donoAnimal.getTipoUsuario(),
                        tipoSave = 2, progressDialog);
                Mensagem.mensagemAtualizarDonoAnimal(getActivity());
            }

        }else if(!VerificaDado.isMesmoValor(telefone,telefoneEdit)){

            if(validarCampos()) {

                TelaCarregamento.iniciarCarregamento(progressDialog);

                donoAnimal.setTelefone(telefoneEdit);
                donoAnimalDAO.salvarDonoAnimalRealtimeDatabase(donoAnimal, progressDialog,
                        tipoSave = 2, getActivity());
            }
        }
    }

    public Boolean validarCampos() {

        Boolean validado = false;
        if(!VerificaDado.isVazio(telefoneEdit) && telefoneEdit.length() == 15) {
            if (!VerificaDado.isVazio(cepEdit) && cepEdit.length() == 9) {
                if (!VerificaDado.isVazio(logradouroEdit)) {
                    if (!VerificaDado.isVazio(bairroEdit)) {
                        if (!VerificaDado.isVazio(localidadeEdit)) {
                            if (validarUf(ufEdit)) {

                                validado = true;

                            } else { Mensagem.toastIt("Preencha o UF corretamente", getActivity());}
                        } else { Mensagem.toastIt("Preencha a Cidade", getActivity());}
                    } else { Mensagem.toastIt("Preencha o Bairro", getActivity());}
                } else { Mensagem.toastIt("Preencha o Logradouro", getActivity());}
            }else { Mensagem.toastIt("Preencha o CEP corretamente", getActivity());}
        }else{ Mensagem.toastIt("Preenche o telefone corretamente", getActivity()); }

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

    //          Método para verificar de onde será pego a foto, da camera ou galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){
            // null = por que pode receber dados de dois lugares , camera ou galeria
            Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        // Recupera o local da imagem selecionada
                        Uri localImagemSelecionada = data.getData();
                        // getContentResolver() = responsável por recupera conteúdo dentro do app android
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                if(imagem != null) {
                    // Envia a imagem para o XML
                    campoFotoPerfil.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    // Converte a imagem para um array de bytes
                    fotoPerfil = baos.toByteArray();
                }

                }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}