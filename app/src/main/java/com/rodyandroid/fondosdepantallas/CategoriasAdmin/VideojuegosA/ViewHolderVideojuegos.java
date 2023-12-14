package com.rodyandroid.fondosdepantallas.CategoriasAdmin.VideojuegosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodyandroid.fondosdepantallas.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder  {

    View mview;
    private ViewHolderVideojuegos.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);//El Admin presiona normal el Item
        void OnItemLongClick(View view, int position);//El admin mantiene presionado el Item

    }

    //metodo para poder presionar o mantener presionado una item
    public void setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener){

        mClickListener = clickListener;
    }
    public ViewHolderVideojuegos(@NonNull View itemView) {
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

    public void SeteoVideojuego(Context context, String nombre, int vista, String imagen){
        ImageView ImagenVideojuego;
        TextView NOmbreImagenVideojuego;
        TextView VistaVideojuegos;

        //Conexion con el item
        ImagenVideojuego = mview.findViewById(R.id.ImagenVideojuego);
        NOmbreImagenVideojuego = mview.findViewById(R.id.NOmbreImagenVideojuego);
        VistaVideojuegos = mview.findViewById(R.id.VistaVideojuegos);

        NOmbreImagenVideojuego.setText(nombre);

        //convertimos a string el parametro vista
        String vistaString = String.valueOf(vista);
        VistaVideojuegos.setText(vistaString);

        //Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente

            Picasso.get().load(imagen).into(ImagenVideojuego);
        }catch (Exception e){

            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
