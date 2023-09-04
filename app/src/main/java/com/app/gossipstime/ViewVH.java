package com.app.gossipstime;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewVH extends RecyclerView.ViewHolder {

    ImageView iv;
    TextView nametv, timetv;



    public ViewVH(@NonNull View itemView) {



        super(itemView);

    }

    public  void  setUser(Application application , String name, String url, String time, String uid){
        //pas parameter
        iv = itemView.findViewById(R.id.ivviewbs_item);
        timetv = itemView.findViewById(R.id.timetvViewbs_item);
        nametv = itemView.findViewById(R.id.namestatus_tv_item);

        nametv.setText(name);
        timetv.setText(time);
        Picasso.get().load(url).into(iv);


    }
}
