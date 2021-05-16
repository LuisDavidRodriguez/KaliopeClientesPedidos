 package com.example.kaliopeclientespedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaliopeclientespedidos.adapter.DetallesImagenAdapter;
import com.example.kaliopeclientespedidos.adapter.SeleccionarCantidadAdapter;
import com.example.kaliopeclientespedidos.adapter.SpinnerColoresAdapter;
import com.example.kaliopeclientespedidos.adapter.SpinnerTallasAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.kaliopeclientespedidos.Constantes.offline;

public class DetallesActivity extends AppCompatActivity {


    //fuente para las animaciones y el snapHelper https://www.youtube.com/watch?v=4yyLeI4H1rQ   esta padrisimo!!

    TextView numeroImagenTV,
            nombreProductoTV,
            modeloTV,
            precioTV,
            fechaEntregaTV,
            fechaCierreTV;

    Spinner spinerColores, spinnerTallas;
    RecyclerView cantidadRecyclerView;
    Button buttonAgregarCarrito;
    String colorSeleccionado = "";
    String tallaSeleccionada = "";
    int cantidadSeleccioanda = 0;


    String id_producto;

    JSONObject informacionProductoInicial;



    ArrayList<HashMap> listaImagenesPrincipal = new ArrayList<HashMap>();;
    DetallesImagenAdapter detallesImagenAdapter;
    RecyclerView recyclerViewDetalles;
    RecyclerView.LayoutManager layoutManager;
    SnapHelper snapHelper;




    RecyclerView.LayoutManager layoutManagerCantidad;
    SeleccionarCantidadAdapter seleccionarCantidadAdapter;
    SnapHelper snapHelperCantidad;
    ArrayList<Integer> cantidadesSeleccionables = new ArrayList<>();



    MediaPlayer mediaPlayer;


    public final String URL_DETALLES_PRODUCTO = "app_movil/consultar_detalles_producto.php";
    public final String URL_DETALLES_POR_COLOR = "app_movil/consultar_detalles_por_color.php";
    public final String URL_RECIBIR_PEDIDO = "app_movil/recibir_pedido.php";

    Animation animationSacudida;
    Animation animationLlegada;

    ProgressDialog progressDialog;
    Activity activity = this;
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



        animationSacudida = AnimationUtils.loadAnimation(this,R.anim.sacudida_2); //cargamos la animacion pruebas de animacion de elementos en la pantalla
        animationLlegada = AnimationUtils.loadAnimation(this,R.anim.llegada); //cargamos la animacion pruebas de animacion de elementos en la pantalla

        animationSacudida.setFillAfter(false);//para que se quede donde termina la anim
        animationSacudida.setRepeatMode(Animation.REVERSE); //modo de repeticion, en el reverse se ejecuta la animacion y cuando termine de ejecutarse va  adar reversa
        animationSacudida.setRepeatCount(10); //cuantas veces queremos que se repita la animacion, podria ser un numero entero 20 para 20 veces por ejemplo

        animationLlegada.setFillAfter(false);//para que se quede donde termina la anim
        animationLlegada.setRepeatMode(Animation.REVERSE); //modo de repeticion, en el reverse se ejecuta la animacion y cuando termine de ejecutarse va  adar reversa
        animationLlegada.setRepeatCount(4); //cuantas veces queremos que se repita la animacion,

        spinerColores = (Spinner) findViewById(R.id.detalles_spinner_color);
        spinnerTallas = (Spinner) findViewById(R.id.detalles_spinner_talla);
        cantidadRecyclerView = (RecyclerView) findViewById(R.id.detalles_cantidadRecyclerView);
        buttonAgregarCarrito = (Button) findViewById(R.id.detalles_agregarCarritoButton);

