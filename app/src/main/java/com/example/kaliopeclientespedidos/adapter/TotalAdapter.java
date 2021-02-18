package com.example.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kaliopeclientespedidos.ConfiguracionesApp;
import com.example.kaliopeclientespedidos.KaliopeServerClient;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.utilidadesApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolderCarrito> {

   JSONObject jsonObjectTotales;
   Activity activity;

   CarritoAdapter carritoAdapter;                   //necesitamos una referencia del adaptador que lista los productos del carrito para poder enviarle desde auqi la nueva lista de productos confirmados y notificarle el cambio al adaptador

   ProgressDialog progressDialog;


    public TotalAdapter(JSONObject jsonObjectTotales, Activity activity) {
        this.jsonObjectTotales = jsonObjectTotales;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_carrito_totales,
                parent,
                false);

        return new ViewHolderCarrito(view);
    }

    public void cambiarJsonObject(JSONObject totales){
        jsonObjectTotales = totales;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, final int position) {


        //D/totales: {"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-09","suma_cantidad":5,"suma_credito":4,"suma_inversion":1,"suma_productos_etiqueta":2015,"suma_productos_inversion":360,"suma_productos_credito":1288,"suma_ganancia_cliente":367,"diferencia_credito":-112,"mensaje_diferencia_credito":"Aun dispones de $112 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $367","mensaje_resumido_puntos":" + 300 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 300 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido."}
        //no usamos la variable posicion porque aqui solo habra un item totales
        try {


            holder.cuentaCliente.setText(jsonObjectTotales.getString("cuenta"));
            holder.nombreCliente.setText(jsonObjectTotales.getString("nombre"));
            holder.limiteCredito.setText(jsonObjectTotales.getString("limite_credito"));
            holder.gradoCliente.setText(jsonObjectTotales.getString("grado"));
            holder.diasCredito.setText(jsonObjectTotales.getString("dias"));
            holder.zona.setText(jsonObjectTotales.getString("ruta"));
            holder.numeroPedido.setText(jsonObjectTotales.getString("numero_pedido"));
            holder.fechaEntrega.setText(jsonObjectTotales.getString("fecha_entrega"));
            holder.piezasTotales.setText(jsonObjectTotales.getString("suma_cantidad"));
            holder.piezasCredito.setText(jsonObjectTotales.getString("suma_credito"));
            holder.importeCredito.setText(jsonObjectTotales.getString("suma_productos_credito"));
            holder.piezasInversion.setText(jsonObjectTotales.getString("suma_inversion"));
            holder.importeInversion.setText(jsonObjectTotales.getString("suma_productos_inversion"));
            holder.gananciaTotal.setText(jsonObjectTotales.getString("suma_ganancia_cliente"));
            holder.puntosCorto.setText(jsonObjectTotales.getString("mensaje_resumido_puntos"));
            holder.mensajeLimiteCredito.setText(jsonObjectTotales.getString("mensaje_diferencia_credito"));
            holder.mensajeTodoInversion.setText(jsonObjectTotales.getString("mensaje_todo_inversion"));



            //Dependiendo de si la cantidad es 0 entonces mostramos la imagen de la bolsita triste y si no mostramos los totales
            if(jsonObjectTotales.getString("suma_cantidad").equals("0")){
                holder.constraintLayoutCarritoVacio.setVisibility(View.VISIBLE);
                holder.constraintLayoutTotales.setVisibility(View.GONE);
            }else{
                holder.constraintLayoutCarritoVacio.setVisibility(View.GONE);
                holder.constraintLayoutTotales.setVisibility(View.VISIBLE);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }




        holder.confirmarEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conectarServerConfirmarPedido(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 1;           //ensimamos a 1 el total de items porque solo se desplegara un solo total
    }


    public void setCarritoAdapterReferencia(CarritoAdapter carritoAdapter){
        this.carritoAdapter = carritoAdapter;
    }


    private void conectarServerConfirmarPedido(final int position){
        RequestParams params = new RequestParams();
        params.put("OPERACION",3);      //para confirmar todo el carrito sera 3
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(activity));

        showProgresDialog(activity);

        KaliopeServerClient.post(CarritoAdapter.URL_EDICION_CARRITO,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("responseCode1", String.valueOf(response));
                //D/responseCode1: {"resultado":"Exito","mensaje":"Hemos confirmado la totalidad de tu pedido, apartamos las existencias del almacen para tu pedido",
                // "carritoCliente":[{"id":"35","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Rosa","no_color":"rgb(240, 74, 141)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":"280","imagen_permanente":"fotos\/SM5898-ROSA-1.jpg","producto_confirmado":"1","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado"}],
                // "totales":{"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-23","suma_cantidad":1,"suma_credito":1,"suma_inversion":0,"suma_productos_etiqueta":339,"suma_productos_inversion":0,"suma_productos_credito":298,"suma_ganancia_cliente":41,"diferencia_credito":-1102,"mensaje_diferencia_credito":"Aun dispones de $1102 en tu credito Kaliope","mensaje_todo_inversion":"","mensaje_resumido_puntos":"+0 puntos Kaliope","mensaje_completo_puntos":"No has ganado puntos Kaliope en este pedido, pedido minimo para puntos son $500"}}
                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONArray carritoCliente = response.getJSONArray("carritoCliente");
                    JSONObject totalesJsonObjet = response.getJSONObject("totales");
                    cambiarJsonObject(totalesJsonObjet);                                            //cambiamos los totales con lo que nos envia el servidor y notificamos al adapter totaels
                    carritoAdapter.cambiarJsonArray(carritoCliente);                                //se envia el nuevo carrito al adaptador que lista los carritos y se le notifica
                    utilidadesApp.dialogoResultadoConexion(activity, resultado, mensaje);



                    if (resultado.compareToIgnoreCase("exito") == 0) {                        //comparamos la respuesta del servidor ignoramos las mayusculas por cualquier cosa, si es igual a exito entonces notificamos a nuestro adaptador para que actualice la imagen, en caso que el servidor tenga un error al modificar el item y retonre error entonces no notificamos al adaptador y no cambiamos el metodo de pago



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

        TextView cuentaCliente,
        nombreCliente,
        limiteCredito,
        gradoCliente,
        diasCredito,
        zona,
        numeroPedido,
        fechaEntrega,
        piezasTotales,
        piezasCredito,
        importeCredito,
        piezasInversion,
        importeInversion,
        gananciaTotal,
        puntosCorto,
        mensajeLimiteCredito,
        mensajeTodoInversion;

        Button confirmarEnviarPedido;

        ConstraintLayout constraintLayoutTotales;
        ConstraintLayout constraintLayoutCarritoVacio;


        public ViewHolderCarrito(@NonNull View itemView) {
            super(itemView);

           cuentaCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_cuentaClienteTV);
           nombreCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_nombreClienteTV);
           limiteCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_limiteCreditoTV);
           gradoCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_gradoTV);
           diasCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_diasTV);
           zona = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_nombreZonaTV);
           numeroPedido = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_numeroPedidoTV);
           fechaEntrega = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_fechaEntregaTV);
           piezasTotales = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasTotalesTV);
           piezasCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasCreditoTV);
           importeCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_importeCreditoTV);
           piezasInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasInversionTV);
           importeInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_importeInversionTV);
           gananciaTotal = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_gananciaTotalTV);
           puntosCorto = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajePuntosCortos);
           mensajeLimiteCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajeLimiteCreditoTV);
           confirmarEnviarPedido = (Button) itemView.findViewById(R.id.item_container_carrito_totales_confirmarEnviarPedidoButton);
           mensajeTodoInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajeTodoInversionTV);
           constraintLayoutTotales = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_constrainLayoutTotales);
           constraintLayoutCarritoVacio = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_layout_vacio);

        }
    }
}
