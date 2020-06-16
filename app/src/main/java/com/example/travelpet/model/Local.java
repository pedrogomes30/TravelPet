package com.example.travelpet.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

// Classe usada no processo do buttonChamarMotorista = aula 495
// Fragment ViagemFragment
public class Local
{
    @Exclude
    public static final String ORIGEM ="origem";
    @Exclude
    public static final String DESTINO = "destino";

    private String idLocal;
    private String rua;
    private String numero;
    private String cidade;
    private String bairro;
    private String cep;
    private String tipoLocal;

    private double latitude;
    private double longitude;

    public Local() {}

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(String tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {this.longitude = longitude;}

    @Exclude
    public Location getLocation ()
    {
        Location location = new Location("Local");
        location.setLatitude(getLatitude());
        location.setLongitude(getLongitude());

        return location;
    }
}
