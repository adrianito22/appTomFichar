package com.tiburela.android.controlAsistencia.demo.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tiburela.android.controlAsistencia.demo.R;
import com.tiburela.android.controlAsistencia.demo.models.Empleado;

import java.util.ArrayList;


public class AdapterEmpleado extends RecyclerView.Adapter<AdapterEmpleado.MyViewHolder>  implements   View.OnClickListener  {
    private static ClickListener clickListener;
    private View.OnClickListener listener;

    private LayoutInflater inflater;
    public  ArrayList<Empleado> listEmpleado;


    private Context ctx;

    public AdapterEmpleado(Context ctx, ArrayList<Empleado> listEmpleado) {

        inflater = LayoutInflater.from(ctx);
        this.listEmpleado = listEmpleado;
        this.ctx = ctx;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_empleado, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }






    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


            holder.txtName.setText(listEmpleado.get(position).getNombreYapellidoEmpleado());
             holder.txtCodigoMarcacion.setText(listEmpleado.get(position).getCodigoPaFichar());

             dowloadAndSetImg(listEmpleado.get(position).getUrlPickEmpleado(),holder.imageView,ctx);


    }



    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public int getItemCount() {
        return listEmpleado.size();
    }

    @Override
    public void onClick(View view) {


        if (listener!=null){
            listener.onClick(view);
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName,txtCodigoMarcacion;
        private LinearLayout linearLayout;
        private ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtName =  itemView.findViewById(R.id.txtName);
            txtCodigoMarcacion=itemView.findViewById(R.id.txtCodigoMarcacion);
            linearLayout =itemView.findViewById(R.id.linearLayout);
            imageView=itemView.findViewById(R.id.imageView);

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
        AdapterEmpleado.clickListener = clickListener;


    }
/*
    private void dowloadAndSetImg(ImagenReport imagenReport, ImageView holder, Context context){


        storageRef  = StorageData.rootStorageReference.child("imagenes_all_reports/"+imagenReport.getUniqueIdNamePic());

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {



                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)  //ESTABA EN ALL         //ALL or NONE as your requirementDiskCacheStrategy.DATA
                        //.thumbnail(Glide.with(OfertsAdminActivity.context).load(R.drawable.enviado_icon))
                        //.error(R.drawable.)
                        //aqi cargamos una version lower

                        .into(holder);



                //  imagenReport.setUrlStoragePic(uri.toString());

                //  ImagenReport.hashMapImagesData.put(imagenReport.getUniqueIdNamePic(),imagenReport);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("ladtastor","es un fallo y es "+exception.getMessage());

                try{

                    //   Glide.with(ActivitySeeReports.context)
                    //.load(R.drawable.acea2)
                    // .fitCenter()
                    // .into(holder.imgViewLogoGIFTc);


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });






    }
*/

    private void dowloadAndSetImg(String url, ImageView holder, Context context){

        Glide.with(context)
                .load(url)

                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)  //ESTABA EN ALL         //ALL or NONE as your requirementDiskCacheStrategy.DATA
                //.thumbnail(Glide.with(OfertsAdminActivity.context).load(R.drawable.enviado_icon))
                //.error(R.drawable.)
                //aqi cargamos una version lower
                .apply(RequestOptions.circleCropTransform())

                .circleCrop()
                .into(holder);




    }




}