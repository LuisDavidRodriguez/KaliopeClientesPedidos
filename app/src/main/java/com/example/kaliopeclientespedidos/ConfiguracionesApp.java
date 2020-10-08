package com.example.kaliopeclientespedidos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

/**Clase usada para manejar las configuraciones de la app
 * y mantenerlas aunque la app se cierre
 */
public class ConfiguracionesApp {

    //Usando SharedPreferences
    //Guardando las configuraciones del usuario, en este caso solo sera la pulcera que hemos seleccionado
    //fuente:http://www.ajpdsoft.com/modules.php?name=News&file=print&sid=556


    /**
     * Creamos los nombres de nuestros archivos utilizados y de la informacion colocada para acceder a ellos
     * mediante la instancia de este objeto y no tener que escribir el nombre del archivo o del parametro
     * en la llamada a esta actividad
     */

    //VersionName1.0

    private static final String NOMBRE_ARCHIVO_CONFIGURACIONES = "configuracionesKaliope";

    private static final String MOSTRAR_AVISO_COMO_INVITADO = "avisoComoInvitado";
    private static final String ENTRADA_COMO_INVITADO = "entradaComoInvitado";
    private static final String MANTENER_SESION_INICIADA = "mantenerSesionIniciada";
    private static final String NOMBRE_USUARIO_INICIADO = "agenteAsignado";
    private static final String NOMBRE_COMPLETO_CLIENTE = "nombreCompletoCliente";
    private static final String NOMBRE_ESTADO_SESION = "estadoDeLaSesion";
    private static final String NOMBRE_FECHA_HORA_INICIO_SESION = "fechaHora";
    private static final String NOMBRE_DIA_INICIO_SESION = "diaInicioSesion";

    private static final String NOMBRE_CODIGO_DISPOSITIVO_UNICO_UUID = "codigoDeDispositivo";






    /**
 * Nos permitira saber si anteriormente el usuario marco la casilla de no volver a mostrar otra vez cuando
 * le indicamos las restricciones al entrar como invitado.
 * @return true=cuando queramos seguir mostrando el mensaje <p> false=cuando se el usuario indique no msotrar otra vez
 * */
    public static boolean getMostrarAvisoComoInvitado(Activity activity){

        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);



