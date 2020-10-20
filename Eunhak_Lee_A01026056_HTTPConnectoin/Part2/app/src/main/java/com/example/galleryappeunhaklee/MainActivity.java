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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView1;
    private Button buttonLeft,buttonRight, buttonUpload;
    private File storageDir;
    private String[] imageList;
    private int currentPicPosition;
    private File imgFile;
    private GestureDetectorCompat mDetector;
    private TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageView1.setImageResource(R.drawable.ic_launcher_background);

        // Populates the array with names of files and directories

        imageList = storageDir.list();
        currentPicPosition = 0;
        if (imageList != null && imageList.length > 0) {
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

            buttonUpload =(Button) findViewById(R.id.buttonUpload);
            buttonUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadWebPageTask task = new DownloadWebPageTask();
                    task.execute(new String[] { "https://www.bcit.ca" });
                }
            });

            mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        }
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
        public boolean onDown(MotionEvent event) {
            if (event.getX() < 350 && event.getY() < 1411 & event.getY() > 390)
                imageLeft();
            if (event.getX() > 774 && event.getY() < 1411 & event.getY() > 390)
                imageRight();
            return true;
        }
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

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            //for (String url : urls) {

            //String host = "www.google.com"
            BufferedReader br = null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                //URL url = new URL("http://10.0.2.2:8080/midp/hits");
                URL url = new URL("http://192.168.1.75:8080/midp/hits");
                //URL url = new URL(urls[0]);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(inputStream));

                String line = null;
                while ((line = br.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    response += line;
                }

                if (response.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            //}
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }
}