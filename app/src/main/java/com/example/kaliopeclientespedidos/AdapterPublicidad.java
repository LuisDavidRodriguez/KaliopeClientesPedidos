package com.example.kaliopeclientespedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterPublicidad extends BaseAdapter {

    Context context;
    String [] url_Imagenes={};
    int [] imagenes;
    LayoutInflater layoutInflater;

    public AdapterPublicidad(Context context, int[] imagenes) {
        this.context = context;
        this.imagenes = imagenes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagenes.length;
    }

    @Override
    public Object getItem(int position) {
        return imagenes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.publicidad,null);
        ImageView imagenPublicidad = convertView.findViewById(R.id.publicidad_IV_imagen);

        imagenPublicidad.setImageResource(imagenes[position]);

        return convertView;
    }
}
