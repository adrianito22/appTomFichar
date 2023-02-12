package com.google.android.cameraview.demo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.Utils.Utils;
import com.google.android.cameraview.demo.models.PromedioAsistenceEmpleado;

import java.util.ArrayList;


public class AdapterAsistencePromedio extends RecyclerView.Adapter<AdapterAsistencePromedio.MyViewHolder>  implements   View.OnClickListener  {
    private static ClickListener clickListener;
    private View.OnClickListener listener;

    private LayoutInflater inflater;
    public  ArrayList<PromedioAsistenceEmpleado> listPromedioAsistencePromedioAsistenceEmpleado;


    private Context ctx;

    public AdapterAsistencePromedio(Context ctx, ArrayList<PromedioAsistenceEmpleado> listPromedioAsistencePromedioAsistenceEmpleado) {

        inflater = LayoutInflater.from(ctx);
        this.listPromedioAsistencePromedioAsistenceEmpleado = listPromedioAsistencePromedioAsistenceEmpleado;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if(Utils.tipodeDatoMostrar==Utils.ITEM_MARACIONES_MODO){
            View view = inflater.inflate(R.layout.item_marcaciones_promedio, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            return holder;

        }else{
            View view = inflater.inflate(R.layout.item_asistencia_dias, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            return holder;


        }



    }






    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

            if(Utils.tipodeDatoMostrar==Utils.ITEM_MARACIONES_MODO){

                holder.txtNameEmpleado.setText(listPromedioAsistencePromedioAsistenceEmpleado.get(position).getEmpleadoName());
                holder.txtEntradaHora.setText(listPromedioAsistencePromedioAsistenceEmpleado.get(position).getEntradaPromedio());
                holder.txtHoraSalida.setText(listPromedioAsistencePromedioAsistenceEmpleado.get(position).getSalidaPromedio());




            }else{ //son dias de asitencia

                holder.txtNameEmpleado.setText(listPromedioAsistencePromedioAsistenceEmpleado.get(position).getEmpleadoName());
                holder.txtHoraSalida.setText(String.valueOf(listPromedioAsistencePromedioAsistenceEmpleado.get(position).getAsistenciaPromedio()));


            }


             /**condiciones pintamos en rojo los que salieron muy temprano y entraron un poco tarde de la hora selecionada..*/

             /*** CREAR SECCION QUE DIGA ESTABLCER HORARIOS UN FIEL DONSE SELECIONE HORA DE ENTRADA Y OTRO DE SALIDA Y DIAS LABORABLES...
              *MARCA Y SELECIONA SI ESE DIA ES ,MEDIO TIEMPO
              * //TAMBIEN OTRO PARA MEDIO TIEMPO SABADO / M TIME. d**/


             /**BASE DE DATOS......*/


    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public int getItemCount() {
        return listPromedioAsistencePromedioAsistenceEmpleado.size();
    }

    @Override
    public void onClick(View view) {


        if (listener!=null){
            listener.onClick(view);
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNameEmpleado,txtEntradaHora,txtHoraSalida;
        private LinearLayout linearLayout;



        public MyViewHolder(View itemView) {
            super(itemView);

            txtNameEmpleado =  itemView.findViewById(R.id.txtNameEmpleado);
            txtEntradaHora=itemView.findViewById(R.id.txtEntradaHora);
            txtHoraSalida=itemView.findViewById(R.id.txtHoraSalida);

            linearLayout =itemView.findViewById(R.id.linearLayout);

            linearLayout.findViewById(R.id.linearLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onItemClick(getAdapterPosition(), v);

                }
            });


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);

        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }
    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterAsistencePromedio.clickListener = clickListener;


    }





}