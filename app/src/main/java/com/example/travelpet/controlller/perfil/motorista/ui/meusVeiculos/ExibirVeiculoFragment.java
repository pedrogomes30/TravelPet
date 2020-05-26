package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelpet.R;

import org.w3c.dom.Text;


public class ExibirVeiculoFragment extends Fragment {

    private TextView placa,modelo,marca,ano,crvl,status;
    private Button btExcluirVeiculo;

    public ExibirVeiculoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_exibir_veiculo, container, false);

       placa    =   view.findViewById(R.id.tv_exibir_placa);
       modelo   =   view.findViewById(R.id.tv_exibir_modelo);
       marca    =   view.findViewById(R.id.tv_exibir_marca);
       ano      =   view.findViewById(R.id.tv_exibir_ano);
       crvl     =   view.findViewById(R.id.tv_exibir_crvl);
       status   =   view.findViewById(R.id.tv_exibir_status);

       btExcluirVeiculo = view.findViewById(R.id.bt_excluirVeiculo);




       return view;
    }

    public void onClickExcluirVeiculo ()
    {
        btExcluirVeiculo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
    }


}
