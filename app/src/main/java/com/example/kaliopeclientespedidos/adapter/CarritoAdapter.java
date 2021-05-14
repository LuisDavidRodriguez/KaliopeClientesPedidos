package com.example.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.ConfiguracionesApp;
import com.example.kaliopeclientespedidos.KaliopeServerClient;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.utilidadesApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.cache.Resource;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolderCarrito> {

    JSONArray listaCarrito;
    /*
            {"carritoCliente":[{"id":"108","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Rosa","no_color":"rgb(240, 74, 141)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"109","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"110","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"111","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"}]

             */

    Activity activity;
    ProgressDialog progressDialog;

    TotalAdapter totalAdapter;          //solicitamos una referencia de nuestro totalAdapter para poder solicitar una actualizacion de ese adaptador


    Animation animationLatido;




    public static final String URL_EDICION_CARRITO = "app_movil/editar_pedido.php";





    public CarritoAdapter(JSONArray listaCarrito, Activity activity, TotalAdapter totalAdapter) {
        this.listaCarrito = listaCarrito;
        this.activity = activity;
        this.totalAdapter = totalAdapter;
    }

    public void cambiarJsonArray(JSONArray listaCarrito){
        this.listaCarrito = listaCarrito;
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, final int position) {

        /*
 {"carritoCliente":[{"id":"39","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"2","color":"Gris","no_color":"rgb(142, 142, 142)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":"280","imagen_permanente":"fotos\/SM5898-VERDE-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":41,"ganancia_inversion":59,"existencias_restantes":0,"apurate_confirmar":"Ya no hay piezas disponibles para surir tu pedido, otros clientes confirmaron su pedido antes que tu y agotaron las existencias"},
                {"id":"40","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"GRIS","no_color":"rgb(122, 125, 130)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":"280","imagen_permanente":"fotos\/ST5898-GRIS-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":49,"ganancia_inversion":79,"existencias_restantes":8,"apurate_confirmar":""},
                {"id":"41","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Gris","no_color":"rgb(142, 142, 142)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":"280","imagen_permanente":"fotos\/SM5898-VERDE-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":41,"ganancia_inversion":59,"existencias_restantes":0,"apurate_confirmar":"Ya no hay piezas disponibles para surir tu pedido, otros clientes confirmaron su pedido antes que tu y agotaron las existencias"},

         */


        try {
            Log.d("carritoAdapter", listaCarrito.getJSONObject(position).toString());
            //es donde actualizamos la vista de cada item
            final JSONObject jsonObject = listaCarrito.getJSONObject(position);           //nuestro map lo creamos aqui asi podremos tener una instancia para cada producto en la memoria que podremos modificar, si lo creamos como campo, esa posibilidad no es ya que solo abra una instancia que se reciclara en cada producto, esto nos sirve para poder cambiar la info en nuestro adapter

            String urlImagenPermanente = KaliopeServerClient.BASE_URL + jsonObject.getString("imagen_permanente");

            Glide.with(holder.itemView)
                    .load(urlImagenPermanente)
                    .thumbnail(.1f)                                     //Mostramos la imagen al 10% de su resolucion en el carrito hasta que la imagen este lista
                    .into(holder.imagenPermanente);

            holder.tvId.setText(jsonObject.getString("id"));
            holder.tvIdProducto.setText(jsonObject.getString("id_producto"));
            holder.tvDescripcion.setText(jsonObject.getString("descripcion"));
            holder.tvCantidad.setText(jsonObject.getString("cantidad"));
            holder.tvTalla.setText(jsonObject.getString("talla"));
            holder.tvColor.setText(jsonObject.getString("color"));
            holder.tvPrecioPublico.setText(jsonObject.getString("precio_etiqueta"));




            final String gradoCliente = jsonObject.getString("grado_cliente");
            final String formaDePago = jsonObject.getString("estado_producto");                     //aqui recibimos CREDITO INVERSION AGOTADO jaja yo se el agotado rompe las normas pero hay que hacerlo
            final String limiteDeCretido = jsonObject.getString("credito_cliente");
            final String idDataBase = jsonObject.getString("id");
            final String productoConfirmado = jsonObject.getString("producto_confirmado");          //obtenemos en texto true or false dependiendo de si el producto fue confirmado



            String ganancia = jsonObject.getString("ganancia");
            String gananciaInversion = jsonObject.getString("ganancia_inversion");


            //obtenemos el precio de distribucion segun el grado o forma de pago del cliente y a su ves activamos los botones
            if(formaDePago.equals("CREDITO")){

                holder.activarBotonCredito();
                holder.tvGanancia.setText(ganancia);

                if(gradoCliente.equals("VENDEDORA")){
                    holder.tvPrecioDistribucion.setText(jsonObject.getString("precio_vendedora"));

                }else if(gradoCliente.equals("SOCIA")){
                    holder.tvPrecioDistribucion.setText(jsonObject.getString("precio_socia"));

                }else if(gradoCliente.equals("EMPRESARIA")){
                    holder.tvPrecioDistribucion.setText(jsonObject.getString("precio_empresaria"));

                }

            }else if(formaDePago.equals("INVERSION")){
                holder.activarBotonInversion();
                holder.tvPrecioDistribucion.setText(jsonObject.getString("precio_inversionista"));
                holder.tvGanancia.setText(gananciaInversion);

            }else{
                //si el producto esta como agotado
                holder.marcarComoAgotado();
            }


            if(productoConfirmado.equals("true")){
                //si el producto ya esta confirmado ocultamos los botones y datos inecesarios
                holder.marcarComoConfirmado(formaDePago);
            }





            String comentarioCredito = holder.itemView.getResources().getString(R.string.aPrecioDe) + gradoCliente;
            holder.tvComentarioCredito.setText(comentarioCredito);

            String comentarioInversion = holder.itemView.getResources().getString(R.string.ganaMas);
            holder.tvComentarioInversion.setText(comentarioInversion);


            holder.tvApurateConfirmar.setText(jsonObject.getString("apurate_confirmar"));
            holder.tvEstatusDelProducto.setText(jsonObject.getString("seguimiento_producto"));









        /*===========El boton credito e inversion no se deberan conectar al servidor sino que mostraran los datos
        inmediatamente y hasta que se le de en confirmar ahora si se conectara al servidor para actualizar la informacion, tendre un problema
        podria confundir al cliente ya que al hacer los cambios inmediatamente el cliente vera su producto como inversion pero solo mientras
        esta en la actividad carrito, cuando se salga y vuelva a entrar se obtendra la lista nueva del servidor y el producto que puso
        como inversion antes de confirmar ahora aparecera como credito otra vez. lo mejor seria que al hacer el cambio se conecte al servidor
        y cambie ese producto a inversion.=========*/
            final String gananciaMensaje = String.valueOf(ganancia);
            final String gananciaInversionMensaje = String.valueOf(gananciaInversion);


            holder.buttonCreditoKaliope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String recurso = activity.getResources().getString(R.string.instrucciones_credito);
                    String mensajeCredito = String.format(recurso,gradoCliente,limiteDeCretido);


                    if (formaDePago.equals("INVERSION")) {              //solo mostramos el mensaje si la forma de pago no es credito

                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.cambiar_metodo_pago)
                                .setMessage(mensajeCredito)
                                .setPositiveButton(R.string.confirmar_cambio_credito, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        conectarServerActualizarMetodoPago(idDataBase, "CREDITO", jsonObject, position);
                                    }
                                }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();

                    }
                }
            });


            holder.buttonInversion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String recurso = activity.getResources().getString(R.string.instrucciones_inversion);
                    String mensajeInversion = String.format(recurso, gananciaMensaje, gananciaInversionMensaje);    //remplaso en mi mensaje que esta guardado en values String los aprametros que quiero

                    if (formaDePago.equals("CREDITO")) {                    //solo mostramos el mensaje si la forma de pago no es inversion
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.cambiar_metodo_pago)
                                .setMessage(mensajeInversion)
                                .setPositiveButton(R.string.confirmar_cambio_inversion, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        conectarServerActualizarMetodoPago(idDataBase, "INVERSION", jsonObject, position);

                                    }
                                })
                                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                    }
                }
            });


            /*===========Definimos la accion de los botones cada uno se debera conectar al servidor=========*/


            holder.imageButtonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.titulo_eliminar)
                            .setMessage(R.string.instrucciones_eliminar)
                            .setPositiveButton(R.string.titulo_eliminar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    conectarServerEliminarProducto(idDataBase, position);

                                }
                            })
                            .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return listaCarrito.length();
    }


    private int calgularGananciaDesdeString(String precioPublico, String precioDis){
        //calculamos la ganancia
        try {
            int precioEtiqueta = Integer.parseInt(precioPublico);
            int precioDistribucion = Integer.parseInt(precioDis);
            return precioEtiqueta - precioDistribucion;

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     *
     * @param idBaseDeDatos
     * @param metodoDePago
     * @param jsonObject   Recibimos el map que contiene la info del producto donde se modificara, esto para cambiar sus valores
     *              y poder cumplir con lo de la documentacion, notificaremos al adaptador que a cambiado los datos
     */
    private void conectarServerActualizarMetodoPago(String idBaseDeDatos, final String metodoDePago, final JSONObject jsonObject, final int position){
        RequestParams params = new RequestParams();
        params.put("OPERACION",1);      //para actualizar el metodo de pago el codigo de operacion sera 1
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(activity));      //para actualizar el metodo de pago el codigo de operacion sera 1
        params.put("ID_PRODUCTO", idBaseDeDatos);
        params.put("METODO_PAGO",metodoDePago);

        showProgresDialog(activity);

        KaliopeServerClient.post(URL_EDICION_CARRITO,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("responseCode1", String.valueOf(response));
               //responseCode1: {"resultado":"Exito","mensaje":"Hemos cambiado tu pago a INVERSION",
                //totales: {"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-09","suma_cantidad":5,"suma_credito":4,"suma_inversion":1,"cantidad_sin_confirmar":3,"suma_productos_etiqueta":2015,"suma_productos_inversion":360,"suma_productos_credito":1288,"suma_ganancia_cliente":367,"diferencia_credito":-112,"mensaje_diferencia_credito":"Aun dispones de $112 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras tu pedido en Inversion ganarias $367","mensaje_resumido_puntos":" + 300 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 300 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar": "Tienes 1 producto sin confirmar"}}
                //"mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}


                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONObject totalesJsonObject = response.getJSONObject("totales");
                    JSONObject mensajesTotales = response.getJSONObject("mensajesFinalTotales");
                    utilidadesApp.dialogoResultadoConexion(activity, resultado, mensaje);


                    if (resultado.compareToIgnoreCase("exito") == 0) {                        //comparamos la respuesta del servidor ignoramos las mayusculas por cualquier cosa, si es igual a exito entonces notificamos a nuestro adaptador para que actualice la imagen, en caso que el servidor tenga un error al modificar el item y retonre error entonces no notificamos al adaptador y no cambiamos el metodo de pago

                        jsonObject.put("estado_producto", metodoDePago);                //modificamos en el map el metodo de pago, este map contiene los datos del producto al cual se le hiso clicl en el boton cambiar metodo de pago, cambiamos su informacion
                        listaCarrito.put(position,jsonObject);                                       //renovamos en la lista de productos, el nuevo map modificado con la nueva forma de pago, usamos la posicion donde se hiso clicl para renovar solo ese map en la lista
                        //notifyDataSetChanged();                                                 //notificamos al adaptador que se cambiaron elementos de la lista, para que los actualice y los muestre al usuario
                        notifyItemChanged(position);                                            //notificamos al adaptadro que cambiamos solo un elementod de la lista para que solo actualice ese

                        totalAdapter.cambiarJsonObject(totalesJsonObject, mensajesTotales);                      //actualizara nuestro adapter totales


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //cuando por se recibe como respuesta un objeto que no puede ser convertido a jsonData
                //es decir si se conecta al servidor, pero desde el retornamos un echo de error
                //con un simple String lo recibimos en este metodo, crei que lo recibiria en el metodo onSUcces que tiene como parametro el responseString
                //pero parese que no, lo envia a este onFaiulure con Status Code

                //Si el nombre del archivo php esta mal para el ejemplo el correcto es: comprobar_usuario_app_kaliope.php
                // y el incorrecto es :comprobar_usuario_app_kaliop.php se llama a este metodo y entrega el codigo 404
                //lo que imprime en el log es un codigo http donde dice que <h1>Object not found!</h1>
                //            <p>
                //
                //
                //                The requested URL was not found on this server.
                //
                //
                //
                //                If you entered the URL manually please check your
                //                spelling and try again.
                //es decir si se encontro conexion al servidor y este respondio con ese mensaje
                //tambien si hay errores con alguna variable o algo asi, en este medio retorna el error como si lo viernas en el navegador
                //te dice la linea del error etc.


                String info = "Status Code: " + String.valueOf(statusCode) + "  responseString: " + responseString;
                Log.d("onFauile 1", info);
                //Toast.makeText(MainActivity.this,responseString + "  Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", responseString + "\nStatus Code: " + String.valueOf(statusCode));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                //cuando no se ha podido conectar con el servidor el statusCode=0 cz.msebera.android.httpclient.conn.ConnectTimeoutException: Connect to /192.168.1.10:8080 timed out
                //para simular esto estoy en un servidor local, obiamente el celular debe estar a la misma red, lo desconecte y lo movi a la red movil

                //cuando no hay coneccion a internet apagados datos y wifi se llama al metodo retry 5 veces y arroja la excepcion:
                // java.net.ConnectException: failed to connect to /192.168.1.10 (port 8080) from /:: (port 0) after 10000ms: connect failed: ENETUNREACH (Network is unreachable)


                //Si la url principal del servidor esta mal para simularlo cambiamos estamos a un servidor local con:
                //"http://192.168.1.10:8080/KALIOPE/" cambiamos la ip a "http://192.168.1.1:8080/KALIOPE/";
                //se llama al onRetry 5 veces y se arroja la excepcion en el log:
                //estatus code: 0 java.net.ConnectException: failed to connect to /192.168.1.1 (port 8080) from /192.168.1.71 (port 36134) after 10000ms: isConnected failed: EHOSTUNREACH (No route to host)
                //no hay ruta al Host

                //Si desconectamos el servidor de la ip antes la ip en el servidor de la computadora era 192.168.1.10, lo movimos a 192.168.1.1
                //genera lo mismo como si cambiaramos la ip en el programa android la opcion dew arriba. No
                //StatusCode0  Twhowable:   java.net.ConnectException: failed to connect to /192.168.1.10 (port 8080) from /192.168.1.71 (port 37786) after 10000ms: isConnected failed: EHOSTUNREACH (No route to host)
                //Llamo a reatry 5 veces


                String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.toString();
                Log.d("onFauile 2", info);
                //Toast.makeText(MainActivity.this,info, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", info);

            }


            @Override
            public void onRetry(int retryNo) {
                progressDialog.setMessage("Reintentando conexion No: " + String.valueOf(retryNo));
            }



        });
    }


    private void conectarServerEliminarProducto(String idBaseDeDatos, final int position){
        RequestParams params = new RequestParams();
        params.put("OPERACION",2);      //para eliminar un producto sera el el codigo 2
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(activity));      //para actualizar el metodo de pago el codigo de operacion sera 1
        params.put("ID_PRODUCTO", idBaseDeDatos);

        showProgresDialog(activity);

        KaliopeServerClient.post(URL_EDICION_CARRITO,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("responseCode1", String.valueOf(response));
                //D/responseCode1: {"resultado":"Exito","mensaje":"Hemos eliminado el producto de tu carrito",  totales: {"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-09","suma_cantidad":5,"suma_credito":4,"suma_inversion":1,"cantidad_sin_confirmar":3,"suma_productos_etiqueta":2015,"suma_productos_inversion":360,"suma_productos_credito":1288,"suma_ganancia_cliente":367,"diferencia_credito":-112,"mensaje_diferencia_credito":"Aun dispones de $112 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras tu pedido en Inversion ganarias $367","mensaje_resumido_puntos":" + 300 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 300 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar": "Tienes 1 producto sin confirmar"}}
                //"mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}
                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONObject totalesJsonObjet = response.getJSONObject("totales");
                    JSONObject mensajesTotales = response.getJSONObject("mensajesFinalTotales");
                    utilidadesApp.dialogoResultadoConexion(activity, resultado, mensaje);



                    if (resultado.compareToIgnoreCase("exito") == 0) {                        //comparamos la respuesta del servidor ignoramos las mayusculas por cualquier cosa, si es igual a exito entonces notificamos a nuestro adaptador para que actualice la imagen, en caso que el servidor tenga un error al modificar el item y retonre error entonces no notificamos al adaptador y no cambiamos el metodo de pago

                        Log.d("lista antes", String.valueOf(listaCarrito.length()));
                        Log.d("Posicion", String.valueOf(position));

                        listaCarrito.remove(position);                                         // Eliminamos de la lista ese item
                        notifyItemRemoved(position);                                           //notificamos al adaptador que eliminamos ese elemento para que lo deje de mostrar al usuario

                        totalAdapter.cambiarJsonObject(totalesJsonObjet,mensajesTotales);                       //notificamos a nuestro adaptador totales que le enviamos un nuevo jsonobhjet con los totales acutalizados por el servidor y que actualice las vistas

                        Log.d("lista Despues", String.valueOf(listaCarrito.length()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //cuando por se recibe como respuesta un objeto que no puede ser convertido a jsonData
                //es decir si se conecta al servidor, pero desde el retornamos un echo de error
                //con un simple String lo recibimos en este metodo, crei que lo recibiria en el metodo onSUcces que tiene como parametro el responseString
                //pero parese que no, lo envia a este onFaiulure con Status Code

                //Si el nombre del archivo php esta mal para el ejemplo el correcto es: comprobar_usuario_app_kaliope.php
                // y el incorrecto es :comprobar_usuario_app_kaliop.php se llama a este metodo y entrega el codigo 404
                //lo que imprime en el log es un codigo http donde dice que <h1>Object not found!</h1>
                //            <p>
                //
                //
                //                The requested URL was not found on this server.
                //
                //
                //
                //                If you entered the URL manually please check your
                //                spelling and try again.
                //es decir si se encontro conexion al servidor y este respondio con ese mensaje
                //tambien si hay errores con alguna variable o algo asi, en este medio retorna el error como si lo viernas en el navegador
                //te dice la linea del error etc.


                String info = "Status Code: " + String.valueOf(statusCode) + "  responseString: " + responseString;
                Log.d("onFauile 1", info);
                //Toast.makeText(MainActivity.this,responseString + "  Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", responseString + "\nStatus Code: " + String.valueOf(statusCode));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                //cuando no se ha podido conectar con el servidor el statusCode=0 cz.msebera.android.httpclient.conn.ConnectTimeoutException: Connect to /192.168.1.10:8080 timed out
                //para simular esto estoy en un servidor local, obiamente el celular debe estar a la misma red, lo desconecte y lo movi a la red movil

                //cuando no hay coneccion a internet apagados datos y wifi se llama al metodo retry 5 veces y arroja la excepcion:
                // java.net.ConnectException: failed to connect to /192.168.1.10 (port 8080) from /:: (port 0) after 10000ms: connect failed: ENETUNREACH (Network is unreachable)


                //Si la url principal del servidor esta mal para simularlo cambiamos estamos a un servidor local con:
                //"http://192.168.1.10:8080/KALIOPE/" cambiamos la ip a "http://192.168.1.1:8080/KALIOPE/";
                //se llama al onRetry 5 veces y se arroja la excepcion en el log:
                //estatus code: 0 java.net.ConnectException: failed to connect to /192.168.1.1 (port 8080) from /192.168.1.71 (port 36134) after 10000ms: isConnected failed: EHOSTUNREACH (No route to host)
                //no hay ruta al Host

                //Si desconectamos el servidor de la ip antes la ip en el servidor de la computadora era 192.168.1.10, lo movimos a 192.168.1.1
                //genera lo mismo como si cambiaramos la ip en el programa android la opcion dew arriba. No
                //StatusCode0  Twhowable:   java.net.ConnectException: failed to connect to /192.168.1.10 (port 8080) from /192.168.1.71 (port 37786) after 10000ms: isConnected failed: EHOSTUNREACH (No route to host)
                //Llamo a reatry 5 veces


                String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.toString();
                Log.d("onFauile 2", info);
                //Toast.makeText(MainActivity.this,info, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", info);

            }


            @Override
            public void onRetry(int retryNo) {
                progressDialog.setMessage("Reintentando conexion No: " + String.valueOf(retryNo));
            }



        });
    }


    private void showProgresDialog(Activity activity){

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Conectando al Servidor");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

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
                tvComentarioInversion,
        tvApurateConfirmar,
        tvEstatusDelProducto,
        tvEliminalo,
        tvSeleccionarFormaPago;

        Button buttonCreditoKaliope,
                buttonInversion;

        ImageButton imageButtonEliminar;


        ImageView imagenPermanente;

        CardView cardView;



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
            tvComentarioInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_comentarioInversion);
            buttonCreditoKaliope = (Button) itemView.findViewById(R.id.item_container_carrito_botonCredito);
            buttonInversion = (Button) itemView.findViewById(R.id.item_container_carrito_botonInversion);
            tvApurateConfirmar = (TextView) itemView.findViewById(R.id.item_container_carrito_textApurateConfirmar);
            imageButtonEliminar = (ImageButton) itemView.findViewById(R.id.item_container_carrito_botonEliminar);
            tvEstatusDelProducto = (TextView) itemView.findViewById(R.id.item_container_carrito_textSeguimientoDelProducto);
            tvEliminalo = (TextView) itemView.findViewById(R.id.item_container_carrito_textEliminalo);
            tvSeleccionarFormaPago = (TextView) itemView.findViewById(R.id.item_container_carrito_seleccionaFormaPagoTV);

            imagenPermanente = (ImageView) itemView.findViewById(R.id.item_container_carrito_imagen);
            cardView = (CardView) itemView.findViewById(R.id.item_container_carrito_cardView);


            animationLatido = AnimationUtils.loadAnimation(activity,R.anim.latido);
            animationLatido.setFillAfter(true);//para que se quede donde termina la anim
            animationLatido.setRepeatMode(Animation.REVERSE); //modo de repeticion, en el reverse se ejecuta la animacion y cuando termine de ejecutarse va  adar reversa
            animationLatido.setRepeatCount(Animation.INFINITE); //cuantas veces queremos que se repita la animacion, podria ser un numero entero 20 para 20 veces por ejemplo


        }



        //creamos metodos auxiliares para activar el boton seleccionado
        public void activarBotonCredito(){

            buttonCreditoKaliope.setBackgroundResource(R.drawable.boton_redondo_credito);
            buttonInversion.setBackgroundResource(R.drawable.boton_redondo_gris);
        }

        public void activarBotonInversion(){
            buttonCreditoKaliope.setBackgroundResource(R.drawable.boton_redondo_gris);
            buttonInversion.setBackgroundResource(R.drawable.boton_redondo_inversion);
        }

        public void marcarComoAgotado(){

            tvId.setTextColor(Color.GRAY);
            tvIdProducto.setTextColor(Color.GRAY);
            tvDescripcion.setTextColor(Color.GRAY);
            tvCantidad.setTextColor(Color.GRAY);
            tvTalla.setTextColor(Color.GRAY);
            tvColor.setTextColor(Color.GRAY);
            tvPrecioPublico.setTextColor(Color.GRAY);
            tvPrecioDistribucion.setVisibility(View.INVISIBLE);
            tvGanancia.setVisibility(View.INVISIBLE);
            tvComentarioCredito.setTextColor(Color.GRAY);
            tvComentarioInversion.setTextColor(Color.GRAY);
            cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.colorRepaso));
            buttonCreditoKaliope.setVisibility(View.GONE);
            buttonInversion.setVisibility(View.GONE);
            tvComentarioCredito.setVisibility(View.GONE);
            tvComentarioInversion.setVisibility(View.GONE);
            tvApurateConfirmar.setVisibility(View.GONE);

            tvSeleccionarFormaPago.setText(activity.getResources().getString(R.string.agotado));
            tvSeleccionarFormaPago.setTextColor(Color.RED);
            tvSeleccionarFormaPago.setTextSize(25);


            imageButtonEliminar.startAnimation(animationLatido);
            imageButtonEliminar.setVisibility(View.VISIBLE);
        }


        public void marcarComoConfirmado (String formaDePago){
            imageButtonEliminar.setVisibility(View.GONE);
            tvEliminalo.setVisibility(View.GONE);
            cardView.setCardBackgroundColor(Color.WHITE);



            tvId.setTextColor(Color.BLACK);
            tvIdProducto.setTextColor(Color.BLACK);
            tvDescripcion.setTextColor(Color.BLACK);
            tvCantidad.setTextColor(Color.BLACK);
            tvTalla.setTextColor(Color.BLACK);
            tvColor.setTextColor(Color.BLACK);
            tvPrecioPublico.setTextColor(Color.BLACK);
            tvPrecioDistribucion.setVisibility(View.VISIBLE);
            tvGanancia.setVisibility(View.VISIBLE);
            tvComentarioCredito.setTextColor(Color.BLACK);
            tvComentarioInversion.setTextColor(Color.BLACK);


           tvApurateConfirmar.setVisibility(View.GONE);
           tvSeleccionarFormaPago.setText(activity.getResources().getString(R.string.producto_confirmado));
           tvSeleccionarFormaPago.setTextColor(Color.GREEN);
           tvSeleccionarFormaPago.setTextSize(25);


            if(formaDePago.equals("INVERSION")){
                buttonInversion.setVisibility(View.VISIBLE);
                tvComentarioInversion.setVisibility(View.VISIBLE);
                buttonCreditoKaliope.setVisibility(View.INVISIBLE);
                tvComentarioCredito.setVisibility(View.INVISIBLE);
            }else{
                buttonInversion.setVisibility(View.INVISIBLE);
                tvComentarioInversion.setVisibility(View.INVISIBLE);

                buttonCreditoKaliope.setVisibility(View.VISIBLE);
                tvComentarioCredito.setVisibility(View.VISIBLE);

            }
        }






    }






}
