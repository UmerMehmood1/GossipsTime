package com.app.gossipstime;

import android.app.Application;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class GroupChatVH  extends RecyclerView.ViewHolder {

    TextView sunametv,runametv,smtv,rmtv;
    String currentuid;
    ImageView iv_r,iv_s;
    CardView cv_r,cv_s;

    public GroupChatVH(@NonNull View itemView) {
        super(itemView);
    }
    public void setgroupmessage(Application application, String sname, String message,
                                String time, String delete,
                                String seen, String search, String suid, String type,
                                String url){

        sunametv = itemView.findViewById(R.id.uname_m_items);
        runametv = itemView.findViewById(R.id.uname_m_itemr);
        smtv = itemView.findViewById(R.id.gm_items);
        rmtv = itemView.findViewById(R.id.gm_itemr);

        // image switcher
        iv_r = itemView.findViewById(R.id.iv_item_gcr);
        iv_s = itemView.findViewById(R.id.iv_item_gcs);


        cv_r = itemView.findViewById(R.id.cv_r_gc);
        cv_s = itemView.findViewById(R.id.cv_s_gc);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();

        String encodemessage = Decode.decode(message);

        if (suid.equals(currentuid)){

            // sender side

            switch (type){

                case "txt":
                    rmtv.setVisibility(View.GONE);
                    runametv.setVisibility(View.GONE);
                    smtv.setText(encodemessage);
                    sunametv.setText(sname);
                    sunametv.setVisibility(View.GONE);
                    cv_s.setVisibility(View.GONE);
                    cv_r.setVisibility(View.GONE);
                    break;

                case "img":
                    rmtv.setVisibility(View.GONE);
                    runametv.setVisibility(View.GONE);
                    smtv.setVisibility(View.GONE);
                    smtv.setText(encodemessage);
                    sunametv.setText(sname);
                    cv_s.setVisibility(View.VISIBLE);
                    cv_r.setVisibility(View.GONE);
                    iv_s.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(iv_s);
                    break;
            }

        }else  {

            switch (type){
                case  "txt":
                    sunametv.setVisibility(View.GONE);
                    smtv.setVisibility(View.GONE);
                    rmtv.setText(encodemessage);
                    runametv.setText(sname);
                    cv_s.setVisibility(View.GONE);
                    cv_r.setVisibility(View.GONE);
                    break;
                case "img":
                    sunametv.setVisibility(View.GONE);
                    smtv.setVisibility(View.GONE);
                    rmtv.setText(encodemessage);
                    rmtv.setVisibility(View.GONE);
                    runametv.setText(sname);
                    cv_r.setVisibility(View.VISIBLE);
                    cv_s.setVisibility(View.GONE);
                    iv_r.setVisibility(View.VISIBLE);
                    Picasso.get().load(url).into(iv_r);

                    break;

            }
            // receiver side

        }
    }
}
