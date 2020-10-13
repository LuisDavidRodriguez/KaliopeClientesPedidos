package com.example.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.models.Ads;
import com.example.kaliopeclientespedidos.models.Categorias;
import com.example.kaliopeclientespedidos.models.Item;
import com.example.kaliopeclientespedidos.models.Producto;
import com.example.kaliopeclientespedidos.models.TodasCategorias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Crearemos en este mismo "Adaptador para un Recycler view"
 *
 * varias clases view Holder porque en el mismo adaptador
 * queremos mostrar varios tipos diferentes de vistas, a pesar que lo podriamos
 * hacer con concatAdapter me agrado un poco mas esta manera porque no tenemos que
 * hacer 4 adaptadores diferentes para cada tipo de item que queremos mostrar
 * sino solo creamos 1 y añadimos varios View Holder de diferente tipo
 *
 * solo nos falta ver si pdoremos manejar los eventos onCLick jeje
 *
 * auqnue creo que si seria algo bueno probar con concatAdapter
 * porque me estoy dando cuenta que se esta volviendo muy dificil de comprender
 * creo que es a las ventajas que se refieren con el concatAdapter
 *
 * Fuente:https://www.youtube.com/watch?v=GwK1Y_4cQWc
 */

class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Item> items;       //una lista de objetos de nuestra clase Item, nos ayudara a elegir que tipo de view holder mostrar en determinada posicion del recycler View

    public MenuAdapter(List<Item> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Aqui los typos son: 0=producto, 1=categorias, 2= anuncio, 3=TodasCategorias



        switch (viewType){
            case 0:
                //si la vista se trata de un item tipo producto entonces mostraremos esa vista
                return new ProductooViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_container_producto,
                                parent,
                                false)
                );





            case 1:
                //Si la vista se trata de un item con vista de categorias
                return new CategoriasViewHolde(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_container_categorias,
                                parent,
                                false
                        )
                );





            case 2:
                //si queremos mostrar una vista de tipo anuncio
                return new AdsViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_container_ads,
                                parent,
                                false
                        )
                );







            default:
                //si se trata de una vista de tipo categorias todas
                return new TodasssCategoriasViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_container_todascategorias,
                                parent,
                                false
                        )
                );







        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //aqui llenaremos las vistas pero llamando a los metodos de cada uno
        //Aqui los typos son: 0=producto, 1=categorias, 2= anuncio, 3=TodasCategorias


        if(getItemViewType(position) == 0){
            //creamos un contenedor del objeto al que equivale la vista
            Producto producto = (Producto) items.get(position).getObject();
            ((ProductooViewHolder) holder).setData(producto);
        }else if(getItemViewType(position) ==1){
            Categorias categorias = (Categorias) items.get(position).getObject();
            ((CategoriasViewHolde) holder).categoriasSetData(categorias);
        }else if(getItemViewType(position) ==2){
            Ads anuncio = (Ads) items.get(position).getObject();
            ((AdsViewHolder) holder).adsSetData(anuncio);
        }else{
            TodasCategorias todasCategorias = (TodasCategorias) items.get(position).getObject();
            ((TodasssCategoriasViewHolder) holder).todasCategoriasSetData(todasCategorias);
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //implementamos este metodo para seleccionar que tipo de vista mostrar
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();

        //este metodo se llama cuando el onCreateViewHolder se ejecuta y el viewType ahora tiene el valor de este metodo
    }


    //TODO ESTO LO HICIMOS PRIMERO HACIA ABAJO


    static class ProductooViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageProducto;
        private TextView  nombre, precio, existencias;

        public ProductooViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProducto =(ImageView) itemView.findViewById(R.id.item_container_producto_image);
            nombre = (TextView) itemView.findViewById(R.id.item_container_producto_textNombre);
            precio = (TextView) itemView.findViewById(R.id.item_container_producto_price);
            existencias = (TextView) itemView.findViewById(R.id.item_container_producto_existencias);
        }

        //lo hacemos como en el primer ejemplo con un metodo en esta clase para setar los datos
        void setData(Producto producto){
            String url_image_producto = producto.getUrl_image();

            nombre.setText(producto.getNombre());
            precio.setText(producto.getPrecio());
            existencias.setText(producto.getExistencias());

            Glide.with(itemView)
                    .load(url_image_producto)
                    .into(imageProducto);
        }
    }




    static class CategoriasViewHolde extends RecyclerView.ViewHolder{

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



        public CategoriasViewHolde(@NonNull View itemView) {
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


        void categoriasSetData(Categorias categorias){
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
                    .load(categorias.getUrl_image3())
                    .into(imagen4);


            verTodos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),"BotonVer todos precionado",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    static class AdsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;



        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_container_ads_image);
        }

        void adsSetData(Ads anuncio){
            Glide.with(itemView)
                    .load(anuncio.getUrl_image())
                    .into(imageView);
        }
    }


    static class TodasssCategoriasViewHolder extends  RecyclerView.ViewHolder{

        TextView title;
        ListView listView;
        ArrayList<HashMap> arrayListCategorias;


        public TodasssCategoriasViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_container_todascategorias_textTitle);
            listView = (ListView) itemView.findViewById(R.id.item_container_todascategorias_list);

        }

        void todasCategoriasSetData(TodasCategorias todasCategorias){
            title.setText(todasCategorias.getTitle());
            arrayListCategorias = todasCategorias.getArrayListCategorias();

        }
    }

}
