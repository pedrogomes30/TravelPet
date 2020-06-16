package com.example.travelpet.model;

public class DisponibilidadeMotorista
{
    public static String DISPONIVEL ="disponivel";
    public static String EM_VIAGEM ="em_viagem";

    private String idVeiculo;
    private String idMotorista;
    private String porteAnimalPequeno;
    private String porteAnimalMedio;
    private String porteAnimalGrande;
    private String disponibilidade;
    private double latitudeMotorista;
    private double longitudeMotorista;

    public DisponibilidadeMotorista () {}


    public String getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(String idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getPorteAnimalPequeno() {
        return porteAnimalPequeno;
    }

    public void setPorteAnimalPequeno(String porteAnimalPequeno) {
        this.porteAnimalPequeno = porteAnimalPequeno;
    }

    public String getPorteAnimalMedio() {
        return porteAnimalMedio;
    }

    public void setPorteAnimalMedio(String porteAnimalMedio) {
        this.porteAnimalMedio = porteAnimalMedio;
    }

    public String getPorteAnimalGrande() {
        return porteAnimalGrande;
    }

    public void setPorteAnimalGrande(String porteAnimalGrande) {
        this.porteAnimalGrande = porteAnimalGrande;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public double getLatitudeMotorista() {
        return latitudeMotorista;
    }

    public void setLatitudeMotorista(double latitudeMotorista) {
        this.latitudeMotorista = latitudeMotorista;
    }

    public double getLongitudeMotorista() {
        return longitudeMotorista;
    }

    public void setLongitudeMotorista(double longitudeMotorista) {
        this.longitudeMotorista = longitudeMotorista;
    }
}
