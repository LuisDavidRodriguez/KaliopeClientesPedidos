package com.example.kaliopeclientespedidos;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Activity activity = this;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;



    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarLuisda);
        //TextView textViewTituloPersonalizado = findViewById(R.id.toolbarLuisda_tituloPersonalizado);
        //primero se tiene que remover el titulo que venga pro defecto en la barra de tareas esto porque puse textView personalizados en la toolbar se que se puede poner titulo y subtitulo sin necesidad de definirlo en el Layout de  la toolbar pero quice probar asi para ver que otros items poner
        toolbar.setTitle("Kaliope Pedidos");
        //textViewTituloPersonalizado.setText("Kaliope Pedidos");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayoutMainActivity);
        navigationView = findViewById(R.id.navigationViewLuisda);
        navigationView.bringToFront();      //esto resuelve el problema del on clic
        navigationView.setNavigationItemSelectedListener(this);

        //Le especificamos al toolbar el iconito de hambugesa para abrir el el drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);                                                          //le añadimos el escuchador al drawer cuando se hagla clic en el toolbar
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();                                                                              //esto lo recomienda android studio colocarlo en el onPostCreate, pero lo dejo aqui

        navController = Navigation.findNavController(this,R.id.fragment2);

        navigationView.setCheckedItem(R.id.home);                                                                       //por default se muestra el fragment donde aparecen todos los productos entonces marcamos como seleccionado ese iten en el drawer


        mostrarDatosCliente(activity);



    }


    private void mostrarDatosCliente(Activity activity){


        String nombreCompleto = ConfiguracionesApp.getNombreCliente(activity);
        String usuario = ConfiguracionesApp.getUsuarioIniciado(activity);
        String cuentaCliente = ConfiguracionesApp.getCuentaCliente(activity);

        //para manipular las vistas del header del drawer
        View view = navigationView.getHeaderView(0);
        TextView nombreDrawerTV = (TextView) view.findViewById(R.id.drawer_header_nombreCliente);
        TextView cuentaDrawerTV = (TextView) view.findViewById(R.id.drawer_header_cuentaCliente);
        TextView usuarioDrawerTV = (TextView) view.findViewById(R.id.drawer_header_usuarioCliente);
        nombreDrawerTV.setText(nombreCompleto);
        cuentaDrawerTV.setText(cuentaCliente);
        usuarioDrawerTV.setText(usuario);


        toolbar.setSubtitle(nombreCompleto);





    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        //Toast.makeText(getApplicationContext(),String.valueOf(item.getItemId()),Toast.LENGTH_SHORT).show();

        if(item.getItemId() == R.id.home){
            navController.navigate(R.id.blankFragment);
            //Toast.makeText(getApplicationContext(),"personas",Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.personas){
            navController.navigate(R.id.informacionCuentaFragment);
            //Toast.makeText(getApplicationContext(),"personas",Toast.LENGTH_SHORT).show();
        }

        //cerramos el navigation cuando haya un clicl
        //drawerLayout.closeDrawer(GravityCompat.START);
        drawerLayout.closeDrawers();

        return true;                //retornando true los items ya se sombrean y se quedan seleccionados en false todo funciona pero los item que escojiste no se sombrean
    }



    //Para el menu personalisado del toolbar la lupita y otras opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu,menu);
        return true;
    }

    //lo mismo para el otro menu de la barra de tareas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }




}