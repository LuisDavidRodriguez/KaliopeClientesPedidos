package com.example.kaliopeclientespedidos.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaliopeclientespedidos.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SpinnerTallasAdapter extends BaseAdapter {
    public static final String COLUMNA_TALLA = "talla";
    public static final String COLUMNA_EXISTENCIAS = "existencias" ;


    List<HashMap<String, String>> listTallas;




    public SpinnerTallasAdapter(List<HashMap<String, String>> tallas) {
        this.listTallas = tallas;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return listTallas.size();
    }

    @Override
    public Object getItem(int position) {
        return listTallas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.spinner_tallas_item,
                parent,
                false
        );

        TextView tvTalla = (TextView) view.findViewById(R.id.spinner_tallas_item_TextViewTalla);
        TextView tvExistencias = (TextView) view.findViewById(R.id.spinner_tallas_item_TextViewExistencias);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutTallasItem);

        String tallaTemp = listTallas.get(position).get(COLUMNA_TALLA);
        int existenciasTemp = Integer.parseInt(Objects.requireNonNull(listTallas.get(position).get(COLUMNA_EXISTENCIAS)));
        String mensaje = "";


        if(existenciasTemp <= 0){
            mensaje = "Producto Agotado";
            relativeLayout.setBackgroundResource(R.drawable.cuadro_redondeado_spinner_agotado);
        }else{
            mensaje = existenciasTemp + "pz disponibles";
        }

        tvTalla.setText(tallaTemp);
        tvExistencias.setText(mensaje);


        return view;
    }





}
