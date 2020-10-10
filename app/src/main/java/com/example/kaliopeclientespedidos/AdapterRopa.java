package com.example.kaliopeclientespedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.HashMap;

class AdapterRopa extends RecyclerView.Adapter<AdapterRopa.ViewHolderRopa> {

    ArrayList<HashMap> listaProductos;
    Context context;

    public AdapterRopa(ArrayList<HashMap> listaProductos, Context context) {
        this.listaProductos = listaProductos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderRopa onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ropa,null,false);
        return new ViewHolderRopa(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRopa holder, int position) {
        //aqui actualizaremos los datos en nuestros textView,
        //a diferencia del ejemplo con AdapterDatos donde pusimos un metodo



        //Sacamos los datos del array list



            holder.nombre.setText(listaProductos.get(position).get(MainActivityRecycler.ADAPTER_COLUMN_NOMBRE).toString());
            holder.price.setText(listaProductos.get(position).get(MainActivityRecycler.ADAPTER_COLUMN_PRICE).toString());
            holder.existencias.setText(listaProductos.get(position).get(MainActivityRecycler.ADAPTER_COLUMN_EXISTENCIAS).toString());
            String URL = listaProductos.get(position).get(MainActivityRecycler.ADAPTER_COLUMN_IMAGE_PREVIEW).toString();

        Glide.with(context)
                .load(URL)
                .thumbnail(Glide.with(context).load(R.drawable.loading2)).transform(new RoundedCorners(15)).fitCenter()
                .into(holder.imagen);

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ViewHolderRopa extends RecyclerView.ViewHolder {

        TextView nombre, price, existencias;
        ImageView imagen;

        public ViewHolderRopa(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.itemList_Tv_nombre);
            price = itemView.findViewById(R.id.itemList_TV_price);
            existencias = itemView.findViewById(R.id.itemList_TV_existencias);
            imagen = itemView.findViewById(R.id.itemListIV);
        }
    }
}
