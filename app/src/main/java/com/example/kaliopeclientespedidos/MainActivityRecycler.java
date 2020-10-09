package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivityRecycler extends AppCompatActivity {

    ArrayList<String> listaDatos;
    RecyclerView recyclerView;

    ArrayList<Producto> listaProductos;



    private final String URL_CATEGORIAS ="app_movil/consultar_categorias.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        listaDatos = new ArrayList<>();

        llenarProductos();



        //AdapterDatos adapterDatos = new AdapterDatos(listaDatos);

        //AdapterRopa adapterRopa = new AdapterRopa(listaProductos,this);
        //recyclerView.setAdapter(adapterRopa);
    }


    private void enaviarArecycler(){
        AdapterRopa adapterRopa = new AdapterRopa(listaProductos,this);
        recyclerView.setAdapter(adapterRopa);

    }


    private void llenarProductos(){

        listaProductos = new ArrayList<>();

        KaliopeServerClient.get(URL_CATEGORIAS,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));

                try {
                    JSONArray categorias = response.getJSONArray("categorias");
                    JSONArray imagenDeInicio = response.getJSONArray("imagenInicio");
                    Log.d("datosProces",String.valueOf(categorias.toString()));
                    Log.d("datosProcesImagenInicio",String.valueOf(imagenDeInicio.toString()));


                    for (int i=0; i<categorias.length() ; i++){
                        Log.d("datosProcesCategorias",String.valueOf(categorias.getJSONObject(i).getString("categoria")));
                    }



                    for (int i=0; i<imagenDeInicio.length() ; i++){

                        Log.d("datosProcesImagen",String.valueOf(imagenDeInicio.getJSONObject(i).getString("imagen1")));

                        String nombre = imagenDeInicio.getJSONObject(i).getString("descripcion");
                        String precio = imagenDeInicio.getJSONObject(i).getString("precio_etiqueta");
                        String imagenUrl = KaliopeServerClient.BASE_URL + imagenDeInicio.getJSONObject(i).getString("imagen1");

                        listaProductos.add(new Producto(nombre,precio,imagenUrl));
                    }





                    //llenamos los datos de uso de sesion
                    //ConfiguracionesApp.setDatosInicioSesion(activity,infoUsuario, clientes);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                enaviarArecycler();



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


                String info = "Status Code: " + String.valueOf(statusCode) +"  responseString: " + responseString;
                Log.d("onFauile 1" , info);
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


                String info = "StatusCode" + String.valueOf(statusCode) +"  Twhowable:   "+  throwable.toString();
                Log.d("onFauile 2" , info);
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
}