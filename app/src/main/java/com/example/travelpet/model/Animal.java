package com.example.travelpet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Animal implements Parcelable {

    private String idUsuario;
    private String idAnimal;
    private String nomeAnimal;
    private String especieAnimal;
    private String racaAnimal;
    private String porteAnimal;
    private String observacaoAnimal;
    private byte [] fotoAnimal;
    private String fotoAnimalUrl;

    // Construtor
    public Animal() {
    }
    //      Métodos getter and setter
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getEspecieAnimal() {
        return especieAnimal;
    }

    public void setEspecieAnimal(String especieAnimal) {
        this.especieAnimal = especieAnimal;
    }

    public String getRacaAnimal() {
        return racaAnimal;
    }

    public void setRacaAnimal(String racaAnimal) {
        this.racaAnimal = racaAnimal;
    }

    public String getPorteAnimal() {
        return porteAnimal;
    }

    public void setPorteAnimal(String porteAnimal) {
        this.porteAnimal = porteAnimal;
    }

    public String getObservacaoAnimal() {
        return observacaoAnimal;
    }

    public void setObservacaoAnimal(String observacaoAnimal) {
        this.observacaoAnimal = observacaoAnimal;
    }
    @Exclude
    public byte[] getFotoAnimal() {
        return fotoAnimal;
    }

    public void setFotoAnimal(byte[] fotoAnimal) {
        this.fotoAnimal = fotoAnimal;
    }

    public String getFotoAnimalUrl() {
        return fotoAnimalUrl;
    }

    public void setFotoAnimalUrl(String fotoAnimalUrl) {
        this.fotoAnimalUrl = fotoAnimalUrl;
    }

    // Métodos Necessarios para usar a Interface Parcelable
    protected Animal(Parcel in) {

        idUsuario = in.readString();
        idAnimal = in.readString();
        nomeAnimal = in.readString();
        especieAnimal = in.readString();
        racaAnimal = in.readString();
        porteAnimal = in.readString();
        observacaoAnimal = in.readString();
        fotoAnimalUrl = in.readString();
        fotoAnimal = in.createByteArray();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUsuario);
        dest.writeString(idAnimal);
        dest.writeString(nomeAnimal);
        dest.writeString(especieAnimal);
        dest.writeString(racaAnimal);
        dest.writeString(porteAnimal);
        dest.writeString(observacaoAnimal);
        dest.writeString(fotoAnimalUrl);
        dest.writeByteArray(fotoAnimal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };
    /*
    public static String gerarPushKeyIdAnimal(){
        DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        String chave = referencia.push().getKey();
        return chave;
    }

    //      Método para salvar os dados do animal no firebase
    public void salvarAnimalDatabase(final Activity activity, final String localSalvamentoAnimal){

        // DatabaseReference = Referência do Firebase

         FirebaseDatabase fireDB = ConfiguracaoFirebase.getFirebaseDatabase();
         DatabaseReference animaisRef = fireDB.getReference().child("animais").child(getIdUsuario()).child(getIdAnimal());
        // Referência DatabaseRefence para animais
        // animaisRef.child("animais") = indica o nó filho chamado animais
        // .push(); = pega o id criado pelo nó

        //DatabaseReference animais = animaisRef.child(getIdUsuario()).child(getIdAnimal());

        // Configurando usuário no Firebase
        // this = pois salvara todos os dados de uma vez (idUsuario, idAnimal, nomeAnimal...)
        animaisRef.setValue(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if(localSalvamentoAnimal.equals("EditarAnimalActivity")){
                    Toast.makeText(activity,
                            "Atualização feita com sucesso",
                            Toast.LENGTH_SHORT).show();
                }else if (localSalvamentoAnimal.equals("CadastroAnimalFotoActivity_adicionarAnimal")){
                    Toast.makeText(activity,
                            "Sucesso ao cadastrar animal",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(localSalvamentoAnimal.equals("EditarAnimalActivity")) {

                    Toast.makeText(activity,
                            "Erro ao atualizar dados do animal",
                            Toast.LENGTH_SHORT).show();

                }else if(localSalvamentoAnimal.equals("CadastroAnimalFotoActivity_cadastroUsuario") ||
                         localSalvamentoAnimal.equals("CadastroAnimalFotoActivity_adicionarAnimal")){

                    Toast.makeText(activity,
                            "Erro ao salvar dados do animal",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Método salvar a foto e os dados do animal / função salvar e atualizar
    public static void salvarAnimalStorage(String emailUsuario, final String idAnimal, final String nomeAnimal,
                                        final String especieAnimal, final String racaAnimal, final String porteAnimal,
                                        final String observacaoAnimal, byte[] fotoAnimal,
                                        final Activity activityAtual,
                                        final Class<PerfilPassageiroActivity> activitySeguinte,
                                        final String localSalvamentoAnimal){

        // Salvar imagem no firebase
        StorageReference imagemRef = ConfiguracaoFirebase.getFirebaseStorage()
                .child("animais")
                .child(emailUsuario)
                .child(idAnimal)
                .child(idAnimal+".FOTO.PERFIL.JPEG");

        // Salvando dados da imagem método UploadTask
        // .putBytes = passa os dados da imagem em Bytes
        UploadTask uploadTask = imagemRef.putBytes(fotoAnimal);

        // Método para saber se o salvamento deu certo no Storage
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(activityAtual,
                        "Erro ao fazer upload da imagem",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Método para pegar o caminho(url) da foto, quando salvar ela no storage
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                // url = pega o caminho da imagem
                Uri url = uri.getResult();
                // transforma a url para String, e armazena na variável
                String fotoAnimalUrl = url.toString();

                // Métodos para enviar dados para a classe Animal
                // foi feito aqui por causa do método que pega o caminho da url da foto
                Animal animal = new Animal();
                animal.setIdUsuario(UsuarioFirebase.getIdentificadorUsuario());
                animal.setIdAnimal(idAnimal);
                animal.setNomeAnimal(nomeAnimal);
                animal.setEspecieAnimal(especieAnimal);
                animal.setRacaAnimal(racaAnimal);
                animal.setPorteAnimal(porteAnimal);
                animal.setObservacaoAnimal(observacaoAnimal);
                animal.setFotoAnimal(fotoAnimalUrl);

                // Chama método Salvar dados do Animal no Database
                animal.salvarAnimalDatabase(activityAtual, localSalvamentoAnimal);

                if( localSalvamentoAnimal.equals("EditarAnimalActivity")){

                    activityAtual.finish();
                    activityAtual.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);

                }else if(localSalvamentoAnimal.equals("CadastroAnimalFotoActivity_cadastroUsuario") ||
                        localSalvamentoAnimal.equals("CadastroAnimalFotoActivity_adicionarAnimal")){

                    Intent intent = new Intent(activityAtual, activitySeguinte);
                    // intent.setFlags = Limpa as activitys acumuladas
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activityAtual.startActivity( intent);

                }
            }
        });
    }
    // Método Excluir animal
    public static void excluirAnimal(String emailUsuario, final String idAnimal, final Activity activity){
        //Recuperando o caminho da foto no storage de acordo com animal escolhido
        StorageReference imagemReferencia = ConfiguracaoFirebase.getFirebaseStorage().child(
                "animais/"+emailUsuario+"/"+idAnimal+"/"+idAnimal+".FOTO.PERFIL.JPEG");

        // Caso consiga exccluir, executa
        imagemReferencia.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Exclui o nó do animal do database também
                ConfiguracaoFirebase.getFirebaseDatabaseReferencia().child("animais")
                        .child(UsuarioFirebase.getIdentificadorUsuario())
                        .child(idAnimal).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(activity,
                                "Sucesso ao remover Animal",
                                Toast.LENGTH_SHORT).show();

                        activity.finish();
                        activity.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(activity,
                                "Erro ao remover dados do Animal!",
                                Toast.LENGTH_SHORT).show();

                        activity.finish();
                        activity.overridePendingTransition(R.anim.activity_pai_entrando, R.anim.activity_filho_saindo);
                    }
                });
            }
            // Caso de erro
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity,
                        "Erro ao remover foto do animal do Storage!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    } */


}
