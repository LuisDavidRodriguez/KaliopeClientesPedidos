package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.kaliopeDavid.kaliopeclientespedidos.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DetallesImagenAdapter extends RecyclerView.Adapter<DetallesImagenAdapter.ViewHolderImagen> {

    //Declaramos los nombres de los campos del HashMap que usaremos
    public static final String IMAGEN_NOMBRE = "nombre";
    public static final String IMAGEN_URL = "url";


    ArrayList<HashMap> listaUrlImagenes;

    public DetallesImagenAdapter(ArrayList<HashMap> listaUrlImagenes) {
        this.listaUrlImagenes = listaUrlImagenes;
    }

    @NonNull
    @Override
    public ViewHolderImagen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_imagen_ropa_detalles,
                parent,
                false
        );


        return new ViewHolderImagen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImagen holder, int position) {

        String url = listaUrlImagenes.get(position).get(IMAGEN_URL).toString();

        Glide.with(holder.imageView.getContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.error_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listaUrlImagenes.size();
    }

 

    public class ViewHolderImagen extends RecyclerView.ViewHolder {

        ImageView imageView;



        public ViewHolderImagen(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_imagen_ropa_detalles_IV_imagen);
        }

    }
}
