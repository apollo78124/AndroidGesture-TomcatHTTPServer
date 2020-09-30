package com.example.clickevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView img;
    Button btna,btnb,btnc,btnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //A handler
        btna =(Button) findViewById(R.id.buttonA);
        btna.setOnClickListener(this);

        //B handler
        btnb = (Button) findViewById(R.id.buttonB);
        btnb.setOnClickListener(this);

        //C handler
        btnc = (Button)findViewById(R.id.buttonC);
        btnc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                img =(ImageView) findViewById(R.id.imGView1);
                img.setImageResource(R.drawable.samsung);
            }
        });
    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int getid = view.getId();
        if(getid == R.id.buttonA){
            img = (ImageView)findViewById(R.id.imGView1);
            img.setImageResource(R.drawable.apple);

        }
        if(getid ==R.id.buttonB){
            img = (ImageView)findViewById(R.id.imGView1);
            img.setImageResource(R.drawable.blackberry);
        }

    }
    public void buttonDclick(View view){
        img =(ImageView)findViewById(R.id.imGView1);
        img.setImageResource(R.drawable.sony);
    }

}
