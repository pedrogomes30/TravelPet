package com.example.travelpet.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario implements Parcelable {

    String id;
    String nome;
    String sobrenome;
    String telefone;
    String tipoUsuario;
    String email;
    String fotoUsuarioUrl;
    String fluxoDados;

    // Construtor
    public Usuario() {
    }

    // Métodos Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFotoUsuarioUrl() {
        return fotoUsuarioUrl;
    }

    public void setFotoUsuarioUrl(String fotoUsuarioUrl) {
        this.fotoUsuarioUrl = fotoUsuarioUrl;
    }

    @Exclude // com isso não será salvo o fluxo dados no banco de dados
    public String getFluxoDados() {
        return fluxoDados;
    }

    public void setFluxoDados(String fluxoDados) {
        this.fluxoDados = fluxoDados;
    }


    // Método para salvar os dados do usuário no firebase
    public void salvar(final Activity activity, final String localSalvamento){
        // DatabaseReference = Referência do Firebase
        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = fireDB.getReference().child("usuarios");
        // Referência DatabaseRefence para usuário
        // usuariosRef.child("usuarios") = indica o nó filho chamado usuarios
        // child(getId()) = recupera o id do nó  usuarios
        DatabaseReference usuarios = usuariosRef.child(getId());


        // Configurando usuário no Firebase
        // this = pois salvara todos os dados (id,nome,email,telefone,tipo)
        // excessão vai ser a senha pois já temos ela salva, e não e interessante
        // que se tenha uma senha que fique sendo visualizada sempre
        usuarios.setValue(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(localSalvamento.equals("CadastroAnimalFotoActivity")){

                    Toast.makeText(activity,
                            "Sucesso ao cadastrar Usuário!",
                            Toast.LENGTH_SHORT).show();

                }else if(localSalvamento.equals("ConfiguracaoFragmet")){

                    Toast.makeText(activity,
                            "Alteração feita com Sucesso!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(localSalvamento.equals("CadastroAnimalFotoActivity")){

                    Toast.makeText(activity,
                            "Erro ao cadastrar Usuário!",
                            Toast.LENGTH_SHORT).show();

                }else if(localSalvamento.equals("ConfiguracaoFragmet")){
                    Toast.makeText(activity,
                            "Erro ao atualizar",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Usuario(Parcel in) {
        id = in.readString();
        nome = in.readString();
        sobrenome = in.readString();
        telefone = in.readString();
        tipoUsuario = in.readString();
        email = in.readString();
        fotoUsuarioUrl = in.readString();
        fluxoDados = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(telefone);
        dest.writeString(tipoUsuario);
        dest.writeString(email);
        dest.writeString(fotoUsuarioUrl);
        dest.writeString(fluxoDados);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    /* O motivo deu fazer esse método e colocar a foto no database
    public void atualizarUsuario(){
        // pega id usuario atual
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        // referência do database
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();

        DatabaseReference usuariosRef = database.child("usuarios")
                .child( identificadorUsuario );
        // Criando método Map
        Map<String, Object> valoresUsuario = converterParaMap();

        usuariosRef.updateChildren ( valoresUsuario );
    }
    @Exclude
    // Tranformando Classe Usuario no tipo HashMap, utilizado no salvamento da foto no database
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        // Configurando usuarioMap
        usuarioMap.put("nome", getNome());
        usuarioMap.put("sobrenome",getSobrenome());
        usuarioMap.put("telefone",getTelefone());

        return usuarioMap;
    }*/


}
