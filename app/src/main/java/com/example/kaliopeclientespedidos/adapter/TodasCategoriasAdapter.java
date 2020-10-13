package com.example.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.models.TodasCategorias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TodasCategoriasAdapter extends RecyclerView.Adapter<TodasCategoriasAdapter.ViewHolderTodas> {

    List<TodasCategorias> list;

    public TodasCategoriasAdapter(List<TodasCategorias> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TodasCategoriasAdapter.ViewHolderTodas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_todascategorias,
                parent,
                false
        );

        return new ViewHolderTodas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodasCategoriasAdapter.ViewHolderTodas holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderTodas extends RecyclerView.ViewHolder {
        TextView title;
        ListView listView;
        ArrayList<HashMap> arrayListCategorias;


        public ViewHolderTodas(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_container_todascategorias_textTitle);
            listView = (ListView) itemView.findViewById(R.id.item_container_todascategorias_list);
        }
    }
}
