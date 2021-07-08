package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kaliopeDavid.kaliopeclientespedidos.R;
import com.kaliopeDavid.kaliopeclientespedidos.models.Categorias;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolderCategorias> {

    private List<Categorias> list;

    public CategoriasAdapter(List<Categorias> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_categorias,
                parent,
                false
        );

        return new ViewHolderCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategorias holder, int position) {

        Categorias categorias = list.get(position);
        holder.setData(categorias);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderCategorias extends RecyclerView.ViewHolder {

        TextView title;
        TextView nombre1;
        TextView precio1;
        ImageView imagen1;
        TextView nombre2;
        TextView precio2;
        ImageView imagen2;
        TextView nombre3;
        TextView precio3;
        ImageView imagen3;
        TextView nombre4;
        TextView precio4;
        ImageView imagen4;
        Button verTodos;

        public ViewHolderCategorias(@NonNull View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.item_container_categorias_texTitle);
            nombre1 = (TextView) itemView.findViewById(R.id.item_container_categorias_textName1);
            precio1 = (TextView) itemView.findViewById(R.id.item_container_categorias_textPrice1);
            imagen1 = (ImageView) itemView.findViewById(R.id.item_container_categorias_image1);
            nombre2 = (TextView) itemView.findViewById(R.id.item_container_categorias_textName2);
            precio2 = (TextView) itemView.findViewById(R.id.item_container_categorias_textPrice2);
            imagen2 = (ImageView) itemView.findViewById(R.id.item_container_categorias_image2);
            nombre3 = (TextView) itemView.findViewById(R.id.item_container_categorias_textName3);
            precio3 = (TextView) itemView.findViewById(R.id.item_container_categorias_textPrice3);
            imagen3 = (ImageView) itemView.findViewById(R.id.item_container_categorias_image3);
            nombre4 = (TextView) itemView.findViewById(R.id.item_container_categorias_textName4);
            precio4 = (TextView) itemView.findViewById(R.id.item_container_categorias_textPrice4);
            imagen4 = (ImageView) itemView.findViewById(R.id.item_container_categorias_image4);
            verTodos = (Button) itemView.findViewById(R.id.item_container_categorias_botonTodos);
        }


        //seteamos la data en un metodo aqui para en el bind solo llamemos a este metodo por toda la informacion que tenemos que setear
        void setData(Categorias categorias){
                title.setText(categorias.getTitle());
                nombre1.setText(categorias.getNombre1());
                precio1.setText(categorias.getPrecio1());
                nombre2.setText(categorias.getNombre2());
                precio2.setText(categorias.getPrecio2());
                nombre3.setText(categorias.getNombre3());
                precio3.setText(categorias.getPrecio3());
                nombre4.setText(categorias.getNombre4());
                precio4.setText(categorias.getPrecio4());




                Glide.with(itemView)
                        .load(categorias.getUrl_image1())
                        .into(imagen1);

                Glide.with(itemView)
                        .load(categorias.getUrl_image2())
                        .into(imagen2);

                Glide.with(itemView)
                        .load(categorias.getUrl_image3())
                        .into(imagen3);

                Glide.with(itemView)
                        .load(categorias.getUrl_image4())
                        .into(imagen4);


                verTodos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(itemView.getContext(),"BotonVer todos precionado",Toast.LENGTH_SHORT).show();
                    }
                });

            }



    }
}
