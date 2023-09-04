package com.app.gossipstime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.app.gossipstime.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TabAct extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Superclass superclass;
    DatabaseReference seenref;
    boolean aBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        ImageButton menuBtn = findViewById(R.id.menu_tab);

        DatabaseReference vcref;
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        seenref = database.getReference("privacyls");

        superclass = new Superclass();

        tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF000000"));
        tabs.setSelectedTabIndicatorHeight((int) (5* getResources().getDisplayMetrics().density));
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));

        vcref = database.getReference("vc");

        vcref.child(currentuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String  suid = snapshot.child("callerid").getValue().toString();
                    Intent intent = new Intent(TabAct.this,IncomingCall.class);
                    intent.putExtra("suid",suid);
                    startActivity(intent);
                }else {

                    //    Toast.makeText(TabAct.this, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child(currentuid).getValue().toString();
                    if (status.equals("hideseen")) {
                        aBoolean = false;
                    } else if (status.equals("showseen")) {
                        aBoolean = true;
                        superclass.setonline();
                    }
                } else {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TabAct.this,ShowUsers.class);
                startActivity(intent);

            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(TabAct.this,view);
                popupMenu.setOnMenuItemClickListener(TabAct.this);
                popupMenu.inflate(R.menu.menufile);
                popupMenu.show();


            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.newgroup:

                Intent intent1 = new Intent(TabAct.this,GroupName.class);
                startActivity(intent1);
                return true;

            case R.id.profile:

                Intent intent = new Intent(TabAct.this,Profile.class);
                startActivity(intent);
                return true;

            case R.id.privacy:
                Intent privacy = new Intent(TabAct.this,Privacy.class);
                startActivity(privacy);
                return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (aBoolean == true){
            superclass.setLasteen();
        }else if (aBoolean == false){

        }

    }
}