package com.ginddesign.spp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class QCNewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcnew);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qcnew, menu);
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
            try {
                ParseUser.getCurrentUser().refresh();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.context);
            startActivityForResult(builder.build(), 0);
            this.finish();
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_lock) {
            Intent lock = new Intent(this, LSignInActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_home) {
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }

        return super.onOptionsItemSelected(item);
    }
}
