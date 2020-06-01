package com.example.travelpet.controlller.perfil.motorista.ui.meusVeiculos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.travelpet.R;
import com.example.travelpet.dao.VeiculoDAO;
import com.example.travelpet.model.Veiculo;
import com.google.android.material.textfield.TextInputEditText;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class addVeiculoFragment extends Fragment
{

    private TextInputEditText modelo,marca,ano,placa,crvl;
    private String modeloV, marcaV, anoV, placaV, crvlV;
    private Button btfotocrvl,btcadastrar;
    private byte [] fotoCRVL;
    private Veiculo veiculo;
    private VeiculoDAO veiculoDAO;

    private static final int SELECAO_GALERIA = 200;

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
        veiculoDAO = new VeiculoDAO();


        onclickBtCadastrar();
        onclickBtFotocrvl();

        return view;
    }

    public void onclickBtCadastrar()
    {
        btcadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(fotoCRVL != null)
                {
                    marcaV  =   marca.getText().toString().toUpperCase();
                    modeloV =   modelo.getText().toString().toUpperCase();
                    anoV    =   ano.getText().toString().toUpperCase();
                    placaV  =   placa.getText().toString().toUpperCase();
                    crvlV   =   crvl.getText().toString().toUpperCase();

                    if (verificaCampos()== "completo")
                    {
                        veiculo = new Veiculo();
                        veiculo.setMarcaVeiculo(marcaV);
                        veiculo.setModeloVeiculo(modeloV);
                        veiculo.setAnoVeiculo(anoV);
                        veiculo.setPlacaVeiculo(placaV);
                        veiculo.setCrvlVeiculo(crvlV);
                        veiculo.setFotoCrvl(fotoCRVL);
                        veiculoDAO.salvarVeiculo(veiculo);

                        ToastIt("Veículo Salvo");

                        Navigation.findNavController(view).navigate(R.id.action_veiculoCadastrado);
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        getActivity();
        if (resultCode == Activity.RESULT_OK)
        {
            try
            {
                Uri localImagemSelecionada = data.getData();

                Cursor returnCursor = getActivity().getContentResolver().query(localImagemSelecionada,null,null,null,null);
                returnCursor.moveToFirst();

                Bitmap imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),localImagemSelecionada);

                if ( imagem != null )
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    fotoCRVL = baos.toByteArray();

                    btfotocrvl.setText("Mudar foto");

                    ToastIt("Sucesso ao selecionar Imagem");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onclickBtFotocrvl()
    {
        btfotocrvl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
    }

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
