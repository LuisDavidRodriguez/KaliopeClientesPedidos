package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kaliopeDavid.kaliopeclientespedidos.ConfiguracionesApp;
import com.kaliopeDavid.kaliopeclientespedidos.KaliopeServerClient;
import com.kaliopeDavid.kaliopeclientespedidos.R;
import com.kaliopeDavid.kaliopeclientespedidos.utilidadesApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolderCarrito> {

   JSONObject jsonObjectTotales;
   JSONObject jsonObjectInformacion;                      //"info":{"estatus":"FAIL","MENSAJE":"Tu carrito esta vacio"}---info":{"estatus":"FAIL","MENSAJE":"Tu ultimo pedido ha sido finalizado el dia 24-08-2021, agrega a tu carrito para crear un nuevo pedido!"}---"info":{"estatus":"EXITO","MENSAJE":"Carrito encotrado"}

    private JSONObject mensajesFinalTotalesBreves;          //lo usaremos solo para guardar los mensajes del ultimo balance total que se mostrara "mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}
    //aunque esos 2 de arriba almacenan casi lo mismo y se pudo haber hecho con una sola variable, queria que estuviera un poco mas ordenado,
    //porque una de ellas solo contiene los mensajes que se necesitan para mostrar el balance final, y la otra contiene
    //todos los mensajes que necesitan las preconfirmaciones los dialogos
    //como ya cada que se elimina o modifica un producto se debe mostrar el balance final no queria enviar tanta informacion
    //enviando la cadena larga

    Activity activity;

   CarritoAdapter carritoAdapter;                   //necesitamos una referencia del adaptador que lista los productos del carrito para poder enviarle desde auqi la nueva lista de productos confirmados y notificarle el cambio al adaptador

   ProgressDialog progressDialog;



    public TotalAdapter(JSONObject jsonObjectTotales,JSONObject jsonObjectInformacion,JSONObject mensajesBalanceFinal, Activity activity) {
        this.jsonObjectTotales = jsonObjectTotales;
        this.jsonObjectInformacion = jsonObjectInformacion;
        this.mensajesFinalTotalesBreves = mensajesBalanceFinal;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_carrito_totales,
                parent,
                false);

        return new ViewHolderCarrito(view);
    }

    public void cambiarJsonObject(JSONObject totales, JSONObject mensajesFinalTotales){
        jsonObjectTotales = totales;
        this.mensajesFinalTotalesBreves = mensajesFinalTotales;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCarrito holder, final int position) {


//         //D/totales: {"nombre":"EVA MONDRAGON RIVAS",
//         "cuenta":"2070",
//         "limite_credito":"2400",
//         "grado":"SOCIA",
//         "dias":"28",
//         "ruta":"ACAMBAY",
//         "porcentaje_apoyo_empresa":"0.5",
//         "porcentaje_pago_cliente":"0.5",
//         "fecha_entrega":"2021-05-17",
//         "fecha_pago_del_credito":"14-06-2021",
//         "suma_cantidad":8,
//         "suma_credito":8,
//         "suma_inversion":0,
//         "suma_productos_etiqueta":1622,
//         "suma_productos_inversion":0,
//         "suma_productos_credito":1179,
//         "suma_ganancia_cliente":443,
//         "diferencia_credito":-1221,
//         "cantidad_pagar_cliente_credito":589.5,
//         "cantidad_financiar_empresa":589.5,
//         "pago_al_recibir":589.5,
//         "mensaje_diferencia_credito":"Aun dispones de $1221 en tu credito Kaliope","mensaje_todo_inversion":"Si pagaras todo tu pedido en Inversion ganarias $560","mensaje_resumido_puntos":" + 200 puntos Kaliope","mensaje_completo_puntos":"Tambien has ganado 200 puntos Kaliope, recueda que estos puntos se validaran con tu agente Kaliope y seran solo si realizas los pagos completos de este pedido.","mensaje_cantidad_sin_confirmar":"Tienes 7 productos sin confirmar, envialos!"}
// no usamos la variable posicion porque aqui solo habra un item totales
        try {


            holder.cuentaCliente.setText(jsonObjectTotales.getString("cuenta"));
            holder.nombreCliente.setText(jsonObjectTotales.getString("nombre"));
            holder.limiteCredito.setText(jsonObjectTotales.getString("limite_credito"));
            holder.gradoCliente.setText(jsonObjectTotales.getString("grado"));
            holder.diasCredito.setText(jsonObjectTotales.getString("dias"));
            holder.zona.setText(jsonObjectTotales.getString("ruta"));
            holder.fechaEntrega.setText(jsonObjectTotales.getString("fecha_entrega"));
            holder.piezasTotales.setText(jsonObjectTotales.getString("suma_cantidad"));
            holder.piezasCredito.setText(jsonObjectTotales.getString("suma_credito"));
            holder.importeCredito.setText(jsonObjectTotales.getString("suma_productos_credito"));
            holder.piezasInversion.setText(jsonObjectTotales.getString("suma_inversion"));
            holder.importeInversion.setText(jsonObjectTotales.getString("suma_productos_inversion"));
            holder.gananciaTotal.setText(jsonObjectTotales.getString("suma_ganancia_cliente"));
            holder.mensajeLimiteCredito.setText(jsonObjectTotales.getString("mensaje_diferencia_credito"));
            holder.mensajeTodoInversion.setText(jsonObjectTotales.getString("mensaje_todo_inversion"));





            //Dependiendo de si la cantidad es 0 entonces mostramos la imagen de la bolsita triste y mandamos el mensaje que nos muestra el server si no mostramos los totales
            if(jsonObjectInformacion.getString("estatus").equals("FAIL")){
                holder.constraintLayoutCarritoVacio.setVisibility(View.VISIBLE);
                holder.mensajeBolsaVacia.setText(jsonObjectInformacion.getString("MENSAJE"));           //ponemos el mensaje que el servidor nos envia
                holder.constraintLayoutTotales.setVisibility(View.GONE);
            }else{
                holder.constraintLayoutCarritoVacio.setVisibility(View.GONE);
                holder.constraintLayoutTotales.setVisibility(View.VISIBLE);
            }









            //-----------------------Añadimos el llenado de la vista de dialogoFinalesTotales es una copia de ese metodo colo hay que añadir holder.
                holder.titulo.setText(mensajesFinalTotalesBreves.getString("18"));
                holder.difCredito.setText(jsonObjectTotales.getString("diferencia_credito"));
                holder.difCreditoMessage.setText(mensajesFinalTotalesBreves.getString("19"));
                holder.pedidoPagoCredito.setText(jsonObjectTotales.getString("cantidad_pagar_cliente_credito"));
                holder.pagoCreditoMessage.setText(mensajesFinalTotalesBreves.getString("20"));
                holder.pagoInversion.setText(jsonObjectTotales.getString("suma_productos_inversion"));
                holder.pagoInversionMessage.setText(mensajesFinalTotalesBreves.getString("21"));
                holder.pagoTotal.setText(jsonObjectTotales.getString("pago_al_recibir"));
                holder.pagoTotalMessage.setText(mensajesFinalTotalesBreves.getString("22"));
                holder.cantidadFinanciada.setText(jsonObjectTotales.getString("cantidad_financiar_empresa"));
                holder.cantidadFinanciadaMessage.setText(mensajesFinalTotalesBreves.getString("23"));

                //VAMOS A CONTROLAR SI DEBEN SER VISIBLES CIERTAS SUMAS
                //recordar que diferencia puede venir en negativo significando que aun tiene credito disponible
                float diferencia = Float.parseFloat(jsonObjectTotales.getString("diferencia_credito"));
                if(diferencia<0){
                    holder.difCredito.setText("0");
                    holder.difCreditoSignoPesos.setVisibility(View.GONE);
                    holder.difCredito.setVisibility(View.GONE);
                    holder.difCreditoMessage.setVisibility(View.GONE);
                }else{
                    holder.difCreditoSignoPesos.setVisibility(View.VISIBLE);
                    holder.difCredito.setVisibility(View.VISIBLE);
                    holder.difCreditoMessage.setVisibility(View.VISIBLE);
                }

                //si hay una cantidad por financiar de la emrpesa, o si el cliente no tiene prendas a credito no mostramos este mensaje
                float financiarEmpresa = Float.parseFloat(jsonObjectTotales.getString("cantidad_financiar_empresa"));
                if(financiarEmpresa>0){
                    holder.cantidadFinaSignoPesos.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.VISIBLE);
                }else{
                    holder.cantidadFinaSignoPesos.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.INVISIBLE);
                }
             //---------------------- fin










        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount() {
        return 1;           //ensimamos a 1 el total de items porque solo se desplegara un solo total
    }


    public void setCarritoAdapterReferencia(CarritoAdapter carritoAdapter){
        this.carritoAdapter = carritoAdapter;
    }



    private void showProgresDialog(Activity activity){

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Conectando al Servidor");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }





    public class ViewHolderCarrito extends RecyclerView.ViewHolder {

        TextView cuentaCliente,
        nombreCliente,
        limiteCredito,
        gradoCliente,
        diasCredito,
        zona,
        fechaEntrega,
        piezasTotales,
        piezasCredito,
        importeCredito,
        piezasInversion,
        importeInversion,
        gananciaTotal,
        mensajeLimiteCredito,
        mensajeTodoInversion;

        TextView mensajeBolsaVacia;



        ConstraintLayout constraintLayoutTotales;
        ConstraintLayout constraintLayoutCarritoVacio;








        //------------------------------------Con el include de la vista del dialogo totales al entregar pedido tenemos que poner los campos aqui
        TextView titulo;

        TextView difCreditoSignoPesos;
        TextView difCredito;
        TextView difCreditoMessage;

        TextView pagoCreditoSignoPesos;
        TextView pedidoPagoCredito;
        TextView pagoCreditoMessage;

        TextView pagoInversionSignoPesos;
        TextView pagoInversion;
        TextView pagoInversionMessage;

        TextView pagoTotalSignoPesos;
        TextView pagoTotal;
        TextView pagoTotalMessage;

        TextView cantidadFinaSignoPesos;
        TextView cantidadFinanciada;
        TextView cantidadFinanciadaMessage;
        //-------------------------- basicamente se copio y pego del metodo dialogo confirmacion totales


        public ViewHolderCarrito(@NonNull View itemView) {
            super(itemView);

           cuentaCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_cuentaClienteTV);
           nombreCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_nombreClienteTV);
           limiteCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_limiteCreditoTV);
           gradoCliente = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_gradoTV);
           diasCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_diasTV);
           zona = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_nombreZonaTV);
           fechaEntrega = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_fechaEntregaTV);
           piezasTotales = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasTotalesTV);
           piezasCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasCreditoTV);
           importeCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_importeCreditoTV);
           piezasInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_piezasInversionTV);
           importeInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_importeInversionTV);
           gananciaTotal = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_gananciaTotalTV);
           mensajeLimiteCredito = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajeLimiteCreditoTV);
           mensajeTodoInversion = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajeTodoInversionTV);
           constraintLayoutTotales = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_constrainLayoutTotales);
           constraintLayoutCarritoVacio = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_layout_vacio);
           mensajeBolsaVacia = (TextView) itemView.findViewById(R.id.textView11);







           //----------------------------agregar imagen de totales finales
            titulo = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_titulo);

            difCreditoSignoPesos = (TextView) itemView.findViewById(R.id.textView15);
            difCredito = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_difCredito);
            difCreditoMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_difCreditoMessage);

            pagoCreditoSignoPesos = (TextView) itemView.findViewById(R.id.textView20);
            pedidoPagoCredito = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCredito);
            pagoCreditoMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoCreditoMessage);

            pagoInversionSignoPesos = (TextView) itemView.findViewById(R.id.textView23);
            pagoInversion = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversion);
            pagoInversionMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoInversionMessage);

            pagoTotalSignoPesos = (TextView) itemView.findViewById(R.id.textView33);
            pagoTotal = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotal);
            pagoTotalMessage = (TextView) itemView.findViewById(R.id.dialogo_confirmacion_pedido_5_pagoTotalMessage);

            cantidadFinaSignoPesos = (TextView) itemView.findViewById(R.id.textView35);
            cantidadFinanciada = (TextView) itemView.findViewById(R.id.cantidadFinanciada);
            cantidadFinanciadaMessage = (TextView) itemView.findViewById(R.id.cantidadFinanciadaMessage);


        }
    }


}
