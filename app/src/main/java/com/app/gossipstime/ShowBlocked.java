package com.app.gossipstime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.gossipstime.ui.main.BlockedUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShowBlocked extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUserId;
    BlockedUserAdapter blockedUserAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blocked);
        recyclerView = findViewById(R.id.showblockeduser_rv);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        db.collection("user").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserModal> userList = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    // Convert each document to a UserModal object and add it to the list
                    UserModal user_from_db = document.toObject(UserModal.class);
                    if (user_from_db != null) {
                        userList.add(user_from_db);
                    }
                }

                // Initialize the adapter and set it to the RecyclerView after data retrieval
                 blockedUserAdapter = new BlockedUserAdapter(this, userList);
                recyclerView.setAdapter(blockedUserAdapter);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setVisibility(View.VISIBLE);
    }
}
