package com.app.gossipstime;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Fragment3 extends Fragment implements View.OnClickListener {


    TextView taptoaddtv, mystatustv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference statusref, chatlist;
    String uid , url , time ;
    Long delete;
    ImageView iv_mystatus;
    private Uri imaguri;
    RecyclerView recyclerView;
    FloatingActionButton fbadd;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment3,container,false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        taptoaddtv = getActivity().findViewById(R.id.taptoadd_tv);
        mystatustv = getActivity().findViewById(R.id.mystatus_tv);
        iv_mystatus = getActivity().findViewById(R.id.iv_mystatus);
        recyclerView = getActivity().findViewById(R.id.rv_f3);

        fbadd = getActivity().findViewById(R.id.addstatusfb);


        // change this laststatus to status
        statusref = database.getReference("Status");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        taptoaddtv.setOnClickListener(this);
        mystatustv.setOnClickListener(this);
        fbadd.setOnClickListener(this);

        // statusref.keepSynced(true);

        chatlist = database.getReference("chat list").child(uid);

        checkpermission();

        fetchStatus();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (delete != null){
                        deletestatus();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Error :" + e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        },1000);

    }

    private void deletestatus() {

        if (System.currentTimeMillis() >= delete){

            Query query = statusref.orderByChild("delete").equalTo(delete);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }else {

        }
    }

    private void fetchStatus() {

        statusref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    url = snapshot1.child("image").getValue().toString();
                    time = snapshot1.child("time").getValue().toString();
                    delete = Long.valueOf(snapshot1.child("delete").getValue().toString());

                    Picasso.get().load(url).into(iv_mystatus);
                    taptoaddtv.setText(time);
                    iv_mystatus.setPadding(0,0,0,0);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkpermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CAMERA)
                .check();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.taptoadd_tv:
                statusref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(uid)){
                            openstatus();
                        }else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    opencameraBs();
                                }
                            },2000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            case R.id.mystatus_tv:
                statusref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(uid)){
                            openstatus();
                        }else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    opencameraBs();
                                }
                            },2000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            case R.id.addstatusfb:
                opencameraBs();


        }
    }

    private void openstatus() {

        Intent intent = new Intent(getActivity(),ShowStatus.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    private void opencameraBs() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.camera_bs);

        TextView opencamera = dialog.findViewById(R.id.open_camertv);
        TextView opengallery = dialog.findViewById(R.id.open_galleytv);

        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),UploadImage.class);
                startActivity(intent);
            }
        });

        opengallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickimages();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void pickimages() {

        Intent intenth = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intenth.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intenth,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1 || resultCode == RESULT_OK ||
                    data != null || data.getData() != null){
                imaguri = data.getData();

                if (imaguri.toString().contains("image")){
                    String url = imaguri.toString();
                    Intent intent = new Intent(getActivity(),ImageActivity.class);
                    intent.putExtra("u",url);
                    startActivity(intent);
                }


            }
        }catch (Exception e){

        }



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ListModal> options =
                new FirebaseRecyclerOptions.Builder<ListModal>()
                        .setQuery(chatlist,ListModal.class)
                        .build();

        FirebaseRecyclerAdapter<ListModal,StatusVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ListModal, StatusVH>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StatusVH holder, int position, @NonNull ListModal model) {

                        holder.fetchStatus(getActivity(),model.getTime(),model.getLastm(),model.getName()
                                ,model.getUrl(),model.getUid());

                        String userid = getItem(position).getUid();

                        holder.nametv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),ShowStatus.class);
                                intent.putExtra("uid",userid);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public StatusVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.status_item,parent,false);

                        return new StatusVH(view);

                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();



    }
}
