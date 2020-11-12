package com.example.kaliopeclientespedidos.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaliopeclientespedidos.AdapterPublicidad;
import com.example.kaliopeclientespedidos.DetallesActivity;
import com.example.kaliopeclientespedidos.KaliopeServerClient;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.adapter.ProductoAdapter;
import com.example.kaliopeclientespedidos.models.Producto;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements ProductoAdapter.OnProductListener{


    RecyclerView recyclerView;

    JSONArray categorias;
    JSONArray imagenDeInicio;

    Producto producto;
    List<Producto> listProducto;



    private AdapterViewFlipper adapterViewFlipperPublicidad;
    private AdapterPublicidad adapterPublicidad;

    private final String URL_CATEGORIAS ="app_movil/consultar_categorias.php";
    private final String URL_IMAGEN_PRINCIPAL = "app_movil/consultar_principal.php";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d("fragment","OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("fragment","OnCreateView");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);

    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //cuando ya tenemos la vista inflada este se llama inmediatamente, es mejor hacerlo aqui porque depronto
        //El onCreateView falla, en este casos abemos que ya se creo completamente la interfas

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerId);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        Log.d("fragment","OnViewCreated");









        consultarImagenPrincipal();








        adapterViewFlipperPublicidad = (AdapterViewFlipper) view.findViewById(R.id.main_AVF_publicidad);
        int[]imagenes = {R.drawable.cortador1, R.drawable.cortador2};

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterViewFlipperPublicidad.showNext();

            }
        };

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    adapterViewFlipperPublicidad.stopFlipping();    //si se toca la imagen dejamos de mover la imagen para que la puedan leer
                    return true;
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    adapterViewFlipperPublicidad.startFlipping();
                    adapterViewFlipperPublicidad.showNext();            //al implementar esto el onClick normal dejo de funcionar entonces cuando se levante el dedo de la imagen cambia a la siguiete

                    return false;
                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){      //si se mueve el dedo fuera de la imagen volvemos a iniciar el recorrido
                    adapterViewFlipperPublicidad.startFlipping();
                    return false;
                }

                return false;
            }
        };

        adapterPublicidad = new AdapterPublicidad(getActivity(), imagenes, onClickListener, onTouchListener);
        adapterViewFlipperPublicidad.setAdapter(adapterPublicidad);
        adapterViewFlipperPublicidad.setFlipInterval(5000);
        adapterViewFlipperPublicidad.setAutoStart(true);
        adapterViewFlipperPublicidad.startFlipping();


    }






    private void enaviarArecycler(){

        listProducto = new ArrayList<>();

        for (int i=0; i<imagenDeInicio.length() ; i++){


            try {

                String URL_Imagen = KaliopeServerClient.BASE_URL + imagenDeInicio.getJSONObject(i).getString("imagen1");

                producto = new Producto(
                        imagenDeInicio.getJSONObject(i).getString("descripcion"),
                        URL_Imagen,
                        imagenDeInicio.getJSONObject(i).getString("precio_etiqueta"),
                        imagenDeInicio.getJSONObject(i).getString("existencias"),
                        0,
                        imagenDeInicio.getJSONObject(i).getString("id_producto"));

                listProducto.add(producto);

                Log.d("UrlImagenes",String.valueOf(URL_Imagen));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        //configuramos nuestro layoutmanager la manera en que queremos que se vea el recycler view
        final int spans = getResources().getInteger(R.integer.number_of_columns);       //dependiendo de la pantalla del dispositivo mostraremos mas o menos columnas
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),spans,GridLayoutManager.VERTICAL,false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                Producto aux = listProducto.get(position);

                /*
                Aqui
                el primer numero reprecenta cada cuantos items quieres que se el siguiente item ocupe 2 espacios
                es facil si pones siete buscara divisores de 7 y se mostrara cada 7, hasta que Mob sea 0 pondra 2 en los demas
                usara 1

                pero ahora queremos que dependiendo de los multiplos que puede mostrar la pantalla

                por ejemplo cuando peude mostrar 2 columnas esta bien porque un item ocupara las 2 columnas,
                pero cuando pueda mostrar 3 llega un punto

                espera dejare esto pendiente despues le damos solucion

                 */

                if(aux.getType()==0){
                    //Si es 0 es que es un producto
                    return (position % 7 == 0 ? 2 : 1);
                }else{
                    //si es uno es que es un cortador le retornamos el numerod e columnas que puede mostrar un dispositivo
                    //asi si por ejemplo el dispositivo solo peude mostrar 2 columnas el cortador o la imagen usara toda la pantalla o 2 columnas
                    //pero si volteas el dispositivo horizontalmente y ahi puede mostrar 3 columnas entonces queremos que el cortados o publicidad use
                    //3 columnas o toda la pantalla
                    return spans;
                }

            }
        });








        recyclerView.setLayoutManager(gridLayoutManager);
        ProductoAdapter productoAdapter = new ProductoAdapter(listProducto,this);
        recyclerView.setAdapter(productoAdapter);


    }


    private void consultarImagenPrincipal(){


        KaliopeServerClient.get(URL_IMAGEN_PRINCIPAL,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));
                /*

                D/datosRecibidos: {"categorias":[{"categoria":"sudadera"},{"categoria":"ropa interior"},{"categoria":null}],
                "imagenInicio":[{"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","imagen1":"fotos\/SM5898-VERDE-1.jpg"},{"id_producto":"ST5898","descripcion":"Sueter dama","estado":"ACTIVO","precio_etiqueta":"359","imagen1":"fotos\/ST5898-VERDE AGUA-1.jpg"},{"id_producto":"PD5898","descripcion":"Pantalon Dama","estado":"ACTIVO","precio_etiqueta":"429","imagen1":"fotos\/PD5898-AZUL-1.jpg"},{"id_producto":"PD5899","descripcion":"Pantalon Dama","estado":"ACTIVO","precio_etiqueta":"429","imagen1":"fotos\/PD5899-AZUL-1.jpg"},{"id_producto":"PH5899","descripcion":"Pantalon Caballero","estado":"ACTIVO","precio_etiqueta":"499","imagen1":"fotos\/PH5899-AZUL-1.jpg"},{"id_producto":"PH56000","descripcion":"Pantalon Caballero","estado":"ACTIVO","precio_etiqueta":"499","imagen1":"fotos\/PH6000-AZUL-1.jpg"},{"id_producto":"PH56001","descripcion":"Pantalon Caballero","estado":"ACTIVO","precio_etiqueta":"499","imagen1":"fotos\/PH6001-AZUL-1.jpg"},{"id_producto":"SH1589","descripcion":"Sudadera Caballero","estado":"ACTIVO","precio_etiqueta":"399","imagen1":"fotos\/SH1589-ROJA-1.jpg"},{"id_producto":"SH1590","descripcion":"Sudadera Caballero","estado":"ACTIVO","precio_etiqueta":"399","imagen1":"fotos\/SH1590-ROJA-1.jpg"},{"id_producto":"SH1591","descripcion":"Sudadera Caballero","estado":"ACTIVO","precio_etiqueta":"399","imagen1":"fotos\/SH1591-GRIS-1.jpg"},{"id_producto":"SH1592","descripcion":"Sudadera Caballero","estado":"ACTIVO","precio_etiqueta":"399","imagen1":"fotos\/SH1592-BLANCO-1.jpg"},{"id_producto":"PD8598","descripcion":"Pants Dama","estado":"ACTIVO","precio_etiqueta":"299","imagen1":"fotos\/PD8598-ROSA-1.jpg"},{"id_producto":"PD8500","descripcion":"Pants Completo Dama","estado":"ACTIVO","precio_etiqueta":"539","imagen1":"fotos\/PD8500-NEGRO-1.jpg"},{"id_producto":"M7891","descripcion":"Malla Dama","estado":"ACTIVO","precio_etiqueta":"279","imagen1":"fotos\/M7891-AZUL-1.jpg"},{"id_producto":"M7892","descripcion":"Malla Dama","estado":"ACTIVO","precio_etiqueta":"279","imagen1":"fotos\/M7892-VERDE-1.jpg"},{"id_producto":"B4598","descripcion":"Brassier Encaje","estado":"ACTIVO","precio_etiqueta":"149","imagen1":"fotos\/B4598-BEIGE-1.jpg"},{"id_producto":"B4599","descripcion":"Brassier Encaje","estado":"ACTIVO","precio_etiqueta":"149","imagen1":"fotos\/B4599-NEGRO-1.jpg"},{"id_producto":"B4600","descripcion":"Brassier Encaje","estado":"ACTIVO","precio_etiqueta":"149","imagen1":"fotos\/B4600-CAFE-1.jpg"},{"id_producto":"C4600","descripcion":"Coordinado Encaje","estado":"ACTIVO","precio_etiqueta":"199","imagen1":"fotos\/C4600-VERDE-1.jpg"},{"id_producto":"P1451","descripcion":"Pantaleta","estado":"ACTIVO","precio_etiqueta":"99","imagen1":"fotos\/P1451-NEGRO-1.jpg"},{"id_producto":"P1452","descripcion":"Pantaleta","estado":"ACTIVO","precio_etiqueta":"79","imagen1":"fotos\/P1452-ROJA-1.jpg"},{"id_producto":"P1453","descripcion":"Pantaleta","estado":"ACTIVO","precio_etiqueta":"89","imagen1":"fotos\/P1453-MORADA-1.jpg"},{"id_producto":"CM899","descripcion":"Chamarra","estado":"ACTIVO","precio_etiqueta":"799","imagen1":"fotos\/CM899-ROJA-1.jpg"},{"id_producto":"CM900","descripcion":"Chamarra","estado":"ACTIVO","precio_etiqueta":"799","imagen1":"fotos\/CM900-ROSA-1.jpg"},{"id_producto":"CM901","descripcion":"Chamarra","estado":"ACTIVO","precio_etiqueta":"799","imagen1":"fotos\/CM901-CAFE-1.jpg"},{"id_producto":"CH9000","descripcion":"Chamarra caballero","estado":"ACTIVO","precio_etiqueta":"999","imagen1":"fotos\/CH9000-NEGRO-1.jpg"},{"id_producto":"CH9001","descripcion":"Chamarra caballero","estado":"ACTIVO","precio_etiqueta":"1199","imagen1":"fotos\/CH9001-AZUL-1.jpg"},{"id_producto":"CH9002","descripcion":"Chamarra caballero","estado":"ACTIVO","precio_etiqueta":"1199","imagen1":"fotos\/CH9002-AZUL-1.jpg"},{"id_producto":"CH9003","descripcion":"Chamarra caballero","estado":"ACTIVO","precio_etiqueta":"699","imagen1":"fotos\/CH9003-GRIS-1.jpg"},{"id_producto":"BD1545","descri
                 */
                try {
                    categorias = response.getJSONArray("categorias");
                    imagenDeInicio = response.getJSONArray("datosOffline");

                    Log.d("datosProces1",String.valueOf(categorias.toString()));
                    //[{"categoria":"sudadera"},{"categoria":"ropa interior"},{"categoria":null}]
                    Log.d("datosProces2",String.valueOf(imagenDeInicio.toString()));
                    /*

                    [{"id_producto":"SM5898","descripcion":"Sudadera dama","estado":"ACTIVO","precio_etiqueta":"339","precio_vendedora":"298","precio_empresaria":"291","imagen1":"fotos\/SM5898-VERDE-1.jpg","imagen2":"fotos\/SM5898-VERDE-2.jpg","imagen3":"fotos\/SM5898-VERDE-3.jpg","categoria":"sudadera","existencias":110,"tallas":[{"talla":"UNT","existencias":55},{"talla":"G","existencias":40},{"talla":"M","existencias":15}],"colores":[{"color":"Gris","noColor":"rgb(142, 142, 142)","imagen1":"fotos\/SM5898-VERDE-1.jpg","existencias":45,"tallas":[{"talla":"UNT","existencias":5},{"talla":"G","existencias":40}]},{"color":"Rosa","noColor":"rgb(240, 74, 141)","imagen1":"fotos\/SM5898-ROSA-1.jpg","existencias":55,"tallas":[{"talla":"UNT","existencias":40},{"talla":"M","existencias":15}]},{"color":"Negro","noColor":"rgb(13, 13, 13)","imagen1":"fotos\/SM5898-NEGRO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]},{"color":"Azul","noColor":"rgb(135, 182, 205)","imagen1":"fotos\/SM5898-AZUL-1.jpg","existencias":0,"tallas":[{"talla":"G","existencias":0}]}]},{"id_producto":"ST5898","descripcion":"Sueter dama","estado":"ACTIVO","precio_etiqueta":"359","precio_vendedora":"310","precio_empresaria":"301","imagen1":"fotos\/ST5898-VERDE AGUA-1.jpg","imagen2":"fotos\/ST5898-VERDE AGUA-2.jpg","imagen3":"fotos\/ST5898-VERDE AGUA-3.jpg","categoria":null,"existencias":40,"tallas":[{"talla":"UNT","existencias":40}],"colores":[{"color":"VERDE AGUA","noColor":"rgb(200, 235, 231)","imagen1":"fotos\/ST5898-VERDE AGUA-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]},{"color":"BEIGE","noColor":"rgb(220, 202, 188)","imagen1":"fotos\/ST5898-BEIGE-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]},{"color":"GRIS","noColor":"rgb(122, 125, 130)","imagen1":"fotos\/ST5898-GRIS-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]},{"color":"BLANCO","noColor":"rgb(248, 248, 248)","imagen1":"fotos\/ST5898-BLANCO-1.jpg","existencias":10,"tallas":[{"talla":"UNT","existencias":10}]}]},{"id_producto":"PD5898","descripcion":"Pantalon Dama","estado":"ACTIVO","precio_etiqueta":"429","precio_vendedora":"394","precio_empresaria":"381","imagen1":"fotos\/PD5898-AZUL-1.jpg","imagen2":"SM5898-ROSA-2","imagen3":"SM5898-ROSA-3","categoria":null,"existencias":30,"tallas":[{"talla":"28","existencias":10},{"talla":"32","existencias":10},{"talla":"36","existencias":10}],"colores":[{"color":"AZUL","noColor":"rgb(48, 68, 104)","imagen1":"fotos\/PD5898-AZUL-1.jpg","existencias":30,"tallas":[{"talla":"28","existencias":10},{"talla":"32","existencias":10},{"talla":"36","existencias":10}]}]},{"id_producto":"PD5899","descripcion":"Pantalon Dama","estado":"ACTIVO","precio_etiqueta":"429","precio_vendedora":"394","precio_empresaria":"381","imagen1":"fotos\/PD5899-AZUL-1.jpg","imagen2":"SM5898-ROSA-2","imagen3":"SM5898-ROSA-3","categoria":null,"existencias":30,"tallas":[{"talla":"28","existencias":20},{"talla":"38","existencias":10}],"colores":[{"color":"AZUL","noColor":"rgb(69, 143, 192)","imagen1":"fotos\/PD5899-AZUL-1.jpg","existencias":20,"tallas":[{"talla":"28","existencias":10},{"talla":"38","existencias":10}]},{"color":"GRIS","noColor":"rgb(108, 108, 116)","imagen1":"fotos\/PD5898-GRIS-1.jpg","existencias":10,"tallas":[{"talla":"28","existencias":10}]}]},{"id_producto":"PH5899","descripcion":"Pantalon Caballero","estado":"ACTIVO","precio_etiqueta":"499","precio_vendedora":"394","precio_empresaria":"381","imagen1":"fotos\/PH5899-AZUL-1.jpg","imagen2":"SM5898-ROSA-2","imagen3":"SM5898-ROSA-3","categoria":null,"existencias":10,"tallas":[{"talla":"28","existencias":10}],"colores":[{"color":"AZUL","noColor":"rgb(109, 149, 210)","imagen1":"fotos\/PH5899-AZUL-1.jpg","existencias":10,"tallas":[{"talla":"28","existencias":10}]}]},{"id_producto":"PH56000","descripcion":"Pantalon Caballero","estado":"ACTIVO","precio_etiqueta":"499","precio_vendedora":"394","precio_empresaria":"381","imagen1":"fotos\/PH6000-AZUL-1.jpg","imagen2":"SM5898-ROSA-2","imagen3":"SM5898-ROSA-3","categoria":null,"existencias"
                     */


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
    public void onProductClick(int position) {
        Producto producto = listProducto.get(position);

        //vamos a animar el item cuando se haga clic en el
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.item_container_producto_LinearLayout);
        linearLayout.animate().scaleY(1.2f).scaleX(1.2f).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
        linearLayout.animate().scaleY(1).scaleX(1).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();


        Intent intent = new Intent(getActivity(), DetallesActivity.class);
        intent.putExtra("ID_PRODUCTO", producto.getId());
        startActivity(intent);
    }

}