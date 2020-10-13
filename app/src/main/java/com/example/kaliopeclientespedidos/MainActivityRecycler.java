package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;

import com.example.kaliopeclientespedidos.adapter.AdsAdapter;
import com.example.kaliopeclientespedidos.adapter.CategoriasAdapter;
import com.example.kaliopeclientespedidos.adapter.ProductoAdapter;
import com.example.kaliopeclientespedidos.adapter.TodasCategoriasAdapter;
import com.example.kaliopeclientespedidos.models.Ads;
import com.example.kaliopeclientespedidos.models.Categorias;
import com.example.kaliopeclientespedidos.models.Item;
import com.example.kaliopeclientespedidos.models.Producto;
import com.example.kaliopeclientespedidos.models.TodasCategorias;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivityRecycler extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<HashMap> listaProductos;
    HashMap map;


    JSONArray categorias;
    JSONArray imagenDeInicio;



    List<Ads> listaPublicidad;
    List<Categorias> listCategorias;
    List<Producto> listProducto;
    List<TodasCategorias> listTodasCategorias;


    private AdapterViewFlipper adapterViewFlipperPublicidad;
    private AdapterPublicidad adapterPublicidad;

    private final String URL_CATEGORIAS ="app_movil/consultar_categorias.php";
    private final String URL_IMAGEN_PRINCIPAL = "app_movil/consultarImagenPrincipal.php";

    public static final String ADAPTER_COLUMN_PRICE = "PRICE";
    public static final String ADAPTER_COLUMN_EXISTENCIAS = "EXISTENCIAS";
    public static final String ADAPTER_COLUMN_DISPONIBILIDAD = "DISP";
    public static final String ADAPTER_COLUMN_DESCRIPCION = "DESCRIPTION";
    public static final String ADAPTER_COLUMN_NOMBRE = "NAME";
    public static final String ADAPTER_COLUMN_IMAGE_PREVIEW = "PREVIEW";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);
        Slide slide = new Slide(Gravity.RIGHT);
        slide.setDuration(1000);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);




        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));



        Producto producto1 = new Producto("Sudadera Dama","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR7RzXfwWUCVpkZ3x3IWx3dRqG8YFp8YhfBDw&usqp=CAU","359","50");
        Ads ads1 = new Ads("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUSExMWFRUXFxgVFhgYGBgaGhgVGRcWFxgYFhkYHSggHxslHxYXITEiJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGxAQGy8lICUtLS0tLS0tMC0tLS0tNTctLS0tLS0vLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIALgBEwMBEQACEQEDEQH/xAAcAAABBAMBAAAAAAAAAAAAAAAABAUGBwEDCAL/xABKEAABAwEEBAkICQIFAgcAAAABAAIDEQQFEiEGMUFRBxMiMlRhcZHSFBYXcnOBsbIjMzRCUqGzwdGSkzVTYoKiFfEkQ2SDo+Hi/8QAGwEBAAIDAQEAAAAAAAAAAAAAAAIDAQQFBgf/xAA6EQACAQMABgcIAgIBBAMAAAAAAQIDBBEFEhMhMVEGFDJBUnGhFRYiMzRTYYFykbHBQkPh8PEjJNH/2gAMAwEAAhEDEQA/AFk3CbOHOHk8ORI+/sNN60esy8J7Cn0apSipbRnj0oT9Hh73+JOtS8JL3Zp/cZn0oT9Hh75PEnWpeEe69L7jD0oT9Hh75PEnWpeEe69L7jD0oT9Hh75PEnWpeEe69L7jD0oT9Hh75PEnWpeEe69L7jD0oT9Hh75PEnWpeEe69L7jD0oT9Hh/5+JOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD/z8Sdal4R7r0vuMPShP0eH/AJ+JOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpQn6PD3yeJOtS8I916X3GHpPn6PD3v8Sdal4R7r0vuMPSfP0eHvf4k61Lwj3XpfcYelCfo8Pe/xJ1qXhHuvS+4w9KE/R4e+TxJ1qXhHuvS+4w9KE/R4e+TxJ1qXhHuvS+4zfd3CRNJNHGbPCA+RjCRjqA5waSM+tZVzJyS1Sq46O06VKU1NvCyWU7IkDefits8muBzva+e/wBZ3zFcqXE+rUflxNKiWAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgF9xfabP7aL9RqlDtLzNW9+nn/FnQDzme0/ErrHzA52tfPf6zvmK5MuJ9Vo/LiaVEsBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBAL7i+02f20X6jVKHaXmat79PP+LOgHHM9p+JXWPl5zta+e71nfMVyZcT6tR+XE0qJYCAEALIBYAIAQAgBACAygBAYQAgBACAEAIAQAgBACAEGQQZQIAQZBACAEAIAQAgF9xfabP7aL9RqlDtLzNW9+nn/ABZ0A8ZntPxK6x8vOdrXz3+s75iuTLifVqPy4mlRLAQAhhvBP7p4L5poWSmZkZe0OwFhJbXVU4tdFtK0bWWzzNfpHClVlBQzjvFR4I5elM/tu8Sl1Pmyp9KI/b9SD3/dgs074BIJMBALgKDFTMAEnUtapFQeEegsbh3FJVXHGRuUDcBAbbNZ3vcGMaXuOprRUn3BZSbeEQqVoUo603hE2ungvtcgDpXRwjceW73hpoD71sQtZS3vceeuOklGOVBa3oPHoiFPtRr7MU+ZWO0/JpLpPJf9P1Ga9uC+1xisTmTjcKsd7g4kHvVcrVx4G7b9JKM3irHV9SFWizuY4se0tc3ItcKEdoK13lPDPQUqkJx1oPKNSwWAgBACAEAIAQGUGRysNxzy0IZhadTnZDUTltOrYCho3GkaFFPMsv8AA4WfRV+MskcGlpo7VlXMVJIoKdR9yPccyrp6H/COfM83ndNnhDQTM4uqWvbgLMPYWiprrFUe41lpuu3lRWD3FdUbnZRODS0lokeW1drzw1OGgJrlmRrTcUS01c92DxbrusrWtIe+tCX4S1zWZ0oSTntyFSs45EqenbjO9JiN9gY8B0JIaGuLi9zTzdvIzbX/AFAa0wbdLTr/AOrH+hM+ysDC8TxGlBhq4GvViAy69SYNqGmqEpYaaQlcwj+agj3EZLB06VxTqL4WeULwQAgF9xfabP7aL9RqlDtLzNW9+nn/ABZ0EdZ7XfErrHy850tfPf6zvmK5MuJ9Wo/LiaVEsBASPQO4/K7WxhHIZ9JJ6rdQ95y71bQhryOVpi96tbtri9yL+Y2gouofOd/eMumN9iyWWSX71MLBve7Ifz7lXWlqxybuj7V3NeMO7vOe5JC4lzjUkkk7ycyVy2297PpcIKCUY8EeFgmbbNA57msYMTnENaN5JoAspZeEV1aipwc5cEXzodonHYohkHTOH0j+v8LdzQunRpKC/J850npGpeT3vEe5DXpFwiMslofZzA95bTMOaBmAcgR1qupcaksYNuy0FO6oqrGeExt9LkfRpP62/wAKHW/wbnuxV8aHTRrhBZbLQ2ziBzC4ONS5pHJBOoKynca8sYNK/wBCTtKLqSkmhw010TjtsRNA2Zo+jft9V29p1dSlVpKaNfRmkqlnUXhfFFDzxOY4scKOaS0jcRkVzcNbmfRadRTipR4M1rBMEAIAQAgPTGkkACpJoBvTOCM5qEdZk8ubR2KHlSgySVFARyA0tzoNpz2qt1VwPJ3ukqtaTjDchVNC5wANQ0ZZayyuTd1AAMlRKeWc/V73xNtmYWksDnNjcana4UoRQ78llVMEXHcJ7zsbXSBskdCG1oHDN7qZvOqmWoUVk6jT3kIx3DdeUTnSufha5sQoAAcAFKYqHPWdqObZmMFjIjf9U1j8eJhLoubhGulW0oc9qkpmHDfuEvkzuPLmhs1Q0OOH6OrwBhIyGVQp6wfAa7ddQa90dRxnGFhaAaAbM91TRZ3hYwbLXI6OVsZIaI6NcY25uprqHihPaE1sE6UpQeYCdz2lrNTXVLXa89rXHYK6skzk7lhpWUXq1v0aiEPRxaaTRhDIvuL7TZ/bRfqNUodpeZq3v08/4s6AeDU9p+JXWPmBzta+e/1nfMVyZcT6rR+XE0qJYZRhl4cF9xeT2USOFJJuWd4b9xvdn2ldKhBRj5nz3Td5t7jVj2Y7iZFXnGKU4Vb94+08Q01jhyPXIdZ92rvXPuamZYR7jo9ZbOjtZLfIg61j0aBZBNuCW7hJbDIRURMLh6zuSD3YlsW0MyzyPO9JK7hbqC/5P/Bdq6J4UoXhN/xKf/Z+m1cy47Z9D0B9FH9kXoqMnZJbwWf4jF6snyFX27+NHD6Q/RPzRelF0jwBSPCvd4itpeBQSsa//cOSfgFz7qOJZ5nu+jtd1LZwf/Eha1j0AIAQAgMhATvQPR6rBaXipcSGDc0a3e9U1N55jS969fYxe7vJuyw0yy5QPu1fnmobPCODrZe4BYRQ12UrvpqySMFxZiVTG40usDc6Dae5QcN5NVHgTz2VmZw7N/3thU8J8TCb7hnlgADuXSowltOcNqzGOCTlvwIyM21AcGtc1oOoYq/uarKMvc9w2NDmgNrkHB1N5G1ZDwze9oc2Z4YANYNK0dvcddSc8lNNlWMbhmt0WKjnGrqUIpqA1Z7SjLY4QjkhHFltMy6pNNgGQHvWMkmu8RvgLR1KUWem0TdOpF05dxrUjsoX3F9ps/tov1GqUO0vM1b36ef8WdAuOZ7T8SusfLznW189/rO+Yrky4n1aj8uJpUSwf9Cbk8rtccZHIBxyeqNnvOStow15HK0td9WtnJcXuR0ExtABuyXUR86y222Mmmd+iyWWSWox0wxje92ru1+5Qqz1I5NzR9q7mvGHd3nPT5KkkuqTmTXMk5klcnfnLPpUNSK1Y9xhCwFlBlmcCf1lq9WL4yLbteL/AEeS6UcKf7/0Wut08iVVfV0RWi87WJQTRsRbQka2AH4Bec0zczoPMT1lpc1aNjTcOGWZ8zbJ+B39bv5XDWlK/Ms9qXC7z1cF0xWe9LMIgRWOYuqSfu0GvtK72hbidZuUyq/ualaxm5vO9FpBehPKFScNQ+ms/s3/ADNWjd8Uex6L9iovyiuFqHqjCAEAIDIQFs6KXtCywxEuADW4Hb8QyIp161VJYZ4bSVKcbmWt+j2/S2Kp4trjWlMqVIGakotmlqjbPpq8FwEOwipcdevdmpbLmHBMZJ9NZ6ahqzoKg9da6+pTdFBREcmm8/4G/mP3WNimSSZol0pmJb9FHr1YnVPapqjEwsia1aWSvfURMjGohrjrGvWFl0o4Mb0J59InAirNueefcoKmg5NHoaSgAhwdhzy1ZkZE7+xT2ZDWbZ4hvxhAq6naCsOmyWs0KWWprqFrmnsKhKGCamF4yDBTbVQO3oenPa63cNhUj1OMC64vtNn9tF+o1Sh2l5mre/Tz/iy/pOcc9p+JXWPl5zxa+e/1nfMVyZcT6tR+XE0qJYy6+Cu4+IsvHOH0k1HdkY5g/f3ro29PVjk+f6du9vcOK4R/yThbBxDTNZmPoXNa6mYqAaHVUVR7zMZOPZeDzLYY3DC5jCNxaCPgsNJklVqJ5Un/AGQ3Sbg3s8zS6zgQSaxh5hO4t2doWvUt4tfDuZ2bLTlei8VHmJUF42CSCR0UrS17TQg/EHaDvWi4uLwz3FvcU69NTpvcywuBL6y1erF8ZFtWvF/o8z0p4U/3/otYhbp5Egmnlxytkbb7MMT2Nwyx/jjG0dYXP0haRrwwzu6Ku4ajtq3B8HyYhN/xeTeU15NMhtxasPbVeMVjN1tlg3FaVNtsn/4hx0DuKXjHW+0ikj24Y2f5cZzz6zkva2FnG3p4Ro6VvKbSt6PZXF82TlbxxCpeGv66zeo/5mrRu+KPY9F+xU80VstQ9UCAEAID01tSB7lnBGTwsknu26nRyAV4xr2Ym01F41tOzEFCElJnl9I3arUcyWJJ4/QvvK2mKkeHlfh3agR1qWvvwef188DVd9imtDxRpDXCtaYRTVkNpUotviZbYXxcU7HOMAa+NozLzhFR1gUzUFXik9Z7jMpau8jtss742GSQQ5kgNYXPNeugoKKaqxk/hKncDPPaKEfR1xECuLLPIa1ZDf3mesrkSKXRu0tj43iw47A1wJJNMx21UJVIxlvJbVMfLpuxhgay0WdokYKOxNBcczmHN/dcq5uZRqfDLcVOTyN98aGMczHZ3Bj/APKe7kkHc7WD+S2re6cu2SVRohFrs7onFkjMJz9/ZsIW/GUZcGS2vMUWJ0ZryTWmVK5DWTUZrEt3EyqibFsLC91Aa9prQdZVZ6nRNzGFCTZ4kbQkVBoaVGpYZ3qU9eClzFtxfabP7aL9RqzDtLzKL36ef8WX/Icz2n4ldY+YHO9q57vWd8xXJlxPqtH5cRw0Yuvym1RQbHO5XqDN35CnvUqcNaSRq6Ruer28p/jd5nRUcYaABkBkOxdVbj5k22233mJpg1pc4gACpJNABvKw2lxMxi5PCW8g94cKdkY7CxkkoH3m4QD2YiCQqHcxTO7S6PXU4a0sLI+6N6XWa2VERLXgVLH5OpvGwjsVkKsZcDQvdG17T5i3c0PysNAgfCvo+2Wz+UtH0kOZO+L7wPZrWvcQUlk72gL10a2yk/hl/kZeBP6y1erF8ZFVacX+jodKOFL9/wCi11unkRNeFsZFG6SQhrGglxOqiw9yyTpQlUqKEFlsoSK0x8aLQWycV5XjwU+jwYsX9dPyXHUlt+4+gOlU2OyWNbV/ef8A8L9sVpZIxskbg5jgC0jUQV2Fw3Hz6cJU5OMlvRvWSJUvDX9dZvUf8zVo3fFHsei/YqeaK2WoeqMgIZW8FlbzBhYAqu4xh440Es20r+yjV1tT4TUvI1nSexe8sPRSzxTwvMbOLax4DCcgSBnQe/WVXTpuK1pPeeMv7erCaVWWW+I8viZEQ4xxyPqauGwbqlYdRxZq0qaZ4lmxPD2hrSMgSa5a8goTqSl3l8aeqgvG0F0Jia5oxayRt30UdVMqdHLIlFozGQ8Su41xNQXVyPuIVjeOzuIO2Hq7YI4mNaY4nObqc6ME02UNfzVka7SwOrjraLZhsrn0B5zRTLXSmfv/ACVNSbkyqUMSwiPaOzRwh/GPc8Pq6mH6t2wA1q6vXuUalGMkljeS2LQz3laosRkbHUk812XwUIUJcDCpvO88W+SKdoa6gywsaBQNJ1mtK16ytiEdm8IvVKL3CC654rK51Y6vOWJufJyyburRTqRqVFxKXTwzM8Ucz3vjY2JslBhxanjPszCi5Sg8M6FjcyoSzFZQzSNIJB1jI9oV2c7z3NOanFSQtuL7TZ/bRfqNUodpeZRe/Tz/AIsv2U8o9p+JXWPmBzzaue713fMVyZcT6rR+XEnfA1ZQ60yyH7kYA6i92f5NWzar4mzzfSao1ShDmy4lvHjCvuGK8HMs0cTTTjXnF1taK095IWtdSxFHoOjlBVLiU5dyKdXOwe77hbc9vdBNHM00LHA+7aD1EVU6bxJM1by3jWoyhJdx0nE6oB3iq6y5ny9xw8Gq32cSRvjdqe1zT2EEI1lYJU5OM1JdzKw4EvrLTXXgi+Mi07TjI9V0mlrQpPz/ANFrlbp5Ipzhbvt77QLKCRHGGucPxPcK57wAQtG5qPOqj2nR2ziqW3lxZDg0+TVo+nG66/R83Vh/F1rnJrafo72c1sZWcfv/ANE04Ib6e20Gyk1je1z2j8L25mnURXuXRtpvW1Tz3SO0hslXXFPBcC3jxxUvDX9dZvUf8zVo3fFHsei/YqeaIFdN2yWiVsMQq5x9wG1x6gM1rQi5ywj0N1cwtqTqTLfubg2skbBxwMz6CpcSGg5VwgUyqO5dCNrBcd54i50/c1ZfA9VfgL64NrJI08SDC+nJIJw1zOYOzNJ20JLduFtp+5pS+N5RUF7Xc+zyvhkFHMND1jY4dRGa584uEsHt7W6hc01Uh3iQFYybPkWLwdsx2dwrQNkcKe5p/da1Vbzyemvhrp80SzyRtFU1k4+sxPNZWjUoPcSUmN9pgCkiakJWMAcN+alkNmm3S5ZHNSRlMb70tZLcFThBrTrWUt4xEZpbUaq3AEbpiSaqeGReDw56Y5mDXI0EKRHVQnmdsWPM9JoaitnJtGood9LHAXXF9ps/tov1GqUO0vM1b36ef8WdAPOZ7T8SusfLzna1c93ru+Yrky4n1aj8uJYXArJSa0N2ljD3OcD8Qtm0e9nmOlEcxpyXNltrePHkA4YLudJZmStBPFPJdT8LhQn3EBa11Fyivwd/o5cqlcuEv+SKcotA94Lblu9088cLASXuA7BXlE9QFVmmnKSSNW8uY0KMpvkdJxtoANwouv3Hy9yy8ni0yBrS46gCT2AErDMwWZJLmVZwKy1ktW8tjP8Ayf8AytO1a1pHqekixTpLln/RbC3TyZR2n1hfNekzI2lxPF9gHFtzJ2Bci8qQpNymz3uh68KNhGUnzFnmO3icPGO43nV+5WmrDu69a897WxV4fCV+15KrnHw+po4PLBJDekbJGlpwydh5BzB2heisasask4GdM14VrBuD70XWF1jwxUnDU76ezjdG897m/wALRu8ZR7HounqVH+UZ4GbO0yTyHnNa1o6gSSdnUs2iWWyPSibUYR7mxRar7tX01qFpI4q18Q2z0bhLMQbyhrxGutSlUlnOe81oWdviNHU4w1tb84yPNjZabRb7WzyuWOKCSKjGhuYcwOLakasvzU1mcuJpVXRoWtN7NNyT37yPcM9naJbO/LE5jmnrDS2mz/Udu5U3axJHX6MVG6c4d24rham89VnvJXoZeromSNDaguBBrqcW7uxqhKm5M81pyMdeMs9w5v0itPK5UQA20Jy7a5f91JUTgbhrl0itTjXjGYduQ92tYlSRlMQyX9aD/wCZvyAbXq2Js4JE1gRG/bQ00MnfQ06tSns4tbjGUhHaL3tBBcZNmQy37ApqksGNZCOa+Jvx1J7NlOpT2SMOZsltkwGR2VBIoD2FFBDXEjbU92t1PxZrLSI6zZske/Ih5IJp16lHHMyefKJcgKGu9NxJLIoikdWjwPcoSS7j0Wir1Raotfs3KB6EXXF9ps/tov1GqUO0vM1b36ef8WdBHWfWd8SusfLznS1c93ru+Yrky4n1aj8uI+6BXwLNbY3uNGO+jf1B1KE9horKEsTObpi16xatLit5f7XVXTPnX4PMsQcCHCoORBzBHWhmLaeUQq8ODGxyOLm8ZFU1IY4YfcHA0VDt4N5O1Q0/dU46u5+Y96O6KWax1MTOUci9xq4jdXYOxWQpxgtxpXmkK9081H+h9UzRIpwlXyLPYngHlyjimb+Vk4+4VVVaWrE6mh7V3Fyt25b2VpwZXoILc0ONGygxV2BxILK+8U960bZ6ssHqukFs61tmPFF6grqHgCs7ZaWMvS1l72sBZDTEQK0b1ry+nKc5rEUepoU5ysaequ9i0XtB/nR/1t/leddrW8JHq9Xwv+jRd9rZJedkwPa+jJ64SDTkjXRei0BTnTlJTI3VOcLKesmt6LGqvT4PN5KI4Sr0E9tfhNWxgRDrIzce809y5tw9aR9B0Fbyo2mXxe816AaQix2kOf8AVPAZJ1CuTtezb1JRqar3ktM2DuqHwcY7y3W6PWKWQWoRMe91Hh41E0BDqDKurNb+pCW/B4nr11ShsdZpcP8AsLBZ4LOZp8mGSjpXE5EsbQE16tyykomup1K2rTW/HApTT3SEWy04mfVsGBldorm7XtP7Ln16inLce90NYO0ofFxfESaHRh1tgaQ0gvzDhUEYSSKe5UKWrvNjSksWsmiyY7sskWIHIPcHFpoAXCoDgG6qg6hkqdspHj51atdLW34MT2CGVpYyFhbnU0w0pvJ3op/koa1eJ4dohZMJo0UHOo92RptFVr1NrhygzNOrh4aPdl0QsmAObG0jI1113ZqqMqsoazkWOqlLGBtvy4LujZ9IxrANoNDXqz1qtXNbOIbyyMNbeyLXPYbFNMWxteSG5PkNQQ3XTUdS2Ks7jVzJ48iE9SK4DpZRY4X5siORpVgrVYoVJ53tsoUnJYSHiQ2W1NIfgcQAG1oaDPIbuxXtyznIipR3EHdZ7PFJha2MSYuS85gbBkchRWqc3EzPctw5TcHBcx8plaXnlYW1a2p9+v8AlZ28jXjU5iF2jsUYZjFXHKmIinbRTk5JZLVMxfVywRcXxXOOLFRxdllStfeobSTNq0uZU6muN1pu57RWmQ1moUlk9VZaTjV+Gb3nq4vtNn9tF+o1Th2l5m7e/TT/AIs6AfWp7T8SusfLzna1c93rO+Yrky4n1aj8uJpUSwtPQDT9oa2zWp2GnJjlOojY2Q7D16lvUK67Mjxul9CyTdags80WbHICAQQQcwRnUdS28ruPLNNPDPaAwUMDVf8ApFBZI8crwD91g5zzua391XOpGKyzatLOrdSUaa/fcUXpTpDJbZjK/IDKNo1Nb+5O0rnVZ67yfQtHaPhZ0tRb2+LGhpp1KvOHk33FNYfAuLQbTxk7WwWhwZMAAHHJslNRrqDt427F0aVdSW/ieD0roepQk50lmP8AglFr0bsczzJLZ43vNKuc0EmgoPyVjpxby1k5VK9uKUdSE2lyNXmfYOiQ/wBATYw5Is9o3X3H/ZusWjtkheJIrPHG8Vo5rQCARQrKpxi8pFdW9uKsdSc20RXTzTxkTXQWZ4fKQWueM2xDbntf1bNqprV1Hcjr6K0NOtJVKyxFepTritBs90kksAgwLruvq0QfUzPjB2NOWsHUctgUlOS4M1a9jb199SCYXjfVon+ume8bictp1DLaUlUlLizNCxoUHmnFIQqBtGyzTuY4PYcLgagjYUIVIRnFxktzLS0KtBkgEkhxPc5xJOvIkCm5ak1GMmeL0pT2dZwp7kOV6TOc3Czk9n5ntVMszNGFPDyxLdsWCNzNjudvKLK3EpLLFHlL2MLWEUpQV2LDptrCM435ZG7bYMT8TzjPX+ynCCjwLtoe2WZoGoDLYpYzxK5PI2TWFm4ayfdXJTi8bkSUdwllcOoZ1WeLGBA+NoOoZ9mSnvMqI82fSlzIjEakDm02jcT1LGo3uNepb5eUR+1TOmkdI88onZkANwV34LFTSEzYyC5ziaAVG+td+uixJ7sIsp2+1moribrZbcTGtGqgxbyan/6WYJpYZ3tHWWxqvXTyv6MXF9ps/tov1Gqce0vM6t79NP8Aiy/pCantPxK6x8vOeLXz3+s75iuTLifVqPy4mlRLDNUA6XTpFarNlDM9o/DWrf6XVCnGrOPBmlc6OtrjtwWf6JAzhPt4FCYj1mM1/JyuVzPvOY+jlpnflfv/ALCa28Id4SCnGhg/0MA/M1Kg7moy6joC0pvLjnzZGLRaHvcXvc57jrc4kk+8qpvLyzrU6UKaxBYRrWCxbjCAygayPt0aYW2zgNjndhH3X0e33Ysx7iro15rdk5dzoe1rvWlDfzW4kdo0+vNkbJHtiDH8xxZryrscoxv9ebjHuOZT0JYTm4xbyvyRy9tL7ZaBhkndh/C2jB78OZ96SrTl3nUttEWlHfGG/wDO8YVWdPAIAQAgBACAEBItHNIeJaY3c2pIO6usKipS1nk4mk7F1Hrx495IDpJGc8Y71Q6U+44bo6rxJHuO/Gkc8JqVORFxR5lvkfiHes6shqIRy3sDtCzGDGqhNJeg3/mp6jDihDaLxz1/mrFAYEk1srtUtmwIpbUpKLMoTSWpSUWMGnykjUVlQGrngbmPcdZUWdjRljJVNef6PVUPQC64vtNn9tF+o1Sh2l5mre/Tz/iy/wB9KntPxK6x8wOd7Xz3+s75iuTLifVaPy4mlRLDIQEouy4oBZ22mfjHB5oGxgmmvXQdS5lW7qOs6VPG7mca4vazrOlSwsczVZLlhntPFwufxTW43ucMx1DLr29alO5nSpa0sazeFyJzvKtKjrTxrZwhRLcVmmjkdZXSY4hUh+pwGumXUqo3danNKtjD5FSvq9OcVWxh8jMVxWWGKN9qkcHSCoDdg9wPUsyu61SbVFbkHe3FWbVFbkYsdxWXBJaJJH8QHYWUFC7Omwb8gpVbqtrKnBLWE72411SilrHt2jEJks7o3udBMSNeYoxzhnTVyaKKvaijOMl8UUR9o1VCcZdqIoZo5YnySWdsknHNqeodWqhpVVu8uVGNSSWq8EHf3ShGpJLVYksej8EcBntTnDlOYA3qcW6qf6SVbO8qyq7Okt+C2d/Wq1NSiu4d72ups0djgjfyCTRx14AyvetShXlSlVqTW/8ABp29zKjOpOS3jJe1gsMbXtBnbI2oFWmjnDLKopRblCtcyabxh/2b9G5u5yi3q4ZGCuodgwsAEAIAQAgBALBdc/8AkS/23/wpqDfcazu6CfbX9nh9ySnXZ5D/AO0/woo1O5FVSpZz7cosTtuvlYBCcf4Qw4tVebSupZzJbmQ2Fpq7RpY59xuFxS9Hl/tv/hZ+N9xHVsOcTTPYMBo9jmHc4Oae4qLlJFkLa0qL4EjzHYg4hrWFxOoCpJ7AMyik2J2dtBa0ksCg3DJ0eb+3J/Ck3NbsFGpYc4/2YNxSdHl/tv8A4T/5H3GVCw5x/s1QXZjJDInPI14WudTtoo60i2draU988Jfk3f8AQZOjS/2n/wAKTVTkyvVsOcTxJczmDE6B7QNpjcANmsiiw1Uxlk6cbJySg45/Bss9ikeKsje8aqtY5w7KtCjGLlwRtVK9KlunJI0yMIJBBBGRBFCD1grDLotNZTyLbi+02f20X6jVmHaXma179PP+LL/kbme0/ErrHy853tfPf6zvmK5MuJ9Wo/LiaVEsMhDD4E00djnEDTDaogDUlj6HD2bVx7uVOVX44Nvmjg3rpyqvXpvPNDq++YGWtrcTKuiwPkGrHUFoP/L8lr7CpOg3h4T3Jmp1WrKjrYeE9yEV5yWmOOR7rZCRTkgNbVwOzLarKOynJRVN/supRpSnGKhI12yzR2+KBzZmRujbhe12saq5e5Spyla1JJxbTJ0qs7OrNauU3xMWOzxyWZ9h49mNkmJrq5EYsQ/g0Wakp06yrqLxgxOdSFdXLg8CuG0RQusdlEjXOY5znkHIfRybdmblDVqVVUqOOMrcUTpVKqqVdXCfAb7qmaL1ldiGGsmdctQV1eEpWUY437jarwl1GMUuQrnjZa7M6ESsY9kzzyqasbj3UKqi529bXcW8pFUJTtqynqt5SNl5QtwWOOO0sYWEgPqNjaZdurNV0ZSUqk5Qe8hScs1JSpvD7hT5Q5kUnlk8MrC2jQ0CpKjhSqx2UZJ5IailUjsIyTK1XofM9THON5hDIIAQAgBAZCyuJGfZfkdAaSX95FZGz4MecbcNac4b6HcunKepDJ82tLTrdy6WccSH+lz/ANL/APJ/+VT1r8Hb915fc9Br0OvPym+haMOHjMZw1rSkWHX7lXSlr1Ms29I2vVtF7LOcY/yTLTLTnyGZsXE8ZiYH1x4dZIpSh3K+rWdN4OFo3Q0r2DmpYwz3dtus98WV7XxUocLg7MscRUOa4KUXGtHLI16Nxoq4jh/0VpoVZzHesMZ1slkYe1rZB+y06axU1T1GlKiqaOdTmkWZpppr5BJGzieMxtLq4sNKGlNRW5VrKDweX0Zol3sZNSxgjU3CziaW+S6wR9ZvHqql3W7gdWPRlxkntPQT8C/11o9mz5ysWzzJlnSZYpU/Me9IOErya0y2fyfHxbgMXGUrUA6sPWrZ3Gq8HPstAu5oqrr4yRrSfhD8rsz7P5Pgx4eVjrTC4O1YRuVM7jWWMHWsej7t68arnnBIeBd9bPON0oPewfwrLXdH9nO6Tr/7EX+CstIJMVqtDt80nzuWnU7b8z1lisW1NfhGLi+02f20X6jViHaXmL36ef8AFnQLtZ9Z3xK6p8vOdbXz3+s75iuVLifVqPy4mlRLAQCmw2F8rxHG3E4/DeTuUatSNOOtIpr1oUVrSHO8NF7RDGZHhmEc6jtXfRa1K9pzlqps1aWkaNSWqshY9FLTIwSNa0AircRoSOoUUal/RhLDfAjU0nRpyceQnsej80r3xtaA6PnBxp2U7lZVu6cIqT4MsqXtGnFSfeK5NELQ0sB4sFxo0F410Jpq3AqqOkKMk+O4pWlKLTe/d+BFBcsr53WYAcY2tQTQZa8/erncwjT13wL53dONJVXnVf4HG02WVljLTHEWNkLTICS/EJKEDLMVyVMZwlX4vOOHcacKlKdznL3rg+B4h0QtTmB+BoyqAXUdTsWXf0VLULpaTt1LUNN36M2iUFzWgCpHKIFSNdN6lVvqVN4b4k6ukKEHjP8AQ3XhYHwvwSNwuGfu3g7lfSqRqR1omzQrQrR1o8BOrDYMIYBACAEAIDKyuKIz7LOhL9sFnnszWWp2GLkGuPBygMuUupJRcMSPmdvWrUa7lR7W8i0uiVzAEiYVpl/4ga+9UOlRXA68dKaU1llP+iH8F3+Iw+rJ8hVNv8zcdrTjzYt+X+SydKdFbJa52unlc2TCGNaHsaSKk6iKnMraq0oze88to/SNza03sY7ueDVaZLLc1mIYHEvJw1qXPkpliNKACn5I9WlDCJQVfStwtZ/+itdBpS+9IHu5zpHuPa5khPxWpReaiZ6rS9JU9Hygu5JFo6XWG7ZHsNue1rg04MUrmVbXPU4VzW5UjTb+I8jYV72nF9WTx34WSFaWXZdDLK91kkYZhhwgTOeecK5Fx2VVFSNPVeDuaPudJTrpVk9XvyjZwLfXWj2bPnKxacWS6UdiHmSK/LruZ88jrTIwTEjGDO9prQawHAaqK+caed5yLW50lCklRT1e7CINp1YrvjEXkL2uri4zDI5+rDh1k02rWrqCXwnodD1rypKXWluxuysEj4FpOTaRuMbvycP2Vtq9zOd0mj8dNlZ219ZHne9x7yStOTy2est1ilFfhCm4vtNn9tF+o1IdpeZVe/Tz/izoE6z2u+JXWPl5zra+e/1nfMVyZcT6tR+XE0qJYZQD1ona3xTYmxOl5JDmt5wbkaju/Nat9CM6eHLBztI0ozprMsD4+67PPFLJGyeEs5bhJXCSOVQg169Wqq50a9WlNQbUs8jmKvWoyUW088hx0hdEJInujtLyGgxmLm6xTaM1TbKcozWYrf3lFvGcoSWYrf38Qu63CSe1Pax8bhCwEPADsQxmuRPUs16epThFvO/uM1aThSpptPe+BALPbHtcx+JxLCHipJz26967jpRknDHFHoZ0Iyg4pcUWLbHRxNlt7aEvhYG+sdXfyf6VwKWtOStXwTPNw16ko2z4JjTd5H/Toi7V5Q2tfbiq26n1Uorw/wCjbrrF5JR8P+jRpjDaTaqxiQtwDCW4qUpytWW9SsZUlSxLGc95Zo+VuqOJYznvHG38V5JZscc72BopxJpQ0HOFa11rXp6+3niUV5mpT19vPUlFeYwaYW8SuiHFyRlrSPpQMRacNDkTXUe9dCxpuCe9PPI6mjKMoazynnkR0rfZ1TCwAQAgBACAFmPFGJdll0cKP+GD14fguhX+WeD0H9f/AGUvRc496Szgv/xGH1ZPkK2LbtnF0/8ARS/X+R14YHkW2JzTRwia4HcRI8g96ldPEkzR6ORU7acX3v8A0S8ht7XYNQe5v9M7f2J/IrY+bTOGnLRl9+E/QrTQKMtvOztcKOEj2kbiGSAj8lp0Y4mer0xUU7CUl34wT/hD0Rnt0sT4TGAxjmnG4jMurlRpW3WoubTR5vQ2lKNlCSqJvL7iJScGFtaCS6HIVye7Z/tVDtpJHbj0jtpPCTHDgY+vtHs2fOVm1zrM1uk7UqVPHM36V8H1qtFrmnjdFge4EYnOBya0ZgNO5TqW7lLKKtHadt7e3jTknlciN33oHarLC6eV0RY2lcLiTm4NGto3qmdvKKydW107b3FZU4p7+Yu4PritUzJZLNa/J+UI3DDixZYht/1LNCEmm4vBrabvKFOpGFWnrd6IfeFnMcskZNSx7mE6qlpLa/kqJLDwd+hUVSlGaWMpG+4vtNn9tF+o1IdpeZVe/Tz/AIs6COs9rviV1j5ec6Wvnu9Z3zFcmXE+rUflxNKiWAgYru+8ZIXY4nlppQ7iNxBVdWjCosTKa9CnXWJoVW/SG0TNwPk5J1gACvbRVUrOlSeYo16VhRpvWSPVj0ltMTAxknJGQqAaDcCo1LOjNttGKmjqNRuTW9mqG/J2ue4P5Ugo8kA1GerdrVjt6bSTXAslZUXGKxwG1Xs2vIWyXpK6JsBdWNpqBTdWmfvVSo01PXXE142tNVdolvD/AKnJxPk9foq4sNBrrXX2rKox19p3meqwVXa43iuHSe1NZgEuQFBUAmnaqXY0dbWxvNd6NoOWtg8WDSG0wghkhoSTRwBzOZIqs1LOlU7SJ1LChU4oSW+3yTOxyuLnauoDcArqVKFJYii6jQhRWIISqZeCAEAIAQAgMhDDSa3jveOklqnj4qWYvjyOEhusatQVrqyawzRo6MtqNTaQW8Z1Ub4qu68JIJBLE7A8Vo4UqKgg6+orMZOLyii4t4V4OFRZRsvW9prS4PnkL3AYQSAMtdMgpzm575ELe0pW0dWksG+6dIbTZmlsEpY1xqQA0gmlK5gpCrOG5Fd1o+3uWpVI5Zpbe0wn8pD6TVxY6DnEYSaUpq+KjtHraxY7Ok6Oxx8PIdPPq8OlO/pZ4Vb1iozTehLLugYfpxeBFDaXEHLms8KbeYWhbNb1AbbovqezOc6CQxlwAcQAagZ7QVVGco70bd1ZUblKNSOUhz8+rw6U7+lnhVvWKjNP2LZP/gJrx0rtk8Zilnc9jqVaQ3OhBGobwFiVabWGW0dFW1GanCOGjXdGkdpszS2CUxtccRADTU0pXMdSjCpKC3Fl1o+3uZa1VZG20TOe5z3GrnEuJ3k5kqDbb3m3TgoRUY8EK7i+02f20X6jVmHaXmUXv08/4sv+QZntPxK6x8vOd7Xz3+s75iuTLifVqPy4mlRLAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQAgBACAEAIAQC+4vtNn9tF+o1Sh2l5mre/Tz/izoFxzOf3j8SusfLznW189/rO+Yrky4n1Wj8uJpWMFoJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgAmACYAJgC+4vtNn9tF+o1ZiviXmat79PP+LOgHtzPafiV1j5gUpaNDLeXOIsr6FziM2aiSfxLmujPPA+hU9MWagk5r1NfmVeHRX97PEmynyJ+2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niTZT5D2zZ+NeoeZV4dFf3s8SbKfIe2bPxr1DzKvDor+9niWdjU5D2zZ+NeosufQ63NtELnWZ4a2WNzjVuQD2knnbgsqjUytxrXelrSdGcYz3tPuZcUgFT2n4ldA8Ef/9k=");
        Producto producto2 = new Producto("Sudadera Hombre","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRZL_7MopFMN0Af3bKBdEalLgzSfMoH6PcEkg&usqp=CAU","359","50");
        //este crea una previsualizacion de algunas de ropa interior
        Categorias categoriaRopaInterior = new Categorias(
                "Ropa Interior",
                "Pantaleta encaje",
                "129",
                "https://ae01.alicdn.com/kf/H06ab763772254ea0b729f490d5f47d594/3-unidades-por-paquete-Bragas-Sexy-de-encaje-para-mujer-ropa-interior-bragas-de-encaje-S.jpg_q50.jpg",
                "brasier",
                "239",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSNArPZszZkmkhHCwJ9fMznL9XOOPZkBZFxBg&usqp=CAU",
                "tanga",
                "139",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSE919M_6aRSAPlD0fpnmbHkxLfuQs20TAkog&usqp=CAU",
                "cachetero",
                "289",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQu1pw-03q9sBySdowYlOSvIyjlU7a-ZrSEGA&usqp=CAU");




        listaPublicidad = new ArrayList<>();
        listProducto = new ArrayList<>();
        listCategorias = new ArrayList<>();
        listTodasCategorias = new ArrayList<>();

        listaPublicidad.add(ads1);
        listProducto.add(producto1);
        listCategorias.add(categoriaRopaInterior);


        ArrayList<String> listaDeTodasLasCategorias = new ArrayList<String>();
        listaDeTodasLasCategorias.add("ropa interior");
        listaDeTodasLasCategorias.add("Hombre");
        listaDeTodasLasCategorias.add("Dama");
        listaDeTodasLasCategorias.add("Pantalones");
        listaDeTodasLasCategorias.add("Sudaderas");
        listaDeTodasLasCategorias.add("Calzado");


        //ya que creamos unos cuantos objetos y los llenamos vamos a crear los adaptadores
        AdsAdapter adsAdapter = new AdsAdapter(listaPublicidad);
        ProductoAdapter productoAdapter = new ProductoAdapter(listProducto);
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(listCategorias);
        ProductoAdapter productoAdapter2 = new ProductoAdapter(listProducto);
        TodasCategoriasAdapter todasCategoriasAdapter = new TodasCategoriasAdapter(listaDeTodasLasCategorias);


        ConcatAdapter concatAdapter = new ConcatAdapter(adsAdapter,productoAdapter,categoriasAdapter,productoAdapter2, todasCategoriasAdapter);

        recyclerView.setAdapter(concatAdapter);


        //consultarImagenPrincipal();







/**
        adapterViewFlipperPublicidad = (AdapterViewFlipper) findViewById(R.id.main_AVF_publicidad);
        int[]imagenes = {R.drawable.cortador1, R.drawable.cortador2};

        adapterPublicidad = new AdapterPublicidad(this,imagenes);
        adapterViewFlipperPublicidad.setAdapter(adapterPublicidad);
        adapterViewFlipperPublicidad.setFlipInterval(4000);
        adapterViewFlipperPublicidad.setAutoStart(true);

**/


    }


    private void enaviarArecycler(){

        listaProductos = new ArrayList<>();

        for (int i=0; i<imagenDeInicio.length() ; i++){


            try {

                map = new HashMap();
                map.put(ADAPTER_COLUMN_PRICE,imagenDeInicio.getJSONObject(i).getString("precio_etiqueta"));
                map.put(ADAPTER_COLUMN_NOMBRE,imagenDeInicio.getJSONObject(i).getString("descripcion"));
                map.put(ADAPTER_COLUMN_EXISTENCIAS,imagenDeInicio.getJSONObject(i).getString("existencia"));
                map.put(ADAPTER_COLUMN_DISPONIBILIDAD, "DISPONIBLE");
                map.put(ADAPTER_COLUMN_DESCRIPCION, "");


                String URL_Imagen = KaliopeServerClient.BASE_URL + imagenDeInicio.getJSONObject(i).getString("imagen1");
                map.put(ADAPTER_COLUMN_IMAGE_PREVIEW,URL_Imagen);
                Log.d("UrlImagenes",String.valueOf(URL_Imagen));
                listaProductos.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }






        AdapterRopa adapterRopa = new AdapterRopa(listaProductos,this);




        //prueba con merge adapter

        //ConcatAdapter concatAdapter = new ConcatAdapter(adapterPublicidadRecycler,adapterRopa);
        recyclerView.setAdapter(adapterRopa);


    }


    private void consultarImagenPrincipal(){


        KaliopeServerClient.get(URL_CATEGORIAS,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();

                Log.d ("datosRecibidos",String.valueOf(response));

                try {
                    categorias = response.getJSONArray("categorias");
                    imagenDeInicio = response.getJSONArray("imagenInicio");
                    Log.d("datosProces",String.valueOf(categorias.toString()));
                    Log.d("datosProcesImagenInicio",String.valueOf(imagenDeInicio.toString()));


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
    public void onBackPressed() {
        if(ConfiguracionesApp.getEntradaComoInvitado(this)){
            super.onBackPressed();
        }else{
            moveTaskToBack(true);
        }

    }






/*
    //https://es.switch-case.com/53695591
    private static void setListViewHeightBasedOnChildren(GridView gridView) {
        AdapterGrid gridAdapter =(AdapterGrid) gridView.getAdapter();
        if (gridAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        //obtenemos cada item ya inflado con los datos requeridos
        //medimos cada altura de item
        //y en base al numero de elementos, sumamos todas sus alturas
        //asi sabremos la medida total de todos los elementos
        for (int i = 0; i < gridAdapter.getCount(); i++) {
            View listItem = gridAdapter.getView(i, null, gridView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight()+20;//sumo a la medida de cada item no entiendo en que parte se esta comiendo medida, al final sale mas corto
        }


        ViewGroup.LayoutParams params = gridView.getLayoutParams();

        //sabemos la medida total de todos los items pero! no esta incluida en esa medida
        //el espacio del separador entre las filas entonces hay que sacar la medida del separador
        //y multiplicarla por el numero de items para obtener una altura total con items y separadores
        //ahora todas esas medidas tomadas totales las dividimos entre el numero de columnas que se estan
        //desplegando al mismo tiempo en el grid view, porque la medida obtenida es total pero como si los
        //items estuvieran en una lista, como mostramos 2 columnas entonces dividimos entre dos la medida total
        int verticalSpacing = (gridView.getVerticalSpacing() * (gridAdapter.getCount()));
        int finalHeight = totalHeight + verticalSpacing;
        params.height = finalHeight/2;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

 */
}