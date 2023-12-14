package com.rodyandroid.fondosdepantallas.FragmentosClientes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rodyandroid.fondosdepantallas.Carga;
import com.rodyandroid.fondosdepantallas.InicioSesion;
import com.rodyandroid.fondosdepantallas.R;


public class AcercadeCliente extends Fragment {

    Button Acceder1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_acercade_cliente, container, false);

        Acceder1 = view.findViewById(R.id.Acecder1);

        Acceder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getActivity(), InicioSesion.class));
            }
        });

        return view;
    }
}