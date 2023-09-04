package com.app.gossipstime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.gossipstime.databinding.ActivityLogInOptionsBinding;

public class LogInOptions extends AppCompatActivity {
    ActivityLogInOptionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_options);

        binding = ActivityLogInOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInOptions.this , SignInWithGoogle.class);
                startActivity(intent);

            }
        });

        //for phone number
        binding.btnLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInOptions.this , Login.class);
                startActivity(intent);

            }
        });


    }
}