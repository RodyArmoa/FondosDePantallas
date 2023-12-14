package com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodyandroid.fondosdepantallas.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPelicula extends RecyclerView.ViewHolder {

    View mview;
    private ViewHolderPelicula.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);//El Admin presiona normal el Item
        void OnItemLongClick(View view, int position);//El admin mantiene presionado el Item

    }

    //metodo para poder presionar o mantener presionado una item
    public void setOnClickListener(ViewHolderPelicula.ClickListener clickListener){

        mClickListener = clickListener;
    }
    public ViewHolderPelicula(@NonNull View itemView) {
        super(itemView);
        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.OnItemLongClick(v, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.OnItemLongClick(v, getBindingAdapterPosition());
                return true;
            }
        });

    }

    public void SeteoPeliculas(Context context, String nombre, int vista, String imagen){
        ImageView ImagenPelicula;
        TextView NOmbreImagenPelicula;
        TextView VistaPelicula;

        //Conexion con el item
        ImagenPelicula = mview.findViewById(R.id.ImagenPelicula);
        NOmbreImagenPelicula = mview.findViewById(R.id.NOmbreImagenPelicula);
        VistaPelicula = mview.findViewById(R.id.VistaPelicula);

        NOmbreImagenPelicula.setText(nombre);

        //convertimos a string el parametro vista
        String vistaString = String.valueOf(vista);
        VistaPelicula.setText(vistaString);

        //Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente

            Picasso.get().load(imagen).into(ImagenPelicula);
        }catch (Exception e){

            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
