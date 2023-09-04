package com.app.gossipstime;

import android.app.Application;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ListVH extends RecyclerView.ViewHolder {

    ImageView listiv,ivau;
    TextView nametv,timetv,seentv,lastmtv,mnotv;
    TextView nameTvau,abouttvAu;
    CheckBox checkBox;
    DatabaseReference sRef,rRef,seenstatus, blockref,blockrefreceiver;
    int mno , count;
    LinearLayout ll1,ll2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public ListVH(@NonNull View itemView) {
        super(itemView);
    }

    public void setList(FragmentActivity application, String time,
                        String lastm,
                        String name,
                        String url,
                        String uid){

        String decodemessage = Decode.decode(lastm);

        listiv = itemView.findViewById(R.id.iv_list);
        nametv = itemView.findViewById(R.id.nametv_list);
        timetv = itemView.findViewById(R.id.time_list);
        seentv = itemView.findViewById(R.id.seentv);
        lastmtv = itemView.findViewById(R.id.lastm_list);

        Picasso.get().load(url).into(listiv);
        nametv.setText(name);
        timetv.setText(time);
        seentv.setText("");
        lastmtv.setText(decodemessage);

        try {
            blockuser(uid);

        }catch (Exception e){

            Toast.makeText(application, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void blockuser(String uid) {

        ll1 = itemView.findViewById(R.id.listitemll1);
        ll2 = itemView.findViewById(R.id.listitemll2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        blockrefreceiver = database.getReference("Block list");

        blockrefreceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(uid).hasChild(currentuid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setListau(Application application, String time,
                          String lastm,
                          String name,
                          String url,
                          String uid){

        ivau = itemView.findViewById(R.id.iv_au);
        nameTvau = itemView.findViewById(R.id.nametv_au);
        abouttvAu = itemView.findViewById(R.id.abouttv_au);
        checkBox = itemView.findViewById(R.id.cb_au);

        DocumentReference dr ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        dr = db.collection("user").document(uid);

        dr.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                            String name = task.getResult().getString("name");
                            String about = task.getResult().getString("about");
                            String phoneno = task.getResult().getString("phone");
                            String url = task.getResult().getString("url");

                            if (url.equals("")){
                                nameTvau.setText(name);
                                abouttvAu.setText(about);
                            }else {
                                nameTvau.setText(name);
                                abouttvAu.setText(about);
                                Picasso.get().load(url).into(ivau);
                            }


                        }else {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        // disabling blocked user
        ll1 = itemView.findViewById(R.id.adduseritemll1);
        ll2 = itemView.findViewById(R.id.adduseritmell2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        blockrefreceiver = database.getReference("Block list");

        blockrefreceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(uid).hasChild(currentuid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void setListforward(Application application,String time,
                               String lastm,
                               String name,
                               String url,
                               String uid){

        String decodemessage = Decode.decode(lastm);

        listiv = itemView.findViewById(R.id.iv_listf);
        nametv = itemView.findViewById(R.id.nametv_listf);
        seentv = itemView.findViewById(R.id.seentvf);
        lastmtv = itemView.findViewById(R.id.lastm_listf);


        Picasso.get().load(url).into(listiv);
        nametv.setText(name);
        seentv.setText("Send");
        lastmtv.setText(decodemessage);

        ll1 = itemView.findViewById(R.id.listitemf1ll);
        ll2 = itemView.findViewById(R.id.listitemf2ll);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        blockref = database.getReference("Block list");

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(currentuid).hasChild(uid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        blockrefreceiver = database.getReference("Block list");

        blockrefreceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(uid).hasChild(currentuid)){
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void checksendermcount(String suid, String ruid) {

        seenstatus = database.getReference("seenstatus");

        mnotv = itemView.findViewById(R.id.m_notv);
        seentv = itemView.findViewById(R.id.seentv);

        seenstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(suid+ruid).hasChild("counts")){
                    seentv.setVisibility(View.VISIBLE);
                    seentv.setText("Sent");

                }else {

                    checkMNo(suid,ruid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void checkMNo(String suid,String ruid){

        seenstatus = database.getReference("seenstatus");

        mnotv = itemView.findViewById(R.id.m_notv);
        seentv = itemView.findViewById(R.id.seentv);

        seenstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(ruid+suid).hasChild("counts")){

                    int childrencount = (int) snapshot.child(ruid+suid).child("counts").getChildrenCount();
                    mnotv.setVisibility(View.VISIBLE);
                    if (childrencount > 0 ){
                        seentv.setVisibility(View.VISIBLE);
                        seentv.setText("New");
                    }else {

                    }

                    mnotv.setText(String.valueOf(childrencount));
                }else {
                    seentv.setVisibility(View.VISIBLE);
                    seentv.setText("Seen");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
