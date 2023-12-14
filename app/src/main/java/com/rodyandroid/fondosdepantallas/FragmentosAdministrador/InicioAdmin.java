package com.rodyandroid.fondosdepantallas.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rodyandroid.fondosdepantallas.CategoriasAdmin.MusicasA.MusicaA;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.PeliculasA;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.SeriesA.SeriesA;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.VideojuegosA.VideojuegosA;
import com.rodyandroid.fondosdepantallas.R;


public class  InicioAdmin extends Fragment {

    Button Peliculas, Series, Videojuegos, Musica;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Videojuegos = view.findViewById(R.id.Videojuegos);
        Musica = view.findViewById(R.id.Musica);


        Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), PeliculasA.class));

            }
        });

        Series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SeriesA.class));

            }
        });

        Videojuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), VideojuegosA.class));

            }
        });

        Musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MusicaA.class));

            }
        });
        return view;
    }
}