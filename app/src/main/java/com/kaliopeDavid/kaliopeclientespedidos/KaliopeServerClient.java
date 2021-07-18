package com.kaliopeDavid.kaliopeclientespedidos;

import com.loopj.android.http.*;

public class KaliopeServerClient {
    public static final String BASE_URL = "https://www.kaliope.com.mx/";
    //public static final String BASE_URL = "http://192.168.0.105/PhpProject_clientes_pedidos/";
    //public static final String BASE_URL = "http://192.168.1.80:8080/";

    public static final String CARPETAS_URL = "app_movil/2.0/"; //añadimos esta constante para no tener que poner en los archivos a donde nos queramos dirigir a lo largo del programa toda la ruta. asi por ejemplo cuando actualicemos la version y hagamos una nueva carpeta a 3.0 solo cambiamos aqui la ruta, y no hay que buscar a lo largo del programa para editar

    private static AsyncHttpClient client = new AsyncHttpClient(true,80,443);

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        //client.setMaxRetriesAndTimeout(3,15000); //definimos de maximos reatrys 3 por default son 5
        client.get(getAbsoluteUrl(url), params, responseHandler);

    }


    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void postNumeroIntentosTimeOut(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setMaxRetriesAndTimeout(1,5000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
