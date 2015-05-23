package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LImageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;
    ImageButton snapShot;
    EditText picName;
    String name;
    Bitmap bitmap;
    Bitmap photo;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> imgrray = new ArrayList<>();
    public static ArrayList<Bitmap> bmpArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limage);
        nameArray = new ArrayList<>();
        bmpArray = new ArrayList<>();
        picName = (EditText) findViewById(R.id.nameImage);
        snapShot = (ImageButton) findViewById(R.id.snapPic);
        final ListView lv = (ListView) findViewById(R.id.ilist);


        imgrray = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("images");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            final ParseObject object = (ParseObject) list.get(i);
                            final ParseFile photoFile = object.getParseFile("spp_image");
                            Log.i("XXXXXXXXXXXXXXXXXX", photoFile.getName());
                            Log.i("XXXXXXXXXXXXXXXXXX", String.valueOf(photoFile.isDataAvailable()));
                            Log.i("XXXXXXXXXXXXXXXXXX", photoFile.getUrl());
                            final String pUrl = photoFile.getUrl();
                            Log.i("XXXXXXXXXXXXXXXXXX", pUrl);
                            photoFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {
                                        try {
                                            byte[] decodedString = photoFile.getData();
                                            InputStream inputStream  = new ByteArrayInputStream(decodedString);
                                            photo  = BitmapFactory.decodeStream(inputStream);
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                        String name = object.getString("Name");

                                        nameArray.add(name);
                                        imgrray.add(pUrl);
                                        bmpArray.add(photo);
                                        Log.i("Seeeee", bmpArray.toString());

                                        mainListAdapter.notifyDataSetChanged();
                                        Log.i("WTF", nameArray.toString());
                                    } else {
                                        Log.d("test", "There was a problem downloading the data.");
                                    }
                                }
                            });
                        }
                    }

                    mainListAdapter.notifyDataSetChanged();
                }
            });


            snapShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = picName.getText().toString().trim();
                    if (!name.equals("")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    } else {
                        Toast.makeText(context, "Please Provide Name of Image Before launching camera", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            mainListAdapter = new ArrayAdapter<>(context, R.layout.activity_photocell, R.id.photoName,  nameArray);

            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);

            lv.setAdapter(mainListAdapter);
        }
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
                String imgName = name.replaceAll("\\s+", "");
                final ParseObject images = new ParseObject("images");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteMe = stream.toByteArray();
                ParseFile imageFile = new ParseFile(imgName + ".png", byteMe);
                imageFile.saveInBackground();
                images.setACL(new ParseACL(ParseUser.getCurrentUser()));
                images.put("Name", name);
                images.put("spp_image", imageFile);
                nameArray.add(name);
                images.pinInBackground();
                images.saveInBackground();
                mainListAdapter.notifyDataSetChanged();
            }
        }
    }


}
