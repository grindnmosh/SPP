package com.ginddesign.spp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class LockersActivity extends ActionBarActivity {

    Button cLocker;
    Button iLocker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockers);

        cLocker = (Button) findViewById(R.id.cLButt);
        iLocker = (Button) findViewById(R.id.iLButt);



        cLocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LockersActivity.this, LChildActivity.class);
                startActivity(home);
            }
        });

        iLocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LockersActivity.this, LImageActivity.class);
                startActivity(home);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lockers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ParseUser.logOut();
            ParseLoginBuilder builder = new ParseLoginBuilder(LockersActivity.this);
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

        return super.onOptionsItemSelected(item);
    }
}
