package com.example.kaliopeclientespedidos.models;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class TodasCategorias {
    private String title;
    private ArrayList <HashMap> arrayListCategorias;

    public TodasCategorias(String title, ArrayList<HashMap> arrayListCategorias) {
        this.title = title;
        this.arrayListCategorias = arrayListCategorias;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<HashMap> getArrayListCategorias() {
        return arrayListCategorias;
    }
}
