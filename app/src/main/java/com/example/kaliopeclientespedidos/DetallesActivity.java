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

    JSONObject informacionProductoInicial;



    JSONArray coloresTotales;
    JSONArray tallasTotales;



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
            nombrePro = informacionProductoInicial.getString("descripcion");
            precio = informacionProductoInicial.getString("precio_etiqueta");

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


                 /*
                 Si estamos en modo ofline le enviamos como parametro las tallas que
                 existen en el color seleccionado
                 si estamos online consultamos los datos al servidor
                  */
                 if(offline){
                    //le enviamos el array de las tallas del color seleccionado
                     try {
                         JSONArray tallasPorColor = coloresTotales.getJSONObject(position).getJSONArray("tallas");
                         llenarSpinnerTallas(tallasPorColor);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }else{

                     consultarDetallePorColor(id_producto,colorSeleccionado);
                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        List <HashMap<String,String>> listaColores = new ArrayList<>();
        /*
            COLORES TOTALES con informacion de cuantas tallas tiene disponible cada color
             [{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
             ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
            ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
            ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]

            No importa si estamos en modo offline o online se mostraran todos los colores en el
            spinner colores,
        */

        for (int i=0; i<coloresTotales.length(); i++){
            HashMap<String, String> map = new HashMap<>();
            try {
                String colorTemp = coloresTotales.getJSONObject(i).getString("color");
                String noColorTemp = coloresTotales.getJSONObject(i).getString("noColor");
                String urlColorTemp = KaliopeServerClient.BASE_URL + "/" + coloresTotales.getJSONObject(i).getString("imagen1");


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

    private void llenarSpinnerTallas(JSONArray tallasPorColor){

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
        /*
            TALLAS TOTALES sin importar color, con existencias
            [{"talla":"UNT","existencias":55}
             ,{"talla":"G","existencias":50}
             ,{"talla":"M","existencias":15}]


             Llenamos la lista de tallas que se mostrara en el spinner
            sera con las tallas totales existentes de ese modelo


             en modo ofline vamos a llenar el spinner con estas tallas,
             y seran inmutable, es decir una ves cargado no cambiaran
             dependiendo del color que se escoja simplemente se mostraran
             todas las tallas que existen para este producto no importa
             los colores diferentes que existan esto le permitira al usuario
             decirle a su cliente de modo offline que tiene de ese producto
             5 tallas diferentes que ofrecer


             Si esta en modo Online igualmente se mostraran todas las tallas
             que exsiten pero dependiendo del color que se escoja en el spinner
             color entonces se pondran de color gris las tallas que ya no estan
             disponibles en ese color y en color negro las que si estan disponibles
         */
        Log.d("tallasTotales", tallasTotales.toString());                   //[{"talla":"UNT","existencias":55},{"talla":"G","existencias":50},{"talla":"M","existencias":15}]
        Log.d("tallasColorSeleccionado", tallasPorColor.toString());        //[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]

        try {


            for (int i = 0; i < tallasTotales.length(); i++) {
                map = new HashMap<>();
            /*
                necesitamos comparar los 2 arrays para saber que talla existe en el color y ponerle sus existencias,
                y las que no existan colocarlas como no Activas para que aparescan en gris y no se puedan seleccioanr
                Para lograr esto vamos a recorrer el array mas grande lo cual ya se esta haciendo en el bucle exterior y comparando los nombres de las tallas
                del array mas pequeño
                */
                String tallaMayor = tallasTotales.getJSONObject(i).getString("talla");                          //tomamos la talla en curso del array total de tallas sin importar color
                //recorremos el array pequeño de tallas
                for (int z = 0; z < tallasPorColor.length(); z++) {
                    String tallaMenor = tallasPorColor.getJSONObject(z).getString("talla");                     //tomamos la talla en curso del array de tallas filtrado por color

                    Log.d("comparamosMayorMenor","talla mayor en curso: " + tallaMayor + " talla menor en curso: " + tallaMenor);
                    if (tallaMayor.equals(tallaMenor)) {
                        /*
                        si la talla menor es igual a la talla mayor significa que la talla menor si
                        existe en el array todas las tallas
                        entonces tomamos esa talla la ponemos como activa
                         */
                        String existencia = tallasPorColor.getJSONObject(z).getString("existencias");           //sacamos las existencias pero solo de esa talla en ese color
                        map.put(SpinnerTallasAdapter.COLUMNA_TALLA, tallaMenor);
                        map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS, existencia);
                        map.put(SpinnerTallasAdapter.COLUMNA_ACTIVO, "true");
                        listaTallas.add(map);
                        Log.d("comparamosMayorMenor","La talla menor concuerda con una talla en todas las tallas, marcando como activo");
                    }else{
                        /*
                        Si la talla menor no es igual a la talla mayor en curso entonces
                        el for salta a la siguiente talla menor para comprararla porque podrian venir desacomodadas
                         */
                        Log.d("comparamosMayorMenor","La talla menor no concuerda con la talla en todas las talals analizada, saltando a la siguiente talla menor");
                    }

                }
                /*
                  Si ya se compararon todas las tallas menores y no se encontro fueran iguales con la talla mayor
                  entonces la talla mayor se pone como inactiva y se muestra en el spinner
                  el bucle for cambia a la siguiente talla mayor y vuelve a compararla con todas las tallas menores
                         */
                map.put(SpinnerTallasAdapter.COLUMNA_TALLA, tallaMayor);
                map.put(SpinnerTallasAdapter.COLUMNA_EXISTENCIAS, "0");
                map.put(SpinnerTallasAdapter.COLUMNA_ACTIVO, "false");
                listaTallas.add(map);
            }
            /*
            Ya que se termino de recorrer todas las tallas mayores se finaliza el bucle for y asignamos
            la lista al spiner aqui ya vendran las tallas inactivas o activas
             */
            SpinnerTallasAdapter spinnerTallasAdapter = new SpinnerTallasAdapter(listaTallas);
            spinnerTallas.setAdapter(spinnerTallasAdapter);
        }catch (JSONException e){
            e.printStackTrace();
        }











    }




    private void consultaInicial(){

        RequestParams params = new RequestParams();
        params.put("ID_PRODUCTO",id_producto);

        KaliopeServerClient.post(URL_DETALLES_PRODUCTO, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("detalles1",String.valueOf(response));
                //{"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","precio_vendedora":"298","precio_empresaria":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","existencias":120,
                // "tallas":[{"talla":"UNT","existencias":55},{"talla":"G","existencias":50},{"talla":"M","existencias":15}],
                // "colores":[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
                // ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
                // ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
                // ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]}

                try {

                    coloresTotales = response.getJSONArray("colores");
                    tallasTotales = response.getJSONArray("tallas");
                    Log.d("coloresTotales",String.valueOf(coloresTotales.toString()));
                    //[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]}
                    // ,{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]}
                    // ,{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}
                    // ,{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"G","existencias":10}]}]
                    Log.d("tallasTotales",String.valueOf(tallasTotales.toString()));
                    //[{"talla":"UNT","existencias":55}
                    // ,{"talla":"G","existencias":50}
                    // ,{"talla":"M","existencias":15}]

                    informacionProductoInicial = response;



                    //=======LLENAMOS LAS 3 IMAGENES QUE SE MOSTRARAN EN EL RECYCLER VIEW========
                    //debido a que las 3 imagenes del producto vienen en el solo objeto json entonces vamos a llenar la lista haciendo 3 veces lo mismo xD
                    //con la imagen 1 la 2 y la 3 para que se manden al recycler view y ademas enviamos el nombre de la imagen por si se tiene que hacer algo con ella cuando se haga clic.

                    listaImagenesPrincipal = new ArrayList<HashMap>();

                    HashMap<String, String> map = new HashMap<>();
                    String nombre = informacionProductoInicial.get("imagen1").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = informacionProductoInicial.get("imagen2").toString();
                    map.put(DetallesImagenAdapter.IMAGEN_NOMBRE,nombre);
                    map.put(DetallesImagenAdapter.IMAGEN_URL,KaliopeServerClient.BASE_URL + nombre);
                    listaImagenesPrincipal.add(map);

                    map = new HashMap<>();
                    nombre = informacionProductoInicial.get("imagen3").toString();
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
                    JSONArray tallasPorColor = response.getJSONArray("tallas");
                    Log.d("detalles2.1",String.valueOf(productoPorColor.toString()));
                    //{"imagen1":"fotos\/SM5898-ROSA-1.jpg","0":"fotos\/SM5898-ROSA-1.jpg","imagen2":"fotos\/SM5898-ROSA-2.jpg","1":"fotos\/SM5898-ROSA-2.jpg","imagen3":"fotos\/SM5898-ROSA-3.jpg","2":"fotos\/SM5898-ROSA-3.jpg"}
                    Log.d("detalles3.1",String.valueOf(tallasPorColor.toString()));
                    //[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]



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




}
