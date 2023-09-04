package com.app.gossipstime;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StatusVH extends RecyclerView.ViewHolder {
    ImageView statusiv;
    TextView nametv, timetv;
    String urlresult,deleteresult,timeresult;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference statusref,laststatus,blockref,blockrefreceiver;
    LinearLayout ll_ss, ll_ss2;

    public StatusVH(@NonNull View itemView) {
        super(itemView);

    }

    public void fetchStatus(FragmentActivity application, String time,
                            String lastm,
                            String name,
                            String url,
                            String uid){

        statusref = database.getReference("Status");
        laststatus = database.getReference("laststatus");

        ll_ss = itemView.findViewById(R.id.ll_status_item1);
        ll_ss2 = itemView.findViewById(R.id.ll_status_item2);
        statusiv = itemView.findViewById(R.id.iv_mystatus_item);
        nametv = itemView.findViewById(R.id.namestatus_tv_item);
        timetv = itemView.findViewById(R.id.time_tvstatus_item);


        statusref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(uid)){
                    ll_ss.setVisibility(View.VISIBLE);
                    statusref.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                urlresult = snapshot1.child("image").getValue().toString();
                                timeresult = snapshot1.child("time").getValue().toString();
                                deleteresult = snapshot1.child("delete").getValue().toString();

                                Picasso.get().load(urlresult).into(statusiv);
                                timetv.setText(timeresult);
                                statusiv.setPadding(0,0,0,0);
                                nametv.setText(name);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {

                    ll_ss.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll_ss.setVisibility(View.VISIBLE);
                    ll_ss2.setVisibility(View.VISIBLE);
                }else {

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
                    ll_ss.setVisibility(View.VISIBLE);
                    ll_ss2.setVisibility(View.VISIBLE);
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
