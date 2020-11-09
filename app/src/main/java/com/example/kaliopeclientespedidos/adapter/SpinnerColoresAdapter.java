package com.example.kaliopeclientespedidos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kaliopeclientespedidos.R;

import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.client.cache.Resource;

public class SpinnerColoresAdapter extends BaseAdapter {

    private List<HashMap<String, String>> objects;
    private Context context;


    public static final String NOMBRE_COLOR = "nombre_color";
    public static final String URL_IMAGEN = "url_imagen";
    public static final String RGB_COLOR_STRING = "rgb_color";

    public SpinnerColoresAdapter(@NonNull Context context, @NonNull List<HashMap<String, String>> objects) {
        this.objects = objects;
        this.context = context;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View viewFila = inflater.inflate(R.layout.spinnerColorItem, parent, false);
        View viewFila = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.spinner_colores_item,
                parent,
                false
        );


        TextView textViewColor = (TextView) viewFila.findViewById(R.id.spinner_colores_item_TextViewColor);
        ImageView imageView = (ImageView) viewFila.findViewById(R.id.spinner_colores_item_imageView);

        String nombre = objects.get(position).get(NOMBRE_COLOR).toString();
        String urlImagenColor = objects.get(position).get(URL_IMAGEN).toString();
        String colorRGB = objects.get(position).get(RGB_COLOR_STRING).toString();                //rgb(142,142,142)



        textViewColor.setText(nombre);


        boolean online = true;
        if(online){
            Glide.with(parent).load(urlImagenColor).apply(RequestOptions.centerCropTransform()).into(imageView);
        }else{
            imageView.setBackgroundColor(obtenerColorDesdeRGB(colorRGB));
        }

        return viewFila;

    }



    public int obtenerColorDesdeRGB(@Size(min = 1) String colorRGB){
        //rgb(142, 142, 142)
        int posicionPrimerParentesis = colorRGB.indexOf('(');
        int posicionPrimeraComa = colorRGB.indexOf(',');
        int posicionSegundaComa = colorRGB.indexOf(',',posicionPrimeraComa+1);
        int posicionUltimoParentecis = colorRGB.indexOf(')');

        Log.d("colorRGB", "1 " + String.valueOf(posicionPrimerParentesis));
        Log.d("colorRGB", "2 " + String.valueOf(posicionPrimeraComa));
        Log.d("colorRGB", "3 " + String.valueOf(posicionSegundaComa));
        Log.d("colorRGB", "4 " + String.valueOf(posicionUltimoParentecis));

        String rojo = colorRGB.substring(posicionPrimerParentesis + 1,posicionPrimeraComa);
        String verde = colorRGB.substring(posicionPrimeraComa + 2,posicionSegundaComa);         //a estas le sumamos 2 para saltarnos la posicion del espacio
        String azul = colorRGB.substring(posicionSegundaComa + 2,posicionUltimoParentecis);     //al igual a esta

        Log.d("colorRGB", "R" + rojo);
        Log.d("colorRGB", "G" + verde);
        Log.d("colorRGB", "B" + azul);



        return Color.rgb(Integer.parseInt(rojo),Integer.parseInt(verde),Integer.parseInt(azul));


    }







}
