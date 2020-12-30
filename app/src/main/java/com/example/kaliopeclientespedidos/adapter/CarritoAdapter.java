package com.example.kaliopeclientespedidos.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.cache.Resource;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolderCarrito> {

    ArrayList<HashMap> listaCarrito;



    public static final String ID_DATA = "idDataBase";
    public static final String ID_PRODUCTO = "idProducto";
    public static final String DESCRIPCION = "descripcion";
    public static final String CANTIDAD = "cantidad";
    public static final String TALLA = "talla";
    public static final String COLOR = "color";
    public static final String PRECIO_PUBLICO = "precioPublico";
    public static final String EMPRESARIA = "empresaria";
    public static final String SOCIA = "socia";
    public static final String VENDEDORA = "vendedora";
    public static final String INVERSIONISTA = "inversionista";
    public static final String GANANCIA = "ganacia";
    public static final String FORMA_PAGO = "formaDePago";      //credito, inversionista, regalo
    public static final String COMENTARIO_CREDITO = "comentarioCredito";
    public static final String COMENTARIO_INVERSIONISTA = "comentarioInversionsita";
    public static final String COMENTARIO_PUNTOS = "comentarioPuntos";
    public static final String GRADO_CLIENTE = "gradoCliente";
    public static final String IMAGEN_PERMANENTE = "imagenPermanente";





    public CarritoAdapter(ArrayList<HashMap> listaCarrito) {
        this.listaCarrito = listaCarrito;
    }

    @NonNull
    @Override
    public ViewHolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista de cada item
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_carrito,
                parent,
                false
        );

        return new ViewHolderCarrito(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, int position) {
            //es donde actualizamos la vista de cada item
        HashMap map = listaCarrito.get(position);

        holder.tvId.setText(map.get(ID_DATA).toString());
        holder.tvIdProducto.setText(map.get(ID_PRODUCTO).toString());
        holder.tvDescripcion.setText(map.get(DESCRIPCION).toString());
        holder.tvCantidad.setText(map.get(CANTIDAD).toString());
        holder.tvTalla.setText(map.get(TALLA).toString());
        holder.tvColor.setText(map.get(COLOR).toString());
        holder.tvPrecioPublico.setText(map.get(PRECIO_PUBLICO).toString());


        String gradoCliente = map.get(GRADO_CLIENTE).toString();
        String formaDePago = map.get(FORMA_PAGO).toString();

        int precioEtiqueta = 0;
        String precioDist = "0";
        int precioDistribucion = 0;
        int ganancia = 0;




        //obtenemos el precio de distribucion segun el grado o forma de pago del cliente y a su ves activamos los botones
        if(formaDePago.equals("CREDITO")){

            holder.activarBotonCredito();

            if(gradoCliente.equals("VENDEDORA")){
                precioDist = map.get(VENDEDORA).toString();

            }else if(gradoCliente.equals("SOCIA")){
                precioDist = map.get(SOCIA).toString();

            }else if(gradoCliente.equals("EMPRESARIA")){
                precioDist = map.get(EMPRESARIA).toString();
            }

        }else if(formaDePago.equals("INVERSIONISTA")){
            holder.activarBotonInversionista();
            precioDist = map.get(INVERSIONISTA).toString();

        }else if (formaDePago.equals("REGALO")){
            holder.activarBotonRegalo();
            precioDist = "0";
        }


        //calculamos la ganancia
        try {
            precioEtiqueta = Integer.parseInt(map.get(PRECIO_PUBLICO).toString());
            precioDistribucion = Integer.parseInt(precioDist);
            ganancia = precioEtiqueta - precioDistribucion;

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }



        holder.tvPrecioDistribucion.setText(precioDist);

        holder.tvGanancia.setText(String.valueOf(ganancia));


        String comentarioCredito = holder.itemView.getResources().getString(R.string.aPrecioDe) + gradoCliente;
        holder.tvComentarioCredito.setText(comentarioCredito);

        String comentarioInversionista = holder.itemView.getResources().getString(R.string.ganaMas);
        holder.tvComentarioInversionista.setText(comentarioInversionista);











        Glide.with(holder.itemView)
                .load(map.get(IMAGEN_PERMANENTE).toString())
                .into(holder.imagenPermanente);
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }





    public class ViewHolderCarrito extends RecyclerView.ViewHolder {
        //contendremos todas las vistas en este view Holder
        TextView tvId,
        tvIdProducto,
        tvDescripcion,
        tvCantidad,
        tvTalla,
        tvColor,
        tvPrecioPublico,
        tvPrecioDistribucion,
        tvGanancia,
        tvComentarioCredito,
        tvComentarioInversionista,
        tvComentarioPuntos;

        Button buttonCreditoKaliope,
        buttonInversionista,
        buttonPuntosKaliope;


        ImageView imagenPermanente;



        public ViewHolderCarrito(@NonNull View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.item_container_carrito_idBaseDatos);
            tvIdProducto = (TextView) itemView.findViewById(R.id.item_container_carrito_idProducto);
            tvDescripcion = (TextView) itemView.findViewById(R.id.item_container_carrito_descripcion);
            tvCantidad = (TextView) itemView.findViewById(R.id.item_container_carrito_cantidad);
            tvTalla = (TextView) itemView.findViewById(R.id.item_container_carrito_talla);
            tvColor = (TextView) itemView.findViewById(R.id.item_container_carrito_color);
            tvPrecioPublico = (TextView) itemView.findViewById(R.id.item_container_carrito_precioVenta);
            tvPrecioDistribucion = (TextView) itemView.findViewById(R.id.item_container_carrito_precioDistribucion);
            tvGanancia = (TextView) itemView.findViewById(R.id.item_container_carrito_ganancia);
            tvComentarioCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_comentarioCredito);
            tvComentarioInversionista = (TextView) itemView.findViewById(R.id.item_container_carrito_comentarioInversionista);
            tvComentarioPuntos = (TextView) itemView.findViewById(R.id.item_container_carrito_comentarioPuntos);
            buttonCreditoKaliope = (Button) itemView.findViewById(R.id.item_container_carrito_botonCredito);
            buttonInversionista = (Button) itemView.findViewById(R.id.item_container_carrito_botonInversionista);
            buttonPuntosKaliope = (Button) itemView.findViewById(R.id.item_container_carrito_botonRegalo);
            imagenPermanente = (ImageView) itemView.findViewById(R.id.item_container_carrito_imagen);




        }



        //creamos metodos auxiliares para activar el boton seleccionado
        public void activarBotonCredito(){

            buttonCreditoKaliope.setBackgroundResource(R.drawable.boton_redondo_credito);
            buttonInversionista.setBackgroundResource(R.drawable.boton_redondo_gris);
            buttonPuntosKaliope.setBackgroundResource(R.drawable.boton_redondo_gris);
        }

        public void activarBotonInversionista(){
            buttonCreditoKaliope.setBackgroundResource(R.drawable.boton_redondo_gris);
            buttonInversionista.setBackgroundResource(R.drawable.boton_redondo_inversionista);
            buttonPuntosKaliope.setBackgroundResource(R.drawable.boton_redondo_gris);
        }

        public void activarBotonRegalo(){
            buttonCreditoKaliope.setBackgroundResource(R.drawable.boton_redondo_gris);
            buttonInversionista.setBackgroundResource(R.drawable.boton_redondo_gris);
            buttonPuntosKaliope.setBackgroundResource(R.drawable.boton_redondo_regalo);
        }
    }





}
