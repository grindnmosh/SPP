package com.ginddesign.spp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "bIfkzLusNLlewJ7kGFhHq7WhnHtt0feiUiAYnZ1k", "REgMp3bU0c5bubYdCL9QphwvlFqmkEtep0gN3pZT");
        ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        startActivityForResult(builder.build(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
