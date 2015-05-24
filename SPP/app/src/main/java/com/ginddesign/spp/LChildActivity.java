package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class LChildActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String oidPos;
    ListView lv;
    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> oidArray;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lchild);

        lv = (ListView) findViewById(R.id.childList);

        nameArray = new ArrayList<>();
        oidArray = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("children");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            String oid = ((ParseObject) object).getObjectId();

                            nameArray.add(name);
                            oidArray.add(oid);
                            if (nameArray != null) {
                                Log.i("Array", oidArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });


        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            String oid = ((ParseObject) object).getObjectId();

                            nameArray.add(name);
                            oidArray.add(oid);
                            if (nameArray != null) {
                                Log.i("Array", oidArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, nameArray);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lchild, menu);
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
            Intent add = new Intent(this, LNewActivity.class);
            this.startActivity(add);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);

                            String oid = ((ParseObject) object).getObjectId();
                            oidArray.add(oid);
                            if (nameArray != null) {
                                Log.i("Array", oidArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }
                        Log.i("OIDs", oidArray.toString());
                        oidPos = oidArray.get(position);
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);
                            String oid = ((ParseObject) object).getObjectId();
                            if (oidPos.equals(oid)) {
                                Log.i("ObjectID", oid);
                                Intent update = new Intent(LChildActivity.this, LDetailActivity.class);
                                update.putExtra("object ID", oid);
                                startActivity(update);
                            }
                        }
                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }
}
