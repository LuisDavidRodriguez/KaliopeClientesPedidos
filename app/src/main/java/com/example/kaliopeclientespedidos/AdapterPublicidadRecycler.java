package com.example.kaliopeclientespedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

class AdapterPublicidadRecycler extends RecyclerView.Adapter<AdapterPublicidadRecycler.ViewHolderPublicidad>{

    ArrayList<HashMap> arrayList;
    Context context;

    public static final String ADAPTER_IMAGE_ID = "image_id";

    public AdapterPublicidadRecycler(ArrayList<HashMap> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPublicidad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //crear el inflado del item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_publicidad,null, false);
        return new ViewHolderPublicidad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicidad holder, int position) {
        //el llenado de las vistas
        int imageId =(int) arrayList.get(position).get(ADAPTER_IMAGE_ID);
        holder.imageView.setImageResource(imageId);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderPublicidad extends RecyclerView.ViewHolder {
        //el manejador de las vistas
        ImageView imageView;




        public ViewHolderPublicidad(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.list_publicidad_IV);
        }
    }
}
