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
    private String idMotorista;
    private String idAnimal1;
    private String idAnimal2;
    private String idAnimal3;
    private String idVeiculo;
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
}
