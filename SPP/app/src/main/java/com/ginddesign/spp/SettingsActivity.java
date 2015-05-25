package com.ginddesign.spp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginConfig;


public class SettingsActivity extends AppCompatActivity {

    EditText currUsername;
    EditText newUser;
    EditText newUserVer;
    Button setBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currUsername = (EditText) findViewById(R.id.curUsername);
        newUser = (EditText) findViewById(R.id.newUser);
        newUserVer = (EditText) findViewById(R.id.newUserVer);
        setBut = (Button) findViewById(R.id.setBut);

        final ParseUser parseUser = ParseUser.getCurrentUser();

        setBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("START", "FIGHT");
                Log.i("USERNAME", String.valueOf(currUsername.getText()));
                Log.i("PARSEUSER", String.valueOf(ParseUser.getCurrentUser().getUsername()));
                if (String.valueOf(currUsername.getText()).equals(String.valueOf(ParseUser.getCurrentUser().getUsername()))) {
                    Log.i("ENTRY1", "ENTERED");
                    if (newUser.getText().toString().equals(newUserVer.getText().toString())) {
                        Log.i("ENTRY2", "ENTERED");
                        final ParseUser user = ParseUser.getCurrentUser();
                        user.increment("logins");
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    user.setUsername(newUser.getText().toString());
                                    user.setEmail(newUser.getText().toString());
                                    user.saveInBackground();
                                } else {
                                    if (e != null) {
                                        switch (e.getCode()) {
                                            case ParseException.INVALID_EMAIL_ADDRESS:
                                                Toast.makeText(getApplicationContext(), "Please Provide Valid Email", Toast.LENGTH_LONG).show();
                                                break;
                                            case ParseException.EMAIL_TAKEN:
                                                Toast.makeText(getApplicationContext(), "Requested Email is Already Taken", Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), "Please Ensure All Fields Are Populated", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }

        return super.onOptionsItemSelected(item);
    }
}
