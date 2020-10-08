package com.example.kaliopeclientespedidos;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


/**
 *Esta clase se agrega para corregir el error que creaba Glide en algunas ocaciones que no cargaba imagenes y mostraba el error
 *  W/Glide: Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored
 * W/Glide: Load failed for https://www.google.com/url?sa=i&url=https%3A%2F%2Funderc0de.org%2Fforo%2Fvideo-juegos%2Fdiviertete-con-google!-lista-de-los-juegos-de-google%2F&psig=AOvVaw0a4M-cUMjOJ6led8n5_yhq&ust=1602204216230000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCND2_fzho-wCFQAAAAAdAAAAABAD with size [263x263]
 *el Load failed for es el link de la imagen que intentabamos mostrar
 *
 *
 * El link
 * https://stackoverflow.com/questions/49901629/glide-showing-error-failed-to-find-generatedappglidemodule
 *
 * Whatafaka!!!!
 *
 * no entinendo porque funciono cambie por GlideAppsss en nombre de la clase
 * y dempronto esta siendo usada en
 * GeneratedAppGlideModuleImpl.java
 *
 * yo no lo coloque solo quice eliminar esta clase que hice pensando que no resolvia nada y deprondo marcaba que estaba siendo utilizada
 *
 * cabe destacar que a diferencia como dice el link de arriba no estoy llamando a
 * GlideApp.with cuando quiero cargar la imagen
 *
 * cambie el nombre porque marcaba que habia error que esta clase ya existia y si ya existia en una clase
 * de la libreria glide ya estaba declarada
 * como si la hubieran añadido despues
 *
 * pero si puedes llamar a GlideApp.with y funcionara
 *
 * o puedes llamar solo Glide.with
 *
 * si tratas de llamar a esta clase como dice el link por ejemplo
 * GlideAppsss.with indica que el metodo with no se puede resolver
 *
 *
 * OKAY PARECE QUE EL PROBLEMA ES MUY CLARO, CUANDO LO CORRO EN UN EMULADOR CON UNA API MENOR
 * NO HAY PROBLEMA CARGA LAS IMAGENES DEL SERVIDOR KALIOPE
 *
 * pero cuando cargo la app a mi cel que es api 29
 * marca este error
 *
 * W/Glide: Load failed for http://www.kaliope.com.mx/fotos/BD1547-BEIGE-1.jpg with size [263x263]
 *     class com.bumptech.glide.load.engine.GlideException: Failed to load resource
 *     There was 1 cause:
 *     java.io.IOException(Cleartext HTTP traffic to www.kaliope.com.mx not permitted)
 *      call GlideException#logRootCauses(String) for more detail
 *       Cause (1 of 1): class com.bumptech.glide.load.engine.GlideException: Fetching data failed, class java.io.InputStream, REMOTE
 *     There was 1 cause:
 *
 *
 *     el problema parece ser el ClearText HTTP
 *
 *     parece que a partir de la api 28 no se puden hacer peticiones http con un texto que sea facil de entender
 *     porque pone en riesgo la seguridad
 *
 *     en las apis anteriores habia un atributo que es  cleartextTrafficPermitted="true"
 *     por default era true en las nuebvas apis ahora por default es false
 *     parece que se puede corregir conectando a un https en lugar de http
 *
 *     o generando un archivo network_security_config.xml
 *     y apuntando a el por el manifest
 *
 *     https://medium.com/@imstudio/android-8-cleartext-http-traffic-not-permitted-73c1c9e3b803
 *
 *     https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
 */


@GlideModule
public class GlideAppsss extends AppGlideModule {
}
