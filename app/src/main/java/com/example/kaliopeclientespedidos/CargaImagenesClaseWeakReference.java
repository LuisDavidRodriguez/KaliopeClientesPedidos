package com.example.kaliopeclientespedidos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CargaImagenesClaseWeakReference extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewWeakReference;

    public CargaImagenesClaseWeakReference (ImageView imageView){
        imageViewWeakReference = new WeakReference <ImageView> (imageView);
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        // TODO Auto-generated method stub
        Log.i("doInBackground", "Entra en doInBackground");
        try {
            String url = strings[0];
            return downloadBitmap(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }





    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewWeakReference != null) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
        
    }






    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.e("LoadImage class", "Descargando imagen desde url: " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}