package com.example.motiongestures;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acceleration;
    private TextView textX;
    private TextView textY;
    private TextView textZ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textX = (TextView) findViewById(R.id.TextViewX);
        textY = (TextView) findViewById(R.id.TextViewY);
        textZ = (TextView) findViewById(R.id.TextViewZ);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        acceleration = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, acceleration,
                SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        DecimalFormat dF = new DecimalFormat("#.##");
        x = Float.parseFloat(dF.format(x));
        y = Float.parseFloat(dF.format(y));
        z = Float.parseFloat(dF.format(z));

        textX.setText(String.valueOf(x) + " m/s2");
        textY.setText(String.valueOf(y) + " m/s2");
        textZ.setText(String.valueOf(z) + " m/s2");
    }

    @Override
    protected void onResume() {
        super.onResume(); // register this class as a listener for the
        // orientation and
        // accelerometer sensors
        sensorManager.registerListener(this, acceleration,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() { // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
