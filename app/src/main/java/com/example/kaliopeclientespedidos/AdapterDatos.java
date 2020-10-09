package com.example.kaliopeclientespedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {
    ArrayList<String> listDatos;

    public AdapterDatos(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //este lo que hara es enlazar este adaptador a nuestro layout item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        //se encargara de establecer la cominicacion entre nuestro adaptador y nuestro view holder datos
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }




    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView dato;

        public ViewHolderDatos(@NonNull View itemView) {

            super(itemView);
            dato  =itemView.findViewById(R.id.dato);
        }

        public void asignarDatos(String s) {
            dato.setText(s);
        }
    }
}
