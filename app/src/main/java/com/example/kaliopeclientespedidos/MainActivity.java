package com.example.kaliopeclientespedidos;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Activity activity = this;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    TextView textViewToolModoFuncionamiento;
    TextView textViewToolTitulo;
    TextView textViewToolSubtitulo;


    ProgressDialog progressDialog;



    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarLuisda);
        textViewToolModoFuncionamiento = findViewById(R.id.toolBarLuisdaModoFuncionamiento);
        textViewToolTitulo = findViewById(R.id.toolBarLuisdaTitulo);
        textViewToolSubtitulo = findViewById(R.id.toolBarLuisdaSubtitulo);
        //primero se tiene que remover el titulo que venga pro defecto en la barra de tareas esto porque puse textView personalizados en la toolbar se que se puede poner titulo y subtitulo sin necesidad de definirlo en el Layout de  la toolbar pero quice probar asi para ver que otros items poner
        textViewToolTitulo.setText("Kaliope Pedidos");

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayoutMainActivity);
        navigationView = findViewById(R.id.navigationViewLuisda);
        navigationView.bringToFront();      //esto resuelve el problema del on clic
        navigationView.setNavigationItemSelectedListener(this);

        //Le especificamos al toolbar el iconito de hambugesa para abrir el el drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);                                                          //le añadimos el escuchador al drawer cuando se hagla clic en el toolbar
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();                                                                              //esto lo recomienda android studio colocarlo en el onPostCreate, pero lo dejo aqui

        navController = Navigation.findNavController(this,R.id.fragment2);

        navigationView.setCheckedItem(R.id.home);                                                                       //por default se muestra el fragment donde aparecen todos los productos entonces marcamos como seleccionado ese iten en el drawer


        mostrarDatosCliente(activity);

        checarModoOffline(textViewToolModoFuncionamiento);          //se encarga de actualizar el texto que indica si esta en modo offline la app o online


    }


    private void mostrarDatosCliente(Activity activity){


        String nombreCompleto = ConfiguracionesApp.getNombreCliente(activity);
        String usuario = ConfiguracionesApp.getUsuarioIniciado(activity);
        String cuentaCliente = ConfiguracionesApp.getCuentaCliente(activity);

        //para manipular las vistas del header del drawer
        View view = navigationView.getHeaderView(0);
        TextView nombreDrawerTV = (TextView) view.findViewById(R.id.drawer_header_nombreCliente);
        TextView cuentaDrawerTV = (TextView) view.findViewById(R.id.drawer_header_cuentaCliente);
        TextView usuarioDrawerTV = (TextView) view.findViewById(R.id.drawer_header_usuarioCliente);


            nombreDrawerTV.setText(nombreCompleto);
            cuentaDrawerTV.setText(cuentaCliente);
            usuarioDrawerTV.setText(usuario);
            textViewToolSubtitulo.setText(nombreCompleto);










    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        //Toast.makeText(getApplicationContext(),String.valueOf(item.getItemId()),Toast.LENGTH_SHORT).show();

        if(item.getItemId() == R.id.home){
            navController.navigate(R.id.blankFragment);
            //Toast.makeText(getApplicationContext(),"personas",Toast.LENGTH_SHORT).show();

        }

        /*
        if(item.getItemId() == R.id.personas){
            navController.navigate(R.id.informacionCuentaFragment);
            //Toast.makeText(getApplicationContext(),"personas",Toast.LENGTH_SHORT).show();
        }*/


        if(item.getItemId() == R.id.carrito){
            //navController.navigate(R.id.informacionCuentaFragment);
            //Toast.makeText(getApplicationContext(),"personas",Toast.LENGTH_SHORT).show();

            if(!ConfiguracionesApp.getEntradaComoInvitado(activity)){
                startActivity(new Intent(this, CarritoActivity.class));
            }else{
                Snackbar.make(drawerLayout.getRootView(), "No puedes acceder al carrito como INVITADO", Snackbar.LENGTH_SHORT).setAction("accion",null).show();

            }


        }

        if(item.getItemId() == R.id.cerrarSesion){


            new AlertDialog.Builder(this)
                    .setTitle("Cerrar Sesion")
                    .setMessage("Deseas cerrar sesion en este dispositivo")
                    .setPositiveButton("Si, cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cerrarSesion();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();

        }

        //cerramos el navigation cuando haya un clicl
        //drawerLayout.closeDrawer(GravityCompat.START);
        drawerLayout.closeDrawers();


        return true;                //retornando true los items ya se sombrean y se quedan seleccionados en false todo funciona pero los item que escojiste no se sombrean
    }



    private void cerrarSesion(){
        RequestParams params = new RequestParams();
        params.put("usuario", ConfiguracionesApp.getUsuarioIniciado(activity));
        params.put("UUID", ConfiguracionesApp.getCodigoUnicoDispositivo(activity));
        params.put("modeloDispositivo", Build.MODEL);
        params.put("identificador",3);
        showProgresDialog();

        KaliopeServerClient.post(Ingreso.URL_INICIO_SESION, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));

                //{"informacion":{"id":"inicio de sesion exitoso 15670ac8-824c-4eab-ab6e-b97b338a69fa ","usuario":"PAQUITA"},
                // "datosPersonales":{"no_cuenta":"1049","0":"1049","nombre":"SUSANA BENTARCOURT PIñA","1":"SUSANA BENTARCOURT PIñA"}}


                try {
                    JSONObject informacion = response.getJSONObject("informacion");

                    Log.d("Informacion",informacion.toString());
                    String estatus = informacion.getString("estatus");
                    String mensaje = informacion.getString("info");

                    if(estatus.equalsIgnoreCase("EXITO")){
                        ConfiguracionesApp.cerrarSesion(activity);
                        startActivity(new Intent(MainActivity.this, Ingreso.class));
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


                String info = "Status Code: " + String.valueOf(statusCode) +"  responseString: " + responseString;
                Log.d("onFauile 1" , info);
                //Toast.makeText(MainActivity.this,responseString + "  Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", "Hubo un errror al conectar con el servidor \n" + responseString + "\nStatus Code: " + String.valueOf(statusCode));


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
                utilidadesApp.dialogoResultadoConexion(activity,"Fallo de conexion", "Hubo un errror al conectar con el servidor \n" + "\nStatus Code: " + String.valueOf(statusCode));

            }
        });

    }

    private void showProgresDialog(){

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Conectando al Servidor");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    //Para el menu personalisado del toolbar la lupita y otras opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu,menu);
        return true;
    }

    //lo mismo para el otro menu de la barra de tareas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            if(!ConfiguracionesApp.getEntradaComoInvitado(activity)){
                startActivity(new Intent(this, CarritoActivity.class));
            }else{
                Snackbar.make(drawerLayout.getRootView(), "No puedes acceder al carrito como INVITADO", Snackbar.LENGTH_SHORT).setAction("accion",null).show();

            }

        }
        return super.onOptionsItemSelected(item);
    }






    private void checarModoOffline(final TextView modo){
        //creamos un hilo que estara actualizando constantemente el estado del textView modoOffline dependiendo de la variable en la constante
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Constantes.offline){
                                modo.setText("App en modo sin Internet");
                                modo.setBackgroundColor(Color.YELLOW);
                                modo.setTextColor(Color.BLACK);
                                modo.setTextSize(12);
                            }else{
                                modo.setText("App con conexion a internet");
                                modo.setBackgroundColor(Color.GREEN);
                                modo.setTextColor(Color.BLACK);
                                modo.setTextSize(12);
                            }

                        }
                    });





                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}