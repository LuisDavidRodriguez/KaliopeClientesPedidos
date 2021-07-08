package com.kaliopeDavid.kaliopeclientespedidos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.kaliopeDavid.kaliopeclientespedidos.adapter.CarritoAdapter;
import com.kaliopeDavid.kaliopeclientespedidos.adapter.TotalAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class CarritoActivity extends AppCompatActivity {




    public static final String URL_CONSULTAR_PEDIDO = "app_movil/consultar_pedido.php";


    private JSONArray datosCarrito = new JSONArray();
    private JSONObject totalesCarrito = new JSONObject();
    private JSONObject informacion = new JSONObject();
    private JSONObject mensajesTotalesFinal = new JSONObject(); //contiene lo que debe mostrar el recuadrito que indica lo que el cliente debe pagar al recibir "mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}


    RecyclerView recyclerViewLista;
    SnapHelper snapHelper;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<HashMap> listaCarrito = new ArrayList<HashMap>();

    ProgressDialog progressDialog;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        getSupportActionBar().setTitle(R.string.mi_carrito);

        recyclerViewLista = (RecyclerView) findViewById(R.id.carrito_recyclerViewLista);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewLista.setLayoutManager(layoutManager);


        activity = this;



        //Toast.makeText(this, id_producto + " " + colorSeleccionado + " " + tallaSeleccionada + " " + cantidadSeleccionada, Toast.LENGTH_LONG).show();


        consultarCarrito();
    }


    /**
     * Enviaremos el producto que el cliente selecciono el servidor se encarga de agregarlo
     * una vez agredado el servidor nos retorna un json con los productos que tiene en la lista del
     * carrito del cliente.
     */
    private void consultarCarrito() {
        RequestParams params = new RequestParams();
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(this));

        showProgresDialog(this);


        KaliopeServerClient.post(URL_CONSULTAR_PEDIDO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();
                Log.d("detalles1", String.valueOf(response));
                /*
                {"carritoCliente":[{"id":"39","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"2","color":"Gris","no_color":"rgb(142, 142, 142)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":"280","imagen_permanente":"fotos\/SM5898-VERDE-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":41,"ganancia_inversion":59,"existencias_restantes":0,"apurate_confirmar":"Ya no hay piezas disponibles para surir tu pedido, otros clientes confirmaron su pedido antes que tu y agotaron las existencias"},
                {"id":"40","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"GRIS","no_color":"rgb(122, 125, 130)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":"280","imagen_permanente":"fotos\/ST5898-GRIS-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":49,"ganancia_inversion":79,"existencias_restantes":8,"apurate_confirmar":""},
                {"id":"41","no_pedido":"1","fecha_entrega_pedido":"2021-02-23","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Gris","no_color":"rgb(142, 142, 142)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":"280","imagen_permanente":"fotos\/SM5898-VERDE-1.jpg","producto_confirmado":"true","estado_producto":"CREDITO","seguimiento_producto":"Producto confirmado","ganancia":41,"ganancia_inversion":59,"existencias_restantes":0,"apurate_confirmar":"Ya no hay piezas disponibles para surir tu pedido, otros clientes confirmaron su pedido antes que tu y agotaron las existencias"},

                D/totales: {"nombre":"EVA MONDRAGON RIVAS","cuenta":"2070","limite_credito":"2400","grado":"SOCIA","dias":"28","ruta":"ACAMBAY","porcentaje_apoyo_empresa":"0.5","porcentaje_pago_cliente":"0.5","numero_pedido":"1","fecha_entrega":"2021-05-17","fecha_pago_del_credito":"14-06-2021","suma_cantidad":8,"suma_credito":8,"suma_inversion":0,"cantidad_sin_confirmar":7,"suma_productos_etiqueta":1622,"suma_productos_inversion":0,"suma_productos_credito":1179,"suma_ganancia_cliente":443,"diferencia_credito":-1221,"cantidad_pagar_cliente_credito":589.5,"pago_al_recibir":589.5,"mensaje_diferencia_credito":"Aun dispones de $1221 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $560","mensaje_resumido_puntos":" + 200 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 200 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar":"Tienes 7 productos sin confirmar, envialos!"}

                 Hicimos una modificacion se agrega otro json info, este contiene los datos de la consulta del carrito
                 debido a que pueden ser 3 casos 1
                 "info":{"estatus":"FAIL","MENSAJE":"Tu carrito esta vacio"}
                 "info":{"estatus":"FAIL","MENSAJE":"Tu ultimo pedido ha sido finalizado el dia 24-08-2021, agrega a tu carrito para crear un nuevo pedido!"}
                 "info":{"estatus":"EXITO","MENSAJE":"Carrito encotrado"}

                 añadimos tambien los datos que le muestran al cliente lo que debe pagar al recibir el producto
                 "mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}




                obtendremos de vuelta todos los productos que el cliente tiene en su carrito,
                el campo de error que añadi al final es solo para cuando el producto no pudo ser añadido a la tabla del carrito
                entonces retornamos el carrito pero un texto diciendo "El producto a añadir no pudo ser agregado correctamente al carrito"
                 */
                try {
                    informacion = response.getJSONObject("info");
                    datosCarrito = response.getJSONArray("carritoCliente");
                    totalesCarrito = response.getJSONObject("totales");
                    mensajesTotalesFinal = response.getJSONObject("mensajesFinalTotales");
                    Log.d("totales", String.valueOf(totalesCarrito));
                    llenarRecyclerLista();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();


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


                String info = "Status Code: " + headers.toString() + statusCode + "  responseString: " + responseString + throwable.getMessage();
                Log.d("onFauile 1", info);
                //Toast.makeText(MainActivity.this,responseString + "  Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();
               progressDialog.dismiss();
                //dialogoDeConexion("Fallo de inicio de sesion", responseString + "\nStatus Code: " + String.valueOf(statusCode));


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


                String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.getMessage();
                Log.d("onFauile 2", info);
                //Toast.makeText(MainActivity.this,info, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();


                new AlertDialog.Builder(activity)
                        .setTitle("No hay conexion a internet")
                        .setMessage("No hemos podido conectar con el servidor.Para mostrar tu carrito es necesaria una conexion a internet")
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();           //finalizamos esta actividad para que nos devuelva a la anterior
                            }
                        })
                        .create()
                        .show();


            }


            @Override
            public void onRetry(int retryNo) {
                progressDialog.setMessage("Reintentando conexion No: " + String.valueOf(retryNo));
            }


        });
    }


    private void llenarRecyclerLista() {




        TotalAdapter totalAdapter = new TotalAdapter(totalesCarrito,informacion, mensajesTotalesFinal, this);
        CarritoAdapter carritoAdapter = new CarritoAdapter(datosCarrito,this, totalAdapter);
        totalAdapter.setCarritoAdapterReferencia(carritoAdapter);                                           //le enviamos la referencia para poder notificar al adaptador
        ConcatAdapter concatAdapter = new ConcatAdapter(totalAdapter,carritoAdapter);
        recyclerViewLista.setAdapter(concatAdapter);


    }


    private void showProgresDialog(Activity activity){

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Conectando al Servidor");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}