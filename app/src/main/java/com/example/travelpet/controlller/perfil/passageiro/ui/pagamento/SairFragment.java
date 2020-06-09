package com.example.travelpet.controlller.perfil.passageiro.ui.pagamento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.travelpet.R;
import com.example.travelpet.controlller.LoginActivity;
import com.example.travelpet.dao.UsuarioFirebase;

public class SairFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        UsuarioFirebase.deslogarUsuario(getActivity());
        Intent intent = new Intent(getActivity(), LoginActivity.class );
        startActivity(intent);
        getActivity().finish();

        View root = inflater.inflate(R.layout.fragment_sair, container, false);

        return root;
    }

}