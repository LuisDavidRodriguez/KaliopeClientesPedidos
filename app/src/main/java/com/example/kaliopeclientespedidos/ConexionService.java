package com.example.kaliopeclientespedidos;


import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ConexionService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private Handler esteServicio;

    public static final String URL_PING = "app_movil/ping_servidor.php";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //HANDLER que recive mensajes del Hilo
    private final class ServiceHandler extends Handler{




        public ServiceHandler(Looper looper){
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            //Normalmente hariamos algo de trabajo aqui, como descargar un archivo
            //para nuestro ejemplo esperaremos por 5 segundos









            for (int i =0; i <=10;i++){


                //creamos solo este handler para que corra en el hilo principal
                //y muestre el toast
                Handler handler = new Handler(Looper.getMainLooper());

                Message message = esteServicio.obtainMessage();
                message.arg1=10;
                esteServicio.sendMessage(message);

                /*
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"conteo",Toast.LENGTH_SHORT).show();
                        KaliopeServerClient.postNumeroIntentosTimeOut(URL_PING, null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {
                                    String estado = response.getString("estado");
                                    //Toast.makeText(MenuPrincipalActivity.this, estado, Toast.LENGTH_SHORT).show();
                                    //tvEstadoConexion.setText("Conectado");
                                    //tvEstadoConexion.setEnabled(true);
                                    //tvEstadoConexion.setBackgroundColor(Color.GREEN);
                                    Toast.makeText(getApplicationContext(),"ping ok desde servicio", Toast.LENGTH_SHORT).show();
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
                                //if (tvEstadoConexion.getText().toString().equals("Sin Conexion al servidor\nRevisa WiFi")){
                                //    tvEstadoConexion.setText("Sin Conexion al servidor\nRevisa WiFi.");
                                //    tvEstadoConexion.setBackgroundColor(Color.CYAN);
                                //}else{
                                //    tvEstadoConexion.setText("Sin Conexion al servidor\nRevisa WiFi");
                                //    tvEstadoConexion.setBackgroundColor(Color.YELLOW);
                                //}
                                String info = "StatusCode" + String.valueOf(statusCode) + "  Twhowable:   " + throwable.toString();
                                Log.d("onFauile 2", info);
                                //Toast.makeText(getApplicationContext(), "Falla en Ping Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();


                            }

                        });

                    }
                });
                */

                Intent intentBroadcast = new Intent();
                intentBroadcast.setAction("pingServer");
                intentBroadcast.putExtra("valor",true);
                sendBroadcast(intentBroadcast);

                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    //reestableser el estatus de la interrupcion
                    Thread.currentThread().interrupt();
                }
            }


            Message message = obtainMessage();
            message.arg1 = 10;




             //aqui tambien se manejan los mensajes que le enviemos a nuestro holder
            //en este caso estamos enviando el el mensaje el id del hilo para detener
            //solo este hilo cuando terminemos
            //asi no detenemos el servicio en medio de el manejo de otro trabajo
            stopSelf(msg.arg1);



        }



    }





    @Override
    public void onCreate() {
        //iniciamos el hilo que ejecuta el servicio, Nota: estamos creando
        //un hilo separado porque el servicio normalmente corre en donde
        //esta corriendo el proceso principal, el cual no queremos bloquear
        //tambien lo hacemos como background priority para que el trabajo intencibo
        //del cpu no afecte nuesto UI

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        //obten el looper del hilo y usalo para nuestro Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);


        esteServicio = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(),"hilo Service", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service Starting", Toast.LENGTH_SHORT).show();


        //para cada solicitud de inicio, enviar un mensaje para iniciar un trabajo
        //y entrega el start ID entonces nosotros sabemos que solicitud estaremos deteniendo
        //cuando terminemos el trabajo
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);




        //si nos matan, despues de regresar de aqui, reiniciamos
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Servicio Terminado", Toast.LENGTH_SHORT).show();
    }
}
