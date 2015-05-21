package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class LSignInActivity extends AppCompatActivity {

    public LSignInActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsignin);

        ParseLoginBuilder builder = new ParseLoginBuilder(LSignInActivity.this);
        startActivityForResult(builder.build(), 0);
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (resultCode == RESULT_OK) {
            Intent home = new Intent(LSignInActivity.this, LockersActivity.class);
            startActivity(home);
        } else {
            finish();
        }
    }
}
