package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LImageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;
    ImageButton snapShot;
    EditText picName;
    String name;
    Timer sched;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> createArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limage);

        picName = (EditText) findViewById(R.id.nameImage);
        snapShot = (ImageButton) findViewById(R.id.snapPic);
        lv = (ListView) findViewById(R.id.ilist);

        nameArray = new ArrayList<>();
        createArray = new ArrayList<>();
        oidArray = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        name = object.getString("Name");
                        String oid = (object).getObjectId();
                        Date creation = (object).getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String date = sdf.format(creation);

                        nameArray.add(name);
                        oidArray.add(oid);
                        createArray.add(date);

                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        String name = object.getString("Name");
                        String oid = (object).getObjectId();
                        Date creation = (object).getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String date = sdf.format(creation);

                        nameArray.add(name);
                        oidArray.add(oid);
                        createArray.add(date);

                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        }
        mainListAdapter = new PhotoCell(context, R.layout.activity_photocell, nameArray);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);

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
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        createArray = new ArrayList<>();
                        oidArray = new ArrayList<>();
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject object = objects.get(i);
                            name = object.getString("Name");
                            String oid = (object).getObjectId();
                            Date creation = (object).getCreatedAt();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(creation);

                            nameArray.add(name);
                            oidArray.add(oid);
                            createArray.add(date);

                        }
                        mainListAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        createArray = new ArrayList<>();
                        oidArray = new ArrayList<>();
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject object = objects.get(i);
                            name = object.getString("Name");
                            String oid = (object).getObjectId();
                            Date creation = (object).getCreatedAt();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(creation);

                            nameArray.add(name);
                            oidArray.add(oid);
                            createArray.add(date);

                        }
                        mainListAdapter.notifyDataSetChanged();
                    }
                });
            }
            mainListAdapter = new PhotoCell(context, R.layout.activity_photocell, nameArray);

            lv.setOnItemClickListener(LImageActivity.this);
            lv.setOnItemLongClickListener(LImageActivity.this);

            lv.setAdapter(mainListAdapter);
            Log.i("test", "run");
        }
    };




    public void onPause() {
        super.onPause();

        sched.cancel();
    }

    public void onResume() {
        super.onResume();
        sched = new Timer();
        sched.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String oidPos = oidArray.get(position);
        Log.i("OID", oidPos);
        Intent detail = new Intent(LImageActivity.this, LIDetailActivity.class);
        detail.putExtra("Object ID", oidPos);
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
                images.setACL(new ParseACL(ParseUser.getCurrentUser()));
                images.put("Name", name);
                images.put("spp_image", imageFile);
                images.pinInBackground();
                images.saveInBackground();
                name = "";
                picName.setText("");

            }
        }
    }


}
