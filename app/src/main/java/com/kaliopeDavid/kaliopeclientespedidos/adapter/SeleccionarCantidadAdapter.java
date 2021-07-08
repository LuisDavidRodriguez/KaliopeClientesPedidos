package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaliopeDavid.kaliopeclientespedidos.R;

import java.util.ArrayList;

public class SeleccionarCantidadAdapter extends RecyclerView.Adapter<SeleccionarCantidadAdapter.ViewHolderCantidad> {
    ArrayList<Integer> list;

    public SeleccionarCantidadAdapter(ArrayList<Integer> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderCantidad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_cantidad,
                parent,
                false
        );
        return new ViewHolderCantidad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCantidad holder, int position) {


        try {
            String informacion = String.valueOf(list.get(position));
            holder.setInformacion(informacion);
        } catch (Exception e) {
            e.printStackTrace();
            holder.setInformacion("");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public int getItemValue(int position){
        return list.get(position);
    }




    public class ViewHolderCantidad extends RecyclerView.ViewHolder {

        TextView textViewCantidad;

        public ViewHolderCantidad(@NonNull View itemView) {
            super(itemView);
            textViewCantidad = (TextView) itemView.findViewById(R.id.item_container_cantidad);
        }

        void setInformacion(String informacion){
            textViewCantidad.setText(informacion);
        }
    }
}
