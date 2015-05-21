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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;


public class ListMasterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String namePos;
    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> listNameArray = new ArrayList<>();
    Context context = this;
    public ListMasterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hit", "Hit");
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final ListView lv = (ListView) findViewById(R.id.list_master);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");

                            nameArray.add(name);

                            for (int c = 0; c < nameArray.size(); c++) {
                                String currentName = list.get(i).toString();
                                String compare = nameArray.get(i);
                                for (int l = 0; l < list.size(); l++) {
                                    if (!listNameArray.contains(compare)) {
                                        listNameArray.add(compare);
                                        mainListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            mainListAdapter.notifyDataSetChanged();
                            Log.i("WTF", listNameArray.toString());
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
            mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listNameArray);

            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);

            lv.setAdapter(mainListAdapter);
        }
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
            this.startActivity(add);
        }

        return super.onOptionsItemSelected(item);
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
}
