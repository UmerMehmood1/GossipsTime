package com.app.gossipstime;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Superclass {

    DatabaseReference onlineref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();


    public void setonline(){

        onlineref = database.getReference("online").child(currentuid);

        onlineref.setValue("online");

    }
    public void setLasteen(){

        onlineref = database.getReference("online").child(currentuid);

        Calendar time1 = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm:ss a");
        String  savetime = currenttime.format(time1.getTime());

        onlineref.setValue("Last seen at "+savetime);
    }
}

