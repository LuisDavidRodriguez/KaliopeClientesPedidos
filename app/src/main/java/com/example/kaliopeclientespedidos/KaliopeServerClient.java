package com.example.kaliopeclientespedidos;

import com.loopj.android.http.*;

public class KaliopeServerClient {
    public static final String BASE_URL = "http://www.kaliope.com.mx/";
    //private static final String BASE_URL = "http://192.168.1.80:8080/";

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
