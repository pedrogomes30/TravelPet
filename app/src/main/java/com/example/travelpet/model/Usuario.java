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
import com.google.firebase.database.FirebaseDatabase;

public class Usuario implements Parcelable{

    protected String id;
    protected String nome;
    protected String sobrenome;
    protected String telefone;
    protected String cpf;
    protected String tipoUsuario;
    protected String email;
    protected String fotoUsuarioUrl;


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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    // Método para salvar os dados do usuário no firebase
    public void salvarUsuarioDatabase(final Activity activity, final String localSalvamentoUsuario){
    //public void salvar(){
        // DatabaseReference = Referência do Firebase
        FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
        // DatabaseReference usuariosRef = fireDB.getReference().child(tipoUsuario);
        // Referência DatabaseRefence para usuário
        // usuariosRef.child("usuarios") = indica o nó filho chamado usuarios
        // child(getId()) = recupera o id do nó  usuarios
        DatabaseReference usuarios = fireDB.getReference().child(tipoUsuario).child(getId());


        // Configurando usuário no Firebase
        // this = pois salvara todos os dados (id,nome,email,telefone,tipo)
        // excessão vai ser a senha pois já temos ela salva, e não e interessante
        // que se tenha uma senha que fique sendo visualizada sempre
        usuarios.setValue(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(localSalvamentoUsuario.equals("CadastroAnimalFotoActivity")){

                    Toast.makeText(activity,
                            "Sucesso ao cadastrar usuário",
                            Toast.LENGTH_SHORT).show();

                }else if(localSalvamentoUsuario.equals("ConfiguracaoFragmet")){

                    Toast.makeText(activity,
                            "Alteração feita com sucesso",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(localSalvamentoUsuario.equals("CadastroAnimalFotoActivity")){

                    Toast.makeText(activity,
                            "Erro ao cadastrar usuário",
                            Toast.LENGTH_SHORT).show();

                }else if(localSalvamentoUsuario.equals("ConfiguracaoFragmet")){
                    Toast.makeText(activity,
                            "Erro ao atualizar dados usuário",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Usuario(Parcel in) {
        tipoUsuario = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipoUsuario);
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

}
