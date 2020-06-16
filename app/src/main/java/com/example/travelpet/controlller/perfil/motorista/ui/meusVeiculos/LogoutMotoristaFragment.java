package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelpet.R;
import com.example.travelpet.controlller.LoginActivity;
import com.example.travelpet.dao.UsuarioFirebase;


public class LogoutMotoristaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout_motorista, container, false);

        UsuarioFirebase.deslogarUsuario(getActivity());
        Intent intent = new Intent(getActivity(), LoginActivity.class );
        startActivity(intent);
        getActivity().finish();

        return view;
    }
}
