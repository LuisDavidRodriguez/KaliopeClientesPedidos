package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kaliopeDavid.kaliopeclientespedidos.KaliopeServerClient;
import com.kaliopeDavid.kaliopeclientespedidos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListAdapterPreConfirmacion extends BaseAdapter {
    JSONArray productosPreconfirmacion;
    //[{"cantidad":"1","id_producto":"BR1008","descripcion":"BRASSIER DAMA","color":"AZULMARINO","precio_etiqueta":"189","imagen_permanente":"fotos\/BR1008-AZULMARINO-1.jpg","estado_producto":"CREDITO"},
    // {"cantidad":"2","id_producto":"PT1001","descripcion":"Pantaleta Dama","color":"CIELO","precio_etiqueta":"79","imagen_permanente":"fotos\/PT1001-CIELO-1.jpg","estado_producto":"INVERSION"},
    // {"cantidad":"1","id_producto":"BD1002","descripcion":"BLUSA DAMA","color":"ROSA","precio_etiqueta":"329","imagen_permanente":"fotos\/BD1002-ROSA-1.jpg","estado_producto":"INVERSION"}

    public ListAdapterPreConfirmacion(JSONArray productosPreconfirmacion) {
        this.productosPreconfirmacion = productosPreconfirmacion;
    }

    @Override
    public int getCount() {
        return productosPreconfirmacion.length();
    }

    @Override
    public Object getItem(int i) {

        try {

            return productosPreconfirmacion.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;


    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        //si es la primera vez que cargamos la lista hacemos el llamado a los metados pesados
        if(view==null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_container_pre_confirmacion_list_view,
                    viewGroup,
                    false
            );

            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.imageView3);
            holder.textViewModelo = (TextView) view.findViewById(R.id.pre_confirmacion_modelo);
            holder.textViewCantidad = (TextView) view.findViewById(R.id.pre_confirmacion_cantidad);
            holder.textViewPrecio = (TextView) view.findViewById(R.id.pre_confirmacion_precio);
            holder.textViewFormaPago = (TextView) view.findViewById(R.id.pre_confirmacion_formaPago);
            view.setTag(holder);            //no se que es esto xD

        }else{
            holder = (ViewHolder) view.getTag();    //tampoco se que onda xD esto lo hago por el link de abajo para eficientar el list
        }




        //holder.textViewModelo.setText("ajuuuaaa");

        try {
            JSONObject jsonObject = productosPreconfirmacion.getJSONObject(i);
            //textViewModelo.setText(jsonObject.getString("id_producto"));
            holder.textViewModelo.setText(jsonObject.getString("descripcion"));
            holder.textViewCantidad.setText(jsonObject.getString("cantidad"));
            holder.textViewPrecio.setText(jsonObject.getString("precio_etiqueta"));
            holder.textViewFormaPago.setText(jsonObject.getString("estado_producto"));
            String urlImagen = KaliopeServerClient.BASE_URL + jsonObject.getString("imagen_permanente");

            Glide.with(viewGroup)
                    .load(urlImagen)
                    .into(holder.imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }



    //Optimisamos la lista con un viewHolder https://naps.com.mx/blog/optimizar-un-listview-usando-viewholder/

   private static class ViewHolder{
        private ImageView imageView;
        private TextView textViewModelo, textViewCantidad,textViewPrecio,textViewFormaPago;
    }
}
