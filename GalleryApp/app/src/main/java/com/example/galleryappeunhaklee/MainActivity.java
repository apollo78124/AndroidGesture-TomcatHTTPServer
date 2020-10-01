package com.example.galleryappeunhaklee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView1;
    private Button buttonLeft,buttonRight;
    private File storageDir;
    private String[] imageList;
    private int currentPicPosition;
    private File imgFile;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageView1.setImageResource(R.drawable.ic_launcher_background);

        // Populates the array with names of files and directories

        imageList = storageDir.list();
        currentPicPosition = 0;
        if (imageList != null && imageList.length > 0)
            imgFile = new  File(storageDir + "/" + imageList[currentPicPosition]);

        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView1.setImageBitmap(myBitmap);
        }

        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLeft();
            }
        });

        buttonRight =(Button) findViewById(R.id.buttonRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRight();
            }
        });

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    private void imageLeft() {
        if (currentPicPosition < 1) {
            currentPicPosition = imageList.length - 1;
        } else {
            currentPicPosition--;
        }

        imgFile = new  File(storageDir + "/" + imageList[currentPicPosition]);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView1.setImageBitmap(myBitmap);
        }
    }

    private void imageRight() {
        if (currentPicPosition > imageList.length - 2) {
            currentPicPosition = 0;
        } else {
            currentPicPosition++;
        }

        imgFile = new  File(storageDir + "/" + imageList[currentPicPosition]);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView1.setImageBitmap(myBitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (velocityX < -100) {
                imageLeft();
            }
            if (velocityX > 100) {
                imageRight();
            }
            return true;
        }
    }
}