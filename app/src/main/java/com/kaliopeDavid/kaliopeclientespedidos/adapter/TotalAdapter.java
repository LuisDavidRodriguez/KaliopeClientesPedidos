package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kaliopeDavid.kaliopeclientespedidos.ConfiguracionesApp;
import com.kaliopeDavid.kaliopeclientespedidos.KaliopeServerClient;
import com.kaliopeDavid.kaliopeclientespedidos.R;
import com.kaliopeDavid.kaliopeclientespedidos.utilidadesApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolderCarrito> {

   JSONObject jsonObjectTotales;
   JSONObject jsonObjectInformacion;                      //"info":{"estatus":"FAIL","MENSAJE":"Tu carrito esta vacio"}---info":{"estatus":"FAIL","MENSAJE":"Tu ultimo pedido ha sido finalizado el dia 24-08-2021, agrega a tu carrito para crear un nuevo pedido!"}---"info":{"estatus":"EXITO","MENSAJE":"Carrito encotrado"}

    private JSONObject mensajesPreConfirmacion;              // los mesajes que mostraran los dialogos al preconfirmar"mensajes":{"0":"Confirmaras estos productos","1":"Esto apartara las existencias de los almacenes Kaliope","2":"Recuerda confirmar lo mas pronto posible estas piezas para garantizar las existencias del producto.","3":"Al recibir tu a crédito tú pagaras el 50% y Kaliope te financia 28 días el 50% restante","4":"Tu pedido actual tiene:","5":"en productos a crédito","6":"Tendras que pagar al recibir:","7":"50% del pedido","8":"Con forme aumente tu historial Kaliope aumentaremos tu financiamiento","9":"Quedaran pendientes:","10":"para el14-06-2021","11":"Recomendamos el modo de pago “Inversión” para que obtengas los mejores precios","12":"","13":"","14":"Recomendamos que cambies el método de pago a “Inversión” en algún producto para que obtengas el costo mas bajo.","15":"Tu pago por Inversión al recibir tu pedido será de:","16":"Felicidades has obtenido el precio mas bajo por producto.","17":"Al recibir tu pedido deberás liquidar al agente Kaliope:","18":"Exceso de credito","19":"50% credito","20":"Inversion","21":"por liquidar al recibir el pedido","22":"En crédito Kaliope fecha de pago 14-06-2021"}}
    private JSONObject mensajesFinalTotalesBreves;          //lo usaremos solo para guardar los mensajes del ultimo balance total que se mostrara "mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}
    //aunque esos 2 de arriba almacenan casi lo mismo y se pudo haber hecho con una sola variable, queria que estuviera un poco mas ordenado,
    //porque una de ellas solo contiene los mensajes que se necesitan para mostrar el balance final, y la otra contiene
    //todos los mensajes que necesitan las preconfirmaciones los dialogos
    //como ya cada que se elimina o modifica un producto se debe mostrar el balance final no queria enviar tanta informacion
    //enviando la cadena larga

    Activity activity;

   CarritoAdapter carritoAdapter;                   //necesitamos una referencia del adaptador que lista los productos del carrito para poder enviarle desde auqi la nueva lista de productos confirmados y notificarle el cambio al adaptador

   ProgressDialog progressDialog;



    public TotalAdapter(JSONObject jsonObjectTotales,JSONObject jsonObjectInformacion,JSONObject mensajesBalanceFinal, Activity activity) {
        this.jsonObjectTotales = jsonObjectTotales;
        this.jsonObjectInformacion = jsonObjectInformacion;
        this.mensajesFinalTotalesBreves = mensajesBalanceFinal;
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

    public void cambiarJsonObject(JSONObject totales, JSONObject mensajesFinalTotales){
        jsonObjectTotales = totales;
        this.mensajesFinalTotalesBreves = mensajesFinalTotales;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, final int position) {


//         //D/totales: {"nombre":"EVA MONDRAGON RIVAS",
//         "cuenta":"2070",
//         "limite_credito":"2400",
//         "grado":"SOCIA",
//         "dias":"28",
//         "ruta":"ACAMBAY",
//         "porcentaje_apoyo_empresa":"0.5",
//         "porcentaje_pago_cliente":"0.5",
//         "numero_pedido":"1",
//         "fecha_entrega":"2021-05-17",
//         "fecha_pago_del_credito":"14-06-2021",
//         "suma_cantidad":8,
//         "suma_credito":8,
//         "suma_inversion":0,
//         "cantidad_sin_confirmar":7,
//         "suma_productos_etiqueta":1622,
//         "suma_productos_inversion":0,
//         "suma_productos_credito":1179,
//         "suma_ganancia_cliente":443,
//         "diferencia_credito":-1221,
//         "cantidad_pagar_cliente_credito":589.5,
//         "cantidad_financiar_empresa":589.5,
//         "pago_al_recibir":589.5,
//         "mensaje_diferencia_credito":"Aun dispones de $1221 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $560","mensaje_resumido_puntos":" + 200 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 200 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar":"Tienes 7 productos sin confirmar, envialos!"}
// no usamos la variable posicion porque aqui solo habra un item totales
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



            String mensajeCantidadSinConfirmar = jsonObjectTotales.getString("mensaje_cantidad_sin_confirmar");
            if(!jsonObjectTotales.getString("cantidad_sin_confirmar").equals("0")){
                //Si la cantidad de productos sin confirmar es diferente de 0 entonces mostramos el boton confirmarPedido
                holder.confirmarEnviarPedido.setText(mensajeCantidadSinConfirmar);
                holder.confirmarEnviarPedido.setEnabled(true);
                holder.imageViewPalomita.setVisibility(View.INVISIBLE);


            }else{
                //Si es 0 la cantidad sin confirmar entonces mostramos el boton con palomita verde
                holder.confirmarEnviarPedido.setText(mensajeCantidadSinConfirmar);
                holder.confirmarEnviarPedido.setEnabled(false);
                holder.imageViewPalomita.setVisibility(View.VISIBLE);

            }



            //Dependiendo de si la cantidad es 0 entonces mostramos la imagen de la bolsita triste y mandamos el mensaje que nos muestra el server si no mostramos los totales
            if(jsonObjectInformacion.getString("estatus").equals("FAIL")){
                holder.constraintLayoutCarritoVacio.setVisibility(View.VISIBLE);
                holder.mensajeBolsaVacia.setText(jsonObjectInformacion.getString("MENSAJE"));           //ponemos el mensaje que el servidor nos envia
                holder.constraintLayoutTotales.setVisibility(View.GONE);
            }else{
                holder.constraintLayoutCarritoVacio.setVisibility(View.GONE);
                holder.constraintLayoutTotales.setVisibility(View.VISIBLE);
            }









            //-----------------------Añadimos el llenado de la vista de dialogoFinalesTotales es una copia de ese metodo colo hay que añadir holder.
                holder.titulo.setText(mensajesFinalTotalesBreves.getString("18"));
                holder.difCredito.setText(jsonObjectTotales.getString("diferencia_credito"));
                holder.difCreditoMessage.setText(mensajesFinalTotalesBreves.getString("19"));
                holder.pedidoPagoCredito.setText(jsonObjectTotales.getString("cantidad_pagar_cliente_credito"));
                holder.pagoCreditoMessage.setText(mensajesFinalTotalesBreves.getString("20"));
                holder.pagoInversion.setText(jsonObjectTotales.getString("suma_productos_inversion"));
                holder.pagoInversionMessage.setText(mensajesFinalTotalesBreves.getString("21"));
                holder.pagoTotal.setText(jsonObjectTotales.getString("pago_al_recibir"));
                holder.pagoTotalMessage.setText(mensajesFinalTotalesBreves.getString("22"));
                holder.cantidadFinanciada.setText(jsonObjectTotales.getString("cantidad_financiar_empresa"));
                holder.cantidadFinanciadaMessage.setText(mensajesFinalTotalesBreves.getString("23"));

                //VAMOS A CONTROLAR SI DEBEN SER VISIBLES CIERTAS SUMAS
                //recordar que diferencia puede venir en negativo significando que aun tiene credito disponible
                float diferencia = Float.parseFloat(jsonObjectTotales.getString("diferencia_credito"));
                if(diferencia<0){
                    holder.difCredito.setText("0");
                    holder.difCreditoSignoPesos.setVisibility(View.GONE);
                    holder.difCredito.setVisibility(View.GONE);
                    holder.difCreditoMessage.setVisibility(View.GONE);
                }else{
                    holder.difCreditoSignoPesos.setVisibility(View.VISIBLE);
                    holder.difCredito.setVisibility(View.VISIBLE);
                    holder.difCreditoMessage.setVisibility(View.VISIBLE);
                }

                //si hay una cantidad por financiar de la emrpesa, o si el cliente no tiene prendas a credito no mostramos este mensaje
                float financiarEmpresa = Float.parseFloat(jsonObjectTotales.getString("cantidad_financiar_empresa"));
                if(financiarEmpresa>0){
                    holder.cantidadFinaSignoPesos.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.VISIBLE);
                }else{
                    holder.cantidadFinaSignoPesos.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.INVISIBLE);
                }
             //---------------------- fin










        } catch (Exception e) {
            e.printStackTrace();
        }




        holder.confirmarEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conectarServerPreConfirmarPedido();
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
                // "totales: {"nombre":"MONICA HERNANDEZ GARCIA","cuenta":"4926","limite_credito":"1400","grado":"VENDEDORA","dias":"14","ruta":"EL PALMITO","numero_pedido":"1","fecha_entrega":"2021-02-09","suma_cantidad":5,"suma_credito":4,"suma_inversion":1,"cantidad_sin_confirmar":3,"suma_productos_etiqueta":2015,"suma_productos_inversion":360,"suma_productos_credito":1288,"suma_ganancia_cliente":367,"diferencia_credito":-112,"mensaje_diferencia_credito":"Aun dispones de $112 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $367","mensaje_resumido_puntos":" + 300 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 300 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido."}}
                try {
                    String resultado = response.getString("resultado");
                    String mensaje = response.getString("mensaje");
                    JSONArray carritoCliente = response.getJSONArray("carritoCliente");
                    JSONObject totalesJsonObjet = response.getJSONObject("totales");
                    JSONObject mensajesTotales = response.getJSONObject("mensajesFinalTotales");
                    cambiarJsonObject(totalesJsonObjet, mensajesTotales);                                            //cambiamos los totales con lo que nos envia el servidor y notificamos al adapter totaels
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


    private void conectarServerPreConfirmarPedido(){
        RequestParams params = new RequestParams();
        params.put("OPERACION",4);      //para PRE confirmar el pedido
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(activity));

        showProgresDialog(activity);

        KaliopeServerClient.post(CarritoAdapter.URL_EDICION_CARRITO,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("responseCode1", String.valueOf(response));
                //D/responseCode1: {"productos":[{"cantidad":"1","id_producto":"BR1008","descripcion":"BRASSIER DAMA","color":"AZULMARINO","precio_etiqueta":"189","imagen_permanente":"fotos\/BR1008-AZULMARINO-1.jpg","estado_producto":"CREDITO"},
                // {"cantidad":"2","id_producto":"PT1001","descripcion":"Pantaleta Dama","color":"CIELO","precio_etiqueta":"79","imagen_permanente":"fotos\/PT1001-CIELO-1.jpg","estado_producto":"INVERSION"},
                // {"cantidad":"1","id_producto":"BD1002","descripcion":"BLUSA DAMA","color":"ROSA","precio_etiqueta":"329","imagen_permanente":"fotos\/BD1002-ROSA-1.jpg","estado_producto":"INVERSION"}],
                //"mensajes":{"0":"Confirmaras estos productos","1":"Esto apartara las existencias de los almacenes Kaliope","2":"Recuerda confirmar lo mas pronto posible estas piezas para garantizar las existencias del producto.","3":"Al recibir tu a crédito tú pagaras el 50% y Kaliope te financia 28 días el 50% restante","4":"Tu pedido actual tiene:","5":"en productos a crédito","6":"Tendras que pagar al recibir:","7":"50% del pedido","8":"Con forme aumente tu historial Kaliope aumentaremos tu financiamiento","9":"Quedaran pendientes:","10":"para el14-06-2021","11":"Recomendamos el modo de pago “Inversión” para que obtengas los mejores precios","12":"Tu crédito Kaliope de $1500 será sobre pasado por:","13":"Tendrás que dar esta diferencia en efectivo al recibir tu pedido.","14":"Recomendamos que cambies el método de pago a “Inversión” en algún producto para que obtengas el costo mas bajo.","15":"Tu pago por Inversión al recibir tu pedido será de:","16":"Felicidades has obtenido el precio mas bajo por producto.","17":"Al recibir tu pedido deberás liquidar al agente Kaliope:","18":"Exceso de credito","19":"50% credito","20":"Inversion","21":"por liquidar al recibir el pedido","22":"En crédito Kaliope fecha de pago 14-06-2021"}}

                try {
                    JSONArray productoPorConfirmar = response.getJSONArray("productos");
                    //JSONObject totales = response.getJSONObject("totales");                               //no volvemos a recibir totales porque ya los tenemos desde la primera vez que se conecta la app al carrito
                    mensajesPreConfirmacion = response.getJSONObject("mensajes");

                    dialogoConfirmacion1(productoPorConfirmar);



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

        TextView mensajeBolsaVacia;

        Button confirmarEnviarPedido;

        ImageView imageViewPalomita;

        ConstraintLayout constraintLayoutTotales;
        ConstraintLayout constraintLayoutCarritoVacio;








        //------------------------------------Con el include de la vista del dialogo totales al entregar pedido tenemos que poner los campos aqui
        TextView titulo;

        TextView difCreditoSignoPesos;
        TextView difCredito;
        TextView difCreditoMessage;

        TextView pagoCreditoSignoPesos;
        TextView pedidoPagoCredito;
        TextView pagoCreditoMessage;

        TextView pagoInversionSignoPesos;
        TextView pagoInversion;
        TextView pagoInversionMessage;

        TextView pagoTotalSignoPesos;
        TextView pagoTotal;
        TextView pagoTotalMessage;

        TextView cantidadFinaSignoPesos;
        TextView cantidadFinanciada;
        TextView cantidadFinanciadaMessage;
        //-------------------------- basicamente se copio y pego del metodo dialogo confirmacion totales


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
           mensajeBolsaVacia = (TextView) itemView.findViewById(R.id.textView11);
           imageViewPalomita = (ImageView) itemView.findViewById(R.id.item_container_carrito_totales_palomitaIV);







           //----------------------------agregar imagen de totales finales
            titulo = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_titulo);

            difCreditoSignoPesos = (TextView) itemView.findViewById(R.id.textView15);
            difCredito = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_difCredito);
            difCreditoMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_difCreditoMessage);

            pagoCreditoSignoPesos = (TextView) itemView.findViewById(R.id.textView20);
            pedidoPagoCredito = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCredito);
            pagoCreditoMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCreditoMessage);

            pagoInversionSignoPesos = (TextView) itemView.findViewById(R.id.textView23);
            pagoInversion = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversion);
            pagoInversionMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversionMessage);

            pagoTotalSignoPesos = (TextView) itemView.findViewById(R.id.textView33);
            pagoTotal = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotal);
            pagoTotalMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotalMessage);

            cantidadFinaSignoPesos = (TextView) itemView.findViewById(R.id.textView35);
            cantidadFinanciada = (TextView) itemView.findViewById(R.id.cantidadFinanciada);
            cantidadFinanciadaMessage = (TextView) itemView.findViewById(R.id.cantidadFinanciadaMessage);


        }
    }


    /**
     * Mostramos el primer dialogo que muestra la lista de productos por confirmar
     * @param productosPorConfirmar
     */
    private void dialogoConfirmacion1(JSONArray productosPorConfirmar){
        //inflamos la vista de nuestro dialogo personalizado
        //"mensajes":{"0":"Confirmaras estos productos","1":"Esto apartara las existencias de los almacenes Kaliope",
        // "2":"Recuerda confirmar lo mas pronto posible estas piezas para garantizar las existencias del producto.",
        // "3":"Al recibir tu a crédito tú pagaras el 50% y Kaliope te financia 28 días el 50% restante",
        // "4":"Tu pedido actual tiene:",
        // "5":"en productos a crédito",
        // "6":"Tendras que pagar al recibir:",
        // "7":"50% del pedido",
        // "8":"Con forme aumente tu historial Kaliope aumentaremos tu financiamiento",
        // "9":"Quedaran pendientes:",
        // "10":"para el14-06-2021",
        // "11":"Recomendamos el modo de pago “Inversión” para que obtengas los mejores precios",
        // "12":"Tu crédito Kaliope de $1500 será sobre pasado por:",
        // "13":"Tendrás que dar esta diferencia en efectivo al recibir tu pedido.",
        // "14":"Recomendamos que cambies el método de pago a “Inversión” en algún producto para que obtengas el costo mas bajo.",
        // "15":"Tu pago por Inversión al recibir tu pedido será de:",
        // "16":"Felicidades has obtenido el precio mas bajo por producto.",
        // "17":"Al recibir tu pedido deberás liquidar al agente Kaliope:",
        // "18":"Exceso de credito",
        // "19":"50% credito",
        // "20":"Inversion",
        // "21":"por liquidar al recibir el pedido",
        // "22":"En crédito Kaliope fecha de pago 14-06-2021"}}
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_confirmacion_pedido_1,null);

        TextView mensaje1 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_TVtitulo);
        TextView mensaje2 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_TV1);
        TextView mensaje3 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_TV2);
        ListView listaPorConfirmar = (ListView) view.findViewById(R.id.dialogo_confirmacion_pedido_LV);
        ListAdapterPreConfirmacion listAdapterPreConfirmacion = new ListAdapterPreConfirmacion(productosPorConfirmar);
        listaPorConfirmar.setAdapter(listAdapterPreConfirmacion);



        try {
            mensaje1.setText(mensajesPreConfirmacion.getString("1"));
            mensaje2.setText(mensajesPreConfirmacion.getString("2"));
            mensaje3.setText(mensajesPreConfirmacion.getString("3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        float diferenciaCredito = 0;
                        float cargoCredito = 0;
                        try {
                            diferenciaCredito = Float.parseFloat(jsonObjectTotales.getString("diferencia_credito"));
                            cargoCredito = Float.parseFloat(jsonObjectTotales.getString("suma_productos_credito"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if(diferenciaCredito>0){
                            //si se sobre paso el credito mostramos el mensaje de credito sobrepasado
                            dialogoConfirmacionExcesoCredito();
                        }else if (cargoCredito>0){
                            //si no se sobrepaso el credito checamos si hay mercancia a credito para mostrar el mensaje de totales de credito
                            dialogoConfirmacionTotalesCredito();
                        }else{
                            //si no hay ni diferencia de credito ni productos a credito mostramos solo el mensaje de inversion
                            dialogoConfirmacionInversion();
                        }





                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();

    }

    /**
     * Mostramos el dialogo que muestra el total a credito y lo que el cliente debera pagar con financiamiento
     */
    private void dialogoConfirmacionTotalesCredito(){
        //D/totales: {"nombre":"EVA MONDRAGON RIVAS","cuenta":"2070","limite_credito":"2400","grado":"SOCIA","dias":"28","ruta":"ACAMBAY","porcentaje_apoyo_empresa":"0.5","porcentaje_pago_cliente":"0.5","numero_pedido":"1","fecha_entrega":"2021-05-17","fecha_pago_del_credito":"14-06-2021","suma_cantidad":8,"suma_credito":8,"suma_inversion":0,"cantidad_sin_confirmar":7,"suma_productos_etiqueta":1622,"suma_productos_inversion":0,"suma_productos_credito":1179,"suma_ganancia_cliente":443,"diferencia_credito":-1221,"cantidad_pagar_cliente_credito":589.5,"cantidad_financiar_empresa":589.5,"pago_al_recibir":589.5,"mensaje_diferencia_credito":"Aun dispones de $1221 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $560","mensaje_resumido_puntos":" + 200 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 200 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar":"Tienes 7 productos sin confirmar, envialos!"}
        //"mensajes":{"1":"Confirmaras estos productos",
        // "2":"Esto apartara las existencias de los almacenes Kaliope",
        // "3":"Recuerda confirmar lo mas pronto posible estas piezas para garantizar las existencias del producto.",
        // "4":"Al recibir tu a crédito tú pagaras el 50% y Kaliope te financia 28 días el 50% restante",
        // "5":"Tu pedido actual tiene:",
        // "6":"en productos a crédito",
        // "7":"Tendras que pagar al recibir:",
        // "8":"50% del pedido",
        // "9":"Con forme aumente tu historial Kaliope aumentaremos tu financiamiento",
        // "10":"Quedaran pendientes:",
        // "11":"para el14-06-2021",
        // "12":"Recomendamos el modo de pago “Inversión” para que obtengas los mejores precios",
        // "13":"Tu crédito Kaliope de $1500 será sobre pasado por:",
        // "14":"Tendrás que dar esta diferencia en efectivo al recibir tu pedido.",
        // "15":"Recomendamos que cambies el método de pago a “Inversión” en algún producto para que obtengas el costo mas bajo.",
        // "16":"Tu pago por Inversión al recibir tu pedido será de:",
        // "17":"Felicidades has obtenido el precio mas bajo por producto.",
        // "18":"Al recibir tu pedido deberás liquidar al agente Kaliope:",
        // "19":"Exceso de credito",
        // "20":"50% credito",
        // "21":"Inversion",
        // "22":"por liquidar al recibir el pedido",
        // "23":"En crédito Kaliope fecha de pago 14-06-2021"}}
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_confirmacion_pedido_totales_credito,null);

        TextView mensaje1 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV1);
        TextView cantidadCredito = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TVcantidadCredito);
        TextView mensaje2 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV3);
        TextView mensaje3 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV4);
        TextView cantidadPagoCliente = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TVpagarCliente);
        TextView mensaje4 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV5);
        TextView mensaje5 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV6);
        TextView mensaje6 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV7);
        TextView cantidadPendiente = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TVcantidadPendiente);
        TextView mensaje7 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido__2_TV8);



        try {
            mensaje1.setText(mensajesPreConfirmacion.getString("5"));
            cantidadCredito.setText(jsonObjectTotales.getString("suma_productos_credito"));
            mensaje2.setText(mensajesPreConfirmacion.getString("6"));
            mensaje3.setText(mensajesPreConfirmacion.getString("7"));
            cantidadPagoCliente.setText(jsonObjectTotales.getString("cantidad_pagar_cliente_credito"));
            mensaje4.setText(mensajesPreConfirmacion.getString("8"));
            mensaje5.setText(mensajesPreConfirmacion.getString("9"));
            mensaje6.setText(mensajesPreConfirmacion.getString("10"));
            cantidadPendiente.setText(jsonObjectTotales.getString("cantidad_financiar_empresa"));
            mensaje7.setText(mensajesPreConfirmacion.getString("11"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setPositiveButton("siguiente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            dialogoConfirmacionInversion();


                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();


    }

    /**
     * Muestra si hay diferencias a credito
     * Este mensaje solo debera mostrarse si el campo diferencia_credito del json totales es positivo o mayor a 0
     * si es negativo significa que aun le queda credito disponible por lo tanto no debera mostrarse el mensaje
     * de exceso de credito
     */
    private void dialogoConfirmacionExcesoCredito(){
        //D/totales: {"nombre":"EVA MONDRAGON RIVAS","cuenta":"2070","limite_credito":"2400","grado":"SOCIA","dias":"28","ruta":"ACAMBAY","porcentaje_apoyo_empresa":"0.5","porcentaje_pago_cliente":"0.5","numero_pedido":"1","fecha_entrega":"2021-05-17","fecha_pago_del_credito":"14-06-2021","suma_cantidad":8,"suma_credito":8,"suma_inversion":0,"cantidad_sin_confirmar":7,"suma_productos_etiqueta":1622,"suma_productos_inversion":0,"suma_productos_credito":1179,"suma_ganancia_cliente":443,"diferencia_credito":-1221,"cantidad_pagar_cliente_credito":589.5,"pago_al_recibir":589.5,"mensaje_diferencia_credito":"Aun dispones de $1221 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $560","mensaje_resumido_puntos":" + 200 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 200 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar":"Tienes 7 productos sin confirmar, envialos!"}
        //"mensajes":{"1":"Confirmaras estos productos",
        // "2":"Esto apartara las existencias de los almacenes Kaliope",
        // "3":"Recuerda confirmar lo mas pronto posible estas piezas para garantizar las existencias del producto.",
        // "4":"Al recibir tu a crédito tú pagaras el 50% y Kaliope te financia 28 días el 50% restante",
        // "5":"Tu pedido actual tiene:",
        // "6":"en productos a crédito",
        // "7":"Tendras que pagar al recibir:",
        // "8":"50% del pedido",
        // "9":"Con forme aumente tu historial Kaliope aumentaremos tu financiamiento",
        // "10":"Quedaran pendientes:",
        // "11":"para el14-06-2021",
        // "12":"Recomendamos el modo de pago “Inversión” para que obtengas los mejores precios",
        // "13":"Tu crédito Kaliope de $1500 será sobre pasado por:",
        // "14":"Tendrás que dar esta diferencia en efectivo al recibir tu pedido.",
        // "15":"Recomendamos que cambies el método de pago a “Inversión” en algún producto para que obtengas el costo mas bajo.",
        // "16":"Tu pago por Inversión al recibir tu pedido será de:",
        // "17":"Felicidades has obtenido el precio mas bajo por producto.",
        // "18":"Al recibir tu pedido deberás liquidar al agente Kaliope:",
        // "19":"Exceso de credito",
        // "20":"50% credito",
        // "21":"Inversion",
        // "22":"por liquidar al recibir el pedido",
        // "23":"En crédito Kaliope fecha de pago 14-06-2021"}}

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_confirmacion_pedido_diferencia_credito,null);

        TextView mensaje1 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_3_TV1);
        TextView mensajeExesoCredito = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_3_TVexcesoCredito);
        TextView mensaje2 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_3_TV3);
        TextView mensaje3 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_3_TV4);

        try {
            mensaje1.setText(mensajesPreConfirmacion.getString("13"));
            mensajeExesoCredito.setText(jsonObjectTotales.getString("diferencia_credito"));
            mensaje2.setText(mensajesPreConfirmacion.getString("14"));
            mensaje3.setText(mensajesPreConfirmacion.getString("15"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton("siguiente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogoConfirmacionTotalesCredito();
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();


    }

    /**
     * Dialogo muestra el pago por inversion
     */
    private void dialogoConfirmacionInversion(){
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_confirmacion_pedido_inversion,null);

        TextView mensaje1 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_4_TV1);
        TextView mensajePagoInversion = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_4_TVpagoInversion);
        TextView mensaje2 = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_4_TV2);
        TextView signoPesos = (TextView) view.findViewById(R.id.textView16);


        try {
            String sumaProductosInversion = jsonObjectTotales.getString("suma_productos_inversion");
            float totalInversion = Float.parseFloat(sumaProductosInversion);

            if(totalInversion>0){
                //si hay campos en inversion entonces mostramos el mensaje 1 acorde con la documentacion
                mensaje1.setText(mensajesPreConfirmacion.getString("16"));
                mensajePagoInversion.setText(sumaProductosInversion);
                mensaje2.setText(mensajesPreConfirmacion.getString("17"));

                signoPesos.setVisibility(View.VISIBLE);
                mensajePagoInversion.setVisibility(View.VISIBLE);
            }else{
                //si no ay productos en inversion entonces ocultamos solo el signo de pesos debido a que el servidor ya mandara lso emsnajes correspondientes
                mensaje1.setText(mensajesPreConfirmacion.getString("16"));
                mensaje2.setText(mensajesPreConfirmacion.getString("17"));
                signoPesos.setVisibility(View.INVISIBLE);
                mensajePagoInversion.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton("siguiente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogoConfirmacionFinalTotales();
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();


    }

    /**
     * ultimo mensaje mostrando los totales a pagar al recibir el pedido
     */
    private void dialogoConfirmacionFinalTotales(){
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.dialogo_confirmacion_pedido_final_totales,null);

        TextView titulo;

        TextView difCreditoSignoPesos;
        TextView difCredito;
        TextView difCreditoMessage;

        TextView pagoCreditoSignoPesos;
        TextView pedidoPagoCredito;
        TextView pagoCreditoMessage;

        TextView pagoInversionSignoPesos;
        TextView pagoInversion;
        TextView pagoInversionMessage;

        TextView pagoTotalSignoPesos;
        TextView pagoTotal;
        TextView pagoTotalMessage;

        TextView cantidadFinaSignoPesos;
        TextView cantidadFinanciada;
        TextView cantidadFinanciadaMessage;

        titulo = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_titulo);

        difCreditoSignoPesos = (TextView) view.findViewById(R.id.textView15);
        difCredito = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_difCredito);
        difCreditoMessage = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_difCreditoMessage);

        pagoCreditoSignoPesos = (TextView) view.findViewById(R.id.textView20);
        pedidoPagoCredito = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCredito);
        pagoCreditoMessage = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCreditoMessage);

        pagoInversionSignoPesos = (TextView) view.findViewById(R.id.textView23);
        pagoInversion = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversion);
        pagoInversionMessage = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversionMessage);

        pagoTotalSignoPesos = (TextView) view.findViewById(R.id.textView33);
        pagoTotal = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotal);
        pagoTotalMessage = (TextView) view.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotalMessage);

        cantidadFinaSignoPesos = (TextView) view.findViewById(R.id.textView35);
        cantidadFinanciada = (TextView) view.findViewById(R.id.cantidadFinanciada);
        cantidadFinanciadaMessage = (TextView) view.findViewById(R.id.cantidadFinanciadaMessage);

        try {



            titulo.setText(mensajesPreConfirmacion.getString("18"));
            difCredito.setText(jsonObjectTotales.getString("diferencia_credito"));
            difCreditoMessage.setText(mensajesPreConfirmacion.getString("19"));
            pedidoPagoCredito.setText(jsonObjectTotales.getString("cantidad_pagar_cliente_credito"));
            pagoCreditoMessage.setText(mensajesPreConfirmacion.getString("20"));
            pagoInversion.setText(jsonObjectTotales.getString("suma_productos_inversion"));
            pagoInversionMessage.setText(mensajesPreConfirmacion.getString("21"));
            pagoTotal.setText(jsonObjectTotales.getString("pago_al_recibir"));
            pagoTotalMessage.setText(mensajesPreConfirmacion.getString("22"));
            cantidadFinanciada.setText(jsonObjectTotales.getString("cantidad_financiar_empresa"));
            cantidadFinanciadaMessage.setText(mensajesPreConfirmacion.getString("23"));


            //VAMOS A CONTROLAR SI DEBEN SER VISIBLES CIERTAS SUMAS
            //recordar que diferencia puede venir en negativo significando que aun tiene credito disponible
            float diferencia = Float.parseFloat(jsonObjectTotales.getString("diferencia_credito"));
            if(diferencia<0){
                difCredito.setText("0");
                difCreditoSignoPesos.setVisibility(View.INVISIBLE);
                difCredito.setVisibility(View.INVISIBLE);
                difCreditoMessage.setVisibility(View.INVISIBLE);
            }else{
                difCreditoSignoPesos.setVisibility(View.VISIBLE);
                difCredito.setVisibility(View.VISIBLE);
                difCreditoMessage.setVisibility(View.VISIBLE);
            }

            //si hay una cantidad por financiar de la emrpesa, o si el cliente no tiene prendas a credito no mostramos este mensaje
            float financiarEmpresa = Float.parseFloat(jsonObjectTotales.getString("cantidad_financiar_empresa"));
            if(financiarEmpresa>0){
                cantidadFinaSignoPesos.setVisibility(View.VISIBLE);
                cantidadFinanciada.setVisibility(View.VISIBLE);
                cantidadFinanciadaMessage.setVisibility(View.VISIBLE);
            }else{
                cantidadFinaSignoPesos.setVisibility(View.INVISIBLE);
                cantidadFinanciada.setVisibility(View.INVISIBLE);
                cantidadFinanciadaMessage.setVisibility(View.INVISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        conectarServerConfirmarPedido(1);
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();

    }
}
