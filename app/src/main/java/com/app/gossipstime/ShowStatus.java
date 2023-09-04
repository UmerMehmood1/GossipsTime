package com.app.gossipstime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ShowStatus extends AppCompatActivity implements StoriesProgressView.StoriesListener ,
        PopupMenu.OnMenuItemClickListener , GestureDetector.OnGestureListener {

    List<String> image;
    List<String> uid;
    List<String> caption;
    List<Long> delete;
    List<String> time;

    Long deletvalue;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference,documentReferencesender;
    StatusModal modal;
    StoriesProgressView storiesProgressView;
    String userid,currentuid,name,url,phoneno,about,sname,surl;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference statusref,laststatus,seenuserlist,viewcountref;
    DatabaseReference sRef, rRef, schatlist, rchatlist, lastseenref,seenStatus;
    int counter = 0;
    ImageView s_iv,useriv;
    TextView tvname,storyviewTv,captionTv,timetv,replytv;
    EditText replEt;
    FloatingActionButton replyfb;

    MessageModal smodal, rmodal;
    ListModal listModel, rlistmodel;


    private float x1,x2,y1,y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector ;

    ViewModel viewModel;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_status);

        storiesProgressView = findViewById(R.id.stories);

        s_iv = findViewById(R.id.iv_status_shows);
        useriv = findViewById(R.id.iv_user_shows);

        tvname = findViewById(R.id.tv_uname_ss);
        storyviewTv = findViewById(R.id.statuscount);
        captionTv = findViewById(R.id.storycap_tv);
        timetv = findViewById(R.id.tv_time_ss);

        replytv = findViewById(R.id.replytv_status);

        replyfb = findViewById(R.id.fbReply);
        replEt = findViewById(R.id.statusreplyEt);

        laststatus = database.getReference("laststatus");
        this.gestureDetector = new GestureDetector(ShowStatus.this,this);

        modal = new StatusModal();

        viewModel = new ViewModel();
        listModel = new ListModal();
        rlistmodel = new ListModal();
        smodal = new MessageModal();
        rmodal = new MessageModal();


        View reverse = findViewById(R.id.viewnext);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storiesProgressView.skip();
            }
        });

        reverse.setOnTouchListener(onTouchListener);

        reverse.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                storiesProgressView.pause();
                return false;
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.viewprev);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storiesProgressView.reverse();
            }
        });
        skip.setOnTouchListener(onTouchListener);

        skip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                storiesProgressView.pause();
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            // status user id
            userid = extras.getString("uid");
        }else {

        }

        // currentuserid

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();

        statusref = database.getReference("Status").child(userid);

        seenuserlist = database.getReference("seenlist").child(userid);

        viewcountref = database.getReference("seenlist").child(currentuid);

        storyviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewBS();
            }
        });

        if (userid.equals(currentuid)){

            storyviewTv.setVisibility(View.VISIBLE);
            replytv.setVisibility(View.GONE);
            getviewcount();
        }else {
            replytv.setVisibility(View.VISIBLE);
            storyviewTv.setVisibility(View.GONE);
            storeseenuserdata();
        }

        documentReferencesender = db.collection("user").document(currentuid);


        documentReferencesender.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            sname = task.getResult().getString("name");
                            surl = task.getResult().getString("url");
                        }else {
                            Toast.makeText(ShowStatus.this, "no profile", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        replytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showReplyET(userid,currentuid);
            }
        });

    }

    private void showReplyET(String ruid, String suid) {

        sRef = database.getReference("Message").child(suid).child(ruid);
        rRef = database.getReference("Message").child(ruid).child(suid);
        schatlist = database.getReference("chat list").child(suid);
        rchatlist = database.getReference("chat list").child(ruid);
        //  lastseenref = database.getReference("online");

        seenStatus = database.getReference("seenstatus");

        storiesProgressView.pause();
        replEt.setVisibility(View.VISIBLE);
        replyfb.setVisibility(View.VISIBLE);
        storyviewTv.setVisibility(View.GONE);
        replytv.setVisibility(View.GONE);


        replyfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar time1 = Calendar.getInstance();
                SimpleDateFormat currenttime = new
                        SimpleDateFormat("HH:mm:ss a");
                String  savetime = currenttime.format(time1.getTime());

                String senderreply = "You Replied :" +replEt.getText().toString().toString();
                String receiverreply = "[" + caption.get(counter) +"] Status Reply :" + replEt.getText().toString().trim();

                if (senderreply.isEmpty()){

                    Toast.makeText(ShowStatus.this, "Please write something", Toast.LENGTH_SHORT).show();
                }else {


                    final  String delete = String.valueOf(System.currentTimeMillis());

                    smodal.setMessage(Encode.encode(senderreply));
                    smodal.setSearch(Encode.encode(senderreply.toLowerCase()));
                    smodal.setRuid(ruid);
                    smodal.setSuid(suid);
                    smodal.setTime(savetime);
                    smodal.setType("img");
                    smodal.setUrl(image.get(counter));
                    smodal.setDelete(delete);


                    String key = sRef.push().getKey();

                    sRef.child(key).setValue(smodal);

                    rmodal.setMessage(Encode.encode(receiverreply));
                    rmodal.setSearch(Encode.encode(receiverreply.toLowerCase()));
                    rmodal.setRuid(ruid);
                    rmodal.setSuid(suid);
                    rmodal.setType("img");
                    rmodal.setUrl(image.get(counter));
                    rmodal.setTime(savetime);
                    rmodal.setDelete(delete);

                    String key2 = rRef.push().getKey();

                    rRef.child(key2).setValue(rmodal);

                    // chat list ref

                    listModel.setLastm(Encode.encode(senderreply));
                    listModel.setName(name);
                    listModel.setUid(ruid);
                    listModel.setUrl(url);
                    listModel.setTime(savetime);

                    schatlist.child(ruid).setValue(listModel);


                    // adding data in receiver

                    rlistmodel.setLastm(Encode.encode(receiverreply));
                    rlistmodel.setName(sname);
                    rlistmodel.setUrl(surl);
                    rlistmodel.setUid(suid);
                    rlistmodel.setTime(savetime);

                    rchatlist.child(suid).setValue(rlistmodel);

                    seenStatus.child(suid+ruid).child("counts").child(senderreply).setValue("sent");


                    Toast.makeText(ShowStatus.this, "Reply sent", Toast.LENGTH_SHORT).show();

                    replEt.setVisibility(View.GONE);
                    replyfb.setVisibility(View.GONE);
                    storiesProgressView.resume();
                }
            }
        });
    }

    private void storeseenuserdata() {
        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd-MMMM");
        final String savedate = currentdate.format(callfordate.getTime());


        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm:a");
        final String savetime = currenttime.format(callfortime.getTime());
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                viewModel.setName(name);
                viewModel.setTime(savedate + " :"+ savetime);
                viewModel.setUid(currentuid);
                viewModel.setUrl(url);
                seenuserlist.child(currentuid).setValue(viewModel);

            }
        },2000);

    }

    private void getviewcount() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                viewcountref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int viewcount = (int) snapshot.getChildrenCount();
                        storyviewTv.setText(String.valueOf(viewcount));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },2000);

    }

    private void viewBS() {

        final Dialog dialog = new Dialog(ShowStatus.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_bs);



        TextView viewcounttv = dialog.findViewById(R.id.viewbytv);
        TextView noviewsyettv = dialog.findViewById(R.id.noviewsyettv);
        ImageButton morebtn = dialog.findViewById(R.id.more_btn_view);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_viewbs);

        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        storiesProgressView.pause();

        viewcounttv.setText("Viewed by " + storyviewTv.getText().toString());

        if (storyviewTv.getText().toString().equals("0")){

        }else {
            noviewsyettv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            FirebaseRecyclerOptions<ViewModel> options =
                    new FirebaseRecyclerOptions.Builder<ViewModel>()
                            .setQuery(viewcountref,ViewModel.class)
                            .build();

            FirebaseRecyclerAdapter<ViewModel,ViewVH> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<ViewModel, ViewVH>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ViewVH holder, int position, @NonNull ViewModel model) {

                            holder.setUser(getApplication(),model.getName(),model.getUrl(),model.getTime(),model.getUid());

                            String userid = getItem(position).getUid();

                            holder.nametv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ShowStatus.this,ShowStatus.class);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }
                            });


                        }

                        @NonNull
                        @Override
                        public ViewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.viewbs_item,parent,false);

                            return new ViewVH(view);

                        }
                    };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
        }

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(ShowStatus.this,view);
                popupMenu.setOnMenuItemClickListener(ShowStatus.this);
                popupMenu.inflate(R.menu.viewmenufile);
                popupMenu.show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            getstories(userid);
        }catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }

    private void getstories(String userid) {

        image = new ArrayList<>();
        uid = new ArrayList<>();
        delete = new ArrayList<>();
        caption = new ArrayList<>();
        time = new ArrayList<>();

        statusref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                image.clear();
                uid.clear();
                time.clear();
                caption.clear();
                delete.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    StatusModal statusModal = snapshot1.getValue(StatusModal.class);

                    long timecurrent = System.currentTimeMillis();

                    image.add(statusModal.getImage());
                    uid.add(statusModal.getUid());
                    time.add(statusModal.getTime());
                    delete.add(statusModal.getDelete());
                    caption.add(statusModal.getCaption());


                }
                storiesProgressView.setStoriesCount(image.size());
                storiesProgressView.setStoriesListener(ShowStatus.this);
                storiesProgressView.startStories(counter);
                storiesProgressView.setStoryDuration(9000L);


                s_iv.setVisibility(View.VISIBLE);

                captionTv.setText(caption.get(counter));

                Picasso.get().load(image.get(counter)).into(s_iv);
                timetv.setText(time.get(counter));

                fetchuserinfo(uid.get(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchuserinfo(String s) {

        documentReference = db.collection("user").document(s);


        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            name = task.getResult().getString("name");
                            about = task.getResult().getString("about");
                            phoneno = task.getResult().getString("phone");
                            url = task.getResult().getString("url");

                            if (url.equals("")){
                                tvname.setText(name);

                            }else {
                                tvname.setText(name);
                                Picasso.get().load(url).into(useriv);
                            }

                        }else {
                            Toast.makeText(ShowStatus.this, "np profile", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public void onNext() {

        Picasso.get().load(image.get(++counter)).into(s_iv);
        captionTv.setText(caption.get(counter));
    }

    @Override
    public void onPrev() {

        if ((counter - 1) < 0)return;
        Picasso.get().load(image.get(--counter)).into(s_iv);
        captionTv.setText(caption.get(counter));

    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.delete_status_view:

                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(image.get(counter));
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Query query = statusref.orderByChild("delete").equalTo(delete.get(counter));
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
                    }
                });

                return true;

            case R.id.download_status_view:

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(image.get(counter)));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setTitle("Download");
                request.setDescription("Downloading ");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,caption.get(counter)+ System.currentTimeMillis());
                DownloadManager manager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                Toast.makeText(this, "Downlaoding", Toast.LENGTH_SHORT).show();

                return true;


        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case  MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                float valueX = y2-y1;

                if(Math.abs(valueX) > MIN_DISTANCE){

                    if (x2 > x1){
                        if (userid.equals(currentuid)){
                            viewBS();

                        }else {

                            showReplyET(userid,currentuid);
                        }
                    }else {

                    }
                }else {

                }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}