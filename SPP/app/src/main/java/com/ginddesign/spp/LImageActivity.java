package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class LImageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;
    ImageButton snapShot;
    EditText picName;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limage);

        picName = (EditText) findViewById(R.id.nameImage);
        snapShot = (ImageButton) findViewById(R.id.snapPic);
        final ListView lv = (ListView) findViewById(R.id.ilist);



        snapShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    name = picName.getText().toString().trim();
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

            }
        });

        String[] imageList = getResources().getStringArray(R.array.ilist);

        mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, imageList);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(LImageActivity.this, LIDetailActivity.class);
        startActivity(detail);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_limage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ParseUser.logOut();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent lock = new Intent(this, ListMasterActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_add) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (!name.equals("")) {
                final ParseObject images = new ParseObject("images");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteMe = stream.toByteArray();
                ParseFile imageFile = new ParseFile(name + ".png", byteMe);
                imageFile.saveInBackground();
                images.setACL(new ParseACL(ParseUser.getCurrentUser()));
                images.put("Name", name);
                images.put("spp_image", imageFile);
                images.pinInBackground();
                images.saveInBackground();
            }
        }
    }


}
