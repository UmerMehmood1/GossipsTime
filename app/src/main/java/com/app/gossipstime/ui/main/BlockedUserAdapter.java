package com.app.gossipstime.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.gossipstime.R;
import com.app.gossipstime.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.MyViewHolder> {

    private List<UserModal> itemList;
    private Context context;

    public BlockedUserAdapter(Context context, List<UserModal> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModal model = itemList.get(position);
        holder.setuserinshowblocked(model.getName(),model.getAbout(),model.getUrl(),model.getUid());
        Log.d("onBindViewHolder", "onBindViewHolder: ["+model.getName()+","+model.getPhone()+","+model.getUrl()+","+model.getUid()+"]");
        holder.nametc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.unblockuser(model.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class MyViewHolder  extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nametc,abouttv;
        LinearLayout ll1,ll2;
        DatabaseReference blockref,blockrefreceiver;
        CardView cv;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_userItem);
            nametc = itemView.findViewById(R.id.nametv_useritem);
            abouttv = itemView.findViewById(R.id.abouttv_useritem);
            ll1 = itemView.findViewById(R.id.useritemll1);
            ll2 = itemView.findViewById(R.id.useritem_ll2);
            cv = itemView.findViewById(R.id.cardViewuiv);
        }


        public void setuserinshowblocked(String name,String about,String url,String uid){
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

        }
        private void unblockuser(String uid) {

            blockref = database.getReference("Block list").child(FirebaseAuth.getInstance().getUid());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("UnBlock User").setMessage("Are you sure Unblock").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    blockref.child(uid).removeValue();
                    Toast.makeText(context, "User Unblocked ", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("No ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.create();
            builder.show();

        }
    }

}

