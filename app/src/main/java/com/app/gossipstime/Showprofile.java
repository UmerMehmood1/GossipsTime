package com.app.gossipstime;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Showprofile extends AppCompatActivity {

    ImageView profileiv;
    TextView nametv,phonetv,abouttv,blocktv;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String userid;
    DatabaseReference blockref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);

        db = FirebaseFirestore.getInstance();
        profileiv  = findViewById(R.id.iv_sp);
        nametv = findViewById(R.id.unametv_sp);
        phonetv = findViewById(R.id.phonetv_sp);
        abouttv = findViewById(R.id.abouttv_sp);
        blocktv = findViewById(R.id.blockusertv);

        userid = getIntent().getStringExtra("uid");

        documentReference = db.collection("user").document(userid);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        blockref = database.getReference("Block list").child(currentuid);


        blocktv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Showprofile.this);
                builder.setTitle("Block User")
                        .setMessage("Are you sure block")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                blockref.child(userid).setValue("blocked");
                                Toast.makeText(Showprofile.this, "Blocked ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.create();
                builder.show();
            }
        });

        getData();
    }

    private void getData() {

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String name = task.getResult().getString("name");
                            String about = task.getResult().getString("about");
                            String phoneno = task.getResult().getString("phone");
                            String url = task.getResult().getString("url");

                            if (url.equals("")){
                                nametv.setText(name);
                                abouttv.setText(about);
                                phonetv.setText(phoneno);
                            }else {
                                nametv.setText(name);
                                abouttv.setText(about);
                                phonetv.setText(phoneno);
                                Picasso.get().load(url).into(profileiv);
                            }

                        }else {
                            Toast.makeText(Showprofile.this, "np profile", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}