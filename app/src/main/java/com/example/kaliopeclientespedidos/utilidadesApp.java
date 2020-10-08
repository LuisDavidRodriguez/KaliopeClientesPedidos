package com.example.kaliopeclientespedidos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class utilidadesApp extends AppCompatActivity {

    public static String dameFecha() {

        final Calendar c = Calendar.getInstance();
        int a = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);

        String dia = "";
        String mes = "";
        String anio = Integer.toString(a);

        if (d < 10) {
            dia = Integer.toString(d);
            dia = "0" + dia;
        } else {
            dia = Integer.toString(d);
        }

        if (m < 10) {
            mes = Integer.toString(m);
            mes = "0" + mes;
        } else {
            mes = Integer.toString(m);
        }

        return dia + "-" + mes + "-" + anio;

    }


    /**
     * @return String dd-MM-yyyy
     */
    public static String getFecha(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formatteDate = dateFormat.format(c.getTime());
        return formatteDate;
    }

    public static Date sumarRestarFecha (Date fechaProcesar, int campo, int valor){
        //para restar el valor debe ser negativo para sumar solo positivo,
        //(En campo necesitamos especificar que sumaremos si dias, horas segundos etc
        //    Calendar.MILISECOND – milisegundos
        //    Calendar.SECOND – segundos
        //    Calendar.MINUTE – minutos
        //    Calendar.HOUR – horas
        //    Calendar.DAY_OF_YEAR – días
        //    Calendar.MONTH – meses
        //    Calendar.YEAR – años
        //
        //    )

        if (valor==0) return fechaProcesar;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaProcesar);
        calendar.add(campo,valor);
        return calendar.getTime();

    }


    /**
     * Calculamos la fecha de vencimiento
     * necesitamos saber apartir de una fecha dada añadiendole los dias de credito del cliente
     * cual sera la fecha del siguiente corte
     *
     *
     * @param fechaInicialddMMyyyy      Con formato dd-MM-yyyy
     * @param campo A que se sumara el valor agregado
     *              Calendar.MILISECOND
     *              .SECOND
     *              .MINUTE
     *              .HOUR
     *              .DAY_OF_YEAR
     *              .MONTH
     *              .YEAR
     * @param valor      que cantidad de unidades se le sumara
     * @return
     * String de tipo fecha de cierre formato dd-MM-yyyy
     */
    public static String calcularFechaVencimiento (String fechaInicialddMMyyyy , int campo, int valor){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = simpleDateFormat.parse(fechaInicialddMMyyyy);
            return simpleDateFormat.format(sumarRestarFecha(date,campo,valor));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fechaInicialddMMyyyy;


    }


    //lo usamos especificamente para exportar los movimientos o inventario y que se nombren con diferente nombre gracias a los segundos
    //porque aveces exportababos unos moviminetos en la mañana y luego otros en la tarde pero como venian con el mismo nombre el primero se perdia
    public static String dameFechaConSegundo(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(c.getTime());
    }



    //Obtener el numero de semana
            //este metodo nos entrega el numero de semana del año, un año tiene 52 semanas
            //el numero de semana se actualiza el dia lunes y es el mismo hasta el dia domingo
            //una ves que vuelve a ser lunes cambia el numero de semana, esto siguiendo un ejemplo
            //y una definicion encontrada en google
