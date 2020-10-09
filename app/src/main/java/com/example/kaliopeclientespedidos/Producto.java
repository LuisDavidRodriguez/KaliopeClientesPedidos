package com.example.kaliopeclientespedidos;

class Producto {

    private String nombre;
    private String descripcion;
    private String URL_imagen;

    public Producto(String nombre, String descripcion, String URL_imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.URL_imagen = URL_imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getURL_imagen() {
        return URL_imagen;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setURL_imagen(String URL_imagen) {
        this.URL_imagen = URL_imagen;
    }
}
