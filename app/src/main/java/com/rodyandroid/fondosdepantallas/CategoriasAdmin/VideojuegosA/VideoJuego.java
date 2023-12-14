package com.rodyandroid.fondosdepantallas.CategoriasAdmin.VideojuegosA;

public class VideoJuego {

    private String imagen;
    private String Nombre;
    private int vistas;

    public VideoJuego() {
    }

    public VideoJuego(String imagen, String nombre, int vistas) {
        this.imagen = imagen;
        Nombre = nombre;
        this.vistas = vistas;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }


}
