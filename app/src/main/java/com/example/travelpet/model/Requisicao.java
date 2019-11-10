package com.example.travelpet.model;
// Classe usada como apoio para salvar a requisição - Aula 496
// Fragment utilizada (ViagemFragment.java)

import com.example.travelpet.classes.Motorista;
import com.example.travelpet.classes.Usuario;
import com.example.travelpet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Requisicao {

    private String idRequisicao;
    private String statusRequisicao;

    // Criando objetos relacionado a classes Usuario e Destino
    private Usuario passageiro;
    private Usuario motorista;
    private Destino destino;

    // Definindo Status da Requisição
    public static final String STATUS_AGUARDANDO = "aguardando";
    public static final String STATUS_A_CAMINHO = "acaminho";
    public static final String STATUS_VIAGEM = "viagem";
    public static final String STATUS_FINALIZADA = "finalizada";

    public Requisicao() {
    }

    public String getIdRequisicao() {
        return idRequisicao;
    }

    public void salvarRquisicao(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabaseReferencia();
        DatabaseReference requisicoes = firebaseRef.child("requisicoes");

        String idRequisicao = requisicoes.push().getKey();
        setIdRequisicao(idRequisicao);

        requisicoes.child(getIdRequisicao()).setValue(this);
    }

    public void setIdRequisicao(String idRequisicao) {
        this.idRequisicao = idRequisicao;
    }

    public String getStatusRequisicao() {
        return statusRequisicao;
    }

    public void setStatusRequisicao(String statusRequisicao) {
        this.statusRequisicao = statusRequisicao;
    }

    public Usuario getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Usuario passageiro) {
        this.passageiro = passageiro;
    }

    public Usuario getMotorista() {
        return motorista;
    }

    public void setMotorista(Usuario motorista) {
        this.motorista = motorista;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
}