        //si no encontramos la preferencia guardada devolveremos por default true indicando
        //que deberemos mostrar el aviso de invitado
        return sharedPreferences.getBoolean(MOSTRAR_AVISO_COMO_INVITADO,true);


    }
    /**
     * Cuando el usuario marque la casilla no mostrar mensaje otra vez setearemos
     * el valor en false para que no se vuelva a mostrar el mensaje de invitado
     * @param valor true=cuando queramos seguir mostrando el mensaje <p> false=cuando se el usuario indique no msotrar otra vez
     * */
    public static void setMostrarAvisoComoInvitado(Activity activity, boolean valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putBoolean(MOSTRAR_AVISO_COMO_INVITADO,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }

    /**
     * Si se entra a la aplicacion como invitado esconderemos algunas opciones o permitiremos
     * ciertas acciones en el menu principal
     * Si no se encuentra la configuracion
     * <h2>Hola</h2>
     *
     * @param activity la actividad donde se esta llamando al shared prefernces
     *
     * @return true si se entro como invitado o no se encuentra la configuracion
     *
     *
     * */
    public static boolean getEntradaComoInvitado(Activity activity){

        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);



        //si no encontramos la preferencia guardada devolveremos por default true indicando
        //que deberemos mostrar el aviso de invitado
        return sharedPreferences.getBoolean(ENTRADA_COMO_INVITADO,true);


    }
    /**
     * guardar si el usuario entra como invitado
     * @param activity
     * @param valor true si el usuario entra como invitado
     *              <p>false si no
     *
     * */
    public static void setEntradaComoInvitado(Activity activity, boolean valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putBoolean(ENTRADA_COMO_INVITADO,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }

    /**
     * Cuando el usuario indique que quiere mantener su secion iniciada
     * @return true=cuando el usuario quiere mantener su secion inciada <p>false= (default) cuando iniciara sesion cada momento
     * */
    public static boolean getMantenerSesionIniciada (Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);



        //si no encontramos la preferencia guardada devolveremos por default true indicando
        //que deberemos mostrar el aviso de invitado
        return sharedPreferences.getBoolean(MANTENER_SESION_INICIADA,false);
    }
    /**
     * Si el usuario quiere mantener su sesion iniciada
     * @param valor true=Si el usuario palomea la casilla mantener sesion iniciada en este dispositivo
     *              <p>false=Si el usuario no quiere guardar el inicio de sesion en este dispositivo
     * */
    public static void setMantenerSesionIniciada (Activity activity, boolean valor){
        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putBoolean(MANTENER_SESION_INICIADA,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }



    /**
 * Consulta la cadena unica con la que la instancia de la aplicacion fue creada en un dispositivo
 * @return String=la cadena unica de la instancia de la aplicacion
 * <p> SinValor= si aun no se a guardado ninguna cadena unica
 * */
    public static String getCodigoUnicoDispositivo (Activity activity){

        //(Creamos la variable de tipo SharedPreferences haciendo una llama a "getSharedPreferences",
        // le pasamos como parámetro a este procedimiento el nombre del fichero de configuración, en
        // nuestro caso "configuracionesKaliope" y el tipo de acceso "MODE_PRIVATE".)
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "codigoDeDispositivo"
        // , si no encuentra este valor devolverá el valor por defecto SinValor. para saber que no se encontro
        return sharedPreferences.getString(NOMBRE_CODIGO_DISPOSITIVO_UNICO_UUID,"SinValor");



    }
    /**
     * Se debe de guardar el codigo de la instancia unica de la aplicacion
     * se creara un codigo con la clase UUID
     *
     * @param valor La cadena unica generada por la clase UUID
     * */
    public static void setCodigoDispositivoUnico ( Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_CODIGO_DISPOSITIVO_UNICO_UUID,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }






    public static String getUsuarioIniciado(Activity activity){

        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_USUARIO_INICIADO,"SinValor");


    }
    public static void setUsuarioIniciado(Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_USUARIO_INICIADO,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    private static void deleteUsuarioIniciado (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_USUARIO_INICIADO).apply();

    }

    private static void setNombreEmpleado (Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_COMPLETO_CLIENTE,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static String getNombreEmpleado (Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_COMPLETO_CLIENTE,"SinValor");
    }
    private static void deleteNombreEmpleado (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_COMPLETO_CLIENTE).apply();

    }

    private static void setEstadoDeSesion (Activity activity, boolean valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putBoolean(NOMBRE_ESTADO_SESION,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static boolean getEstadDeSesion (Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getBoolean(NOMBRE_ESTADO_SESION,false);
    }
    private static void deleteEstadoSesion (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_ESTADO_SESION).apply();

    }










    //FECHA de inicio sesion, cuando el usuario inicie sesion se guardara la fecha hora en que lo hiso "13-08-2019 14:21:40"
    //y se enviara esta hora junto con la cadena de mensajes de los clientes, esto porque si
    //el usuario cierra sesion y en la tabla ya tenian mensajes de clientes, si vuelve a iniciar sesion
    //se carga nuevamente la misma zona, y los mensajes nuevos se encimarian a los que ya estan ocacionando perdida de informacion
    //entonces cada inicio de sesion tendra una hora diferente la tabla del servidor los manejara y archivara en otro registro
    //separado del anterior

    private static void setFechaInicioSesion (Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_FECHA_HORA_INICIO_SESION,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static String getFechaInicioSesion(Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_FECHA_HORA_INICIO_SESION,"SinValor");
    }
    private static void deleteFechaInicioSesion (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_FECHA_HORA_INICIO_SESION).apply();

    }







    //(Dia DE INICIO DE SESION, ESTE METODO SOLO GUARDARA EL VALOR DE LA FECHA EN FORMATO dd-MM-yyyy un string
    // cuando se inicie sesion en el movil al igual que ocurre con la fechaInicioSesion guardara el dia en que se inicio
    // cuando la fecha actual del sistema sea mayor en 1 dia a la fecha en que se inicio sesion entonces el sistema
    // forzara un cierre de sesion para que al dia siguiente el usuario forsosamente tenga que cerrar sesion
    // esto para que se cargen los nuevos clientes, y que ademas se borren las tablas como los mensajes, los movimientos
    // y que se comience el dia de trabajo desde 0 y que no se tengan problemas que los movimientos del dia siguiente
    // se guarden con los clientes del dia anterior)

    private static void setDiaInicioSesion (Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_DIA_INICIO_SESION,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static String getDiaInicioSesion(Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_DIA_INICIO_SESION,"SinValor");
    }
    private static void deleteDiaInicioSesion (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_DIA_INICIO_SESION).apply();

    }



    //(Metodo para obtener el nombre cordo del empleado esto porque es el que aparecera en los mensajes de los clientes cuando dicten el audio de voz
    // lo que pasa es que si usamos el nombre de usuario, este podra tener numeros tambien para diferenciarlo de otros usuarios con el mismo nombre
    // por ejemplo gustavo9411, y nimodo que los cleintes digan yo maria entrego a gustavo9411, tampoco podemos usar su nombre completo
    // para eso mejor usaremos su nombre completo pero crearemos un metodo que cortara el nombre completo hasta el primer espacio, y eso retonrnaremos)

    public static String getNombreCorto(Activity activity){

        String nombreCompleto = getNombreEmpleado(activity);
        String nombreCorto = nombreCompleto.substring(0,nombreCompleto.indexOf(' '));
        return nombreCorto;

    }



    public static void setDatosInicioSesion (Activity activity, JSONArray jsonArray, JSONArray jsonArrayFechaClientes){

        for(int i=0; i<jsonArray.length(); i++){
            try {
                setNombreEmpleado(activity,jsonArray.getJSONObject(i).getString("nombre_empleado"));
                setUsuarioIniciado(activity,jsonArray.getJSONObject(i).getString("usuario"));
                setEstadoDeSesion(activity,true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //recuperamos la fecha de clientes que devolvio el servidor, y la zona que se visito
        try {

            //ver el archivo iniciar_sesion_dos_rutas.php para entender mejor el array que enviamos
            //tenemos que recorrer el array de los clientes porque puede que vengan 2 zonas, para asi sacar ambas fechas de la consulta
            //[{"zona":"CANALEJAS","clientes":[{"cuenta":"1146","0":"1146","nombre":"ANA LAURA JUAREZ ARELLANO","1":"ANA LAURA JUAREZ ARELLANO","telefono":"55 39 60 97 72","2":"55 39 60 97 72","dias":"28","3":"28","grado":"EMPRESARIA","4":"EMPRESARIA","credito":"4000","5":"4000","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.021389875920317","7":"20.021389875920317","longitud_fija":"-99.66920361254584","8":"-99.66920361254584","adeudo_cargo":"0","9":"0","piezas_cargo":"13","10":"13","importe_cargo":"2687","11":"2687","fecha_vence_cargo":"12-09-2019","12":"12-09-2019","puntos_disponibles":"0","13":"0","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"1-159-0 1-279-0 1-159-0 4-299-239 2-309-247 1-339-271 2-369-295 1-459-376 ","16":"1-159-0 1-279-0 1-159-0 4-299-239 2-309-247 1-339-271 2-369-295 1-459-376 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 3099*Saldo: 0*Reporte: TODO BIEN****01-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****18-07-2019* Pago: 2700*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 3099*Saldo: 0*Reporte: TODO BIEN****01-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****18-07-2019* Pago: 2700*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"1148","0":"1148","nombre":"MANALI FLORES","1":"MANALI FLORES","telefono":"0","2":"0","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"1500","5":"1500","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.96900203","7":"19.96900203","longitud_fija":"-99.55851103","8":"-99.55851103","adeudo_cargo":"241","9":"241","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"15-08-2019","12":"15-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"LIQUIDA EN 15 DIAS","14":"LIQUIDA EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 200*Saldo: 241*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****18-07-2019* Pago: 77*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 250*Saldo: 77*Reporte: LIQUIDA EN 15 DIAS**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 200*Saldo: 241*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****18-07-2019* Pago: 77*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 250*Saldo: 77*Reporte: LIQUIDA EN 15 DIAS**"},{"cuenta":"1149","0":"1149","nombre":"YOLANDA SANTIAGO AGUILAR","1":"YOLANDA SANTIAGO AGUILAR","telefono":"0","2":"0","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"1400","5":"1400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":" 20.044472","7":" 20.044472","longitud_fija":"-99.655413","8":"-99.655413","adeudo_cargo":"0","9":"0","piezas_cargo":"5","10":"5","importe_cargo":"1345","11":"1345","fecha_vence_cargo":"29-08-2019","12":"29-08-2019","puntos_disponibles":"100","13":"100","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"0","15":"0","mercancia_cargo":"1-329-274 1-299-249 1-309-257 1-359-299 1-319-266 ","16":"1-329-274 1-299-249 1-309-257 1-359-299 1-319-266 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES, PERO DICE HIJO QUE NO ESTABA****04-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES DICEN VECINOS QUE NO ABRIO**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES, PERO DICE HIJO QUE NO ESTABA****04-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES DICEN VECINOS QUE NO ABRIO**"},{"cuenta":"1150","0":"1150","nombre":"ANTONIA GARCIA MIRANDA ","1":"ANTONIA GARCIA MIRANDA ","telefono":"0","2":"0","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"1700","5":"1700","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.94023779","7":"19.94023779","longitud_fija":"-99.56489413","8":"-99.56489413","adeudo_cargo":"639","9":"639","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"01-08-2019","12":"01-08-2019","puntos_disponibles":"550","13":"550","reporte_agente":"QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS","14":"QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS****15-08-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS****01-08-2019* Pago: 500*Saldo: 639*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 214*Saldo: 1372*Reporte: LIQUIDA EN 15 DIAS****04-07-2019* Pago: 928*Saldo: 214*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS****15-08-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINERO NO LE ACABARON DE PAGAR QUEDA DE PAGAR EN 15 DIAS****01-08-2019* Pago: 500*Saldo: 639*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 214*Saldo: 1372*Reporte: LIQUIDA EN 15 DIAS****04-07-2019* Pago: 928*Saldo: 214*Reporte: TODO BIEN**"},{"cuenta":"1153","0":"1153","nombre":"LIZBETH JIMENEZ CAMACHO","1":"LIZBETH JIMENEZ CAMACHO","telefono":"55 82 21 24 43","2":"55 82 21 24 43","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"0","5":"0","estado":"LIO","6":"LIO","latitud_fija":" 19.975901","7":" 19.975901","longitud_fija":"-99.610482","8":"-99.610482","adeudo_cargo":"374","9":"374","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"11-04-2019","12":"11-04-2019","puntos_disponibles":"150","13":"150","reporte_agente":"PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA","14":"PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA","reporte_administracion":"INVESTIGAR CON VECINOS","15":"INVESTIGAR CON VECINOS","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA****15-08-2019* Pago: 0*Saldo: 0*Reporte: PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA****01-08-2019* Pago: 0*Saldo: 0*Reporte: SE INVESTIGA CON VECINOS DICEN QUE NO ESTA QUE SALE A TRABAJAR A QUERETARO Y LLEGA DESPUES DE LAS 8****18-07-2019* Pago: 0*Saldo: 0*Reporte: SU ESPOSO JOSE ALBERTO SE COMPROMETE A LIQUIDAR LA CUENTA DE SU ESPOSA EN 15 DIAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO DICEN FAMILIARES QUE SE FUE A QUERETARO**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA****15-08-2019* Pago: 0*Saldo: 0*Reporte: PARECE QUE LA ESTAN NEGANDO SU FAMILIARES BUSCA AL DELEGADO QUE IVA A HABLAR CON ELLA****01-08-2019* Pago: 0*Saldo: 0*Reporte: SE INVESTIGA CON VECINOS DICEN QUE NO ESTA QUE SALE A TRABAJAR A QUERETARO Y LLEGA DESPUES DE LAS 8****18-07-2019* Pago: 0*Saldo: 0*Reporte: SU ESPOSO JOSE ALBERTO SE COMPROMETE A LIQUIDAR LA CUENTA DE SU ESPOSA EN 15 DIAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO DICEN FAMILIARES QUE SE FUE A QUERETARO**"},{"cuenta":"1154","0":"1154","nombre":"ELIZABET GARCIA GONZALES","1":"ELIZABET GARCIA GONZALES","telefono":"0","2":"0","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"1700","5":"1700","estado":"ACTIVO","6":"ACTIVO","latitud_fija":" 20.021470","7":" 20.021470","longitud_fija":"-99.669315","8":"-99.669315","adeudo_cargo":"0","9":"0","piezas_cargo":"6","10":"6","importe_cargo":"1405","11":"1405","fecha_vence_cargo":"29-08-2019","12":"29-08-2019","puntos_disponibles":"50","13":"50","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"0","15":"0","mercancia_cargo":"1-329-270 1-399-327 2-349-286 2-159-118 ","16":"1-329-270 1-399-327 2-349-286 2-159-118 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 412*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO DICE MAMA QUE SALIO QUE NO ESTABA SE DEJA RECADO****04-07-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA EL DINERO PENDIENTE EN 15 DIAS**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 412*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO DICE MAMA QUE SALIO QUE NO ESTABA SE DEJA RECADO****04-07-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA EL DINERO PENDIENTE EN 15 DIAS**"},{"cuenta":"1155","0":"1155","nombre":"ILSE ANDERIK HERNANDEZ MARTINEZ","1":"ILSE ANDERIK HERNANDEZ MARTINEZ","telefono":"56 11 07 48 99","2":"56 11 07 48 99","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1900","5":"1900","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.01093466745951","7":"20.01093466745951","longitud_fija":"-99.58897021733355","8":"-99.58897021733355","adeudo_cargo":"168","9":"168","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"15-08-2019","12":"15-08-2019","puntos_disponibles":"350","13":"350","reporte_agente":"LIQUIDA EN 15 DIAS","14":"LIQUIDA EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 300*Saldo: 168*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 850*Saldo: 177*Reporte: TODO BIEN****18-07-2019* Pago: 524*Saldo: 130*Reporte: TODO BIEN****04-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 300*Saldo: 168*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 850*Saldo: 177*Reporte: TODO BIEN****18-07-2019* Pago: 524*Saldo: 130*Reporte: TODO BIEN****04-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"1156","0":"1156","nombre":"MARIA LUISA HERNANDEZ MARTINEZ","1":"MARIA LUISA HERNANDEZ MARTINEZ","telefono":"0","2":"0","dias":"14","3":"14","grado":"SOCIA","4":"SOCIA","credito":"2400","5":"2400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.97242562551852","7":"19.97242562551852","longitud_fija":"-99.5655873412939","8":"-99.5655873412939","adeudo_cargo":"294","9":"294","piezas_cargo":"8","10":"8","importe_cargo":"2328","11":"2328","fecha_vence_cargo":"29-08-2019","12":"29-08-2019","puntos_disponibles":"200","13":"200","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"3-399-327 3-359-294 1-259-212 1-309-253 ","16":"3-399-327 3-359-294 1-259-212 1-309-253 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 278*Saldo: 294*Reporte: TODO BIEN****01-08-2019* Pago: 1038*Saldo: 294*Reporte: TODO BIEN****18-07-2019* Pago: 294*Saldo: 294*Reporte: TODO BIEN****04-07-2019* Pago: 270*Saldo: 0*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 278*Saldo: 294*Reporte: TODO BIEN****01-08-2019* Pago: 1038*Saldo: 294*Reporte: TODO BIEN****18-07-2019* Pago: 294*Saldo: 294*Reporte: TODO BIEN****04-07-2019* Pago: 270*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"1157","0":"1157","nombre":"ANTONIA HERNANDEZ CAMACHO","1":"ANTONIA HERNANDEZ CAMACHO","telefono":"55 49 69 62 44","2":"55 49 69 62 44","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"2200","5":"2200","estado":"REACTIVAR","6":"REACTIVAR","latitud_fija":"19.954782323839222","7":"19.954782323839222","longitud_fija":"-99.60686100794508","8":"-99.60686100794508","adeudo_cargo":"0","9":"0","piezas_cargo":"2","10":"2","importe_cargo":"0","11":"0","fecha_vence_cargo":"18-07-2019","12":"18-07-2019","puntos_disponibles":"0","13":"0","reporte_agente":"REACTIVAR EN 2 MESES EN AGOSTO","14":"REACTIVAR EN 2 MESES EN AGOSTO","reporte_administracion":"0","15":"0","mercancia_cargo":"1-399-0 1-159-0 ","16":"1-399-0 1-159-0 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****15-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****01-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****18-07-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****04-07-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****15-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****01-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****18-07-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO****04-07-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 2 MESES EN AGOSTO**"},{"cuenta":"1159","0":"1159","nombre":"EZPERANZA HERNANDEZ FLORENTINO","1":"EZPERANZA HERNANDEZ FLORENTINO","telefono":"0","2":"0","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"2700","5":"2700","estado":"ACTIVO","6":"ACTIVO","latitud_fija":" 19.993895","7":" 19.993895","longitud_fija":"-99.614844","8":"-99.614844","adeudo_cargo":"0","9":"0","piezas_cargo":"6","10":"6","importe_cargo":"1332","11":"1332","fecha_vence_cargo":"12-09-2019","12":"12-09-2019","puntos_disponibles":"0","13":"0","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"INVESTIGAR MAS CON VECINOS","15":"INVESTIGAR MAS CON VECINOS","mercancia_cargo":"1-299-0 1-299-245 1-339-278 1-309-253 1-279-229 1-399-327 ","16":"1-299-0 1-299-245 1-339-278 1-309-253 1-279-229 1-399-327 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 1757*Saldo: 0*Reporte: TODO BIEN****01-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES, QUE ESTA HOSPITALIZADA, SE DEJA RECADO CON HIJA****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 3 VECES SE DEJA RECADO CON HIJAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: DICEN FAMILIARES QUE NO HA REGRESADO DE SU EMERGENCIA DICEN FAMILIARES QUE ES SEGURO QUE PAGE EN 15 DIAS**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 1757*Saldo: 0*Reporte: TODO BIEN****01-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 2 VECES, QUE ESTA HOSPITALIZADA, SE DEJA RECADO CON HIJA****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA 3 VECES SE DEJA RECADO CON HIJAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: DICEN FAMILIARES QUE NO HA REGRESADO DE SU EMERGENCIA DICEN FAMILIARES QUE ES SEGURO QUE PAGE EN 15 DIAS**"},{"cuenta":"1408","0":"1408","nombre":"ORDO\u00d1EZ GARDU\u00d1O YOSELIN","1":"ORDO\u00d1EZ GARDU\u00d1O YOSELIN","telefono":"0","2":"0","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1700","5":"1700","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.96846161","7":"19.96846161","longitud_fija":"-99.56235819","8":"-99.56235819","adeudo_cargo":"145","9":"145","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"01-08-2019","12":"01-08-2019","puntos_disponibles":"200","13":"200","reporte_agente":"LIQUIDA EN 15 DIAS","14":"LIQUIDA EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 650*Saldo: 145*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 200*Saldo: 795*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 507*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 174*Saldo: 507*Reporte: LIQUIDA EN 15 DIAS**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 650*Saldo: 145*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 200*Saldo: 795*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 507*Saldo: 0*Reporte: TODO BIEN****04-07-2019* Pago: 174*Saldo: 507*Reporte: LIQUIDA EN 15 DIAS**"},{"cuenta":"3303","0":"3303","nombre":"CLAUDIA ISABEL S\u00c1NCHEZ ","1":"CLAUDIA ISABEL S\u00c1NCHEZ ","telefono":"0","2":"0","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"2000","5":"2000","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.04443258","7":"20.04443258","longitud_fija":"-99.6554648","8":"-99.6554648","adeudo_cargo":"0","9":"0","piezas_cargo":"7","10":"7","importe_cargo":"1398","11":"1398","fecha_vence_cargo":"29-08-2019","12":"29-08-2019","puntos_disponibles":"50","13":"50","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"0","15":"0","mercancia_cargo":"2-329-274 1-359-299 1-159-0 2-199-159 1-279-233 ","16":"2-329-274 1-359-299 1-159-0 2-199-159 1-279-233 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 1182*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO LE MARCO QUE SALIO A TRABAJAR, QUEDA DE ENTREGAR EN 15 DIAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****15-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****01-08-2019* Pago: 1182*Saldo: 0*Reporte: TODO BIEN****18-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO LE MARCO QUE SALIO A TRABAJAR, QUEDA DE ENTREGAR EN 15 DIAS****04-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"3433","0":"3433","nombre":"VIRGINIA VARGAS HERNANDEZ ","1":"VIRGINIA VARGAS HERNANDEZ ","telefono":"0","2":"0","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1700","5":"1700","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.97496327","7":"19.97496327","longitud_fija":"-99.61408679","8":"-99.61408679","adeudo_cargo":"285","9":"285","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"01-08-2019","12":"01-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"LIQUIDA EN 15 DIAS","14":"LIQUIDA EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 500*Saldo: 285*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 500*Saldo: 785*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS****15-08-2019* Pago: 500*Saldo: 285*Reporte: LIQUIDA EN 15 DIAS****01-08-2019* Pago: 500*Saldo: 785*Reporte: LIQUIDA EN 15 DIAS****18-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"3783","0":"3783","nombre":"GUILLERMINA MART\u00cdNEZ NAVARRETE ","1":"GUILLERMINA MART\u00cdNEZ NAVARRETE ","telefono":"0","2":"0","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1400","5":"1400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"19.98794683","7":"19.98794683","longitud_fija":"-99.65328267","8":"-99.65328267","adeudo_cargo":"0","9":"0","piezas_cargo":"6","10":"6","importe_cargo":"1453","11":"1453","fecha_vence_cargo":"29-08-2019","12":"29-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"1-279-233 1-259-216 1-309-257 3-299-249 ","16":"1-279-233 1-259-216 1-309-257 3-299-249 ","total_pagos":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**","17":"**29-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****15-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN**"}],
            // "fechaClientesConsulta":"29-08-2019"},{"zona":"SAN JUAN DEL RIO","clientes":[{"cuenta":"2170","0":"2170","nombre":"MARIA ISABEL PICHARDO CAMACHO","1":"MARIA ISABEL PICHARDO CAMACHO","telefono":"427 103 42 23","2":"427 103 42 23","dias":"14","3":"14","grado":"EMPRESARIA","4":"EMPRESARIA","credito":"2500","5":"2500","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.33718862282818","7":"20.33718862282818","longitud_fija":"-99.94939814772276","8":"-99.94939814772276","adeudo_cargo":"0","9":"0","piezas_cargo":"10","10":"10","importe_cargo":"2343","11":"2343","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"50","13":"50","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"1-259-207 2-299-239 1-329-263 1-339-271 3-399-319 1-279-0 1-209-167 ","16":"1-259-207 2-299-239 1-329-263 1-339-271 3-399-319 1-279-0 1-209-167 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 529*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 1123*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 1050*Saldo: 2*Reporte: TODO BIEN****02-07-2019* Pago: 813*Saldo: 0*Reporte: TODO BIEN**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 529*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 1123*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 1050*Saldo: 2*Reporte: TODO BIEN****02-07-2019* Pago: 813*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"2172","0":"2172","nombre":"EZPERANZA MARCELO SINECIO","1":"EZPERANZA MARCELO SINECIO","telefono":"427 593 34 01","2":"427 593 34 01","dias":"28","3":"28","grado":"EMPRESARIA","4":"EMPRESARIA","credito":"2500","5":"2500","estado":"REACTIVAR","6":"REACTIVAR","latitud_fija":"20.4379060153793","7":"20.4379060153793","longitud_fija":"-99.94742208072816","8":"-99.94742208072816","adeudo_cargo":"0","9":"0","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"13-08-2019","12":"13-08-2019","puntos_disponibles":"300","13":"300","reporte_agente":"REACTIVAR EN 15 DIAS","14":"REACTIVAR EN 15 DIAS","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 15 DIAS****13-08-2019* Pago: 1396*Saldo: 0*Reporte: REACTIVAR EN 15 DIAS****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 822*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: REACTIVAR EN 15 DIAS****13-08-2019* Pago: 1396*Saldo: 0*Reporte: REACTIVAR EN 15 DIAS****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 822*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"2174","0":"2174","nombre":"ANA LAURA MENDOZA SUAREZ","1":"ANA LAURA MENDOZA SUAREZ","telefono":"266 61 72","2":"266 61 72","dias":"14","3":"14","grado":"SOCIA","4":"SOCIA","credito":"2400","5":"2400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.439199939147645","7":"20.439199939147645","longitud_fija":"-99.89563303688809","8":"-99.89563303688809","adeudo_cargo":"0","9":"0","piezas_cargo":"9","10":"9","importe_cargo":"1968","11":"1968","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"350","13":"350","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"2-159-118 2-209-171 2-279-229 1-339-278 2-399-327 ","16":"2-159-118 2-209-171 2-279-229 1-339-278 2-399-327 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 1145*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 800*Saldo: 1145*Reporte: LIQUIDA EN 15 DIAS****16-07-2019* Pago: 850*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: QUE SU SUEGRA ESTA ENFERMA Y LA ESTA ATENDIENDO DICE MAMA QUE NO DEJO DINERO SE DEJA RECADO**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 1145*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 800*Saldo: 1145*Reporte: LIQUIDA EN 15 DIAS****16-07-2019* Pago: 850*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: QUE SU SUEGRA ESTA ENFERMA Y LA ESTA ATENDIENDO DICE MAMA QUE NO DEJO DINERO SE DEJA RECADO**"},{"cuenta":"2175","0":"2175","nombre":"VICTORIA LOPEZ PEREZ","1":"VICTORIA LOPEZ PEREZ","telefono":"0","2":"0","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"2400","5":"2400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.3325333225939","7":"20.3325333225939","longitud_fija":"-99.98226281483967","8":"-99.98226281483967","adeudo_cargo":"0","9":"0","piezas_cargo":"9","10":"9","importe_cargo":"1635","11":"1635","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"0","15":"0","mercancia_cargo":"1-299-0 1-299-0 1-299-245 1-339-278 4-279-229 1-239-196 ","16":"1-299-0 1-299-0 1-299-245 1-339-278 4-279-229 1-239-196 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****30-07-2019* Pago: 1414*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****02-07-2019* Pago: 785*Saldo: 0*Reporte: TODO BIEN**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****30-07-2019* Pago: 1414*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****02-07-2019* Pago: 785*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"2179","0":"2179","nombre":"MARIA GUADALUPE RAMIREZ PEREZ","1":"MARIA GUADALUPE RAMIREZ PEREZ","telefono":"0","2":"0","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"1900","5":"1900","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.318255430494393","7":"20.318255430494393","longitud_fija":"-99.98400428016876","8":"-99.98400428016876","adeudo_cargo":"0","9":"0","piezas_cargo":"4","10":"4","importe_cargo":"1023","11":"1023","fecha_vence_cargo":"10-09-2019","12":"10-09-2019","puntos_disponibles":"350","13":"350","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"2-279-233 1-299-249 1-369-308 ","16":"2-279-233 1-299-249 1-369-308 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 815*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 1080*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 815*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 1080*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"2180","0":"2180","nombre":"GLORIA CRUZ CRUZ","1":"GLORIA CRUZ CRUZ","telefono":"427 273 13 87","2":"427 273 13 87","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"0","5":"0","estado":"LIO","6":"LIO","latitud_fija":"20.381798019241604","7":"20.381798019241604","longitud_fija":"-99.99014639932028","8":"-99.99014639932028","adeudo_cargo":"400","9":"400","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"02-01-2019","12":"02-01-2019","puntos_disponibles":"300","13":"300","reporte_agente":"NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO","14":"NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO****13-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO****30-07-2019* Pago: 100*Saldo: 400*Reporte: ABONA EN 15 DIAS****16-07-2019* Pago: 300*Saldo: 500*Reporte: ABONA EN 15 DIAS****02-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO SOLO SALE MAMA QUE NO LE DIJO NADA Y NO CONTESTA**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO****13-08-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO PASA SU MAMA QUE NO DEJO NADA QUEDA DE DEPOSIRAR EL SABADO****30-07-2019* Pago: 100*Saldo: 400*Reporte: ABONA EN 15 DIAS****16-07-2019* Pago: 300*Saldo: 500*Reporte: ABONA EN 15 DIAS****02-07-2019* Pago: 0*Saldo: 0*Reporte: NO ESTUBO SOLO SALE MAMA QUE NO LE DIJO NADA Y NO CONTESTA**"},{"cuenta":"2184","0":"2184","nombre":"MARIA DEL CARMEN PEREZ CASTA\u00d1EDA","1":"MARIA DEL CARMEN PEREZ CASTA\u00d1EDA","telefono":"0","2":"0","dias":"14","3":"14","grado":"SOCIA","4":"SOCIA","credito":"2400","5":"2400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.32174212","7":"20.32174212","longitud_fija":"-99.97660145","8":"-99.97660145","adeudo_cargo":"0","9":"0","piezas_cargo":"9","10":"9","importe_cargo":"2346","11":"2346","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"150","13":"150","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"0","15":"0","mercancia_cargo":"3-279-229 1-299-245 1-349-286 1-329-270 1-339-278 1-399-327 1-309-253 ","16":"3-279-229 1-299-245 1-349-286 1-329-270 1-339-278 1-399-327 1-309-253 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 818*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 1421*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 606*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 875*Saldo: 0*Reporte: TODO BIEN**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 818*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 1421*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 606*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 875*Saldo: 0*Reporte: TODO BIEN**"},{"cuenta":"2316","0":"2316","nombre":"MARIELA HERNANDEZ TREJO ","1":"MARIELA HERNANDEZ TREJO ","telefono":"0","2":"0","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"1900","5":"1900","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.32951403480396","7":"20.32951403480396","longitud_fija":"-99.95643287957716","8":"-99.95643287957716","adeudo_cargo":"0","9":"0","piezas_cargo":"10","10":"10","importe_cargo":"2346","11":"2346","fecha_vence_cargo":"10-09-2019","12":"10-09-2019","puntos_disponibles":"0","13":"0","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"*SE CAMBIA A SOCIA*","15":"*SE CAMBIA A SOCIA*","mercancia_cargo":"1-329-0 3-279-229 1-309-253 2-329-270 1-339-278 1-319-261 1-399-327 ","16":"1-329-0 3-279-229 1-309-253 2-329-270 1-339-278 1-319-261 1-399-327 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 506*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 1322*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 506*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****16-07-2019* Pago: 1322*Saldo: 0*Reporte: TODO BIEN****02-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"2948","0":"2948","nombre":"ADRIANA RANGEL ","1":"ADRIANA RANGEL ","telefono":"427 116 8549","2":"427 116 8549","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1400","5":"1400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.3880195","7":"20.3880195","longitud_fija":"-99.97540027","8":"-99.97540027","adeudo_cargo":"490","9":"490","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"13-08-2019","12":"13-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"LIQUIDA EN 15 DIAS QUE NO LE PAGARON","14":"LIQUIDA EN 15 DIAS QUE NO LE PAGARON","reporte_administracion":"PENDIENTE EN 15 DIAS PASAR ANTES DE LA 1","15":"PENDIENTE EN 15 DIAS PASAR ANTES DE LA 1","mercancia_cargo":"0","16":"0","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS QUE NO LE PAGARON****13-08-2019* Pago: 0*Saldo: 490*Reporte: LIQUIDA EN 15 DIAS QUE NO LE PAGARON****30-07-2019* Pago: 233*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 0*Saldo: 0*Reporte: SAN JUAN DEL R\u00edO SAN CAYETANO R\u00edO CLARO R\u00edO CLARO 7. CERCA DEL CENTRO M\u00e9DICO COSCAMI.****02-07-2019* Pago: 0*Saldo: 0*Reporte: SAN JUAN DEL R\u00edO SAN CAYETANO R\u00edO CLARO R\u00edO CLARO 7. CERCA DEL CENTRO M\u00e9DICO COSCAMI.**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: LIQUIDA EN 15 DIAS QUE NO LE PAGARON****13-08-2019* Pago: 0*Saldo: 490*Reporte: LIQUIDA EN 15 DIAS QUE NO LE PAGARON****30-07-2019* Pago: 233*Saldo: 0*Reporte: TODO BIEN****16-07-2019* Pago: 0*Saldo: 0*Reporte: SAN JUAN DEL R\u00edO SAN CAYETANO R\u00edO CLARO R\u00edO CLARO 7. CERCA DEL CENTRO M\u00e9DICO COSCAMI.****02-07-2019* Pago: 0*Saldo: 0*Reporte: SAN JUAN DEL R\u00edO SAN CAYETANO R\u00edO CLARO R\u00edO CLARO 7. CERCA DEL CENTRO M\u00e9DICO COSCAMI.**"},{"cuenta":"3417","0":"3417","nombre":"KARLA JUDITH JUAREZ RIVERA","1":"KARLA JUDITH JUAREZ RIVERA","telefono":"4272715432","2":"4272715432","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"1500","5":"1500","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.43945776","7":"20.43945776","longitud_fija":"-99.98591199","8":"-99.98591199","adeudo_cargo":"0","9":"0","piezas_cargo":"6","10":"6","importe_cargo":"1495","11":"1495","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"0","13":"0","reporte_agente":"TODO BIEN","14":"TODO BIEN","reporte_administracion":"PENDIENTE EN 15","15":"PENDIENTE EN 15","mercancia_cargo":"1-349-291 1-279-233 1-309-257 1-259-216 1-299-249 1-299-249 ","16":"1-349-291 1-279-233 1-309-257 1-259-216 1-299-249 1-299-249 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: SOY DE SAN PEDRO AHUACATLAN X LA \u00faNIDAD DEPORTIVA HAY UNA DESVIACI\u00f3N SAN JUAN DEL R\u00edO**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****13-08-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: SOY DE SAN PEDRO AHUACATLAN X LA \u00faNIDAD DEPORTIVA HAY UNA DESVIACI\u00f3N SAN JUAN DEL R\u00edO**"},{"cuenta":"2171","0":"2171","nombre":"MARIA ELENA NIETO SALASAR","1":"MARIA ELENA NIETO SALASAR","telefono":"414 105 50 79","2":"414 105 50 79","dias":"28","3":"28","grado":"SOCIA","4":"SOCIA","credito":"1500","5":"1500","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.47471218","7":"20.47471218","longitud_fija":"-99.93971997","8":"-99.93971997","adeudo_cargo":"0","9":"0","piezas_cargo":"11","10":"11","importe_cargo":"2551","11":"2551","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"200","13":"200","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"","15":"","mercancia_cargo":"1-279-0 6-279-229 1-309-253 1-329-270 2-399-327 ","16":"1-279-0 6-279-229 1-309-253 1-329-270 2-399-327 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****31-07-2019* Pago: 1013*Saldo: 254*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN SE REALIZO CIERRE EN SAN CLEMENTE****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****31-07-2019* Pago: 1013*Saldo: 254*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN SE REALIZO CIERRE EN SAN CLEMENTE****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"2173","0":"2173","nombre":"YAZMIN CRUZ TORRES","1":"YAZMIN CRUZ TORRES","telefono":"427 132 11 00","2":"427 132 11 00","dias":"28","3":"28","grado":"EMPRESARIA","4":"EMPRESARIA","credito":"1500","5":"1500","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.477873","7":"20.477873","longitud_fija":"-99.9341417","8":"-99.9341417","adeudo_cargo":"0","9":"0","piezas_cargo":"13","10":"13","importe_cargo":"2782","11":"2782","fecha_vence_cargo":"27-08-2019","12":"27-08-2019","puntos_disponibles":"100","13":"100","reporte_agente":"CUENTA DE MES","14":"CUENTA DE MES","reporte_administracion":"SE CAMBIA A EMPRESARIA","15":"SE CAMBIA A EMPRESARIA","mercancia_cargo":"1-349-0 1-279-0 1-239-0 1-309-247 2-299-239 3-349-279 1-329-263 3-399-319 ","16":"1-349-0 1-279-0 1-239-0 1-309-247 2-299-239 3-349-279 1-329-263 3-399-319 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****31-07-2019* Pago: 2216*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN SE REALIZO CIERRE EN SAN CLEMENTE****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****13-08-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES****31-07-2019* Pago: 2216*Saldo: 0*Reporte: TODO BIEN****30-07-2019* Pago: 0*Saldo: 0*Reporte: TODO BIEN SE REALIZO CIERRE EN SAN CLEMENTE****16-07-2019* Pago: 0*Saldo: 0*Reporte: CUENTA DE MES**"},{"cuenta":"2177","0":"2177","nombre":"ANA CAREN ALVARES","1":"ANA CAREN ALVARES","telefono":"0","2":"0","dias":"14","3":"14","grado":"VENDEDORA","4":"VENDEDORA","credito":"0","5":"0","estado":"LIO","6":"LIO","latitud_fija":" 20.475300","7":" 20.475300","longitud_fija":"-99.942300","8":"-99.942300","adeudo_cargo":"0","9":"0","piezas_cargo":"4","10":"4","importe_cargo":"1307","11":"1307","fecha_vence_cargo":"26-02-2019","12":"26-02-2019","puntos_disponibles":"0","13":"0","reporte_agente":"DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE","14":"DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE","reporte_administracion":"0","15":"0","mercancia_cargo":"2-399-333 1-399-333 1-369-308 ","16":"2-399-333 1-399-333 1-369-308 ","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE****13-08-2019* Pago: 0*Saldo: 0*Reporte: DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE****31-07-2019* Pago: 0*Saldo: 0*Reporte: SE ENCUENTRA CASA DE SU MAMA SE PLATICA CON MAMA DICE QUE VA A INVESTIGAR BUSCAR EN DIRECCION DE MAMA****30-07-2019* Pago: 0*Saldo: 0*Reporte: OSWALDO UBICA SU DOMICILIO SE TIENE QUE PASAR A VISITAR A SU MAMA ELLA QUEDO DE PAGAR EN 15 DIAS URGE BUSCAR****16-07-2019* Pago: 0*Saldo: 0*Reporte: SE PLATICO CON DELEGADO QUE NO LA HA PODIDO BUSCAR QUE SI LA V**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE****13-08-2019* Pago: 0*Saldo: 0*Reporte: DA CON DOMICILIO DE SU MAMA PERO NO SALE NADIE****31-07-2019* Pago: 0*Saldo: 0*Reporte: SE ENCUENTRA CASA DE SU MAMA SE PLATICA CON MAMA DICE QUE VA A INVESTIGAR BUSCAR EN DIRECCION DE MAMA****30-07-2019* Pago: 0*Saldo: 0*Reporte: OSWALDO UBICA SU DOMICILIO SE TIENE QUE PASAR A VISITAR A SU MAMA ELLA QUEDO DE PAGAR EN 15 DIAS URGE BUSCAR****16-07-2019* Pago: 0*Saldo: 0*Reporte: SE PLATICO CON DELEGADO QUE NO LA HA PODIDO BUSCAR QUE SI LA V**"},{"cuenta":"2178","0":"2178","nombre":"MARIA DANIELA MARTINEZ ALVAREZ","1":"MARIA DANIELA MARTINEZ ALVAREZ","telefono":"0","2":"0","dias":"28","3":"28","grado":"VENDEDORA","4":"VENDEDORA","credito":"1400","5":"1400","estado":"ACTIVO","6":"ACTIVO","latitud_fija":"20.479807339222255","7":"20.479807339222255","longitud_fija":"-99.93879139981577","8":"-99.93879139981577","adeudo_cargo":"573","9":"573","piezas_cargo":"0","10":"0","importe_cargo":"0","11":"0","fecha_vence_cargo":"16-07-2019","12":"16-07-2019","puntos_disponibles":"0","13":"0","reporte_agente":"QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR","14":"QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR","reporte_administracion":"0","15":"0","mercancia_cargo":"0","16":"0","total_pagos":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR****13-08-2019* Pago: 0*Saldo: 0*Reporte: QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR****31-07-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINEROQUE IVA A CONSEGUIR QUE PASARAMOS EN LA TARDE PERO NO CONSIGUIO QUE EN 15 DIAS****30-07-2019* Pago: 0*Saldo: 0*Reporte: PASA OSWALDO QUE NO TENIA DINERO QUEDA DE LIQUIDAR EN 15 DIAS EXIGIR SU PAGO****16-07-2019* Pago: 300*Saldo: 573*Reporte: NO LE LIQUIDARON LAS CLIENTAS**","17":"**27-08-2019* Pago: 0*Saldo: 0*Reporte: QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR****13-08-2019* Pago: 0*Saldo: 0*Reporte: QUE SE ENFERMO SU HIJO Y QUE NO TENIA DINERO QUEDA DE LIQUIDAR****31-07-2019* Pago: 0*Saldo: 0*Reporte: QUE NO TENIA DINEROQUE IVA A CONSEGUIR QUE PASARAMOS EN LA TARDE PERO NO CONSIGUIO QUE EN 15 DIAS****30-07-2019* Pago: 0*Saldo: 0*Reporte: PASA OSWALDO QUE NO TENIA DINERO QUEDA DE LIQUIDAR EN 15 DIAS EXIGIR SU PAGO****16-07-2019* Pago: 300*Saldo: 573*Reporte: NO LE LIQUIDARON LAS CLIENTAS**"}],"fechaClientesConsulta":"27-08-2019"}]


            //necesitamos saber el tamaño del primer array, si es 1 significa que solo viene una ruta si es 2 es que vienen 2 rutas
            String zona1 = "";
            String fecha1 = "";
            String zona2 = "";
            String fecha2 = "";
            if(jsonArrayFechaClientes.length()==1){
                zona1 = jsonArrayFechaClientes.getJSONObject(0).getString("zona");
                fecha1 = jsonArrayFechaClientes.getJSONObject(0).getString("fechaClientesConsulta");
                zona2 = "";
                fecha2 = "";

            }else if (jsonArrayFechaClientes.length()==2){
                zona1 = jsonArrayFechaClientes.getJSONObject(0).getString("zona");
                fecha1 = jsonArrayFechaClientes.getJSONObject(0).getString("fechaClientesConsulta");
                zona2 = jsonArrayFechaClientes.getJSONObject(1).getString("zona");
                fecha2 = jsonArrayFechaClientes.getJSONObject(1).getString("fechaClientesConsulta");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        setFechaInicioSesion(activity,utilidadesApp.dameFehaHora());
        setDiaInicioSesion(activity,utilidadesApp.dameFecha());
    }

    public static void cerrarSesion (Activity activity){

        deleteNombreEmpleado(activity);
        deleteUsuarioIniciado(activity);
        deleteEstadoSesion(activity);
        deleteFechaInicioSesion(activity);
        deleteDiaInicioSesion(activity);

    }













}
