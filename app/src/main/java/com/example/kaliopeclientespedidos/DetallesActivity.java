package com.example.kaliopeclientespedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaliopeclientespedidos.adapter.DetallesImagenAdapter;
import com.example.kaliopeclientespedidos.adapter.SpinnerColoresAdapter;
import com.example.kaliopeclientespedidos.adapter.SpinnerTallasAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DetallesActivity extends AppCompatActivity {
    private boolean offline = false;


    //fuente para las animaciones y el snapHelper https://www.youtube.com/watch?v=4yyLeI4H1rQ   esta padrisimo!!

    TextView numeroImagenTV,
            nombreProductoTV,
            precioTV,
            fechaEntregaTV;

    Spinner spinerColores, spinnerTallas, spinnerCantidad;
    String colorSeleccionado = "";


    String id_producto;

    JSONObject productoInicial;
    JSONArray coloresIniciales;
    JSONArray tallasTotales;


    JSONArray tallasPorColor;


    ArrayList<HashMap> listaImagenesPrincipal;
   DetallesImagenAdapter detallesImagenAdapter;
    RecyclerView recyclerViewDetalles;
    RecyclerView.LayoutManager layoutManager;
    SnapHelper snapHelper;


    public final String URL_DETALLES_PRODUCTO = "app_movil/consultar_detalles_producto.php";
    public final String URL_DETALLES_POR_COLOR = "app_movil/consultar_detalles_por_color.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        numeroImagenTV = (TextView) findViewById(R.id.detalles_textViewNumero);
        recyclerViewDetalles = (RecyclerView) findViewById(R.id.detalles_recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewDetalles.setLayoutManager(layoutManager);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewDetalles);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id_producto = bundle.getString("ID_PRODUCTO");
        }else{
            throw new IllegalArgumentException("Detalles no puede encontrar extras");
        }

        Toast.makeText(this, id_producto, Toast.LENGTH_SHORT).show();


        consultaInicial();
    }



    private void llenarRecycler(){


        detallesImagenAdapter = new DetallesImagenAdapter(listaImagenesPrincipal);
        recyclerViewDetalles.setAdapter(detallesImagenAdapter);
        recyclerViewDetalles.setPadding(100,0,100,0);         //Creamos al recycler view un padding donde se mostrara la imagen esto es opcional esto con el atributto clipp to padding permitira que los demas items se visualisen en la orillita del recycler

        numeroImagenTV.setText("1 / 3");                                                //colocamos el texto por default

        recyclerViewDetalles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View v = snapHelper.findSnapView(layoutManager);            //encuentra la vista que esta en snap o "enfocada" por el snapHelper
                int position = layoutManager.getPosition(v);

                //buscamos el layout que contiene un item para animar ese layour
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.item_imagen_ropa_detalles_LinearLayoutParent);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    //si el escrol esta inactivo
                    linearLayout.animate().setDuration(350).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();

                    //aparte de la animacion cuando el scroll este inactivo voy a cambiar el valor del textview que indique el numero de imagen
                    String texto = (position + 1) + " / 3";
                    numeroImagenTV.setText(texto);
                }else{
                    linearLayout.animate().setDuration(350).scaleX(0.7f).scaleY(0.7f).setInterpolator(new AccelerateInterpolator()).start();
                }
            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }


        });


        //AHORA HEMOS LOGRADO QUE CUANDO SE HAGA SCROLL EL ITEM SE HAGA PEQUEÑO Y GRANDE CUANDO SE DEJA DE HACER SCROOL
        //LO QUE OCURRE ES QUE CUANDO SE ENTRA POR PRIMERA VES COMO NO SE HA HECHO SCROLL LA IMAGEN SE VE PEQUEÑA A LA ESCALA QUE PUSIMOS DE .75
        //HAREMOS QUE CUANDO SE ENTRA DESPUES DE UN TIEMPO SE AGRANDE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //buscamos el layout que contiene un item para animar ese layout pero ahora como no sabemos la posicion la pondemos en la inicial que es la 0
                RecyclerView.ViewHolder viewHolder = recyclerViewDetalles.findViewHolderForAdapterPosition(0);
                LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.item_imagen_ropa_detalles_LinearLayoutParent);
                linearLayout.animate().setDuration(350).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();
            }
        },500);

    }


    private void llenarVistas(){
        nombreProductoTV = (TextView) findViewById(R.id.detalles_nombreProducto);
        precioTV = (TextView) findViewById(R.id.detalles_TV_precio);
        fechaEntregaTV = (TextView) findViewById(R.id.detalles_TV_instruccionesFechaEntrega);


        String nombrePro = null;
        String precio = null;

        try {
            nombrePro = productoInicial.getString("descripcion");
            precio = productoInicial.getString("precio_etiqueta");

            nombreProductoTV.setText(nombrePro);
            precioTV.setText(precio);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void llenarSpinnerColor() {
        spinerColores = (Spinner) findViewById(R.id.detalles_spinner_color);

        spinerColores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorSeleccionado = String.valueOf(spinerColores.getSelectedItem());
                 HashMap map = (HashMap) spinerColores.getSelectedItem();
                 String colorSeleccionado = map.get(SpinnerColoresAdapter.RGB_COLOR_STRING).toString();
                 consultarDetallePorColor(id_producto,colorSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List <HashMap<String,String>> listaColores = new ArrayList<>();
        for (int i=0; i<coloresIniciales.length(); i++){
            HashMap<String, String> map = new HashMap<>();
            try {
                String colorTemp = coloresIniciales.getJSONObject(i).getString("color");
                String noColorTemp = coloresIniciales.getJSONObject(i).getString("noColor");
                String urlColorTemp = KaliopeServerClient.BASE_URL + "/" + coloresIniciales.getJSONObject(i).getString("imagen1");


                map.put(SpinnerColoresAdapter.NOMBRE_COLOR,colorTemp);
                map.put(SpinnerColoresAdapter.URL_IMAGEN,urlColorTemp);
                map.put(SpinnerColoresAdapter.RGB_COLOR_STRING,noColorTemp);
                listaColores.add(map);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        SpinnerColoresAdapter spinnerColoresAdapter = new SpinnerColoresAdapter(this, listaColores);
        spinerColores.setAdapter(spinnerColoresAdapter);

    }

    private void llenarSpinnerTallas(){
        //[{"talla":"UNT","0":"UNT"},{"talla":"G","0":"G"},{"talla":"M","0":"M"}]
        spinnerTallas = (Spinner) findViewById(R.id.detalles_spinner_talla);

        spinnerTallas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<HashMap<String,String>> listaTallas = new ArrayList<>();
        HashMap<String, String> map;


        if(offline){

            for (int i=0; i<tallasTotales.length(); i++){
                map = new HashMap<>();

                try {
                    String tallaTemp = tallasPorColor.getJSONObject(i).getString("talla");
                    map.put(SpinnerTallasAdapter.COLUMNA_TALLA,tallaTemp);
                    map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS,"0");
                    listaTallas.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }else{
            for (int i=0; i<tallasPorColor.length(); i++){
                map = new HashMap<>();

                try {
                    String tallaTemp = tallasPorColor.getJSONObject(i).getString("talla");
                    map.put(SpinnerTallasAdapter.COLUMNA_TALLA,tallaTemp);
                    map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS,"0");
                    listaTallas.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }



        SpinnerTallasAdapter spinnerTallasAdapter = new SpinnerTallasAdapter(listaTallas);
        spinnerTallas.setAdapter(spinnerTallasAdapter);



    }




    private void consultaInicial(){

        RequestParams params = new RequestParams();
        params.put("ID_PRODUCTO",id_producto);

        KaliopeServerClient.post(URL_DETALLES_PRODUCTO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("detalles1",String.valueOf(response));
                //D/detalles1: {"detalle_principal":{"id":"1","0":"1","id_producto":"SM5898","1":"SM5898","descripcion":"Sudadera dama","2":"Sudadera dama","detalles":"Luce sensacional con esta increible sudadera","3":"Luce sensacional con esta increible sudadera","estado":"ACTIVO","4":"ACTIVO","existencia":"5","5":"5","color":"Gris","6":"Gris","noColor":"rgb(142, 142, 142)","7":"rgb(142, 142, 142)","talla":"UNT","8":"UNT","precio_etiqueta":"339","9":"339","precio_vendedora":"298","10":"298","precio_socia":"295","11":"295","precio_empresaria":"291","12":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","13":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","14":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","15":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","16":"sudadera"},
                //"colores":[{"color":"Gris","2":"Gris","noColor":"rgb(142, 142, 142)","3":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","4":"fotos\/SM5898-VERDE-1.jpg"},
                // {"color":"Rosa","0":"Rosa","noColor":"rgb(240, 74, 141)","1":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","2":"fotos\/SM5898-ROSA-1.jpg"},
                // {"color":"Negro","0":"Negro","noColor":"rgb(13, 13, 13)","1":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","2":"fotos\/SM5898-NEGRO-1.jpg"},
                // {"color":"Azul","0":"Azul","noColor":"rgb(135, 182, 205)","1":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","2":"fotos\/SM5898-AZUL-1.jpg"}]
                // ,"tallas":[{"talla":"UNT","0":"UNT"},{"talla":"G","0":"G"},{"talla":"M","0":"M"}]}

                try {
                    productoInicial = response.getJSONObject("detalle_principal");
                    coloresIniciales = response.getJSONArray("colores");
                    tallasTotales = response.getJSONArray("tallas");
                    Log.d("detalles2",String.valueOf(productoInicial.toString()));
                    //D/detalles2: {"id":"1","0":"1","id_producto":"SM5898","1":"SM5898","descripcion":"Sudadera dama","2":"Sudadera dama","detalles":"Luce sensacional con esta increible sudadera","3":"Luce sensacional con esta increible sudadera","estado":"ACTIVO","4":"ACTIVO","existencia":"5","5":"5","color":"Gris","6":"Gris","noColor":"rgb(142, 142, 142)","7":"rgb(142, 142, 142)","talla":"UNT","8":"UNT","precio_etiqueta":"339","9":"339","precio_vendedora":"298","10":"298","precio_socia":"295","11":"295","precio_empresaria":"291","12":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","13":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","14":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","15":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","16":"sudadera"}
                    Log.d("detalles3",String.valueOf(coloresIniciales.toString()));
                    //D/detalles3:
                    //[{"color":"Gris","0":"Gris","noColor":"rgb(142, 142, 142)","1":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","2":"fotos\/SM5898-VERDE-1.jpg"},
                    //{ "color":"Rosa","0":"Rosa","noColor":"rgb(240, 74, 141)","1":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","2":"fotos\/SM5898-ROSA-1.jpg"},{"id":"3","0":"3","id_producto":"SM5898","1":"SM5898","color":"Negro","2":"Negro","noColor":"rgb(13, 13, 13)","3":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","4":"fotos\/SM5898-NEGRO-1.jpg"},
                    //{ "color":"Azul","0":"Azul","noColor":"rgb(135, 182, 205)","1":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","2":"fotos\/SM5898-AZUL-1.jpg"}
                    //]
                    Log.d("detalles4",String.valueOf(tallasTotales.toString()));
                    //[{"talla":"UNT","0":"UNT"},{"talla":"G","0":"G"},{"talla":"M","0":"M"}]


                    //=======LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========
                    //debido a que las 3 imagenes del producto vienen en el solo objeto json entonces vamos a llenar la lista haciendo 3 veces lo mismo xD
                    //con la imagen 1 la 2 y la 3 para que se manden al recycler view y ademas enviamos el nombre de la imagen por si se tiene que hacer algo con ella cuando se haga clic.

                    listaImagenesPrincipal = new ArrayList<HashMap>();

                    HashMap<String, String> map = new HashMap<>();
                    String nombre = productoInicial.get("imagen1").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = productoInicial.get("imagen2").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = productoInicial.get("imagen3").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);
                    Log.d("detalles4",listaImagenesPrincipal.toString());
                    //=======FIN LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========


                    llenarRecycler();
                    llenarVistas();
                    llenarSpinnerColor();
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



    /**
     * Contectar al servidor y retornaremos las 3 imagenes del color seleccionado para que sea visualizado en el recycler view
     * ademas buscaremos las tallas disponibles en ese color y las existencias totales
     * @param id_producto
     * @param colorSeleccionado
     */
    private void consultarDetallePorColor(String id_producto, String colorSeleccionado) {

        RequestParams params = new RequestParams();
        params.put("ID_PRODUCTO", id_producto);
        params.put("COLOR_SELECCIONADO", colorSeleccionado);

        KaliopeServerClient.post(URL_DETALLES_POR_COLOR, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("detalles1.1",String.valueOf(response));
                //{"imagenesColor":{"imagen1":"fotos\/SM5898-AZUL-1.jpg","0":"fotos\/SM5898-AZUL-1.jpg","imagen2":"fotos\/SM5898-AZUL-2.jpg","1":"fotos\/SM5898-AZUL-2.jpg","imagen3":"fotos\/SM5898-AZUL-3.jpg","2":"fotos\/SM5898-AZUL-3.jpg"},"tallas":[{"talla":"G","0":"G"}]}

                try {
                    JSONObject productoPorColor = response.getJSONObject("imagenesColor");
                    tallasPorColor = response.getJSONArray("tallas");
                    Log.d("detalles2.1",String.valueOf(productoPorColor.toString()));
                    //{"imagen1":"fotos\/SM5898-ROSA-1.jpg","0":"fotos\/SM5898-ROSA-1.jpg","imagen2":"fotos\/SM5898-ROSA-2.jpg","1":"fotos\/SM5898-ROSA-2.jpg","imagen3":"fotos\/SM5898-ROSA-3.jpg","2":"fotos\/SM5898-ROSA-3.jpg"}
                    Log.d("detalles3.1",String.valueOf(tallasPorColor.toString()));
                    //[{"talla":"UNT","0":"UNT"},{"talla":"M","0":"M"}]



                    //=======LIMPIAMOS Y LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW PERO AHORA SERAN LAS DEL COLOR SELECICONADO========
                    //debido a que las 3 imagenes del producto vienen en el solo objeto json entonces vamos a llenar la lista haciendo 3 veces lo mismo xD
                    //con la imagen 1 la 2 y la 3 para que se manden al recycler view y ademas enviamos el nombre de la imagen por si se tiene que hacer algo con ella cuando se haga clic.

                    listaImagenesPrincipal.clear();                         //importante hacer el clear y no volver a inicializar el objeto con new, porque el recycler view usa una referencia de este objeto

                    HashMap<String, String> map = new HashMap<>();
                    String nombre = productoPorColor.get("imagen1").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = productoPorColor.get("imagen2").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = productoPorColor.get("imagen3").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);
                    Log.d("detalles4.1",listaImagenesPrincipal.toString());
                    //=======FIN LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========






                   detallesImagenAdapter.notifyDataSetChanged();
                   llenarRecycler();                        //llenamos otra vez todo el recycler porque tengo problemas con las animaciones, se comienzan a hacer mas y mas chiquito con forme cambio de colores. Asi ya no tiene el problema lo malo es que se regresa a la posicion 0
                   llenarSpinnerTallas();
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
