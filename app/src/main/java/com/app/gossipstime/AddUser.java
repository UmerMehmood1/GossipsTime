package com.app.gossipstime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddUser extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference chatlist,groupref,memberRef;
    String currentuid;
    TextView selectedtv;
    Button doneBtn;
    String groupname,adminname,address,groupurl,savetime,savedate,usertoken;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        recyclerView = findViewById(R.id.rv_au);
        selectedtv = findViewById(R.id.selectedtv);

        LinearLayoutManager manager = new LinearLayoutManager(AddUser.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        groupname = getIntent().getStringExtra("gname");
        address = getIntent().getStringExtra("address");
        groupurl = getIntent().getStringExtra("url");
        adminname = getIntent().getStringExtra("admin");

        doneBtn = findViewById(R.id.done_group);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();

        memberRef = database.getReference("members");

        chatlist = database.getReference("chat list").child(currentuid);
        groupref = database.getReference("groups");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedtv.setVisibility(View.GONE);
            }
        },3000);

        Calendar time1 = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm:ss a");
        savetime = currenttime.format(time1.getTime());


        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd-MMMM-yyyy");
        savedate = currentdate.format(date.getTime());

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(AddUser.this,TabAct.class);
                startActivity(intent);
                finishAndRemoveTask();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ListModal> options =
                new FirebaseRecyclerOptions.Builder<ListModal>()
                        .setQuery(chatlist,ListModal.class)
                        .build();

        FirebaseRecyclerAdapter<ListModal,ListVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ListModal, ListVH>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ListVH holder, int position, @NonNull ListModal model) {

                        holder.setListau(getApplication(),model.getTime(),model.getLastm(),model.getName()
                                ,model.getUrl(),model.getUid());


                        String postkey = getRef(position).getKey();
                        String muid = getItem(position).getUid();


                        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                if (b){

                                    addmembers(muid);
                                }else {
                                    removeuser(muid);

                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.adduser_item,parent,false);

                        return new ListVH(view);

                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    private void removeuser(String muid) {

        groupref.child(muid).child(address).removeValue();

        memberRef.child(address).child(muid).removeValue();


    }

    private void addmembers(String muid) {

        GroupModal modal = new GroupModal();
        String message = Encode.encode("Send first message");

        modal.setAddress(address);
        modal.setAdmin(adminname);
        modal.setAdminid(currentuid);
        modal.setDelete(String.valueOf(System.currentTimeMillis()));
        modal.setSearch(groupname.toLowerCase());
        modal.setUrl(groupurl);
        modal.setTime(savetime);
        modal.setGroupname(groupname);
        modal.setLastm(message);
        modal.setLastmtime("");
        groupref.child(muid).child(address).setValue(modal);

        MemberModal memberModal = new MemberModal();

        memberModal.setUid(muid);
        memberModal.setTime("Joined On "+savedate+savetime);

        memberRef.child(address).child(muid).setValue(memberModal);

        sendNotification(muid);

    }

    public void sendNotification( String ruid){

        FirebaseDatabase.getInstance().getReference().child("Tokens").child(ruid).child("token")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usertoken=dataSnapshot.getValue(String.class);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FcmNotificationsSender notificationsSender =
                        new FcmNotificationsSender(usertoken, "GossipTime", adminname+  " : Added you in a group "+ groupname ,
                                getApplicationContext(),AddUser.this);

                notificationsSender.SendNotifications();


            }
        },1000);

    }
}