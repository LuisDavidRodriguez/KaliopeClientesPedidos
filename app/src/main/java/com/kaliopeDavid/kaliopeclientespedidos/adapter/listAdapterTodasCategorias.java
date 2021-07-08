package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/*
* Esta clase es un adaptador para llenar un el ListView que se muestra en la vista todas las categorias
* bueno por el momento solo llenare la lista con la forma predeterminada de android
* */
class listAdapterTodasCategorias extends BaseAdapter {
    ArrayList<String> arrayList;

    public listAdapterTodasCategorias(ArrayList<String> arrayList) {
        super();
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
