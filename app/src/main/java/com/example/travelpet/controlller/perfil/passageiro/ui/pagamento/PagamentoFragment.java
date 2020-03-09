package com.example.travelpet.controlller.perfil.passageiro.ui.pagamento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.travelpet.R;

public class PagamentoFragment extends Fragment {

    private PagamentoViewModel pagamentoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pagamentoViewModel =
                ViewModelProviders.of(this).get(PagamentoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pagamento, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        pagamentoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}