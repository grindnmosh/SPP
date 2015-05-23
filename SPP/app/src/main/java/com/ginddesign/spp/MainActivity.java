package com.ginddesign.spp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;



public class MainActivity extends AppCompatActivity {



    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
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
                Intent home = new Intent(MainActivity.this, ListMasterActivity.class);
                startActivity(home);
            } else {
                ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                startActivityForResult(builder.build(), 0);
            }

        }
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (resultCode == RESULT_OK) {
            Intent home = new Intent(MainActivity.this, ListMasterActivity.class);
            startActivity(home);
        } else {
            finish();
        }


    }
}
