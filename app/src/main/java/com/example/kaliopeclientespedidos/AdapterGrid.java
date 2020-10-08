package com.example.kaliopeclientespedidos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AdapterGrid extends BaseAdapter {

private Context context;
private ArrayList<HashMap> arrayList;
ImageView imageIV;
ProgressBar progressBarGrid;

    public AdapterGrid(Context context, ArrayList <HashMap> arrayList){
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_grid,null   );
        }

        TextView nombreTV = view.findViewById(R.id.itemGrid_Tv_nombre);
        TextView priceTV = view.findViewById(R.id.itemGrid_TV_price);
        TextView existenciaTV = view.findViewById(R.id.itemGrid_TV_existencias);
        nombreTV.setText(arrayList.get(i).get(MainActivity.ADAPTER_COLUMN_NOMBRE).toString());
        priceTV.setText(arrayList.get(i).get(MainActivity.ADAPTER_COLUMN_PRICE).toString());
        existenciaTV.setText(arrayList.get(i).get(MainActivity.ADAPTER_COLUMN_EXISTENCIAS).toString());



        imageIV = (ImageView) view.findViewById(R.id.itemGridIV);
        progressBarGrid = (ProgressBar) view.findViewById(R.id.itemGridProgress);







            //String url = "https://www.google.es/images/branding/googleg/1x/googleg_standard_color_128dp.png";
            //String url = "https://preview.ibb.co/h09rEQ/Screenshot_2017_9_27_tic_tac_toe_Buscar_con_Google.png";
            String url = arrayList.get(i).get(MainActivity.ADAPTER_COLUMN_IMAGE_PREVIEW).toString();
            //String url = "https://cdn.forbes.com.mx/2016/09/Nueva-York-4-e1474914075275.jpg";





            //Estos son con la clase async task que esta dentro de esta clase, funciona bien
            //el problema es que una ves que se descarga la imagen no se actualiza en la pantalla
            //hasta que deslisamos la pantalla
            //DescargaImagenes nuevaTarea  =new DescargaImagenes();
            //nuevaTarea.execute(url);


        //JAJAJA CON LA LIBRERIA PICASO PUMM TAN SIMPLE
        /*
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.downloading_picture)
                .into(imageIV);
*/



        Glide.with(context)
                .load(url)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(Glide.with(context).load(R.drawable.loading2)).transform(new RoundedCorners(15)).fitCenter()        //para mostrar el gift
                //.placeholder(R.drawable.loading2)
                .into(imageIV);

                //.signature(new ObjectKey(UUID.randomUUID().toString()))       //es parte del glide para asignar un id unico a la foto que se guarda en la cache y de esa manera que glide al buscar en la cache no encuentre ese mismo codigo porque es aleatorio y entonces tenga que descargar la imagen por fuerza, pero es malo porque consume muchos datos, porque cuando tu deslisas por ejemplo un grid view las fotos que ya se descargaron y salen de la pantalla al volver a hacer scroll hacia arriba entonces se descargan otra vez, consumes muchos datos
        return view;
    }











    private class DescargaImagenes extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarGrid.incrementProgressBy(10);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            // TODO Auto-generated method stub
            Log.i("doInBackground", "Entra en doInBackground");
            String url = strings[0];
            Bitmap imagen = descargarImagen(url);
            publishProgress(2);
            return imagen;

        }


        private Bitmap descargarImagen(String imageHttpAddress) {
            URL imageUrl = null;
            Bitmap imagen = null;
            try {
                imageUrl = new URL(imageHttpAddress);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return imagen;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBarGrid.setProgress(100);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageIV.setImageBitmap(bitmap);
            progressBarGrid.setVisibility(View.GONE);
            //notifyDataSetChanged();
            //imageIV.notify();

           //MainActivity.update();
        }
    }
}




