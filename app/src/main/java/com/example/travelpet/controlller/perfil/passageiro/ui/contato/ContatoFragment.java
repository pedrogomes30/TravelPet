package com.example.travelpet.controlller.perfil.passageiro.ui.contato;

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

public class ContatoFragment extends Fragment {

    private ContatoViewModel contatoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contatoViewModel =
                ViewModelProviders.of(this).get(ContatoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contato, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        contatoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}