        layoutManagerCantidad = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        cantidadRecyclerView.setLayoutManager(layoutManagerCantidad);
        snapHelperCantidad = new LinearSnapHelper();
        snapHelperCantidad.attachToRecyclerView(cantidadRecyclerView);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id_producto = bundle.getString("ID_PRODUCTO");
        }else{
            throw new IllegalArgumentException("Detalles no puede encontrar extras");
        }

        Toast.makeText(this, id_producto, Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(this,R.raw.click);



        if(offline){

            recargarModoOffline();

        }else{
            consultaInicial();
        }




        buttonAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(offline){
                    dialogoOffline();
                }else{

                        enviarProductoAlServidor();

                }

            }
        });











    }








    private void consultaInicial(){

        RequestParams params = new RequestParams();
        params.put("ID_PRODUCTO",id_producto);

        showProgresDialog(this);
        KaliopeServerClient.post(URL_DETALLES_PRODUCTO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();

                Log.d ("detalles1",String.valueOf(response));
                //{"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","precio_vendedora":"298","precio_empresaria":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","existencias":120,
                // "tallas":[{"talla":"UNT","existencias":55},{"talla":"G","existencias":50},{"talla":"M","existencias":15}],
                // "colores":[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
                // ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
                // ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
                // ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]}



                informacionProductoInicial = response;




                llenarListaParaRecycler(informacionProductoInicial);
                llenarRecycler();
                llenarVistas();
                llenarSpinnerColor();






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


                String info = "StatusCode" + String.valueOf(statusCode) +"  Twhowable:   "+  throwable.toString();
                Log.d("onFauile 2" , info);
                //Toast.makeText(MainActivity.this,info, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", "No hemos podido conectar con el servidor "+errorResponse + "\nStatus Code: " + String.valueOf(statusCode));

                new AlertDialog.Builder(activity)
                        .setTitle("No hay conexion a internet")
                        .setMessage("No hemos podido conectar con el servidor. Mostraremos los datos en modo \"sin internet\"")
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                offline = true;
                                recargarModoOffline();
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
                    JSONArray tallasPorColor = response.getJSONArray("tallas");
                    Log.d("detalles2.1",String.valueOf(productoPorColor.toString()));
                    //{"imagen1":"fotos\/SM5898-ROSA-1.jpg","0":"fotos\/SM5898-ROSA-1.jpg","imagen2":"fotos\/SM5898-ROSA-2.jpg","1":"fotos\/SM5898-ROSA-2.jpg","imagen3":"fotos\/SM5898-ROSA-3.jpg","2":"fotos\/SM5898-ROSA-3.jpg"}
                    Log.d("detalles3.1",String.valueOf(tallasPorColor.toString()));
                    //[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]




                   listaImagenesPrincipal.clear();                         //importante hacer el clear y no volver a inicializar el objeto con new, porque el recycler view usa una referencia de este objeto
                   llenarListaParaRecycler(productoPorColor);
                   detallesImagenAdapter.notifyDataSetChanged();
                   llenarRecycler();                        //llenamos otra vez todo el recycler porque tengo problemas con las animaciones, se comienzan a hacer mas y mas chiquito con forme cambio de colores. Asi ya no tiene el problema lo malo es que se regresa a la posicion 0
                   llenarSpinnerTallas(tallasPorColor);
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
     * En diversas partes del codigo deberemos llenar la lista con las imagenes del producto
     * que se mostraran en el recycler, debemos usar este metodo porque si no repetiremos mucho codigo
     * cuando se entra por primera vez a detalles dependiendo de si esta en modo offline o online
     * se deberan cargar las imagenes a recycler de diferentes fuentes una de red y otra almacenada
     * en la memoria.
     * Cuando el cliente selecciona el color, se descargan las 3 imagenes del color seleccionado
     * y deberemos cargarlas al recycler de una fuente diferente
     * @param imagenesPorMostrar    El json objet con las imagenes del producto por color
     */
    private void llenarListaParaRecycler(JSONObject imagenesPorMostrar){
        //=======LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========
        //debido a que las 3 imagenes del producto vienen en el solo objeto json entonces vamos a llenar la lista haciendo 3 veces lo mismo xD
        //con la imagen 1 la 2 y la 3 para que se manden al recycler view y ademas enviamos el nombre de la imagen por si se tiene que hacer algo con ella cuando se haga clic.

        try {
            HashMap<String, String> map = new HashMap<>();
            String nombre = imagenesPorMostrar.get("imagen1").toString();
            map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
            map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
            listaImagenesPrincipal.add(map);

            map = new HashMap<>();
            nombre = imagenesPorMostrar.get("imagen2").toString();
            map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
            map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
            listaImagenesPrincipal.add(map);

            map = new HashMap<>();
            nombre = imagenesPorMostrar.get("imagen3").toString();
            map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
            map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
            listaImagenesPrincipal.add(map);
            Log.d("detalles4",listaImagenesPrincipal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //=======FIN LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========

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
        modeloTV = (TextView) findViewById(R.id.detalles_modelo);
        precioTV = (TextView) findViewById(R.id.detalles_TV_precio);
        fechaEntregaTV = (TextView) findViewById(R.id.detalles_TV_instruccionesFechaEntrega);
        fechaCierreTV = (TextView) findViewById(R.id.detalles_fechaCierrePedidoTV);


        String nombrePro = "";
        String modelo = "";
        String precio = "";
        String mensajeFechaEntrega = "";
        String recursoFechaCierrePedido = "";
        String mensajeFechaCierrePedido = "";

        Date auxiliar = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); //EL FORMATO QUE VA A RECIBIR PARA CONVERTIRLO
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE, d-MMM",Locale.ENGLISH); //EL FORMATO QUE VA A ENTREGAR LUN, 16-DIC
        try {
            auxiliar = simpleDateFormat.parse(ConfiguracionesApp.getDatoClienteOffline(this,ConfiguracionesApp.CLAVE_FECHA_FUTURA));
            mensajeFechaEntrega = getResources().getString(R.string.si_ordenas_ahora ) + simpleDateFormat1.format(auxiliar);

            mensajeFechaCierrePedido = ConfiguracionesApp.getDatoClienteOffline(this, ConfiguracionesApp.CLAVE_MENSAJE_CIERRE_PEDIDO);
        } catch (ParseException e) {
            e.printStackTrace();
            mensajeFechaEntrega = getResources().getString(R.string.si_ordenas_ahora );
        }

        fechaEntregaTV.setText(mensajeFechaEntrega);
        fechaCierreTV.setText(mensajeFechaCierrePedido);

        try {
            /*
                {"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","precio_vendedora":"298","precio_empresaria":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","existencias":120,
                "tallas":[{"talla":"UNT","existencias":55},{"talla":"G","existencias":50},{"talla":"M","existencias":15}],
                "colores":[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
                ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
                ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
                ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]}
             */
            nombrePro = informacionProductoInicial.getString("descripcion");
            modelo = informacionProductoInicial.getString("id_producto");
            precio = informacionProductoInicial.getString("precio_etiqueta");


            nombreProductoTV.setText(nombrePro);
            modeloTV.setText(modelo);
            precioTV.setText(precio);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void llenarSpinnerColor() {


        List<HashMap<String, String>> listaColores = new ArrayList<>();
        JSONArray coloresTotales = new JSONArray();
        try {
            coloresTotales = informacionProductoInicial.getJSONArray("colores");                          //aqui contendremos los colores que hay de este modelo con sus tallas existentes
            Log.d("coloresTotales", String.valueOf(coloresTotales.toString()));
             /*
            COLORES TOTALES con informacion de cuantas tallas tiene disponible cada color
             [{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
             ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
            ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
            ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]

            importante las existencias que se muestran aqui son existentes para esa talla y ese color en especifico
        */
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < coloresTotales.length(); i++) {
            HashMap<String, String> map = new HashMap<>();

            try {
                String colorTemp = coloresTotales.getJSONObject(i).getString("color");
                String noColorTemp = coloresTotales.getJSONObject(i).getString("noColor");
                String urlColorTemp = KaliopeServerClient.BASE_URL + "/" + coloresTotales.getJSONObject(i).getString("imagen1");
                int existenciasTemp = Integer.parseInt(coloresTotales.getJSONObject(i).getString("existencias"));


                map.put(SpinnerColoresAdapter.NOMBRE_COLOR, colorTemp);
                map.put(SpinnerColoresAdapter.URL_IMAGEN, urlColorTemp);
                map.put(SpinnerColoresAdapter.RGB_COLOR_STRING, noColorTemp);
                map.put(SpinnerColoresAdapter.EXISTENCIAS, String.valueOf(existenciasTemp));
                map.put(SpinnerColoresAdapter.ACTIVO, existenciasTemp>0?"true":"false");
                listaColores.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }





        final SpinnerColoresAdapter spinnerColoresAdapter = new SpinnerColoresAdapter(this, listaColores);
        spinerColores.setAdapter(spinnerColoresAdapter);

        final JSONArray finalColoresTotales = coloresTotales;
        spinerColores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap map = (HashMap) spinerColores.getSelectedItem();
                boolean itemActivo = Boolean.parseBoolean(map.get(SpinnerColoresAdapter.ACTIVO).toString());


                if(itemActivo){
                    colorSeleccionado = map.get(SpinnerColoresAdapter.RGB_COLOR_STRING).toString();
                    spinnerTallas.setVisibility(View.VISIBLE);
                    spinnerTallas.startAnimation(animationLlegada);
                    buttonAgregarCarrito.setVisibility(View.VISIBLE);



                    /*
                 Si estamos en modo ofline le enviamos como parametro las tallas que
                 existen en el color seleccionado
                 si estamos online consultamos los datos al servidor
                  */

                    if(offline){
                        Log.d(Constantes.TAG_OFFLINE,"Mostrando las tallas de la informacion guardada en shared preferences");
                        //le enviamos las tallas del color seleccionado
                        //estas tallas ya se encuentran el el array que se descargo la primera vez y que se obtubo con COnfiguracionesApp
                        //en modo offline no mostramos las imagenes del color seleccioando en los detalles
                        try {
                            JSONArray tallasPorColor = finalColoresTotales.getJSONObject(position).getJSONArray("tallas");
                            llenarSpinnerTallas(tallasPorColor);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Log.d(Constantes.TAG_ONLINE,"Consultando las tallas con el servidor");
                        consultarDetallePorColor(id_producto,colorSeleccionado);
                        //si esta en online vamos a conectarnos al servidor para consultar las tallas reales
                        // y a su vez vamos a mostrar las imagenes del color seleccionado en los detalles del producto
                    }

                }else{

                    colorSeleccionado = "";
                    buttonAgregarCarrito.setVisibility(View.INVISIBLE);

                    Snackbar.make(view, "Por favor seleccione una color con existencias", Snackbar.LENGTH_SHORT).setAction("accion",null).show();


                    spinerColores.startAnimation(animationSacudida);
                    spinnerTallas.setVisibility(View.INVISIBLE);            //escondemos el item tallas
                    cantidadRecyclerView.setVisibility(View.INVISIBLE);

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void llenarSpinnerTallas(JSONArray tallasPorColor){
        /*
        En este metodo usaremos las 2 listas de informacion de tallas que queremos
        las tallasTotales de ese producto son las tallas que ese producto tiene disponibles sin improtar su color
        y las tallas por color son las tallas existentes de este producto en un color espesifico seleccionado
        queremos ambas listas porque quiero mostrar en el espiner tallas absolutamente todas las talals
        que el producto tiene disponible y luego cuando el cliente seleccione un color especifico
        tomar las tallas que tiene disponibles ese color y poner esas tallas como disponibles y las demas
        marcarlas como agotadas en color gris, asi el cliente se dara una idea
        a bueno para este producto hay talla chica mediana grande y extra, pero cuando selecciono
        un color espesifico de ese la chica esta agotada, la mediana hay 10 pz la grande agotada y la extra 10pz

        lo mas facil seria llenar el sponner tallas solo con las tallas existentes del color seleciconado
        pero pienso personalmente que podria ser confuso para el cliente saber que tallas realmente existen
        porque veria que al cambiar el color a rojo por ejemplo solo hay talla chica pero no puede saber
        si ese producto tambien se maneja en extragrande, tendria que cambiar entre todos los colores para
        darse una idea de las tallas, y quizas podria ser confuso para el que cuando selecciona rojo
        en la lista si aparese la talla mediana, pero cuando selecciona el color azul solo aparece grande y chica
        diria: en donde quedo la talla mediana no la veo!!

        asi en cambio el sistema le dice a bueno aqui esta la talla mediana pero en este color no esta disponible

        ademas el mostrar todas las tallas disponibles me ayudara para el modo offline porque asi el clietne
        entrara al producto aunque no este en red y podria saber que tallas tiene ese producto y tomar el pedido
        a lapicero en una libreta, y de la otra manera no se puede porque se necesita la red para actualizar por el color

        o bueno se podria hacer de ambos modos, que cuando este en offline carge las tallas totales existentes y sea inmutable
        y que cuando sea por red carge solo las tallas de ese color

        pero en mi caso yo crei mejor opcion ir por la mas compleja y mostrar todas las tallas y poner en gris
        las tallas que no estan en ese color de esta manera no me importa si esta online o offline el sistema
        le muestra todas las tallas que puede solicitar
         */



        List<HashMap<String,String>> listaTallas = new ArrayList<>();
        HashMap<String, String> map;
        JSONArray tallasTotales = new JSONArray();
        try {
            tallasTotales = informacionProductoInicial.getJSONArray("tallas");
            Log.d("tallasTotales",String.valueOf(tallasTotales.toString()));
        /*
            TALLAS TOTALES sin importar color, con existencias
            estas son todas las tallas que existen de ese modelo en espesifico sin importar color
            [{"talla":"UNT","existencias":55}
             ,{"talla":"G","existencias":50}
             ,{"talla":"M","existencias":15}]


             Llenamos la lista de tallas que se mostrara en el spinner
            sera con las tallas totales existentes de ese modelo


             lo que queremos mostrarle al cliente es que el spinner
             se llene con todas las tallas que hay disponibles del producto
             y que una vez que seleccione un color las tallas que ya no esten disponibles
             en ese color se pongan de un color diferente y ya no se puedan seleccionar
             y las tallas que estan disponibles muestren las existencias disponibles
         */
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("tallasColorSeleccionado", tallasPorColor.toString());
        /*Estas son las tallas que existen solo en el color que el cliente selecciono
            [{"talla":"UNT","existencias":5}
            ,{"talla":"G","existencias":40}]
        */


        try {

            //metemos el array de tallas pequeño a una lista para despues preguntar si la talla
            //existe dentro de esa lista
            ArrayList<String> tallasPequenas = new ArrayList<>();
            for(int z=0; z<tallasPorColor.length(); z++ ){
                tallasPequenas.add(tallasPorColor.getJSONObject(z).getString("talla"));
            }


            for (int i = 0; i < tallasTotales.length(); i++) {
                map = new HashMap<>();
                String tallaEnCurso = tallasTotales.getJSONObject(i).getString("talla");                          //tomamos la talla en curso del array total de tallas sin importar color
                //ahora recorremos el array de todas las tallas y comparamos la talla en curso
                //a ver si existe en la lista donde estan las tallas por color si existe
                //la talla grande la marcamos como activa y tomamos las existencias de las tallas por color
                //si no existe lo ponemos como inactivo
                if(tallasPequenas.contains(tallaEnCurso)){

                    int posicion = tallasPequenas.indexOf(tallaEnCurso);                                                                           //obtenemos el indice de la lista donde esta la talla que consultamos para saber despues consultar esa posicion en el josonArray que contiene la informacion de las existencias
                    String existencias = tallasPorColor.getJSONObject(posicion).getString("existencias");

                    map.put(SpinnerTallasAdapter.COLUMNA_TALLA, tallaEnCurso);
                    map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS, existencias);      //colocamos las existencias de la talla por color

                    //dependiendo de si hay existencias o no hay lo ponemos que se pueda seleccionar o que no se pueda
                    if(Integer.parseInt(existencias)>0){
                        map.put(SpinnerTallasAdapter.COLUMNA_ACTIVO, "true");
                    }else{
                        map.put(SpinnerTallasAdapter.COLUMNA_ACTIVO, "false");
                    }



                    listaTallas.add(map);
                }else{
                    /*
                    En este punto significa que si en total existe talla G M CH en este modelo pero al consltar color solo se encuentran G
                    lo que es M y CH se mostraran pero se pondran como inactivas, para que cuando el cleinte las seleccine arroje un error
                     */
                    map.put(SpinnerTallasAdapter.COLUMNA_TALLA, tallaEnCurso);
                    map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS,"0");
                    map.put(SpinnerTallasAdapter.COLUMNA_ACTIVO, "false");
                    listaTallas.add(map);

                }

            }
            /*
            Ya que se termino de recorrer todas las tallas mayores se finaliza el bucle for y asignamos
            la lista al spiner aqui ya vendran las tallas inactivas o activas
             */

        }catch (JSONException e){
            e.printStackTrace();
        }



        final SpinnerTallasAdapter spinnerTallasAdapter = new SpinnerTallasAdapter(listaTallas);
        spinnerTallas.setAdapter(spinnerTallasAdapter);







        spinnerTallas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap mapSelected =(HashMap) spinnerTallas.getSelectedItem();         //obtenemos el hasmap que tiene el item seleccionado
                boolean tallaActiva = Boolean.parseBoolean(mapSelected.get(SpinnerTallasAdapter.COLUMNA_ACTIVO).toString());


                if(tallaActiva){
                    tallaSeleccionada = mapSelected.get(SpinnerTallasAdapter.COLUMNA_TALLA).toString();



                    cantidadRecyclerView.setVisibility(View.VISIBLE);
                    cantidadRecyclerView.startAnimation(animationLlegada);
                    buttonAgregarCarrito.setVisibility(View.VISIBLE);
                    try {
                        int cantidadMaxima = Integer.parseInt(mapSelected.get(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS).toString());
                        llenarRecyclerCantidad(cantidadMaxima);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        llenarRecyclerCantidad(1);
                    }

                }else{
                    tallaSeleccionada = "";
                    Snackbar.make(view, "Por favor seleccione una talla con existencias", Snackbar.LENGTH_SHORT).setAction("accion",null).show();

                    cantidadRecyclerView.setVisibility(View.INVISIBLE);
                    spinnerTallas.startAnimation(animationSacudida);

                    buttonAgregarCarrito.setVisibility(View.INVISIBLE);

                    /*
                    //recorremos todos los items del spinner buscando alguno que este como activo, y seleccionamos el primero que este activo
                    for(int i =0; i<spinnerTallasAdapter.getCount(); i++){
                        HashMap buscandoItemActivo =(HashMap) spinnerTallas.getItemAtPosition(i);
                        tallaActiva = Boolean.parseBoolean(buscandoItemActivo.get(SpinnerTallasAdapter.COLUMNA_ACTIVO).toString());
                        if(tallaActiva){
                            spinnerTallas.setSelection(i,true);

                            break;
                        }
                    }*/




                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });










    }

    private void llenarRecyclerCantidad(int cantidadMaxima){

        cantidadesSeleccionables.clear();
        for (int i=1; i<= cantidadMaxima; i++){
            cantidadesSeleccionables.add(i);
        }


        seleccionarCantidadAdapter = new SeleccionarCantidadAdapter(cantidadesSeleccionables);
        seleccionarCantidadAdapter.notifyDataSetChanged();
        cantidadRecyclerView.setAdapter(seleccionarCantidadAdapter);
        cantidadRecyclerView.setPadding(0,120,0,120);         //Creamos al recycler view un padding solo al primer y ultimo item, esto nos ayuda a que aparesca centrado el primer item y ultimo a la atura de nuestro recyclerView lo voy centrando al tanteo a ver que pasa xD



        cantidadRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelperCantidad.findSnapView(layoutManagerCantidad);        //obtenemos la vista que esta snapeada xD
                int posicion = layoutManagerCantidad.getPosition(view);            //obtenemos la posicion del item snapeado

                mediaPlayer.start();


                //obtenemos el layout que contiene la vista del container para animarla
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(posicion);
                LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.item_container_cantidad_layout);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    linearLayout.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
                    linearLayout.setBackgroundResource(R.drawable.cuadro_redondeado);
                    cantidadSeleccioanda = (int) seleccionarCantidadAdapter.getItemValue(posicion);



                }else{
                    linearLayout.animate().scaleX(0.90f).scaleY(0.90f).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
                    linearLayout.setBackgroundResource(R.drawable.cuadro_redondeado_rosa_sin_bordes);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //obtenemos el view holder de la posicion
                RecyclerView.ViewHolder viewHolder = cantidadRecyclerView.findViewHolderForAdapterPosition(0);
                LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.item_container_cantidad_layout);
                linearLayout.animate().scaleY(1).scaleX(1).setInterpolator(new DecelerateInterpolator()).setDuration(500).start();
                linearLayout.setBackgroundResource(R.drawable.cuadro_redondeado);
                cantidadSeleccioanda = 1;
            }
        },500);



    }

    /**
     * Este metodo llama a los metodos necesarios para refrescar la vista en caso de llamar al modo offline
     */
    private void recargarModoOffline(){
        try {
            Log.d(Constantes.TAG_OFFLINE ,"mostrando datos almacenados en shared preferences");
            informacionProductoInicial = ConfiguracionesApp.getInformacionOfflineProducto(this, id_producto);
            llenarListaParaRecycler(informacionProductoInicial);
            llenarRecycler();
            llenarVistas();
            llenarSpinnerColor();
                /*
                    {"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","precio_vendedora":"298","precio_empresaria":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","existencias":120,
                     "tallas":[{"talla":"UNT","existencias":55},{"talla":"G","existencias":50},{"talla":"M","existencias":15}],
                     "colores":[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
                     ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
                     ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
                     ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]}

                 */

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener la cadena en modo offline " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void dialogoOffline(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        String recursoMensaje = getResources().getString(R.string.Offline_carrito);
        String mensaje = "";
        try {
            mensaje = String.format(recursoMensaje,informacionProductoInicial.getString("id_producto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.setTitle(R.string.sin_internet);
        builder.setMessage(mensaje);
        builder.setPositiveButton(R.string.entiendo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();
    }



    private void enviarProductoAlServidor() {
        RequestParams params = new RequestParams();
        params.put("CUENTA_CLIENTE", ConfiguracionesApp.getCuentaCliente(this));
        params.put("ID_PRODUCTO", id_producto);
        params.put("COLOR_SELECCIONADO", colorSeleccionado);
        params.put("TALLA_SELECCIONADA", tallaSeleccionada);
        params.put("CANTIDAD_SELECCIONADA", cantidadSeleccioanda);

        showProgresDialog(activity);

        KaliopeServerClient.post(URL_RECIBIR_PEDIDO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("detalles1", String.valueOf(response));
                //D/detalles1: {"mensaje":"Tu producto se ha agregado correctamente a tu carrito"}


                try {
                    String responseString = response.getString("mensaje");

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle("Exito")
                            .setMessage(responseString + "\n\n Status Code" + statusCode)
                            .setIcon(R.drawable.logo_kaliope_burbuja)
                            .setPositiveButton(R.string.continuar_eligiendo, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    if(!ConfiguracionesApp.getEntradaComoInvitado(activity)){
                        //si no se entro como invitado mostramos la opcion de ver carrito
                        builder.setNegativeButton(R.string.ver_carrito, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(activity, CarritoActivity.class));
                            }
                        });

                    }

                    builder.create();
                    builder.show();



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
                utilidadesApp.dialogoResultadoConexion(activity,"Error al conectarce al servidor", responseString + "\nStatus Code: " + String.valueOf(statusCode));


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
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Error al conectarce al servidor", info);

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
}
