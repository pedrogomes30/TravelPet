package com.example.travelpet.model;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelpet.dao.ConfiguracaoFirebase;
import com.example.travelpet.dao.UsuarioFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonoAnimal extends Usuario implements Parcelable {

    private String fluxoDados;

    public DonoAnimal() {
    }

    @Exclude // Com isso não será salvo o fluxo dados no banco de dados
    public String getFluxoDados() {
        return fluxoDados;
    }

    public void setFluxoDados(String fluxoDados) {
        this.fluxoDados = fluxoDados;
    }

    // Método salva primeiro a foto do usuario no storage e depois salvar os dados no database
    public static void atualizarDonoAnimal(final String email, final String nome, final String sobrenome, final String telefone,
                                           final String tipoUsuario, byte[] fotoUsuario, final Activity activity,
                                           final String localSalvamentoUsuario){
        // Salvar imagem no firebase
        StorageReference imagemRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child("passageiro")
                .child(email)
                .child("foto de perfil")
                .child(email+".PERFIL.JPEG");

        // Salvando dados da imagem método UploadTask
        // .putBytes = passa os dados da imagem em Bytes
        UploadTask uploadTask = imagemRef.putBytes(fotoUsuario);

        // Método para saber se o salvamento deu certo
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity,
                        "Erro ao fazer upload da imagem",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Configurando (atualizando) foto para pegar ela nas configurações do usuário
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                // url = pega o caminho da imagem
                Uri url = uri.getResult();

                // Chama o método atualizaFotoUsuario da classe UsuarioFirebase
                // esse metodo atualiza a foto(Email) de usuário do firebase
                UsuarioFirebase.atualizarFotoUsuario( url );

                String fotoUsuarioUrl = url.toString();

                DonoAnimal donoAnimal = new DonoAnimal();
                donoAnimal.setId(UsuarioFirebase.getIdentificadorUsuario());
                donoAnimal.setEmail(email);
                donoAnimal.setNome(nome);
                donoAnimal.setSobrenome(sobrenome);
                donoAnimal.setTelefone(telefone);
                donoAnimal.setTipoUsuario(tipoUsuario);
                donoAnimal.setFotoUsuarioUrl(fotoUsuarioUrl);
                donoAnimal.salvar(activity, localSalvamentoUsuario);

            }
        });
    }

    // Métodos Necessarios para usar a Interface Parcelable
    public DonoAnimal(Parcel in) {

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
        //dest.writeString(fluxoDados);
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

    public static final Creator<DonoAnimal> CREATOR = new Creator<DonoAnimal>() {
        @Override
        public DonoAnimal createFromParcel(Parcel in) {
            return new DonoAnimal(in);
        }

        @Override
        public DonoAnimal[] newArray(int size) {
            return new DonoAnimal[size];
        }
    };

}
