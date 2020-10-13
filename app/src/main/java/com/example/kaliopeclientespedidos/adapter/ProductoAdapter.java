package com.example.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.models.Producto;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolderProducto> {

    private List<Producto> list;

    public ProductoAdapter(List<Producto> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_producto,
                parent,
                false
        );

        return new ViewHolderProducto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProducto holder, int position) {

        Producto producto = list.get(position);

        String url_image_producto = producto.getUrl_image();

        holder.nombre.setText(producto.getNombre());
        holder.precio.setText(producto.getPrecio());
        holder.existencias.setText(producto.getExistencias());

        Glide.with(holder.itemView)
                .load(url_image_producto)
                .into(holder.imageProducto);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderProducto extends RecyclerView.ViewHolder {
        private ImageView imageProducto;
        private TextView nombre, precio, existencias;



        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);

            imageProducto =(ImageView) itemView.findViewById(R.id.item_container_producto_image);
            nombre = (TextView) itemView.findViewById(R.id.item_container_producto_textNombre);
            precio = (TextView) itemView.findViewById(R.id.item_container_producto_price);
            existencias = (TextView) itemView.findViewById(R.id.item_container_producto_existencias);
        }


    }
}
