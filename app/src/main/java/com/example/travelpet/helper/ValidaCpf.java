package com.example.travelpet.helper;

public class ValidaCpf {
    private String CPF;


    public ValidaCpf(String CPF) {
        this.CPF = CPF;
        this.CPF = this.retirarMascara(CPF);
    }

    private String retirarMascara(String CPF){
        CPF = CPF.replace(".", "").replace("-","");
        return CPF;
    }

    public boolean isCPF() {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11)) {
            return (false);
        }

        char digito10, digito11;
        int soma, resto, num, peso;

        // "try" - protege de erros de conversão do tipo (int)
        try {
            // Calculo do 1º digito verificador
            soma = 0;
            peso = 10;
            for (int i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            resto = 11 - (soma % 11);
            if ((resto == 10) || (resto == 11)) {
                digito10 = '0';
            }
            else {
                digito10 = (char) (resto + 48); // converte o caractere em numerico
            }

            // Calculo do 2º Digito Verificador
            soma = 0;
            peso = 11;
            for(int i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            resto = 11 - (soma % 11);
            if ((resto == 10) || (resto == 11)) {
                digito11 = '0';
            }
            else {
                digito11 = (char) (resto + 48);
            }
            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((digito10 == CPF.charAt(9)) && (digito11 == CPF.charAt(10))) {
                return (true);
            }
            else {
                return (false);
            }
        } catch (Exception erro) {
            return(false);
        }
    }
}
