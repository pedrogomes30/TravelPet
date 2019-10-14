package com.example.travelpet.telasPerfil.passageiro.ui.viagem;

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

public class ViagemFragment extends Fragment {

    private ViagemViewModel viagemViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viagemViewModel =
                ViewModelProviders.of(this).get(ViagemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_viagem, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        viagemViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}