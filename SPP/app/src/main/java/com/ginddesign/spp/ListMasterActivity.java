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


public class ListMasterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String namePos;

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> listNameArray = new ArrayList<>();
    ListView lv;
    Context context = this;

    public ListMasterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hit", "Hit");
        setContentView(R.layout.activity_listmaster);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        lv = (ListView) findViewById(R.id.list_master);

        resume();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            nameArray = new ArrayList<>();
            listNameArray = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("listMaster");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List list, com.parse.ParseException e) {


                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            nameArray.add(name);


                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                    for (int c = 0; c < nameArray.size(); c++) {
                        String compare = nameArray.get(c);
                        for (int l = 0; l < list.size(); l++) {
                            if (!listNameArray.contains(compare)) {
                                listNameArray.add(compare);
                            }
                        }
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            nameArray.add(name);


                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }

                    for (int c = 0; c < nameArray.size(); c++) {
                        String compare = nameArray.get(c);
                        for (int l = 0; l < list.size(); l++) {
                            if (!listNameArray.contains(compare)) {
                                listNameArray.add(compare);

                            }
                        }
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        }
        Log.i("WTF", listNameArray.toString());
        mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listNameArray);

        lv.setOnItemClickListener(ListMasterActivity.this);
        lv.setOnItemLongClickListener(ListMasterActivity.this);

        lv.setAdapter(mainListAdapter);
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        namePos = listNameArray.get(position);
                        Intent update = new Intent(ListMasterActivity.this, IndListActivity.class);
                        update.putExtra("listName", namePos);
                        startActivity(update);
                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
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
                try {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("listMaster");
                    List<ParseObject> objects = query1.find();
                    ParseObject.pinAllInBackground(objects);
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        listNameArray = new ArrayList<>();

                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                Object object = list.get(i);


                                String name = ((ParseObject) object).getString("Name");
                                nameArray.add(name);


                            }

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                        for (int c = 0; c < nameArray.size(); c++) {
                            String compare = nameArray.get(c);
                            for (int l = 0; l < list.size(); l++) {
                                if (!listNameArray.contains(compare)) {
                                    listNameArray.add(compare);
                                    mainListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, com.parse.ParseException e) {

                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                Object object = list.get(i);


                                String name = ((ParseObject) object).getString("Name");
                                nameArray.add(name);


                            }

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }

                        for (int c = 0; c < nameArray.size(); c++) {
                            String compare = nameArray.get(c);
                            for (int l = 0; l < list.size(); l++) {
                                if (!listNameArray.contains(compare)) {
                                    listNameArray.add(compare);
                                    mainListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
            Log.i("WTF", listNameArray.toString());
            mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listNameArray);

            lv.setOnItemClickListener(ListMasterActivity.this);
            lv.setOnItemLongClickListener(ListMasterActivity.this);

            lv.setAdapter(mainListAdapter);

        }
    };

    private void resume() {
        Timer sched;
        sched = new Timer();
        sched.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_lock) {
            Intent lock = new Intent(this, LSignInActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_add) {
            Intent add = new Intent(this, NewListActivity.class);
            add.putExtra("listNameArray", listNameArray);
            this.startActivity(add);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}
