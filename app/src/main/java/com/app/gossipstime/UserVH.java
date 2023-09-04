package com.app.gossipstime;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserVH  extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nametc,abouttv;
    LinearLayout ll1,ll2;
    DatabaseReference blockref,blockrefreceiver;
    CardView cv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public UserVH(@NonNull View itemView) {
        super(itemView);
    }

    public void setUser(Application application,String name,String phone,String  about,String url,String uid){

        imageView = itemView.findViewById(R.id.iv_userItem);
        nametc = itemView.findViewById(R.id.nametv_useritem);
        abouttv = itemView.findViewById(R.id.abouttv_useritem);
        ll1 = itemView.findViewById(R.id.useritemll1);
        ll2 = itemView.findViewById(R.id.useritem_ll2);
        cv = itemView.findViewById(R.id.cardViewuiv);
// errorr
        if (url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else{
            Picasso.get().load(url).into(imageView);
        }

        nametc.setText(name);

        if (about.equals("")){
            abouttv.setVisibility(View.GONE);
        }else {
            abouttv.setText(about);



        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        if (currentuid.equals(uid)){
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            nametc.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            abouttv.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
        }else {
            ll1.setVisibility(View.VISIBLE);
        }

        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    nametc.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    abouttv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                }else {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // here we will handle if other user blocked us
        blockrefreceiver = database.getReference("Block list");

        blockrefreceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(uid).hasChild(currentuid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    nametc.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    abouttv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                }else {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setuserinshowblocked(Application application,String name,String phone,String
            about,String url,String uid){
        imageView = itemView.findViewById(R.id.iv_userItem);
        nametc = itemView.findViewById(R.id.nametv_useritem);
        abouttv = itemView.findViewById(R.id.abouttv_useritem);
        ll1 = itemView.findViewById(R.id.useritemll1);
        ll2 = itemView.findViewById(R.id.useritem_ll2);
        cv = itemView.findViewById(R.id.cardViewuiv);
        if (url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else{
            Picasso.get().load(url).into(imageView);
        }
        nametc.setText(name);
        if (about.equals("")){
            abouttv.setVisibility(View.GONE);
        }else {
            abouttv.setText(about);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        if (currentuid.equals(uid)){
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            nametc.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            abouttv.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
        }else {
            ll1.setVisibility(View.VISIBLE);
        }

        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                }else {
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    nametc.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    abouttv.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
