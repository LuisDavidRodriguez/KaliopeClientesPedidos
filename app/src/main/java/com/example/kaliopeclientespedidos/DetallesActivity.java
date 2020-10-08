package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetallesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        String price = getIntent().getExtras().get("PRICE").toString();
        Toast.makeText(this, price, Toast.LENGTH_SHORT).show();
    }
}
