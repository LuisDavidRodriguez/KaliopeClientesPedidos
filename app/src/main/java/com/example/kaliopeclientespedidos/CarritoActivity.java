package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kaliopeclientespedidos.adapter.CarritoAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.cache.Resource;

public class CarritoActivity extends AppCompatActivity {

    String id_producto = "";
    String colorSeleccionado = "";
    String tallaSeleccionada = "";
    int cantidadSeleccionada = 1;


    public final String URL_RECIBIR_PEDIDO = "app_movil/recibir_pedido.php";


    private JSONArray datosCarrito = new JSONArray();
    private String errorCarrito = "";               //si el producto no se pudo agregar al carrito se entregara aqui un error


    RecyclerView recyclerViewLista;
    SnapHelper snapHelper;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<HashMap> listaCarrito = new ArrayList<HashMap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerViewLista = (RecyclerView) findViewById(R.id.carrito_recyclerViewLista);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewLista.setLayoutManager(layoutManager);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id_producto = bundle.getString("ID_PRODUCTO");
            colorSeleccionado = bundle.getString("COLOR_SELECCIONADO");
            tallaSeleccionada = bundle.getString("TALLA_SELECCIONADA");
            cantidadSeleccionada = bundle.getInt("CANTIDAD_SELECCIONADA");

        } else {
            throw new IllegalArgumentException("No se encontro extras del intent");
        }


        Toast.makeText(this, id_producto + " " + colorSeleccionado + " " + tallaSeleccionada + " " + cantidadSeleccionada, Toast.LENGTH_LONG).show();


        enviarProductoAlServidor();
    }


    /**
     * Enviaremos el producto que el cliente selecciono el servidor se encarga de agregarlo
     * una vez agredado el servidor nos retorna un json con los productos que tiene en la lista del
     * carrito del cliente.
     */
    private void enviarProductoAlServidor() {
        RequestParams params = new RequestParams();
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(this));
        params.put("ID_PRODUCTO", id_producto);
        params.put("COLOR_SELECCIONADO", colorSeleccionado);
        params.put("TALLA_SELECCIONADA", tallaSeleccionada);
        params.put("CANTIDAD_SELECCIONADA", cantidadSeleccionada);

        KaliopeServerClient.post(URL_RECIBIR_PEDIDO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();
                Log.d("detalles1", String.valueOf(response));
                /*
                {"carritoCliente":[{"id":"108","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Rosa","no_color":"rgb(240, 74, 141)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"109","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"110","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"111","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"}]
                ,"error":""}


                obtendremos de vuelta todos los productos que el cliente tiene en su carrito,
                el campo de error que añadi al final es solo para cuando el producto no pudo ser añadido a la tabla del carrito
                entonces retornamos el carrito pero un texto diciendo "El producto a añadir no pudo ser agregado correctamente al carrito"
                 */
                try {
                    datosCarrito = response.getJSONArray("carritoCliente");
                    errorCarrito = response.getString("error");
                    llenarRecyclerLista();
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
                //progressDialog.dismiss();
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


                String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.toString();
                Log.d("onFauile 2", info);
                //Toast.makeText(MainActivity.this,info, Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
                //dialogoDeConexion("Fallo de conexion", info);
            }


            @Override
            public void onRetry(int retryNo) {
                //progressDialog.setMessage("Reintentando conexion No: " + String.valueOf(retryNo));
            }


        });
    }


    private void llenarRecyclerLista() {

        listaCarrito.clear();

        //recorremos el json array del carrito que recibimos del servidor
        for (int i = 0; i < datosCarrito.length(); i++) {
            /*
            {"carritoCliente":[{"id":"108","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"SM5898","descripcion":"Sudadera dama","talla":"UNT","cantidad":"1","color":"Rosa","no_color":"rgb(240, 74, 141)","precio_etiqueta":"339","precio_vendedora":"298","precio_socia":"295","precio_empresaria":"291","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"109","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"110","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"},
                {"id":"111","no_pedido":"4","fecha_entrega_pedido":"2020-12-15","no_cuenta":"4926","nombre_cliente":"MONICA HERNANDEZ GARCIA","credito_cliente":"1400","grado_cliente":"VENDEDORA","puntos_disponibles":"0","id_producto":"ST5898","descripcion":"Sueter dama","talla":"UNT","cantidad":"1","color":"VERDE AGUA","no_color":"rgb(200, 235, 231)","precio_etiqueta":"359","precio_vendedora":"310","precio_socia":"305","precio_empresaria":"301","precio_inversionista":null,"imagen_permanente":null,"producto_confirmado":"false","estado_producto":"CREDITO","seguimiento_producto":"Producto sin confirmar","diferencia_regalo":"0","puntos_tomados":"0"}]

             */

            HashMap map = new HashMap();

            try {
                String urlImagen = KaliopeServerClient.BASE_URL + datosCarrito.getJSONObject(i).getString("imagen_permanente");

                map.put(CarritoAdapter.ID_DATA, datosCarrito.getJSONObject(i).getString("id"));
                map.put(CarritoAdapter.ID_PRODUCTO, datosCarrito.getJSONObject(i).getString("id_producto"));
                map.put(CarritoAdapter.DESCRIPCION, datosCarrito.getJSONObject(i).getString("descripcion"));
                map.put(CarritoAdapter.CANTIDAD, datosCarrito.getJSONObject(i).getString("cantidad"));
                map.put(CarritoAdapter.TALLA, datosCarrito.getJSONObject(i).getString("talla"));
                map.put(CarritoAdapter.COLOR, datosCarrito.getJSONObject(i).getString("color"));
                map.put(CarritoAdapter.PRECIO_PUBLICO, datosCarrito.getJSONObject(i).getString("precio_etiqueta"));
                map.put(CarritoAdapter.EMPRESARIA, datosCarrito.getJSONObject(i).getString("precio_empresaria"));
                map.put(CarritoAdapter.SOCIA, datosCarrito.getJSONObject(i).getString("precio_socia"));
                map.put(CarritoAdapter.VENDEDORA, datosCarrito.getJSONObject(i).getString("precio_vendedora"));
                map.put(CarritoAdapter.INVERSIONISTA, datosCarrito.getJSONObject(i).getString("precio_inversionista"));
                map.put(CarritoAdapter.GRADO_CLIENTE, datosCarrito.getJSONObject(i).getString("grado_cliente"));
                map.put(CarritoAdapter.FORMA_PAGO, datosCarrito.getJSONObject(i).getString("estado_producto"));//CREDITO, REGALO, INVERSIONISTA
                map.put(CarritoAdapter.IMAGEN_PERMANENTE, urlImagen);

                listaCarrito.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        CarritoAdapter carritoAdapter = new CarritoAdapter(listaCarrito);
        recyclerViewLista.setAdapter(carritoAdapter);


        if (!errorCarrito.isEmpty()) {
            /*
            Si el producto que añadio no se agrego correctamente al carrito entonces
            el servidor nos retornara un error, y lo mostraremos en un dialogo
            pero aun asi se mostrara la lista de los productos que tenga en el carrito
             */

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(getResources().getText(R.string.Aviso));
            builder.setMessage(errorCarrito);
            builder.setPositiveButton(getResources().getText(R.string.entiendo), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }


    }

}