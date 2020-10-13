package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MainActivityRecycler extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView recyclerViewPublicidad1;       //una instancia para el recicler view que contendra las imagenes

    ArrayList<HashMap> listaProductos;
    HashMap map;

    ArrayList<HashMap> listaPublicidad;
    HashMap mapPublicidad;



    JSONArray categorias;
    JSONArray imagenDeInicio;


    private AdapterViewFlipper adapterViewFlipperPublicidad;
    private AdapterPublicidad adapterPublicidad;

    private final String URL_CATEGORIAS ="app_movil/consultar_categorias.php";
    private final String URL_IMAGEN_PRINCIPAL = "app_movil/consultarImagenPrincipal.php";

    public static final String ADAPTER_COLUMN_PRICE = "PRICE";
    public static final String ADAPTER_COLUMN_EXISTENCIAS = "EXISTENCIAS";
    public static final String ADAPTER_COLUMN_DISPONIBILIDAD = "DISP";
    public static final String ADAPTER_COLUMN_DESCRIPCION = "DESCRIPTION";
    public static final String ADAPTER_COLUMN_NOMBRE = "NAME";
    public static final String ADAPTER_COLUMN_IMAGE_PREVIEW = "PREVIEW";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);
        Slide slide = new Slide(Gravity.RIGHT);
        slide.setDuration(1000);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);




        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        //recyclerViewPublicidad1 = (RecyclerView) findViewById(R.id.recycler_publicidadMain);   //tuvimos que crear un layout que contenga solo otro recicler para meter ese recycler dentro del recycler primario, no lo se parese que si es asi, tengo que tener un archivo layout con el recycler o crearlo programaticamente, y ademas un item que sera el que se infle en cada item del recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //recyclerViewPublicidad1.setLayoutManager(new GridLayoutManager(this, 2));

        consultarImagenPrincipal();





/**
        adapterViewFlipperPublicidad = (AdapterViewFlipper) findViewById(R.id.main_AVF_publicidad);
        int[]imagenes = {R.drawable.cortador1, R.drawable.cortador2};

        adapterPublicidad = new AdapterPublicidad(this,imagenes);
        adapterViewFlipperPublicidad.setAdapter(adapterPublicidad);
        adapterViewFlipperPublicidad.setFlipInterval(4000);
        adapterViewFlipperPublicidad.setAutoStart(true);

**/


    }


    private void enaviarArecycler(){

        listaProductos = new ArrayList<>();

        for (int i=0; i<imagenDeInicio.length() ; i++){


            try {

                map = new HashMap();
                map.put(ADAPTER_COLUMN_PRICE,imagenDeInicio.getJSONObject(i).getString("precio_etiqueta"));
                map.put(ADAPTER_COLUMN_NOMBRE,imagenDeInicio.getJSONObject(i).getString("descripcion"));
                map.put(ADAPTER_COLUMN_EXISTENCIAS,imagenDeInicio.getJSONObject(i).getString("existencia"));
                map.put(ADAPTER_COLUMN_DISPONIBILIDAD, "DISPONIBLE");
                map.put(ADAPTER_COLUMN_DESCRIPCION, "");


                String URL_Imagen = KaliopeServerClient.BASE_URL + imagenDeInicio.getJSONObject(i).getString("imagen1");
                map.put(ADAPTER_COLUMN_IMAGE_PREVIEW,URL_Imagen);
                Log.d("UrlImagenes",String.valueOf(URL_Imagen));
                listaProductos.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listaPublicidad = new ArrayList<>();
        mapPublicidad = new HashMap();
        mapPublicidad.put(AdapterPublicidadRecycler.ADAPTER_IMAGE_ID,R.drawable.cortador1);
        listaPublicidad.add(mapPublicidad);
        mapPublicidad = new HashMap();
        mapPublicidad.put(AdapterPublicidadRecycler.ADAPTER_IMAGE_ID,R.drawable.cortador2);
        listaPublicidad.add(mapPublicidad);



        AdapterRopa adapterRopa = new AdapterRopa(listaProductos,this);
        AdapterPublicidadRecycler adapterPublicidadRecycler = new AdapterPublicidadRecycler(listaPublicidad,this);




        //recyclerViewPublicidad1.setAdapter(adapterPublicidadRecycler);
        //recyclerView.setAdapter(adapterRopa);

        //prueba con merge adapter

        ConcatAdapter concatAdapter = new ConcatAdapter(adapterPublicidadRecycler,adapterRopa);
        recyclerView.setAdapter(concatAdapter);


    }


    private void consultarImagenPrincipal(){


        KaliopeServerClient.get(URL_CATEGORIAS,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));

                try {
                    categorias = response.getJSONArray("categorias");
                    imagenDeInicio = response.getJSONArray("imagenInicio");
                    Log.d("datosProces",String.valueOf(categorias.toString()));
                    Log.d("datosProcesImagenInicio",String.valueOf(imagenDeInicio.toString()));


                    for (int i=0; i<categorias.length() ; i++){
                        Log.d("datosProcesCategorias",String.valueOf(categorias.getJSONObject(i).getString("categoria")));
                    }



                    for (int i=0; i<imagenDeInicio.length() ; i++){

                        Log.d("datosProcesImagen",String.valueOf(imagenDeInicio.getJSONObject(i).getString("imagen1")));
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


    @Override
    public void onBackPressed() {
        if(ConfiguracionesApp.getEntradaComoInvitado(this)){
            super.onBackPressed();
        }else{
            moveTaskToBack(true);
        }

    }






/*
    //https://es.switch-case.com/53695591
    private static void setListViewHeightBasedOnChildren(GridView gridView) {
        AdapterGrid gridAdapter =(AdapterGrid) gridView.getAdapter();
        if (gridAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        //obtenemos cada item ya inflado con los datos requeridos
        //medimos cada altura de item
        //y en base al numero de elementos, sumamos todas sus alturas
        //asi sabremos la medida total de todos los elementos
        for (int i = 0; i < gridAdapter.getCount(); i++) {
            View listItem = gridAdapter.getView(i, null, gridView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight()+20;//sumo a la medida de cada item no entiendo en que parte se esta comiendo medida, al final sale mas corto
        }


        ViewGroup.LayoutParams params = gridView.getLayoutParams();

        //sabemos la medida total de todos los items pero! no esta incluida en esa medida
        //el espacio del separador entre las filas entonces hay que sacar la medida del separador
        //y multiplicarla por el numero de items para obtener una altura total con items y separadores
        //ahora todas esas medidas tomadas totales las dividimos entre el numero de columnas que se estan
        //desplegando al mismo tiempo en el grid view, porque la medida obtenida es total pero como si los
        //items estuvieran en una lista, como mostramos 2 columnas entonces dividimos entre dos la medida total
        int verticalSpacing = (gridView.getVerticalSpacing() * (gridAdapter.getCount()));
        int finalHeight = totalHeight + verticalSpacing;
        params.height = finalHeight/2;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

 */
}