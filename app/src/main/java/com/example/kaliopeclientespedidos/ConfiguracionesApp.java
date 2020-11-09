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

    private static final String NOMBRE_ARCHIVO_CONFIGURACIONES = "configuracionesKaliopeClientes";

    private static final String MOSTRAR_AVISO_COMO_INVITADO = "avisoComoInvitado";
    private static final String ENTRADA_COMO_INVITADO = "entradaComoInvitado";
    private static final String MANTENER_SESION_INICIADA = "mantenerSesionIniciada";
    private static final String NOMBRE_USUARIO_INICIADO = "usuarioIniciado";
    private static final String NOMBRE_COMPLETO_CLIENTE = "nombreCompletoCliente";
    private static final String NOMBRE_CUENTA_CLIENTE = "cuentaCliente";
    private static final String NOMBRE_ESTADO_SESION = "estadoDeLaSesion";

    private static final String NOMBRE_CODIGO_DISPOSITIVO_UNICO_UUID = "codigoDeDispositivo";

    /*
    Por ejemplo cuando se conecte por primera vez se descarga la informacion principal de todos los productos
    pero solo de los codigos que son distintos,
     */
    private static final String JSON_OFFLINE_INFORMACION_GENERAL_POR_CODIGO = "infoGeneral";






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
    private static void setEntradaComoInvitado(Activity activity, boolean valor){

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
    private static void setUsuarioIniciado(Activity activity, String valor){

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

    private static void setNombreCliente(Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_COMPLETO_CLIENTE,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static String getNombreCliente(Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_COMPLETO_CLIENTE,"SinValor");
    }
    private static void deleteNombreCliente(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_COMPLETO_CLIENTE).apply();

    }

    private static void setCuentaCliente(Activity activity, String valor){

        SharedPreferences preferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);//para que solo la app kaliope pueda leer o escribir sobre el archivo


        SharedPreferences.Editor editor = preferences.edit(); // Declaramos una variable (objeto) de tipo SharedPreferences.Editor, necesario para guardar cambios en el fichero de preferencias.
        editor.putString(NOMBRE_CUENTA_CLIENTE,valor);
        //editor.commit();//guardamos nuestro fichero
        editor.apply();

    }
    public static String getCuentaCliente(Activity activity){
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES, Context.MODE_PRIVATE);


        //(Por último llamamos al método "getString" para leer el valor de la preferencia "numeroRutaAsignada"
        // , si no encuentra este valor devolverá el valor por defecto noAsignado. para saber que no se encontro

        return sharedPreferences.getString(NOMBRE_CUENTA_CLIENTE,"SinValor");
    }
    private static void deleteCuentaCliente(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(NOMBRE_ARCHIVO_CONFIGURACIONES,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(NOMBRE_CUENTA_CLIENTE).apply();

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

















    //(Metodo para obtener el nombre cordo del empleado esto porque es el que aparecera en los mensajes de los clientes cuando dicten el audio de voz
    // lo que pasa es que si usamos el nombre de usuario, este podra tener numeros tambien para diferenciarlo de otros usuarios con el mismo nombre
    // por ejemplo gustavo9411, y nimodo que los cleintes digan yo maria entrego a gustavo9411, tampoco podemos usar su nombre completo
    // para eso mejor usaremos su nombre completo pero crearemos un metodo que cortara el nombre completo hasta el primer espacio, y eso retonrnaremos)
    public static String getNombreCorto(Activity activity){

        String nombreCompleto = getNombreCliente(activity);
        String nombreCorto = nombreCompleto.substring(0,nombreCompleto.indexOf(' '));
        return nombreCorto;

    }



    public static void iniciarSesion(Activity activity, String nombreCompleto, String usuario, String numeroCuenta){

        setNombreCliente(activity,nombreCompleto);
        setUsuarioIniciado(activity, usuario);
        setCuentaCliente(activity, numeroCuenta);

    }


    public static void cerrarSesion (Activity activity){

        deleteNombreCliente(activity);
        deleteUsuarioIniciado(activity);
        deleteEstadoSesion(activity);
        deleteCuentaCliente(activity);

    }













}
