package com.rodyandroid.fondosdepantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Carga extends AppCompatActivity {

    TextView app_name, desarrollador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        //cambio de letras/fuente
        String ubicacion="fuentes/CaviarDreams.ttf";
        Typeface tf =Typeface.createFromAsset(Carga.this.getAssets(),ubicacion);
        //cambio de letras/fuente

        app_name = findViewById(R.id.app_name);
        desarrollador=findViewById(R.id.desarrollador);
        final int duracion = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //codigo que se ejecuta

                Intent intent = new Intent(Carga.this, MainActivityAdministrador.class);
                startActivity(intent);
                finish();
            }
        },duracion);

        app_name.setTypeface(tf);
        desarrollador.setTypeface(tf);
    }
}