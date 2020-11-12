package com.example.galleryappeunhaklee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private Button buttonGoBack, buttonSearch;
    private File storageDir;
    private String[] imageList;
    private int currentPicPosition;
    private File imgFile;
    private ImageView imageView1;
    EditText editText1;
    ListView imageListView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        buttonGoBack =(Button) findViewById(R.id.goBackButton);
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainActivity();
            }
        });

        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageView1.setImageResource(R.drawable.ic_launcher_background);

        // Populates the array with names of files and directories

        imageList = storageDir.list();
        imageListView1 = (ListView) findViewById(R.id.imageListView1);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, imageList);
        imageListView1.setAdapter(itemsAdapter);
        imageListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                imgFile = new  File(storageDir + "/" + selectedItem);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView1.setImageBitmap(myBitmap);
                }
            }
        });

        editText1 = (EditText)findViewById(R.id.searchBarForImage);
        editText1.setHint("Enter Image Name To Search");
        buttonSearch =(Button) findViewById(R.id.searchButton2);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForAnImage();
            }
        });
    }
    public void goBackToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void searchForAnImage() {
        if (editText1.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        } else {
            String searchKeyword = editText1.getText().toString();
            ArrayList<String> searchResult = new ArrayList<String>();
            for (int i = 0; i < imageList.length; i++) {
                if (imageList[i].contains(searchKeyword)) {
                    searchResult.add(imageList[i]);
                }
            }

            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResult);
            imageListView1.setAdapter(itemsAdapter);
        }
    }
}