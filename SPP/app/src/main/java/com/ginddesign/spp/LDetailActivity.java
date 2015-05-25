package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONArray;
import org.json.JSONException;

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
    String oid;
    TextView dName;
    TextView ddob;
    TextView dss;
    TextView dAll;
    TextView dMed;
    ListView lv;
    public static JSONArray fileName = new JSONArray();
    public static JSONArray fileinfo = new JSONArray();
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
        final TextView dShot = (TextView) findViewById(R.id.dShot);
        lv = (ListView) findViewById(R.id.linkList);

        final Intent i = getIntent();
        ois = i.getStringExtra("object ID");




        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            fileName = new JSONArray();
            fileinfo = new JSONArray();
            nameArray = new ArrayList<>();
            nameInfo = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("children");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
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
                        Log.i("TESSSSSST", fileinfo.toString());
                        try {
                            for (int i = 0; i < fileName.length(); i++) {
                                Log.i("Array", "Entry Point Done");
                                String name = fileName.getString(i);
                                String info = fileinfo.getString(i);

                                Log.i("TESSSSSST", name);
                                nameArray.add(name);
                                nameInfo.add(info);
                                Log.i("TESSSSSST", nameInfo.toString());
                                //loadList();
                            }
                            mainListAdapter.notifyDataSetChanged();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        dName.setText(name);
                        ddob.setText(dob);
                        dss.setText(ss);
                        dAll.setText(all);
                        dMed.setText(med);
                        dShot.setText(shot);
                        Linkify.addLinks(dShot, Linkify.WEB_URLS);

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
                        Linkify.addLinks(dShot, Linkify.WEB_URLS);


                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                    mainListAdapter.notifyDataSetChanged();
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
            try {
                ParseUser.getCurrentUser().refresh();
            } catch (ParseException d) {
                d.printStackTrace();
            }
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.context);
            startActivityForResult(builder.build(), 0);
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }
        else if (id == R.id.action_edit) {
            Intent edit = new Intent(this, LNewActivity.class);
            edit.putExtra("Title", "Update Child Information");
            edit.putExtra("Object ID", ois);
            this.startActivity(edit);
        }
        else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, dName.getText() + "\n" + ddob.getText() + "\n" + "Allergies: " + dAll.getText() + "\n" + "Medical Conditions: " + dMed.getText());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        }
        return super.onOptionsItemSelected(item);
    }
}
