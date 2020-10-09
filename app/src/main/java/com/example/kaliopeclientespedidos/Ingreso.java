package com.example.kaliopeclientespedidos;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class Ingreso extends AppCompatActivity {

    public static final long TRANSITION_DURATION = 3000;

    MyBroadcastReceive myBroadcastReceive;

    private Transition transition;
    private Activity activity = this;

    Button botonInvitado,
            botonIngresar;

    TextView tvEstadoConexion;

    EditText etUsuario,
            etPassword;

    CheckBox checkBoxMantenerSesionIniciada;

    ProgressDialog progressDialog;


    boolean continuarHiloHacerPing = true;

    //public static final String URL_PING = "PhpProject_clientes_pedidos/ping_servidor.php";
    public static final String URL_PING = "app_movil/ping_servidor.php";
    public static final String URL_INICIO_SESION = "app_movil/iniciar_sesion.php";
    //public static final String URL_INICIO_SESION = "PhpProject_clientes_pedidos/iniciar_sesion.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
        getSupportActionBar().hide();

        Slide slide = new Slide(Gravity.RIGHT);
        slide.setDuration(1000);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);

        tvEstadoConexion = (TextView) findViewById(R.id.ingreso_TextViewEstadoConexion);
        etUsuario = (EditText) findViewById(R.id.ingreso_editText_usuario);
        etPassword= (EditText) findViewById(R.id.ingreso_editText_password);
        botonInvitado =  (Button) findViewById(R.id.ingreso_boton_invitado);
        botonIngresar =  (Button) findViewById(R.id.ingreso_boton_ingreso);
        checkBoxMantenerSesionIniciada = (CheckBox) findViewById(R.id.ingreso_checkBox_mantenerSesion);

        Intent intent = new Intent(this, ConexionService.class);
        startService(intent);

        botonInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfiguracionesApp.getMostrarAvisoComoInvitado(activity)){
                    dialogoAvisoInvitado();
                }else{
                    Log.i("Ingreso.Dialogo","Entramos sin mostrar dialogo de informacion invitado");
                    ConfiguracionesApp.setEntradaComoInvitado(activity,true);
                    nextActivity();
                }
            }
        });


        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(continuarHiloHacerPing){
                    //si continuarHiloHacerPing no a cambiado a falso significa que no se han apagado o desactivado los datos o wifi
                    if(etUsuario.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "No has ingresado un usuario", Toast.LENGTH_SHORT).show();
                    }else if (etPassword.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "No has ingresado un Password", Toast.LENGTH_SHORT).show();
                    }else {

                        iniciarSesion();

                    }
                }else{
                    dialogoDeConexion("Sin Conexion", "El WI-FI o los Datos moviles estan desactivados, por favor activelos para establecer la conexion a internet");
                }

            }
        });


    }




