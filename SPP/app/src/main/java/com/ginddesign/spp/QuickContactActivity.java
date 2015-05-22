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
import java.util.Timer;
import java.util.TimerTask;


public class QuickContactActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String oidPos;
    private Timer sched;
    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> catArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    public static ArrayList<String> phoneArray = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickcontact);


        final ListView lv = (ListView) findViewById(R.id.qcList);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("contacts");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {

                            Object object = list.get(i);

                            String name = ((ParseObject) object).getString("Name");
                            String cat = ((ParseObject) object).getString("Cat");
                            String phone = ((ParseObject) object).getString("Phone");
                            String oid = ((ParseObject) object).getObjectId();


                            nameArray.add(name);
                            catArray.add(cat);
                            oidArray.add(oid);
                            phoneArray.add(phone);
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
            mainListAdapter = new ContactCell(context, R.layout.activity_contactcell, nameArray);

            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);
            lv.setAdapter(mainListAdapter);

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quickcontact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ParseUser.logOut();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else if (id == R.id.action_home) {
            Intent qc = new Intent(this, ListMasterActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_lock) {
            Intent lock = new Intent(this, LSignInActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_add) {
            Intent add = new Intent(this, QCNewActivity.class);
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
            ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);

                            String oid = ((ParseObject) object).getObjectId();
                            oidArray.add(oid);

                            Log.i("Array", "Here???");


                        }
                        Log.i("OIDs", oidArray.toString());
                        oidPos = oidArray.get(position);
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", oidPos);
                            Object object = list.get(i);
                            String oid = ((ParseObject) object).getObjectId();
                            Log.i("Array", oid);
                            if (oidPos.equals(oid)) {
                                Log.i("ObjectID", oid);
                                Intent update = new Intent(QuickContactActivity.this, QCDetailActivity.class);
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
