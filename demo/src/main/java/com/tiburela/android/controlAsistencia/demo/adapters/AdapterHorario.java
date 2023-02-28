package com.tiburela.android.controlAsistencia.demo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.Utils.Utils;
import com.tiburela.android.controlAsistencia.demo.models.HorarIosTrabajos;

import java.util.ArrayList;


public class AdapterHorario extends RecyclerView.Adapter<AdapterHorario.MyViewHolder>  implements   View.OnClickListener  {
    private static ClickListener clickListener;
    private View.OnClickListener listener;

    private LayoutInflater inflater;
    public  ArrayList<HorarIosTrabajos> listHorarIosTrabajos;


    private Context ctx;

    public AdapterHorario(Context ctx, ArrayList<HorarIosTrabajos> listHorarIosTrabajos) {

        inflater = LayoutInflater.from(ctx);
        this.listHorarIosTrabajos = listHorarIosTrabajos;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_horario_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //aqui vamos a setear los dias.. selecioandos





//        private TextView txtNombreHorario,txtHorarioDesdeHasta,txtNumsEmpleados;

             holder.imgViewEditHorario.setTag(listHorarIosTrabajos.get(position).getIdHorarioHereKEYpreferences());
            holder.txtNombreHorario.setText(listHorarIosTrabajos.get(position).getHorarioNombre());
            holder.txtHorarioDesdeHasta.setText
                    (listHorarIosTrabajos.get(position).getHoraraEntradaString()+" - "+listHorarIosTrabajos.get(position).getHoraraSalidadaString());
       //     holder.txtNumsEmpleados.setText(listHorarIosTrabajos.get(position).getNombreYapellidoHorarIosTrabajos());
         //   holder.txtNumsEmpleados.setText(listHorarIosTrabajos.get(position).getCodigoPaFichar());

        TextView [] arraTextView={

                holder.txtLunes,holder.txtMartes,holder.txtMiercoles,holder.txtJueves,holder.txtViernes,holder.txtSabado,holder.txtDomingo

        };

        for(TextView txtb: arraTextView){

            if(Utils.mihasWhitMhorariosAll.get(listHorarIosTrabajos.get(position).getIdHorarioHereKEYpreferences()).containsKey(String.valueOf(txtb.getTag()))){
                txtb.setTextColor(Color.parseColor("#A4FFFFFF"));
                txtb.setBackground(ContextCompat.getDrawable(  ctx, R.drawable.back_dia_selected));

            }
        }

    }



    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public int getItemCount() {
        return listHorarIosTrabajos.size();
    }

    @Override
    public void onClick(View view) {


        if (listener!=null){
            listener.onClick(view);
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNombreHorario,txtHorarioDesdeHasta,txtNumsEmpleados;
        ///LOS dias de la semana

        private TextView txtLunes,txtMartes,txtMiercoles,txtJueves,txtViernes,txtSabado,txtDomingo;


        private ImageView imgViewEditHorario;



        public MyViewHolder(View itemView) {
            super(itemView);
              txtLunes=  itemView.findViewById(R.id.txtLunes);
              txtMartes=  itemView.findViewById(R.id.txtMartes);
              txtMiercoles=  itemView.findViewById(R.id.txtMiercoles);
              txtJueves=  itemView.findViewById(R.id.txtJueves);
              txtViernes=  itemView.findViewById(R.id.txtViernes);
              txtSabado=  itemView.findViewById(R.id.txtSabado);
              txtDomingo=  itemView.findViewById(R.id.txtDomingo);




            txtNombreHorario =  itemView.findViewById(R.id.txtNombreHorario);
            txtHorarioDesdeHasta=itemView.findViewById(R.id.txtHorarioDesdeHasta);
            txtNumsEmpleados=itemView.findViewById(R.id.txtNumsEmpleados);

            imgViewEditHorario =itemView.findViewById(R.id.imgViewEditHorario);

            imgViewEditHorario.findViewById(R.id.imgViewEditHorario).setOnClickListener(new View.OnClickListener() {
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
        AdapterHorario.clickListener = clickListener;


    }





}