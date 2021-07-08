package com.kaliopeDavid.kaliopeclientespedidos;

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

    //definimos un listener para poder hacer algo cuando se toca la publicidad, en este caso aun no se como hacer para que sea en un item en espesifico
    //yp supongo que va a ser implementandolo como en un recyclerView como interfas y todo el jale
    View.OnClickListener listener;
    View.OnTouchListener onTouchListener;

    public AdapterPublicidad(Context context, int[] imagenes, View.OnClickListener listener, View.OnTouchListener onTouchListener) {
        this.context = context;
        this.imagenes = imagenes;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.onTouchListener = onTouchListener;
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

        convertView.setOnClickListener(listener);
        convertView.setOnTouchListener(onTouchListener);


        return convertView;
    }
}
