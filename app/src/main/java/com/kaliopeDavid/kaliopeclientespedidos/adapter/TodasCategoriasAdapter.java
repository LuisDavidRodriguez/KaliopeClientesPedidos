package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaliopeDavid.kaliopeclientespedidos.R;

import java.util.ArrayList;

public class TodasCategoriasAdapter extends RecyclerView.Adapter<TodasCategoriasAdapter.ViewHolderTodas> {

    ArrayList<String> arrayListCategorias;

    public TodasCategoriasAdapter(ArrayList<String> arrayListCategorias) {
        this.arrayListCategorias = arrayListCategorias;
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
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return arrayListCategorias.size();
    }

    public class ViewHolderTodas extends RecyclerView.ViewHolder {
        TextView title;
        ListView listView;


        public ViewHolderTodas(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_container_todascategorias_textTitle);
            listView = (ListView) itemView.findViewById(R.id.item_container_todascategorias_list);


        }

        void setData(){

            title.setText("Todas las categorias");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(),android.R.layout.simple_list_item_1,arrayListCategorias);
            listView.setAdapter(adapter);

        }
    }
}
