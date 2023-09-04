package com.app.gossipstime;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Privacy extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference profileRef,lastseenRef,onlineref;
    String currentuid;
    RadioButton hideseen,showseen;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);


        hideseen = findViewById(R.id.cb_hideseen);
        showseen = findViewById(R.id.cb_showseen);
        save = findViewById(R.id.saveprivacy);

        lastseenRef = database.getReference("privacyls");

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();

        onlineref = database.getReference("online").child(currentuid);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hideseen.isChecked()){

                    lastseenRef.child(currentuid).setValue("hideseen");
                    onlineref.removeValue();

                }else if (showseen.isChecked()){
                    lastseenRef.child(currentuid).setValue("showseen");

                }
                Toast.makeText(Privacy.this, "Settings updated", Toast.LENGTH_SHORT).show();

            }
        });

        getsetting();

    }

    public void getsetting(){

        lastseenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String  status = snapshot.child(currentuid).getValue().toString();
                    if (status.equals("hideseen")){
                        hideseen.setChecked(true);

                    }else if (status.equals("showseen")){

                        showseen.setChecked(true);
                    }

                    Toast.makeText(Privacy.this, ""+status, Toast.LENGTH_SHORT).show();

                }else {

                    //    Toast.makeText(TabAct.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Privacy.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        });

    }
}