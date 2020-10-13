package com.example.kaliopeclientespedidos.models;

public class Producto {
    private String nombre;
    private String producto;
    private String precio;
    private String existencias;
    private String url_image;

    public Producto(String nombre, String url_image, String precio, String existencias) {
        this.nombre = nombre;
        this.producto = producto;
        this.precio = precio;
        this.existencias = existencias;
        this.url_image = url_image;
    }


    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }
    public String getExistencias() {
        return existencias;
    }

    public String getUrl_image() {
        return url_image;
    }
}
