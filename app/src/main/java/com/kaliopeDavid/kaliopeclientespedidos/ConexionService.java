package com.kaliopeDavid.kaliopeclientespedidos;


import android.app.Service;
import android.content.Intent;
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

public class ConexionService extends Service {
    private Looper looperThreadSecundario;
    private HandlerPersonalizado handlerThreadSecundario;

    private Handler threadPrimarioHandler;

    public static final String URL_PING = KaliopeServerClient.CARPETAS_URL + "ping_servidor.php";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }







    @Override
    public void onCreate() {
        //iniciamos el hilo que ejecuta el servicio, Nota: estamos creando
        //un hilo separado porque el servicio normalmente corre en donde
        //esta corriendo el proceso principal, el cual no queremos bloquear
        //tambien lo hacemos como background priority para que el trabajo intencibo
        //del cpu no afecte nuesto UI
        HandlerThread threadSecundario = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        threadSecundario.start();
        //obten el looper del hilo y usalo para nuestro Handler. Nuestro threadSecundarioHandler ahora esta enlazado
        //a la cola y lopper del hilo threadSecundario que creamos, ahora tenemos 2 hilos, el principal donde corre el servicio y el UI thread
        //y este hilo que creamos. threadSecundarioHandler, enviara mensajes a la lista de este hilo secundario, y seran ejecutados en el hilo secundario
        //es decir desde este UI thread principal, usando este handler enviaremos mensajes al hilo secundario
        looperThreadSecundario = threadSecundario.getLooper();
        handlerThreadSecundario = new HandlerPersonalizado(looperThreadSecundario);








        //threadPrimarioHandler es un Handler que esta enlazado en el contexto que se creo, es decir al contexto de este servicio que a su ves esta corriendo en el UI thread
        //threadPrimarioHandler sera el handler que enviara mensajes a la cola del hilo principal, por medio de el podremos mostrar informacion en el UI thread
        threadPrimarioHandler = new Handler(new Handler.Callback() {
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
        Message msg = handlerThreadSecundario.obtainMessage();
        msg.arg1 = startId;
        handlerThreadSecundario.sendMessage(msg);




        //si nos matan, despues de regresar de aqui, reiniciamos
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Servicio Terminado", Toast.LENGTH_SHORT).show();
    }




    //HANDLER que recive mensajes del Hilo
    private final class HandlerPersonalizado extends Handler{

        public HandlerPersonalizado(Looper looper){
            super(looper);
        }




        @Override
        public void handleMessage(Message msg) {
            //Normalmente hariamos algo de trabajo aqui, como descargar un archivo
            //para nuestro ejemplo esperaremos por 5 segundos


            //creamos nuestro bucle infinito que se dormira cada 5 segundos y consultara el ping
            while (true){


              /*
              Si quicieramos enviarle un mensaje al hilo principal. funcionaria pero deberiamos programar el codigo en handleMessaje
              Message message = threadPrimarioHandler.obtainMessage();
                message.arg1=10;
                threadPrimarioHandler.sendMessage(message);
               */



                //le enviamos el runable para que corra el codigo en el hilo primario
                threadPrimarioHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"conteo",Toast.LENGTH_SHORT).show();
                        KaliopeServerClient.postNumeroIntentosTimeOut(URL_PING, null, new JsonHttpResponseHandler() {



                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                                try {
                                    String estado = response.getString("estado");
                                    //Toast.makeText(getApplicationContext(),estado, Toast.LENGTH_SHORT).show();//con este podemos saber la respuesta del server
                                    Constantes.offline = false;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }




                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {

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
                                //Toast.makeText(getApplicationContext(), "Falla en Ping Status Code: " + String.valueOf(statusCode) , Toast.LENGTH_LONG).show();
                                Constantes.offline=true;

                            }

                        });

                    }
                });







                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    //reestableser el estatus de la interrupcion
                    Thread.currentThread().interrupt();
                }
            }


            /*
            Esto era antes de cambiar el for de 10 intentos al while infinito. Entiendo que ahora
            este hilo jamas se podra detener

            Message message = obtainMessage();
            message.arg1 = 10;

            //aqui tambien se manejan los mensajes que le enviemos a nuestro holder
            //en este caso estamos enviando el el mensaje el id del hilo para detener
            //solo este hilo cuando terminemos
            //asi no detenemos el servicio en medio de el manejo de otro trabajo
            stopSelf(msg.arg1);
            */


        }



    }

}
