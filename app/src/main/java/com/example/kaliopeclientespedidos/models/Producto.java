package com.example.kaliopeclientespedidos.models;

public class Producto {
    private String nombre;
    private String id;
    private String precio;
    private String existencias;
    private String url_image;
    private int type;

    public Producto(String nombre, String url_image, String precio, int type, String id) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
        this.url_image = url_image;
        this.type=type;
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

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
