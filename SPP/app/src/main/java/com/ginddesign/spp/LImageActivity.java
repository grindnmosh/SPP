package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class LImageActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static ArrayAdapter<String> mainListAdapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limage);



        final ListView lv = (ListView) findViewById(R.id.ilist);

        String[] imageList = getResources().getStringArray(R.array.ilist);

        mainListAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, imageList);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);
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
            ParseLoginBuilder builder = new ParseLoginBuilder(LImageActivity.this);
            startActivityForResult(builder.build(), 0);
            finish();
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent lock = new Intent(this, MainActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_add) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(LImageActivity.this, LIDetailActivity.class);
        startActivity(detail);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
