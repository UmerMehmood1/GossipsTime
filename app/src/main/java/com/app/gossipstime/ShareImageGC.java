package com.app.gossipstime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ShareImageGC extends AppCompatActivity {

    Button select, previous, next;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String groupname,url,address,adminid,currentuid,sendername;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    EditText textEt;
    ImageButton sendbtn;
    int cout,upload_count;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    GroupChatModal messageModal;
    ListModal listModel,rlistmodel;
    MessageModal modal;
    LastmModal lastmModal ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupchat,lastmREf;
    Uri imageUrl;
    ArrayList<String> ivmessages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image_gc);

        select = findViewById(R.id.select);
        imageView = findViewById(R.id.image);
        previous = findViewById(R.id.previous);
        mArrayUri = new ArrayList<Uri>();
        next = findViewById(R.id.next);
        sendbtn = findViewById(R.id.send_image_gc);
        textEt = findViewById(R.id.et_gc_iv);
        ivmessages = new ArrayList<String >();


        // all models

        listModel = new ListModal();
        rlistmodel = new ListModal();
        lastmModal = new LastmModal();
        modal = new MessageModal();
        messageModal = new GroupChatModal();

        storageReference = storage.getReference("gcimages");


        groupname = getIntent().getStringExtra("groupname");
        address = getIntent().getStringExtra("address");
        url = getIntent().getStringExtra("url");
        adminid = getIntent().getStringExtra("adminid");


        groupchat = database.getReference("group chat").child(address);
        lastmREf = database.getReference("lastm");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();
        documentReference = db.collection("user").document(currentuid);

        // initializing preview rv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShareImageGC.this,
                LinearLayoutManager.HORIZONTAL, false);


        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                            sendername = task.getResult().getString("name");

                        }else {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(ShareImageGC.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });


        // click here to select image
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog mProgressDialog = new  ProgressDialog(ShareImageGC.this);
                mProgressDialog.setTitle("Please wait");
                mProgressDialog.show();

                for (upload_count = 0; upload_count <mArrayUri.size() ;upload_count++){

                    mProgressDialog.setMessage("Sending images");

                    final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + "jpg");

                    Uri selectedUri = mArrayUri.get(upload_count);

                    UploadTask uploadTask = reference.putFile(selectedUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                imageUrl = task.getResult();
                                Calendar callfortime = Calendar.getInstance();
                                SimpleDateFormat currenttime = new
                                        SimpleDateFormat("HH:mm:ss a");
                                final String savetime = currenttime.format(callfortime.getTime());

                                String message =  textEt.getText().toString().trim();
                                String encodemessage = Encode.encode(message);

                                messageModal.setMessage(encodemessage);
                                messageModal.setDelete(String.valueOf(System.currentTimeMillis()));
                                messageModal.setTime(savetime);
                                messageModal.setSuid(currentuid);
                                messageModal.setSearch(encodemessage.toLowerCase());
                                messageModal.setSeen("no");
                                messageModal.setSname(sendername);
                                messageModal.setType("img");
                                messageModal.setUrl(imageUrl.toString());

                                String key = groupchat.push().getKey();
                                groupchat.child(key).setValue(messageModal);

                                lastmModal.setLastm(encodemessage);
                                lastmModal.setLastmtime(savetime);

                                lastmREf.child(address).setValue(lastmModal);

                                mProgressDialog.dismiss();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
            // Get the Image from data
            if (data.getClipData() != null) {

                ClipData mClipData = data.getClipData();
                cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

}