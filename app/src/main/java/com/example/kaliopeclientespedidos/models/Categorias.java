package com.example.kaliopeclientespedidos.models;

public class Categorias {
    private String title;
    private String nombre1;
    private String precio1;
    private String url_image1;
    private String nombre2;
    private String precio2;
    private String url_image2;
    private String nombre3;
    private String precio3;
    private String url_image3;
    private String nombre4;
    private String precio4;
    private String url_image4;

    public Categorias(String title, String nombre1, String precio1, String url_image1, String nombre2, String precio2, String url_image2, String nombre3, String precio3, String url_image3, String nombre4, String precio4, String url_image4) {
        this.title = title;
        this.nombre1 = nombre1;
        this.precio1 = precio1;
        this.url_image1 = url_image1;
        this.nombre2 = nombre2;
        this.precio2 = precio2;
        this.url_image2 = url_image2;
        this.nombre3 = nombre3;
        this.precio3 = precio3;
        this.url_image3 = url_image3;
        this.nombre4 = nombre4;
        this.precio4 = precio4;
        this.url_image4 = url_image4;
    }


    public String getTitle() {
        return title;
    }

    public String getNombre1() {
        return nombre1;
    }

    public String getPrecio1() {
        return precio1;
    }

    public String getUrl_image1() {
        return url_image1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public String getPrecio2() {
        return precio2;
    }

    public String getUrl_image2() {
        return url_image2;
    }

    public String getNombre3() {
        return nombre3;
    }

    public String getPrecio3() {
        return precio3;
    }

    public String getUrl_image3() {
        return url_image3;
    }

    public String getNombre4() {
        return nombre4;
    }

    public String getPrecio4() {
        return precio4;
    }

    public String getUrl_image4() {
        return url_image4;
    }
}
