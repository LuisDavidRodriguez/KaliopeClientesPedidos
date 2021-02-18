package com.example.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.ConfiguracionesApp;
import com.example.kaliopeclientespedidos.KaliopeServerClient;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.utilidadesApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolderCarrito> {

    ArrayList<HashMap> listaCarrito;

    Activity activity;
    ProgressDialog progressDialog;

    TotalAdapter totalAdapter;          //solicitamos una referencia de nuestro totalAdapter para poder solicitar una actualizacion de ese adaptador



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
    public static final String INVERSION = "inversion";
    public static final String GANANCIA = "ganacia";
    public static final String FORMA_PAGO = "formaDePago";      //credito, inversion
    public static final String COMENTARIO_CREDITO = "comentarioCredito";
    public static final String COMENTARIO_INVERSION = "comentarioInversionsita";
    public static final String COMENTARIO_APURATE_CONFIRMAR = "apurateConfirmar";       //quedan 33 piezas en existencias apurate a confirmar
    public static final String GRADO_CLIENTE = "gradoCliente";
    public static final String LIMITE_CREDITO = "limiteCredito";
    public static final String IMAGEN_PERMANENTE = "imagenPermanente";
    public static final String PRODUCTO_CONFIRMADO = "productoConfirmado";//false true
    public static final String SEGUIMIENTO_PRODUCTO = "segimientoProducto";//producto sin confirmar, producto confirmado surtiendoce en almacen etc




    public static final String URL_EDICION_CARRITO = "app_movil/editar_pedido.php";





    public CarritoAdapter(ArrayList<HashMap> listaCarrito, Activity activity, TotalAdapter totalAdapter) {
        this.listaCarrito = listaCarrito;
        this.activity = activity;
        this.totalAdapter = totalAdapter;
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
            //es donde actualizamos la vista de cada item
        HashMap map = listaCarrito.get(position);           //nuestro map lo creamos aqui asi podremos tener una instancia para cada producto en la memoria que podremos modificar, si lo creamos como campo, esa posibilidad no es ya que solo abra una instancia que se reciclara en cada producto, esto nos sirve para poder cambiar la info en nuestro adapter


        Glide.with(holder.itemView)
                .load(map.get(IMAGEN_PERMANENTE).toString())
                .into(holder.imagenPermanente);

        holder.tvId.setText(map.get(ID_DATA).toString());
        holder.tvIdProducto.setText(map.get(ID_PRODUCTO).toString());
        holder.tvDescripcion.setText(map.get(DESCRIPCION).toString());
        holder.tvCantidad.setText(map.get(CANTIDAD).toString());
        holder.tvTalla.setText(map.get(TALLA).toString());
        holder.tvColor.setText(map.get(COLOR).toString());
        holder.tvPrecioPublico.setText(map.get(PRECIO_PUBLICO).toString());


        final String gradoCliente = map.get(GRADO_CLIENTE).toString();
        final String formaDePago = map.get(FORMA_PAGO).toString();


        String precioDist = "0";
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

        }else if(formaDePago.equals("INVERSION")){
            holder.activarBotonInversion();
            precioDist = map.get(INVERSION).toString();
        }
        //calculamos la ganancia
        ganancia = calgularGananciaDesdeString(map.get(PRECIO_PUBLICO).toString(),precioDist);
        int gananciaInversion = calgularGananciaDesdeString(map.get(PRECIO_PUBLICO).toString(), map.get(INVERSION).toString());     //obtenemos aun asi la ganancia de inversion solo para usarla en los mensajes de motivacion



        holder.tvPrecioDistribucion.setText(precioDist);

        holder.tvGanancia.setText(String.valueOf(ganancia));


        String comentarioCredito = holder.itemView.getResources().getString(R.string.aPrecioDe) + gradoCliente;
        holder.tvComentarioCredito.setText(comentarioCredito);

        String comentarioInversion = holder.itemView.getResources().getString(R.string.ganaMas);
        holder.tvComentarioInversion.setText(comentarioInversion);




        holder.tvApurateConfirmar.setText(map.get(COMENTARIO_APURATE_CONFIRMAR).toString());
        holder.tvEstatusDelProducto.setText(map.get(SEGUIMIENTO_PRODUCTO).toString());


        if(map.get(PRODUCTO_CONFIRMADO).toString().equals("true")){
            //si el producto ya esta confirmado ocultamos los botones y datos inecesarios
            holder.productoConfirmado();
        }






        /*===========El boton credito e inversion no se deberan conectar al servidor sino que mostraran los datos
        inmediatamente y hasta que se le de en confirmar ahora si se conectara al servidor para actualizar la informacion, tendre un problema
        podria confundir al cliente ya que al hacer los cambios inmediatamente el cliente vera su producto como inversion pero solo mientras
        esta en la actividad carrito, cuando se salga y vuelva a entrar se obtendra la lista nueva del servidor y el producto que puso
        como inversion antes de confirmar ahora aparecera como credito otra vez. lo mejor seria que al hacer el cambio se conecte al servidor
        y cambie ese producto a inversion.=========*/
        final HashMap mapFinal = map;
        final String gananciaMensaje = String.valueOf(ganancia);
        final String gananciaInversionMensaje = String.valueOf(gananciaInversion);


        holder.buttonCreditoKaliope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recurso = activity.getResources().getString(R.string.instrucciones_credito);
                String mensajeCredito = String.format(recurso,gradoCliente, mapFinal.get(LIMITE_CREDITO).toString());


                if (formaDePago.equals("INVERSION")) {              //solo mostramos el mensaje si la forma de pago no es credito

                    new android.app.AlertDialog.Builder(activity)
                            .setTitle(R.string.cambiar_metodo_pago)
                            .setMessage(mensajeCredito)
                            .setPositiveButton(R.string.confirmar_cambio_credito, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    conectarServerActualizarMetodoPago(mapFinal.get(ID_DATA).toString(), "CREDITO", mapFinal, position);
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
                                    conectarServerActualizarMetodoPago(mapFinal.get(ID_DATA).toString(), "INVERSION", mapFinal, position);

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
                                conectarServerEliminarProducto(mapFinal.get(ID_DATA).toString(), position);

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


    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
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
     * @param map   Recibimos el map que contiene la info del producto donde se modificara, esto para cambiar sus valores
     *              y poder cumplir con lo de la documentacion, notificaremos al adaptador que a cambiado los datos
     */
    private void conectarServerActualizarMetodoPago(String idBaseDeDatos, final String metodoDePago, final HashMap map, final int position){
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
                // "totales":{"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-23","suma_cantidad":2,"suma_credito":1,"suma_inversion":1,"suma_productos_etiqueta":678,"suma_productos_inversion":280,"suma_productos_credito":298,"suma_ganancia_cliente":100,"diferencia_credito":-1102,"mensaje_diferencia_credito":"Aun dispones de $1102 en tu credito Kaliope","mensaje_todo_inversion":"","mensaje_resumido_puntos":" + 100 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 100 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido."}}


                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONObject totalesJsonObject = response.getJSONObject("totales");
                    utilidadesApp.dialogoResultadoConexion(activity, resultado, mensaje);


                    if (resultado.compareToIgnoreCase("exito") == 0) {                        //comparamos la respuesta del servidor ignoramos las mayusculas por cualquier cosa, si es igual a exito entonces notificamos a nuestro adaptador para que actualice la imagen, en caso que el servidor tenga un error al modificar el item y retonre error entonces no notificamos al adaptador y no cambiamos el metodo de pago

                        map.put(FORMA_PAGO, metodoDePago);                                      //modificamos en el map el metodo de pago, este map contiene los datos del producto al cual se le hiso clicl en el boton cambiar metodo de pago, cambiamos su informacion
                        listaCarrito.set(position, map);                                         //renovamos en la lista de productos, el nuevo map modificado con la nueva forma de pago, usamos la posicion donde se hiso clicl para renovar solo ese map en la lista
                        //notifyDataSetChanged();                                                 //notificamos al adaptador que se cambiaron elementos de la lista, para que los actualice y los muestre al usuario
                        notifyItemChanged(position);                                            //notificamos al adaptadro que cambiamos solo un elementod de la lista para que solo actualice ese

                        totalAdapter.cambiarJsonObject(totalesJsonObject);                      //actualizara nuestro adapter totales


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
                //D/responseCode1: {"resultado":"Exito","mensaje":"Hemos eliminado el producto de tu carrito","totales":{"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-23","suma_cantidad":1,"suma_credito":0,"suma_inversion":1,"suma_productos_etiqueta":339,"suma_productos_inversion":280,"suma_productos_credito":0,"suma_ganancia_cliente":59,"diferencia_credito":-1400,"mensaje_diferencia_credito":"Aun dispones de $1400 en tu credito Kaliope","mensaje_todo_inversion":"","mensaje_resumido_puntos":"+0 puntos Kaliope","mensaje_completo_puntos":"No has ganado puntos Kaliope en este pedido, pedido minimo para puntos son $500"}}

                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONObject totalesJsonObjet = response.getJSONObject("totales");
                    utilidadesApp.dialogoResultadoConexion(activity, resultado, mensaje);



                    if (resultado.compareToIgnoreCase("exito") == 0) {                        //comparamos la respuesta del servidor ignoramos las mayusculas por cualquier cosa, si es igual a exito entonces notificamos a nuestro adaptador para que actualice la imagen, en caso que el servidor tenga un error al modificar el item y retonre error entonces no notificamos al adaptador y no cambiamos el metodo de pago

                        Log.d("lista antes", String.valueOf(listaCarrito.size()));
                        Log.d("Posicion", String.valueOf(position));

                        listaCarrito.remove(position);                                         // Eliminamos de la lista ese item
                        notifyItemRemoved(position);                                           //notificamos al adaptador que eliminamos ese elemento para que lo deje de mostrar al usuario

                        totalAdapter.cambiarJsonObject(totalesJsonObjet);                       //notificamos a nuestro adaptador totales que le enviamos un nuevo jsonobhjet con los totales acutalizados por el servidor y que actualice las vistas

                        Log.d("lista Despues", String.valueOf(listaCarrito.size()));
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
        tvExtraTextoO,
        tvExtraTextoEliminalo;

        Button buttonCreditoKaliope,
                buttonInversion,
        buttonConfirmar;

        ImageButton imageButtonEliminar;


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
            tvComentarioInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_comentarioInversion);
            buttonCreditoKaliope = (Button) itemView.findViewById(R.id.item_container_carrito_botonCredito);
            buttonInversion = (Button) itemView.findViewById(R.id.item_container_carrito_botonInversion);
            tvApurateConfirmar = (TextView) itemView.findViewById(R.id.item_container_carrito_textApurateConfirmar);
            imageButtonEliminar = (ImageButton) itemView.findViewById(R.id.item_container_carrito_botonEliminar);
            tvEstatusDelProducto = (TextView) itemView.findViewById(R.id.item_container_carrito_textSeguimientoDelProducto);
            tvExtraTextoEliminalo = (TextView) itemView.findViewById(R.id.item_container_carrito_textEliminalo);

            imagenPermanente = (ImageView) itemView.findViewById(R.id.item_container_carrito_imagen);




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


        public void productoConfirmado(){
            tvApurateConfirmar.setVisibility(View.GONE);
            imageButtonEliminar.setVisibility(View.GONE);
            buttonConfirmar.setVisibility(View.GONE);
            tvExtraTextoO.setVisibility(View.GONE);
            tvExtraTextoEliminalo.setVisibility(View.GONE);
        }




    }






}
