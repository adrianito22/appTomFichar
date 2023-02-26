package com.google.android.cameraview.demo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.models.Fichar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class AdapterMarcacione extends RecyclerView.Adapter<AdapterMarcacione.MyViewHolder>  implements   View.OnClickListener  {
    private static ClickListener clickListener;
    private View.OnClickListener listener;
    private LayoutInflater inflater;
    public  ArrayList<Fichar> listPromedioAsistenceFichar;
    private int minutos;
    private int hora;
    private String dia;

    private String horasTrabajadas;



  private  DateFormat dateFormat = new SimpleDateFormat("dd");
    private  DateFormat dateFormatHorasTrabajadas = new SimpleDateFormat("hh:mm");


    private Context ctx;

    public AdapterMarcacione(Context ctx, ArrayList<Fichar> listPromedioAsistenceFichar) {

        inflater = LayoutInflater.from(ctx);
        this.listPromedioAsistenceFichar = listPromedioAsistenceFichar;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



     //   if(Utils.tipodeDatoMostrar==Utils.ITEM_MARACIONES_MODO){
            View view = inflater.inflate(R.layout.item_marcacion, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            return holder;

     //   }

        /*
        else{
            View view = inflater.inflate(R.layout.item_asistencia_dias, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            return holder;


        }

*/

    }






    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


         Fichar ficharObjec=listPromedioAsistenceFichar.get(position);

        dia = dateFormat.format(ficharObjec.getEntradaMilliseconds());
        holder.txtNameEmpleado.setText(dia);


        minutos = (int) ((ficharObjec.getEntradaMilliseconds() / (1000*60)) % 60);
        hora = (int) ((ficharObjec.getEntradaMilliseconds() / (1000*60*60)) % 24);
        holder.txtEntradaHora.setText(hora +":"+ minutos);

        if(ficharObjec.getHoraSalidaMilliseconds()>0){

            minutos = (int) ((ficharObjec.getEntradaMilliseconds() / (1000*60)) % 60);
            hora = (int) ((ficharObjec.getEntradaMilliseconds() / (1000*60*60)) % 24);
            holder.txtEntradaHora.setText(hora +":"+ minutos);



            minutos = (int) ((ficharObjec.getHoraSalidaMilliseconds() / (1000*60)) % 60);
            hora = (int) ((ficharObjec.getHoraSalidaMilliseconds() / (1000*60*60)) % 24);
            holder.txtHoraSalida.setText(hora +":"+ minutos);

            horasTrabajadas=dateFormatHorasTrabajadas.format(ficharObjec.getHoraSalidaMilliseconds()- ficharObjec.getEntradaMilliseconds());
            holder.txtTotalHoras.setText(horasTrabajadas+"h");




        }

        else

        {

            holder.txtHoraSalida.setText("----");
            holder.txtTotalHoras.setText("____");


        }



    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public int getItemCount() {
        return listPromedioAsistenceFichar.size();
    }

    @Override
    public void onClick(View view) {


        if (listener!=null){
            listener.onClick(view);
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNameEmpleado,txtEntradaHora,txtHoraSalida,txtTotalHoras;
        private LinearLayout linearLayout;



        public MyViewHolder(View itemView) {
            super(itemView);

            txtNameEmpleado =  itemView.findViewById(R.id.txtNameEmpleado);
            txtEntradaHora=itemView.findViewById(R.id.txtEntradaHora);
            txtHoraSalida=itemView.findViewById(R.id.txtHoraSalida);
            txtTotalHoras=itemView.findViewById(R.id.txtTotalHoras);

            linearLayout =itemView.findViewById(R.id.layotItem);

            linearLayout.findViewById(R.id.layotItem).setOnClickListener(new View.OnClickListener() {
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
        AdapterMarcacione.clickListener = clickListener;


    }





}