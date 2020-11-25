package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class CarritoActivity extends AppCompatActivity {

    String id_producto = "";
    String colorSeleccionado = "";
    String tallaSeleccionada = "";
    int cantidadSeleccionada = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            id_producto = bundle.getString("ID_PRODUCTO");
            colorSeleccionado = bundle.getString("COLOR_SELECCIONADO");
            tallaSeleccionada = bundle.getString("TALLA_SELECCIONADA");
            cantidadSeleccionada = bundle.getInt("CANTIDAD_SELECCIONADA");
        }else{
            throw new IllegalArgumentException("No se encontro extras del intent");
        }


        Toast.makeText(this, id_producto + " " + colorSeleccionado + " " + tallaSeleccionada + " " + cantidadSeleccionada,Toast.LENGTH_LONG).show();

    }





}