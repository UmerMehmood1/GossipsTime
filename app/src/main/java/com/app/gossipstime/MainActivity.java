package com.app.gossipstime;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;



import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import com.app.gossipstime.R;
import com.bumptech.glide.Glide;





public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Glide.with(this).load(R.drawable.splash).into((ImageView) findViewById(R.id.splash));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this , Profile.class));
            }
        } , 3000);
    }
}