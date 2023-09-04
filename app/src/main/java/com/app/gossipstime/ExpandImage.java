package com.app.gossipstime;



import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ExpandImage extends AppCompatActivity {


    ImageView imageView;
    TextView nametv;
    String message,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_image);

        imageView = findViewById(R.id.iv_expand);
        nametv = findViewById(R.id.tv_expand);

        message = getIntent().getStringExtra("m");
        url = getIntent().getStringExtra("u");

        File imgFile = new  File(url);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

        }else {
            Toast.makeText(this, "cannot find image", Toast.LENGTH_SHORT).show();
        }

        //  Picasso.get().load(url).into(imageView);
        nametv.setText(Decode.decode(message));

        // Toast.makeText(this, url, Toast.LENGTH_SHORT).show();


    }
}