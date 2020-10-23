package com.example.galleryappeunhaklee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView1;
    private Button buttonLeft,buttonRight, buttonUpload, buttonRetreive;
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
                    UploadImageTask task = new UploadImageTask();
                    task.execute(new String[] {String.valueOf(storageDir), imageList[currentPicPosition] });
                }
            });
            buttonRetreive =(Button) findViewById(R.id.buttonRetrieve);
            buttonRetreive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetreiveImageTask task = new RetreiveImageTask();
                    task.execute(new String[] {String.valueOf(storageDir), imageList[currentPicPosition] });
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

    private class UploadImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            String storageDirectory = urls[0];
            String fileName = urls[1];
            BufferedReader br = null;
            String exsistingFileName = urls[1];

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://10.0.2.2:8080/midp/hits");
                //URL url = new URL("http://192.168.1.67:8080/midp/hits");
                //URL url = new URL(urls[0]);
                FileInputStream  fileInputStream = new FileInputStream(storageDirectory + "/" + fileName);
                // Create the request to OpenWeatherMap, and open the connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.connect();
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                // create a buffer of maximum size
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                // read file and write it into form...
//                dos.writeBytes(fileName);
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // close streams
                Log.e(null, "File is written");
                fileInputStream.close();
                dos.flush();

                InputStream is = conn.getInputStream();
                // retrieve the response from server
                int ch;

                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                String s = b.toString();
                response = b.toString();
//                Log.i("Response", s);
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

    private class RetreiveImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            //for (String url : urls) {

            //String host = "www.google.com"
            BufferedReader br = null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://10.0.2.2:8080/midp/hits");
                //URL url = new URL("http://192.168.1.67:8080/midp/hits");
                //URL url = new URL(urls[0]);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream input = urlConnection.getInputStream();
                byte[] buf = new byte[1024];
                int bytesRead;
                String fileName = fileNameGeneration() + ".jpg";
                OutputStream output = new FileOutputStream(storageDir + "/" + fileName);
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
                Log.e(null, "File Downloaded");
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

            imageList = storageDir.list();
            textView.setText("Image downloaded");
        }

        private String fileNameGeneration() {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();

            return generatedString;
        }
    }
}