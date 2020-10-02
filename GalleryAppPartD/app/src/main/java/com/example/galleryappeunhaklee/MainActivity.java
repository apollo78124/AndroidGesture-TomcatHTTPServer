package com.example.galleryappeunhaklee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView imageView1;
    private Button buttonLeft,buttonRight;
    private File storageDir;
    private String[] imageList;
    private int currentPicPosition;
    private File imgFile;
    private GestureDetectorCompat mDetector;
    private SensorManager sensorManager;
    private Sensor mGravity;
    private long lastUpdate;
    private boolean color = false;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastUpdate = System.currentTimeMillis();
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageView1.setImageResource(R.drawable.ic_launcher_background);
        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.GREEN);

        // Populates the array with names of files and directories

        imageList = storageDir.list();
        currentPicPosition = 0;
        if (imageList != null && imageList.length > 0)
            imgFile = new  File(storageDir + "/" + imageList[currentPicPosition]);

        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView1.setImageBitmap(myBitmap);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
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
        sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            if (x < 0) {
                imageLeft();
            } else {
                imageRight();
            }

        }
    }


}