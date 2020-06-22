package com.example.travelpet.controlller.perfil.passageiro.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.travelpet.R;

import mehdi.sakout.aboutpage.AboutPage;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

//AAAA
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //metodo para inflar o fragment
        //return inflater.inflate(R.layout.fragment_about, container, false);

        /*esta classe utiliza uma biblioteca AboutPage, no qual não é necessário implementar o XML,
        todo layout é realizado dentro da classe controll e gerado automaticamente.*/

        mehdi.sakout.aboutpage.Element versaoPrincipal = new mehdi.sakout.aboutpage.Element();
        mehdi.sakout.aboutpage.Element versaoAdm =new mehdi.sakout.aboutpage.Element();

        versaoPrincipal.setTitle("aplicação principal versao 61.0");
        versaoAdm.setTitle("aplicação administrativa versao 16.0");

        return new AboutPage(getActivity())
                .setImage(R.drawable.logo_travel_pet_laranja)
                .setDescription(getString(R.string.descricao))
                .addGroup("Entre em contato")
                .addEmail("travelpetapp@gmail.com.br","Envie um e-mail")
                .addFacebook("TravelPet-102032251464773","Facebook")
                .addGroup("paginas do projeto")
                .addGitHub("pedrogomes30/TravelPet","Aplicativo Principal")
                .addGitHub("pedrogomes30/TravelPetADM","Aplicativo ADM")
                .addItem(versaoPrincipal)
                .addItem(versaoAdm)
                .create();


    }
}