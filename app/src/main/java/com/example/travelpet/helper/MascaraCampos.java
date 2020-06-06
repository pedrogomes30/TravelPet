package com.example.travelpet.helper;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

// usado no ConfiguracaoFragment e no CadastroUsuarioDadosActivity
public class MascaraCampos {

    public static void mascaraTelefone(TextInputEditText campoTelefone){

        SimpleMaskFormatter telefoneSMF = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher telefoneMTW = new MaskTextWatcher(campoTelefone, telefoneSMF);
        campoTelefone.addTextChangedListener(telefoneMTW);
    }

    public static void mascaraCpf(TextInputEditText campoCpf){

        SimpleMaskFormatter cpfSMF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher cpfMTW = new MaskTextWatcher(campoCpf, cpfSMF);
        campoCpf.addTextChangedListener(cpfMTW);
    }

    public static void mascaraCep(TextInputEditText campoCep){

        SimpleMaskFormatter cepSMF = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher cepMTW = new MaskTextWatcher(campoCep, cepSMF);
        campoCep.addTextChangedListener(cepMTW);
    }

}
