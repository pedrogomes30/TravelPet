package com.example.travelpet.controlller.perfil.passageiro.ui.pagamento;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PagamentoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PagamentoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}