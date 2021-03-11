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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.kaliopeclientespedidos.Constantes;
import com.example.kaliopeclientespedidos.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.client.cache.Resource;

import static com.example.kaliopeclientespedidos.Constantes.offline;

public class SpinnerColoresAdapter extends BaseAdapter {

    private List<HashMap<String, String>> objects;
    private Context context;


    public static final String NOMBRE_COLOR = "nombre_color";
    public static final String URL_IMAGEN = "url_imagen";
    public static final String RGB_COLOR_STRING = "rgb_color";
    public static final String EXISTENCIAS = "existencias";
    public static final String ACTIVO = "activo";

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
        TextView textViewDisponibilidad = (TextView) viewFila.findViewById(R.id.spinner_colores_item_TextViewDisponibilidad);
        ImageView imageView = (ImageView) viewFila.findViewById(R.id.spinner_colores_item_imageView);

        String nombre = objects.get(position).get(NOMBRE_COLOR).toString();
        String urlImagenColor = objects.get(position).get(URL_IMAGEN).toString();
        String colorRGB = objects.get(position).get(RGB_COLOR_STRING).toString();                //rgb(142,142,142)
        int existenciasTemp = Integer.parseInt(Objects.requireNonNull(objects.get(position).get(EXISTENCIAS)));
        boolean itemActivo = Boolean.parseBoolean(objects.get(position).get(ACTIVO).toString());

        String mensaje = "";

        if(itemActivo){
            if(existenciasTemp>0){
                mensaje =  (existenciasTemp>1)? existenciasTemp + " disponibles":"Ultimo disponible!";
                textViewColor.setTextColor(textViewColor.getResources().getColor(R.color.colorBlack));
                textViewDisponibilidad.setVisibility(View.VISIBLE);
                textViewDisponibilidad.setTextSize(14);
                textViewDisponibilidad.setTextColor(textViewColor.getResources().getColor(R.color.colorGris));


            }else{
                mensaje = "Color Agotado!";
                textViewColor.setTextColor(textViewColor.getResources().getColor(R.color.colorGris));
                textViewDisponibilidad.setVisibility(View.VISIBLE);
                textViewDisponibilidad.setTextSize(16);
                textViewDisponibilidad.setTextColor(Color.RED);
            }

        }else{
            mensaje = "Color Agotado!";
            textViewColor.setTextColor(textViewColor.getResources().getColor(R.color.colorGris));
            textViewDisponibilidad.setVisibility(View.VISIBLE);
            textViewDisponibilidad.setTextSize(16);
            textViewDisponibilidad.setTextColor(Color.RED);
        }


        textViewColor.setText(nombre);
        textViewDisponibilidad.setText(mensaje);

        if(nombre.length()>=8){                      //dependiendo del largo del nombre del color definimos el tamaño de la letra
            textViewColor.setTextSize(18);
        }else if(nombre.length()>=6){
            textViewColor.setTextSize(20);
        }else{
            textViewColor.setTextSize(24);
        }



        if(offline){
            Log.d(Constantes.TAG_OFFLINE,"Mostrando solo cuadritos con el color");
            //si estamos en modo offline solo mostramos los colores con un cuadrito de color
            imageView.setBackgroundColor(obtenerColorDesdeRGB(colorRGB));
        }else{
            //si estamos en online mostramos los colores con la imagen del producto
            Log.d(Constantes.TAG_ONLINE,"Mostrando y descargando imagenes de colores del producto");
            Glide.with(parent)
                    .load(urlImagenColor)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.error_image)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView);
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
