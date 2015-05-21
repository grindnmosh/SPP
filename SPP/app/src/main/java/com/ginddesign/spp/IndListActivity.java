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

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class IndListActivity extends AppCompatActivity {

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> itemArray = new ArrayList<>();
    public static ArrayList<String> desArray = new ArrayList<>();
    Context context = this;
    String passedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indlist);

        final Intent i = getIntent();
        passedName = i.getStringExtra("listName");

        final ListView lv = (ListView) findViewById(R.id.list);
        nameArray = new ArrayList<>();
        itemArray = new ArrayList<>();
        desArray = new ArrayList<>();
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
            query.whereEqualTo("Name", passedName);
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (ParseObject listmasterobject : list) {
                            String name = listmasterobject.get("Name").toString();
                            String item = listmasterobject.get("item").toString();
                            String descrip = listmasterobject.get("Descrip").toString();
                            Log.i("TEST Run", name);
                            nameArray.add(name);
                            itemArray.add(item);
                            desArray.add(descrip);
                            Log.i("TEST Run", desArray.toString());
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        mainListAdapter = new IndListCell(context, R.layout.activity_indlistcell, itemArray);

        lv.setAdapter(mainListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_indlist, menu);
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
            Intent logout = new Intent(this, MainActivity.class);
            this.startActivity(logout);
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
            Intent add = new Intent(this, NewListActivity.class);
            this.startActivity(add);
        }
        else if (id == R.id.action_share) {
            Log.i("DO", "NOTHING");
        }

        return super.onOptionsItemSelected(item);
    }
}
