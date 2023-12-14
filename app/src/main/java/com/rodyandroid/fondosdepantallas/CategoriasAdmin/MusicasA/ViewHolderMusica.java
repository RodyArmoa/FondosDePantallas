package com.rodyandroid.fondosdepantallas.CategoriasAdmin.MusicasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodyandroid.fondosdepantallas.R;
import com.squareup.picasso.Picasso;

public class ViewHolderMusica extends RecyclerView.ViewHolder {

    View mview;
    private ViewHolderMusica.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);//El Admin presiona normal el Item
        void OnItemLongClick(View view, int position);//El admin mantiene presionado el Item

    }

    //metodo para poder presionar o mantener presionado una item
    public void setOnClickListener(ViewHolderMusica.ClickListener clickListener){

        mClickListener = clickListener;
    }
    public ViewHolderMusica(@NonNull View itemView) {
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

    public void SeteoMusica(Context context, String nombre, int vista, String imagen){
        ImageView Imagen_Musica;
        TextView NOmbreImagen_Musica;
        TextView Vista_Musica;

        //Conexion con el item
        Imagen_Musica = mview.findViewById(R.id.Imagen_Musica);
        NOmbreImagen_Musica = mview.findViewById(R.id.NOmbreImagen_Musica);
        Vista_Musica = mview.findViewById(R.id.Vista_Musica);

        NOmbreImagen_Musica.setText(nombre);

        //convertimos a string el parametro vista
        String vistaString = String.valueOf(vista);
        Vista_Musica.setText(vistaString);

        //Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente

            Picasso.get().load(imagen).into(Imagen_Musica);
        }catch (Exception e){

            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
