package com.example.travelpet.helper;


// Usado no ConfiguracaoFragment
public class VerificaCampo {

    public static boolean isMesmoValor(String valorInicial, String valorEditado){
        boolean resultado = valorInicial.equals(valorEditado);
        return resultado;
    }

    public static boolean isVazio(String valor){
        boolean resultado = valor.trim().isEmpty();
        return resultado;
    }
}
