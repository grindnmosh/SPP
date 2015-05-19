package com.ginddesign.spp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
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

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hit", "Hit");
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "bIfkzLusNLlewJ7kGFhHq7WhnHtt0feiUiAYnZ1k", "REgMp3bU0c5bubYdCL9QphwvlFqmkEtep0gN3pZT");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        ParseACL.setDefaultACL(defaultACL, true);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), 0);

        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Log.i("DO", "NOTHING");

            } else {
                ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                startActivityForResult(builder.build(), 0);

            }
        }
        final ListView lv = (ListView) findViewById(R.id.list_master);

        String[] listMaster = getResources().getStringArray(R.array.listMaster);

        mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listMaster);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);
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
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), 0);
            finish();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(MainActivity.this, IndListActivity.class);
        startActivity(detail);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