//    Resulta que en Java existe un método que ya nos devuelve el número de la semana
//    int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
//    pero este número de la semana es dependiente de la región, por lo que podríamos obtener valores que no esperamos.
//    De acuerdo con la norma ISO_8601:
//    Se considera la primera semana de un año (semana W01) aquella que contiene el primer jueves de dicho año,
//    o lo que es lo mismo, aquella que contiene el día 4 de enero.
//    Los días de la semana se representan numéricamente con un dígito, siendo el primero día el lunes (día 1) y el último el domingo (día 7).
//    La semana empieza siempre, por tanto, en lunes.
//    En mi caso necesito obtener el numero de la semana de acuerdo a esta norma ISO y el código queda como sigue:

    public static int getNumeroSemana(){

        final Calendar calendar = Calendar.getInstance(new Locale("en","UK"));
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        //definimos la fecha de donde comenzara esto solo lo use para hacer pruebas y determinar
        //si devolvia correctamente el numero de semana
        //calendar.set(Calendar.YEAR,2020);
        //calendar.set(Calendar.MONTH,2);
        //calendar.set(Calendar.DATE,2);

        int numSemana = calendar.get(Calendar.WEEK_OF_YEAR);
        return numSemana;
    }

    //prueba de set Time
    //al parecer para poner el mes si se define el numero 10 inicia en noviembre significa que los meses
    //inician desde 0, para saber que diferencia hay entre usar Date o calendar.set

    public static String getFechaPrueba(){
        final Calendar calendar = Calendar.getInstance();
        Date date = new Date(0, 01, 01);
        //este de date parece que funciona sumando el año de 1900 mas lo que le pongas porque si en año se le define
        //0 entonces marca 1900 si le ponemos 18 marca 1918 mejor usamos los set de abajo
        calendar.set(Calendar.YEAR,2005);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DATE,1);
        //calendar.setTime(date);
        return String.valueOf(calendar.getTime());
    }



    public static String dameMes() {

        final Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH) + 1;

        return String.valueOf(m);

    }

    public static String dameFechaAyer() {

        final Calendar c = Calendar.getInstance();
        int a = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH) - 1;

        String dia = "";
        String mes = "";
        String anio = Integer.toString(a);

        if (d < 10) {
            dia = Integer.toString(d);
            dia = "0" + dia;
        } else {
            dia = Integer.toString(d);
        }

        if (m < 10) {
            mes = Integer.toString(m);
            mes = "0" + mes;
        } else {
            mes = Integer.toString(m);
        }

        return dia + "-" + mes + "-" + anio;

    }

    public static String dameHora(){

        Calendar calendario = new GregorianCalendar();
        int hora, minutos;

        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);

        String sHoras;
        String sMinutos;

        if (hora < 10) {
            sHoras = "0" + Integer.toString(hora);
        } else {
            sHoras = Integer.toString(hora);
        }

        if (minutos < 10) {
            sMinutos = "0" + Integer.toString(minutos);
        } else {
            sMinutos = Integer.toString(minutos);
        }

       return sHoras + ":" + sMinutos;
    }

    public static String dameHoraCompleta(){

        Calendar calendario = new GregorianCalendar();
        int hora, minutos, segundos;

        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        String sHoras;
        String sMinutos;
        String sSegundos;

        if (hora < 10) {
            sHoras = "0" + Integer.toString(hora);
        } else {
            sHoras = Integer.toString(hora);
        }

        if (minutos < 10) {
            sMinutos = "0" + Integer.toString(minutos);
        } else {
            sMinutos = Integer.toString(minutos);
        }

        if (segundos < 10) {
            sSegundos = "0" + Integer.toString(segundos);
        } else {
            sSegundos = Integer.toString(segundos);
        }

        return sHoras + ":" + sMinutos + ":" + sSegundos;
    }

    public static String dameFehaHora(){
        return dameFecha() + " " + dameHoraCompleta();
    }


/*
    public static void dialogoError(Activity activity, String message) {

        TextView title = new TextView(activity);
        title.setText("Title");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Error de Aplicación");
        builder.setIcon(R.drawable.icono_error);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                System.exit(0);

            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }
*/

    public static String dameFechaVencimiento(String fechaMovimiento, String diasVencimiento) {
        Date fechaVencimiento = new Date();
        Date fechaMovimientoDate = null;
        SimpleDateFormat sdf = null;

        try{
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            fechaMovimientoDate = sdf.parse(fechaMovimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(fechaMovimientoDate);
        cal.add(Calendar.DATE, Integer.parseInt(diasVencimiento));

        return String.valueOf(sdf.format(cal.getTime()));
    }


    public static boolean evaluarFechaVencimiento (String fechaVencimiento){
        //(usare este metodo para que en el adaptador del listView clientes
        // el cliente que sea de mes, es decir que su fecha de vencimiento es
        // mayor a la fecha actual aparecera en un color grisaceo difuminado)

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaVencimientoDate = null;


        Log.i ("fecha recibida",fechaVencimiento);

        try{
            fechaVencimientoDate = sdf.parse(fechaVencimiento);
        }catch (ParseException e){
            e.printStackTrace();
        }


        //si la fecha actual esta despues de la fecha de vencimiento
        //entonces regresamos true lo que significa que deberiamos pintar
        //los items de color solido.
        //pero si no es asi es decir que la fecha de vencimiento es mayor que la
        // actual debemos pintarlos en difuminado
        if (new Date().after(fechaVencimientoDate)){
            return true;
        }

        return false;


    }

    /*
    public static void dialogoAviso(Activity activity, String message) {

        TextView title = new TextView(activity);
        title.setText("Title");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Aviso Importante");
        builder.setIcon(R.drawable.icono_aviso);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }
*/

/*
    public static void dialogoAvisoFuncion(Activity activity, String message, final EditText et) {

        TextView title = new TextView(activity);
        title.setText("Title");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Aviso Importante");
        builder.setIcon(R.drawable.icono_aviso);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                et.requestFocus();
                dialog.cancel();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

*/
    public static void ponerEnPortapapeles (String mensaje, Activity activity){
        //String version = String.valueOf(android.os.Build.VERSION.SDK_INT);
        //int versionSDK = android.os.Build.VERSION.SDK_INT;
        //Toast.makeText(this,String.valueOf(versionSDK),Toast.LENGTH_SHORT).show();

        //vALIDAMOS SI LA VERSION DEL SDK ES MALOR O IGUAL A 11 USAMOS LA SINTAXIS UNO DE CLIPBOARD

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //Toast.makeText(this, "tu version es mayor a 11", Toast.LENGTH_SHORT).show();
            ClipData clip = ClipData.newPlainText("El mensaje",mensaje);
            ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clip);

        }else

        {
            //Toast.makeText(this, "tu version es menor a 11", Toast.LENGTH_SHORT).show();
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)activity.getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText (mensaje);
        }
    }










}


