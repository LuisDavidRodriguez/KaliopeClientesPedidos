package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaliopeDavid.kaliopeclientespedidos.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SpinnerTallasAdapter extends BaseAdapter {
    public static final String COLUMNA_TALLA = "talla";
    public static final String COLUMNA_EXISTENCIAS = "existencias" ;
    public static final String COLUMNA_ACTIVO = "activo";


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
        boolean activo = Boolean.parseBoolean(listTallas.get(position).get(COLUMNA_ACTIVO));
        String mensaje = "";


        if(activo){
            if(existenciasTemp>0){
                mensaje = (existenciasTemp>1)? existenciasTemp+" disponibles":"Ultimo disponible!";
                tvTalla.setTextColor(tvTalla.getResources().getColor(R.color.colorBlack));
                tvTalla.setBackgroundResource(R.drawable.cuadro_redondeado);
                tvExistencias.setTextColor(tvTalla.getResources().getColor(R.color.colorGris));
            }else{
                mensaje = "Talla agotada en este color";
                tvTalla.setTextColor(tvTalla.getResources().getColor(R.color.colorGris));
                tvTalla.setBackgroundResource(R.drawable.cuadro_redondeado_gris);
                tvExistencias.setTextColor(Color.RED);
            }

        }else{
            mensaje = "Talla agotada en este color";
            //relativeLayout.setBackgroundResource(R.drawable.cuadro_redondeado_spinner_agotado);
            tvTalla.setTextColor(tvTalla.getResources().getColor(R.color.colorGris));
            tvTalla.setBackgroundResource(R.drawable.cuadro_redondeado_gris);
            tvExistencias.setTextColor(Color.RED);
        }

        tvTalla.setText(tallaTemp);
        tvExistencias.setText(mensaje);


        return view;
    }





}