@SuppressWarnings("uncheked")
    private void nextActivity(){
        transition = new Slide(Gravity.LEFT);

        transition.setDuration(1000);
        transition.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(transition);
        getWindow().setReenterTransition(transition);

        Intent intent = new Intent(this, MainActivityRecycler.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onResume (){
        super.onResume();
        //Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show();

        registerReceiver(networkStateReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        tvEstadoConexion.setBackgroundColor(Color.RED);
        tvEstadoConexion.setText("Sin Internet");
        if (ConfiguracionesApp.getEstadDeSesion(activity)){
            //ingresar();
        }



        //registramos nuestr broadcast que recibira los datos que este trabajando el servicio que coteja el ping
        myBroadcastReceive = new MyBroadcastReceive();
        IntentFilter intentFilter = new IntentFilter("pingServer");
        registerReceiver(myBroadcastReceive,intentFilter);

    }



    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
        continuarHiloHacerPing = false;
        unregisterReceiver(myBroadcastReceive);
    }

    private void dialogoAvisoInvitado(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        //creamos nuestro objeto vista que se referira al layout personalizado creado
        View view = layoutInflater.inflate(R.layout.dialogo_aviso_ingreso_invitado,null);



        final CheckBox checkBoxNoMostrarOtraVez = (CheckBox) view.findViewById(R.id.dialogoAvisoIngresoInvitado_CheckBoxNoMostrarOtraVez);
        final Button buttonContinuar = (Button) view.findViewById(R.id.dialogoAvisoIngresoInvitado_ButtonContinuar);
        final Button buttonVolver = (Button) view.findViewById(R.id.dialogoAvisoIngresoInvitado_ButtonVolver);

        final AlertDialog alertDialog;
        alertDialog = builder.create();

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfiguracionesApp.setEntradaComoInvitado(activity,true);

                if(checkBoxNoMostrarOtraVez.isChecked()){
                    //si el usuario marco el mensaje como no volver a mostrar
                    ConfiguracionesApp.setMostrarAvisoComoInvitado(activity,false);
                    Log.i("Ingreso.CheckNoMostrar","Marcado");
                    alertDialog.cancel();
                    nextActivity();
                }else{
                    ConfiguracionesApp.setMostrarAvisoComoInvitado(activity,true);
                    Log.i("Ingreso.CheckNoMostrar","No Marcado");
                    alertDialog.cancel();
                    nextActivity();
                }

            }
        });

        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alertDialog.cancel();
            }
        });





        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.create();
        alertDialog.show();




    }


    private void showProgresDialog(){

        progressDialog = new ProgressDialog(Ingreso.this);
        progressDialog.setMessage("Conectando al Servidor");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    void dialogoDeConexion (String title,String mensaje){
        new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }












    //CONEXION AL SERVIDOR
    //coneccion al servidor
    public void iniciarSesion (){
        String uniqueUUID;
        if (ConfiguracionesApp.getCodigoUnicoDispositivo(activity).equals("SinValor")){
            //si aun no se guarda en las preferencias el UUID lo creamos y lo guardamos.
            uniqueUUID  = UUID.randomUUID().toString();
            Log.d("ID","UUID ID: " + uniqueUUID);
            ConfiguracionesApp.setCodigoDispositivoUnico(activity,uniqueUUID);
        }else{
            //corroboramos que el UUID se haya guardado en las preferencias
            uniqueUUID = ConfiguracionesApp.getCodigoUnicoDispositivo(activity);
            Log.d("ID","UUID ID preferences: " + uniqueUUID);
        }


        showProgresDialog(); //mostramos el progreso  indeterminado


        RequestParams parametros = new RequestParams();
        parametros.put("usuario" , etUsuario.getText().toString());
        parametros.put("password" , etPassword.getText().toString());
        parametros.put("modeloDispositivo" , Build.MODEL);
        parametros.put("UUID" , uniqueUUID);
        parametros.put("mantenerSesionIniciada",checkBoxMantenerSesionIniciada.isChecked());



        //KaliopeServerClient.get("PhpProject_clientes_pedidos//iniciar_sesion.php",parametros,new JsonHttpResponseHandler(){
        KaliopeServerClient.get(URL_INICIO_SESION,parametros,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));

                //{"informacion":{"id":"inicio de sesion exitoso 3e49b86d-c447-4c35-a895-73306ef7a2e0 "},"datosPersonales":{"no_cuenta":"1049","0":"1049","nombre":"SUSANA BENTARCOURT PIñA","1":"SUSANA BENTARCOURT PIñA"}}



                try {
                    JSONObject informacion = response.getJSONObject("informacion");

                    Log.d("Informacion",String.valueOf(informacion.getString("id")));




                    //llenamos los datos de uso de sesion
                    //ConfiguracionesApp.setDatosInicioSesion(activity,infoUsuario, clientes);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                nextActivity();

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
                dialogoDeConexion("Fallo de inicio de sesion", responseString + "\nStatus Code: " + String.valueOf(statusCode));


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
                dialogoDeConexion("Fallo de conexion", info);
            }


            @Override
            public void onRetry(int retryNo) {
                progressDialog.setMessage("Reintentando conexion No: " + String.valueOf(retryNo));
            }
        });
    }









    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //https://medium.com/alvareztech/verificar-estado-de-conexi%C3%B3n-a-internet-en-tu-aplicaci%C3%B3n-android-d55e2b501302

            if(networkInfo != null && networkInfo.isConnected()){
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    Log.d("Main","conectado");

                    hacerPingAlServidor(tvEstadoConexion);
                    tvEstadoConexion.setText("Intentando Conexion");
                    tvEstadoConexion.setBackgroundColor(Color.CYAN);


                }
                Log.d("Main",String.valueOf(networkInfo.getState()));
            }else{
                Log.d("Main","desconectado");
                continuarHiloHacerPing = false;
                tvEstadoConexion.setText("WiFi o datos apagados\nActivalos");
                tvEstadoConexion.setBackgroundColor(Color.RED);
            }

        }
    };


    /**
     * Este metodo se conectara al servidor cada cierto tiempo para ayudar a identificar
     * si el acceso a la red ha sido completado, ademas mostrara en un textView el resultado
     * del PING
     * @param textView Un textView donde se mostrara el resultado y cambiara de color
     */
    public void hacerPingAlServidor(final TextView textView){
        continuarHiloHacerPing = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(continuarHiloHacerPing){


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            KaliopeServerClient.postNumeroIntentosTimeOut(URL_PING, null, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    try {
                                        String estado = response.getString("estado");

                                        //Toast.makeText(MenuPrincipalActivity.this, estado, Toast.LENGTH_SHORT).show();
                                        textView.setText("Conectado");
                                        textView.setEnabled(true);
                                        textView.setBackgroundColor(Color.GREEN);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                                    if (textView.getText().toString().equals("Sin Conexion al servidor\nRevisa WiFi")){
                                        textView.setText("Sin Conexion al servidor\nRevisa WiFi.");
                                        textView.setBackgroundColor(Color.CYAN);
                                    }else{
                                        textView.setText("Sin Conexion al servidor\nRevisa WiFi");
                                        textView.setBackgroundColor(Color.YELLOW);
                                    }
                                    String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.toString();
                                    Log.d("onFauile 2", info);
                                    //Toast.makeText(getApplicationContext(), "Falla en Ping Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();


                                }

                            });



                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();

    }




    private class MyBroadcastReceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean valida = intent.getExtras().getBoolean("valida");
            Toast.makeText(getApplicationContext(),String.valueOf(valida),Toast.LENGTH_SHORT).show();
        }
    }

}
