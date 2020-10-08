package com.example.kaliopeclientespedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;

public class Presentacion extends AppCompatActivity {

    public static int MILISEGUNDOS_ESPERA = 2000;
    public static final long TRANSITION_DURATION = 10000;

    private Transition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        getSupportActionBar().hide();



        cambiar(MILISEGUNDOS_ESPERA);


    }


    private void cambiar(int millis){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, millis);
    }

    @SuppressWarnings("uncheked")
    private void nextActivity(){
        transition = new Slide(Gravity.RIGHT);

        transition.setDuration(TRANSITION_DURATION);
        transition.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(transition);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setReenterTransition(transition);

        Intent intent = new Intent(this, Ingreso.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }
}
