package com.kaliopeDavid.kaliopeclientespedidos.models;

import java.util.ArrayList;

public class TodasCategorias {
    private String title;
    private ArrayList <String> arrayListCategorias;

    public TodasCategorias(String title, ArrayList<String> arrayListCategorias) {
        this.title = title;
        this.arrayListCategorias = arrayListCategorias;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getArrayListCategorias() {
        return arrayListCategorias;
    }
}
