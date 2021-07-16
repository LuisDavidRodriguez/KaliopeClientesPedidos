package com.kaliopeDavid.kaliopeclientespedidos.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kaliopeDavid.kaliopeclientespedidos.R;

import org.json.JSONObject;

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ViewHolderCarrito> {

   JSONObject jsonObjectTotales;
   JSONObject jsonObjectInformacion;
   /*
   "info":{"estatus":"EXITO","MENSAJE":{"bloqueado":0,"alerta":2,"mensaje":"Tienes hoy y mañana para editar tu carrito\n\nEn 2 dias sera enviado al almacen y ya no podras editar tu pedido"}},

   ATENCION EN CASO QUE EL CLIENTE NO TENGA CARRITO O SE RETORNE VACIO el campo info se configura de esta manera
   {"info":{"estatus":"FAIL","MENSAJE":{"mensaje":"¡Oh no!\nTu carrito aun esta vacio\nagrega productos a tu carrito"}}

    */
    private JSONObject mensajesFinalTotalesBreves;          //lo usaremos solo para guardar los mensajes del ultimo balance total que se mostrara "mensajesFinalTotales":{"18":"Al recibir tu pedido deberás liquidar al agente Kaliope:","19":"Exceso de credito","20":"0% credito","21":"Inversion","22":"por liquidar al recibir el pedido","23":"En crédito Kaliope fecha de pago "}}


    Activity activity;

   CarritoAdapter carritoAdapter;                   //necesitamos una referencia del adaptador que lista los productos del carrito para poder enviarle desde auqi la nueva lista de productos confirmados y notificarle el cambio al adaptador

   ProgressDialog progressDialog;

    Animation animationLatido;

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

    public void cambiarJsonObject(JSONObject totales, JSONObject mensajesFinalTotales, JSONObject jsonObjectInformacion){
        jsonObjectTotales = totales;
        this.mensajesFinalTotalesBreves = mensajesFinalTotales;

        if(jsonObjectInformacion!= null){
            this.jsonObjectInformacion = jsonObjectInformacion;
        }

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

            Log.d("TotalOnview","control1");
            //Dependiendo de si info es FAIL entonces mostramos la imagen de la bolsita triste y mandamos el mensaje que nos muestra el server si no mostramos los totales
            if (jsonObjectInformacion.getString("estatus").equals("FAIL")) {
                holder.mensajeBolsaVacia.setText(jsonObjectInformacion.getJSONObject("MENSAJE").getString("mensaje"));           //ponemos el mensaje que el servidor nos envia
                holder.constraintLayoutTotales.setVisibility(View.GONE);
                holder.constraintLayoutCarritoVacio.setVisibility(View.VISIBLE);
                Log.d("TotalOnview","MostrandoBOlsita");

            } else {
                //sino viene FAIL entonces viene exito

                boolean bloqueado = jsonObjectInformacion.getJSONObject("MENSAJE").getBoolean("bloqueado");
                String alerta = jsonObjectInformacion.getJSONObject("MENSAJE").getString("alerta");
                String mensajeBloqueo = jsonObjectInformacion.getJSONObject("MENSAJE").getString("mensaje");
                carritoAdapter.setBloqueo(bloqueado);

                //Si el mensaje del pedido llega como Pedido finalizado, quiero igual no mostrarlo
                //porque confunde mucho al cliente por ejemplo cuando tiene productos agregados
                //y cuando elimina productos, todos, la pantalla de totales recarga los totales del pedido anterior
                //y eso confunde. el servidor nos retornara Pedido finalizado y volveremos a mostrar la bolsita triste
                if(mensajeBloqueo.contains("Pedido Finalizado")){
                    holder.mensajeBolsaVacia.setText("Tu carrito esta vacio. Tienes otros pedidos anteriores puedes verlos aqui");           //ponemos el mensaje que el servidor nos envia
                    holder.constraintLayoutTotales.setVisibility(View.GONE);
                    holder.constraintLayoutCarritoVacio.setVisibility(View.VISIBLE);
                    holder.setOnClickListener();
                    holder.buttonHistorialPedidos.setVisibility(View.VISIBLE);
                    carritoAdapter.setOcultar(true);

                }else{
                    //mostramos los totales y recuperamos las variables de info para bloquear la edicion
                    holder.constraintLayoutCarritoVacio.setVisibility(View.GONE);
                    holder.constraintLayoutTotales.setVisibility(View.VISIBLE);
                    Log.d("TotalOnview","exito");
                }

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
                holder.mensajeBloqueo.setText(mensajeBloqueo);


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
                if (diferencia < 0) {
                    holder.difCredito.setText("0");
                    holder.difCreditoSignoPesos.setVisibility(View.GONE);
                    holder.difCredito.setVisibility(View.GONE);
                    holder.difCreditoMessage.setVisibility(View.GONE);
                } else {
                    holder.difCreditoSignoPesos.setVisibility(View.VISIBLE);
                    holder.difCredito.setVisibility(View.VISIBLE);
                    holder.difCreditoMessage.setVisibility(View.VISIBLE);
                }

                //si hay una cantidad por financiar de la emrpesa, o si el cliente no tiene prendas a credito no mostramos este mensaje
                float financiarEmpresa = Float.parseFloat(jsonObjectTotales.getString("cantidad_financiar_empresa"));
                if (financiarEmpresa > 0) {
                    holder.cantidadFinaSignoPesos.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.VISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.VISIBLE);
                } else {
                    holder.cantidadFinaSignoPesos.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciada.setVisibility(View.INVISIBLE);
                    holder.cantidadFinanciadaMessage.setVisibility(View.INVISIBLE);
                }
                //---------------------- fin

                //1 alta 2 media 3 baja 4 off
                if(alerta.equals("1")){
                    holder.setAlertaAlta();
                }
                if(alerta.equals("2")){
                    holder.setAlertaMedia();
                }
                if(alerta.equals("3")){
                    holder.setAlertaBaja();
                }
                if(alerta.equals("4")){
                    holder.setAlertaOff();
                }


            }


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
        mensajeTodoInversion,
        mensajeBloqueo,
        aviso;


        TextView mensajeBolsaVacia;
        Button buttonHistorialPedidos;



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
           mensajeBloqueo = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_mensajeBloqueoTV);
           aviso = (TextView) itemView.findViewById(R.id.item_container_carrito_totales_alertaBloqueoTV);
           constraintLayoutTotales = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_constrainLayoutTotales);
           constraintLayoutCarritoVacio = (ConstraintLayout) itemView.findViewById(R.id.item_container_carrito_totales_layout_vacio);
           buttonHistorialPedidos = (Button) itemView.findViewById(R.id.botonHistorialPedidos);
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


            animationLatido = AnimationUtils.loadAnimation(activity,R.anim.latido);
            animationLatido.setFillAfter(true);//para que se quede donde termina la anim
            animationLatido.setRepeatMode(Animation.REVERSE); //modo de repeticion, en el reverse se ejecuta la animacion y cuando termine de ejecutarse va  adar reversa
            animationLatido.setRepeatCount(Animation.INFINITE); //cuantas veces queremos que se repita la animacion, podria ser un numero entero 20 para 20 veces por ejemplo


        }


        void setAlertaAlta(){
            animationLatido.setRepeatCount(20);
            aviso.setVisibility(View.VISIBLE);
            aviso.setText("Importante");
            aviso.startAnimation(animationLatido);
            aviso.setTextColor(Color.RED);
        }

        void setAlertaMedia(){
            animationLatido.setRepeatCount(10);
            aviso.setVisibility(View.VISIBLE);
            aviso.setText("Aviso:");
            aviso.startAnimation(animationLatido);
            aviso.setTextColor(Color.GRAY);
        }

        void setAlertaBaja(){
            aviso.setText("Aviso:");
            aviso.setVisibility(View.VISIBLE);
            aviso.setTextColor(Color.BLACK);
            mensajeBloqueo.setTextColor(Color.GRAY);

        }

        void setAlertaOff(){

            aviso.setVisibility(View.INVISIBLE);
            aviso.setTextColor(Color.BLACK);
            mensajeBloqueo.setTextColor(Color.GRAY);
        }


        void setOnClickListener(){
            buttonHistorialPedidos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Historial", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


}
