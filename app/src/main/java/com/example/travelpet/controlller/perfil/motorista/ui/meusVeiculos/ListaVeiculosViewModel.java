package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.model.Veiculo;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListaVeiculosViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Veiculo>> veiculos;
    public LiveData<ArrayList<Veiculo>> getVeiculos()
    {
        if(veiculos == null)
        {
            veiculos = new MutableLiveData<ArrayList<Veiculo>>();
        }
        return veiculos;
    }

}
