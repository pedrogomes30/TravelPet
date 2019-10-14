package com.example.travelpet.telasPerfil.passageiro.ui.viagem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViagemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViagemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}