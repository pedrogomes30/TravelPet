package com.example.travelpet.model;

public class Viagem
{
    public static final String AGUARDANDO_MOTORISTA = "aguardando_motorista";
    public static final String BUSCANDO_MOTORISTA   = "buscando_motorista";
    public static final String EM_ANDAMENTO         = "em_andamento";
    public static final String FINALIZADA           = "finalizada";
    public static final String CANCELADA            = "cancelada";

    private String idViagem;
    private String idDonoAnimal;
    private String nomeDonoAnimal;
    private String fotoDonoAnimalUrl;
    private String idMotorista;
    private String nomeMotorista;
    private String fotoMotoristaUrl;

    private String idAnimal1;
    private String nomeAnimal1;
    private String fotoAnimalUrl1;
    private String observacaoAnimal1;

    private String idAnimal2;
    private String nomeAnimal2;
    private String fotoAnimalUrl2;
    private String observacaoAnimal2;

    private String idAnimal3;
    private String nomeAnimal3;
    private String fotoAnimalUrl3;
    private String observacaoAnimal3;

    private String idVeiculo;
    private String modeloVeiculo;
    private String marcaVeiculo;
    private String placaVeiculo;
    private String idOrigem;
    private String idDestino;
    private Double custo;
    private String data;
    private Double distancia;
    private String horaInicio;
    private String horaFim;
    private String StatusViagem;

    public Viagem () {}

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }

    public String getIdDonoAnimal() {
        return idDonoAnimal;
    }

    public void setIdDonoAnimal(String idDonoAnimal) {
        this.idDonoAnimal = idDonoAnimal;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getIdAnimal1() {
        return idAnimal1;
    }

    public void setIdAnimal1(String idAnimal1) {
        this.idAnimal1 = idAnimal1;
    }

    public String getIdAnimal2() {
        return idAnimal2;
    }

    public void setIdAnimal2(String idAnimal2) {
        this.idAnimal2 = idAnimal2;
    }

    public String getIdAnimal3() {
        return idAnimal3;
    }

    public void setIdAnimal3(String idAnimal3) {
        this.idAnimal3 = idAnimal3;
    }

    public String getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(String idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(String idOrigem) {
        this.idOrigem = idOrigem;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getStatusViagem() {
        return StatusViagem;
    }

    public void setStatusViagem(String statusViagem) {
        StatusViagem = statusViagem;
    }

    public String getNomeDonoAnimal() {
        return nomeDonoAnimal;
    }

    public void setNomeDonoAnimal(String nomeDonoAnimal) {
        this.nomeDonoAnimal = nomeDonoAnimal;
    }

    public String getFotoDonoAnimalUrl() {
        return fotoDonoAnimalUrl;
    }

    public void setFotoDonoAnimalUrl(String fotoDonoAnimalUrl) {
        this.fotoDonoAnimalUrl = fotoDonoAnimalUrl;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getFotoMotoristaUrl() {
        return fotoMotoristaUrl;
    }

    public void setFotoMotoristaUrl(String fotoMotoristaUrl) {
        this.fotoMotoristaUrl = fotoMotoristaUrl;
    }

    public String getNomeAnimal1() {
        return nomeAnimal1;
    }

    public void setNomeAnimal1(String nomeAnimal1) {
        this.nomeAnimal1 = nomeAnimal1;
    }


    public String getNomeAnimal2() {
        return nomeAnimal2;
    }

    public void setNomeAnimal2(String nomeAnimal2) {
        this.nomeAnimal2 = nomeAnimal2;
    }


    public String getNomeAnimal3() {
        return nomeAnimal3;
    }

    public void setNomeAnimal3(String nomeAnimal3) {
        this.nomeAnimal3 = nomeAnimal3;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getMarcaVeiculo() {
        return marcaVeiculo;
    }

    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getFotoAnimalUrl1() {
        return fotoAnimalUrl1;
    }

    public void setFotoAnimalUrl1(String fotoAnimalUrl1) {
        this.fotoAnimalUrl1 = fotoAnimalUrl1;
    }

    public String getObservacaoAnimal1() {
        return observacaoAnimal1;
    }

    public void setObservacaoAnimal1(String observacaoAnimal1) {
        this.observacaoAnimal1 = observacaoAnimal1;
    }

    public String getFotoAnimalUrl2() {
        return fotoAnimalUrl2;
    }

    public void setFotoAnimalUrl2(String fotoAnimalUrl2) {
        this.fotoAnimalUrl2 = fotoAnimalUrl2;
    }

    public String getObservacaoAnimal2() {
        return observacaoAnimal2;
    }

    public void setObservacaoAnimal2(String observacaoAnimal2) {
        this.observacaoAnimal2 = observacaoAnimal2;
    }

    public String getFotoAnimalUrl3() {
        return fotoAnimalUrl3;
    }

    public void setFotoAnimalUrl3(String fotoAnimalUrl3) {
        this.fotoAnimalUrl3 = fotoAnimalUrl3;
    }

    public String getObservacaoAnimal3() {
        return observacaoAnimal3;
    }

    public void setObservacaoAnimal3(String observacaoAnimal3) {
        this.observacaoAnimal3 = observacaoAnimal3;
    }
}
