package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelpet.R;
import com.google.android.material.textfield.TextInputEditText;

public class addVeiculoFragment extends Fragment
{

    private TextInputEditText modelo,marca,ano,placa,crvl;
    private String modeloV, marcaV, anoV, placaV, crvlV;
    Button btfotocrvl,btcadastrar;


    public addVeiculoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_veiculo, container, false);

        modelo  =   view.findViewById(R.id.et_addVeiculo_modelo);
        marca   =   view.findViewById(R.id.et_addVeiculo_marca);
        ano     =   view.findViewById(R.id.et_addVeiculo_ano);
        placa   =   view.findViewById(R.id.et_addVeiculo_placa);
        crvl    =   view.findViewById(R.id.et_addVeiculo_crvl);

        btfotocrvl  =   view.findViewById(R.id.bt_addVeiculo_fotocrvl);
        btcadastrar =   view.findViewById(R.id.bt_addVeiculo_cadastrar);


        return view;
    }


    public void onclickBtCadastrar()
    {}

    public void onclickBtFotocrvl()
    {}

    public String verificaCampos ()
    {
        String verificado = "incompleto";

        if (!marcaV.isEmpty())
        {
            if (!modeloV.isEmpty())
            {
                if (!anoV.isEmpty())
                {
                    if (!placaV.isEmpty())
                    {
                        if (!crvlV.isEmpty())
                        {
                            verificado = "completo";
                        } else { ToastIt("Preencha o CRVL"); }
                    } else { ToastIt("Preencha a Placa"); }
                } else { ToastIt("Preencha o Ano"); }
            } else { ToastIt("Preencha o Modelo"); }
        } else { ToastIt("Preencha o CRVL"); }

        return verificado;
    }

    public void ToastIt (String mensagem)
    {
        Toast.makeText(getActivity().getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
    }

}
