package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class LDetailActivity extends AppCompatActivity {

    String ois;
    String name;
    String dob;
    String ss;
    String all;
    String med;
    String shot;
    TextView dName;
    TextView ddob;
    TextView dss;
    TextView dAll;
    TextView dMed;
    TextView dShot;
    ListView lv;
    JSONArray fileName = new JSONArray();
    JSONArray fileinfo = new JSONArray();
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> nameInfo = new ArrayList<>();
    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldetail);

        dName = (TextView) findViewById(R.id.dName);
        ddob = (TextView) findViewById(R.id.ddob);
        dss = (TextView) findViewById(R.id.dss);
        dAll = (TextView) findViewById(R.id.dAll);
        dMed = (TextView) findViewById(R.id.dMed);
        dShot = (TextView) findViewById(R.id.dShot);
        lv = (ListView) findViewById(R.id.linkList);

        final Intent i = getIntent();
        ois = i.getStringExtra("object ID");

        nameArray = new ArrayList<>();
        nameInfo = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.getInBackground(ois, new GetCallback<ParseObject>() {
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        name = object.getString("Name");
                        dob = object.getString("dob");
                        ss = object.getString("SS");
                        all = object.getString("Allergies");
                        med = object.getString("Medical");
                        shot = object.getString("Shot");
                        fileName = object.getJSONArray("AdditionalNames");
                        fileinfo = object.getJSONArray("AdditionalInfo");
                        try {
                            for (int i = 0; i < fileName.length(); i++) {
                                Log.i("Array", "Entry Point Done");
                                String name = fileName.getString(i);
                                String info = fileinfo.getString(i);
                                Log.i("TESSSSSST", name);
                                nameArray.add(name);
                                nameInfo.add(info);
                                Log.i("TESSSSSST", nameArray.toString());
                                //loadList();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        dName.setText(name);
                        ddob.setText(dob);
                        dss.setText(ss);
                        dAll.setText(all);
                        dMed.setText(med);
                        dShot.setText(shot);




                        String[] links = getResources().getStringArray(R.array.links);

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });

        }else{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.fromLocalDatastore();
            query.getInBackground(ois, new GetCallback<ParseObject>() {
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        name = object.getString("Name");
                        dob = object.getString("Age");
                        ss = object.getString("SS");
                        all = object.getString("Allergies");
                        med = object.getString("Medical");
                        shot = object.getString("Shot");
                        fileName = object.getJSONArray("AdditionalNames");
                        fileinfo = object.getJSONArray("AdditionalInfo");
                        try {
                            for (int i = 0; i < fileName.length(); i++) {
                                Log.i("Array", "Entry Point Done");
                                String name = fileName.getString(i);
                                String info = fileinfo.getString(i);
                                Log.i("TESSSSSST", name);
                                nameArray.add(name);
                                nameInfo.add(info);

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        dName.setText(name);
                        ddob.setText(dob);
                        dss.setText(ss);
                        dAll.setText(all);
                        dMed.setText(med);
                        dShot.setText(shot);


                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }
        loadList();
    }

    public void loadList() {
        mainListAdapter = new addInfoCell(context, R.layout.activity_add_info_cell, nameArray);

        lv.setAdapter(mainListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }
        return super.onOptionsItemSelected(item);
    }
}
