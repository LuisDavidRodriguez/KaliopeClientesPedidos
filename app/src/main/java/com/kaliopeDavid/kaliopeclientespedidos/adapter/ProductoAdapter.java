package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.kaliopeDavid.kaliopeclientespedidos.R;
import com.kaliopeDavid.kaliopeclientespedidos.models.Producto;

import java.util.List;


/*
Para crear un On >CLick listener al aprecer de la mejor practica y manera mas eficiente
https://www.youtube.com/watch?v=69C1ljfDvl0
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolderProducto> {

    private List<Producto> list;
    //====9=====
    private OnProductListener myOnproductListener;

    //====10==== Modificamso el constructor para solicitar como parametro el listener
    public ProductoAdapter(List<Producto> list, OnProductListener onProductListener) {
        this.list = list;
        this.myOnproductListener = onProductListener;
    }

    @NonNull
    @Override
    public ViewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_producto,
                parent,
                false
        );

        //====11====
        return new ViewHolderProducto(view,myOnproductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProducto holder, int position) {

        Producto producto = list.get(position);

        final String url_image_producto = producto.getUrl_image();

        holder.nombre.setText(producto.getNombre());
        String precio = "$" + producto.getPrecio();
        holder.precio.setText(precio);
        holder.modelo.setText(producto.getId());
        if(producto.getExistencias()>0){
            int existencias = producto.getExistencias();
            String mensaje = (existencias>1? String.valueOf(existencias) + " disponibles": "Ultimo disponible!");

            holder.existencias.setTextColor(Color.GRAY);
            holder.existencias.setTextSize(14);
            holder.existencias.setText(mensaje);

            holder.nombre.setTextColor(Color.BLACK);
            holder.precio.setTextColor(Color.BLACK);
        }else{
            holder.existencias.setTextColor(Color.RED);
            holder.existencias.setTextSize(18);
            holder.existencias.setText("Producto agotado");

            holder.nombre.setTextColor(Color.LTGRAY);
            holder.precio.setTextColor(Color.LTGRAY);
        }





                Glide.with(holder.itemView)
                        .load(url_image_producto)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.error_image)
                        .thumbnail(0.5f)
                        .into(holder.imageProducto);

/*
        //https://heartbeat.fritz.ai/using-glide-to-efficiently-load-images-in-android-aec0ba9639ea
        //.skipMemoryCache(true)            para que no guarde las nuevas imagenes en la cache pero las que ya esten guardadas las v a aseguir cargando de ahi
        // .diskCacheStrategy(DiskCacheStrategy.NONE)   para no recuperar nada de la cache
        //// invalidate the disk cache.
        ////This method should always be called on a background thread
        //Glide.get(this).clearDiskCache()
        //
        //// invalidate the memory cache.
        //Glide.get(this).clearMemory()
        /*
        RequestOptions options = new RequestOptions()
                .signature(currentItem.getSignature())
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(imageView.getContext())
                .load(currentItem.getUri())
                .apply(options)
                .thumbnail(0.5f)
                .into(imageView);
         */


    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolderProducto extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageProducto;
        private TextView nombre, precio, existencias, modelo;

        //====5=====
        OnProductListener onProductListener;


        //====6==== solicitar ese onProductListener como parametro del constructor del view Holder
        public ViewHolderProducto(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);

            imageProducto =(ImageView) itemView.findViewById(R.id.item_container_producto_image);
            nombre = (TextView) itemView.findViewById(R.id.item_container_producto_textNombre);
            precio = (TextView) itemView.findViewById(R.id.item_container_producto_price);
            existencias =(TextView) itemView.findViewById(R.id.item_container_producto_existencias);
            modelo = (TextView) itemView.findViewById(R.id.item_container_producto_modelo);




            //=====7====
            this.onProductListener = onProductListener;

            //====4====
            //aqui relacionamos o enlasamos el escuchador a el item que toca, porque es una iteracion aqui recordar
            itemView.setOnClickListener(this);
        }


        //====3=====
        //este metodo fue el que implementamos y sobreescribiremos para que escuche nuestros clics
        //ahora enlasaremos nuestro recycler view completo al click listener para que este escuchando en todos los item el evento
        @Override
        public void onClick(View v) {

            //====8====
            //Ahora queremos que en este onclick se llame al nuestro y le pasamos la psoicion del adaptador
            onProductListener.onProductClick(getAbsoluteAdapterPosition());
        }
    }

    //====1====
    //para crear el onclick listener
    //ya declaramos esta interfas con el metodo abstracto ahora debemos hacer que este escuchando los clic en neustro recycler
    //y nos vamos a la clase ViewHolderProducto e implementamos la clase On Click Listener, y sobre escribimos los metodos
    // el paso 2 se hace en el mainActivytyRecycler
    public interface OnProductListener{
        void onProductClick(int position);
    }
}
