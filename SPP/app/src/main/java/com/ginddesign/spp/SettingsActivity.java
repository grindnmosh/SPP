package com.ginddesign.spp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;


public class SettingsActivity extends AppCompatActivity {

    EditText currUsername;
    EditText newUser;
    EditText newUserVer;
    Button setBut;
    String email1;
    String email2;
    String email3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currUsername = (EditText) findViewById(R.id.curUsername);
        newUser = (EditText) findViewById(R.id.newUser);
        newUserVer = (EditText) findViewById(R.id.newUserVer);
        setBut = (Button) findViewById(R.id.setBut);

        setBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1 = currUsername.getText().toString().trim();
                email2 = newUser.getText().toString().trim();
                email3 = newUserVer.getText().toString().trim();
                String PFUser = String.valueOf(ParseUser.getCurrentUser().getUsername());
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Log.i("START", "FIGHT");
                if (email1.equals(PFUser)) {
                    Log.i("ENTRY1", "ENTERED");
                    if (email2.equals(email3) && email2.matches(emailPattern) && email3.matches(emailPattern) && email2.length() > 0 ) {
                        Log.i("ENTRY2", "ENTERED");
                        final ParseUser user = ParseUser.getCurrentUser();
                        user.increment("logins");
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                                    query.whereEqualTo("email", email2);

                                    query.countInBackground(new CountCallback() {

                                        @Override
                                        public void done(int count, ParseException e) {
                                            if (e == null) {
                                                if (count == 0) {
                                                    user.setUsername(email2);
                                                    user.setEmail(email2);
                                                    user.saveInBackground();
                                                    Toast.makeText(getApplicationContext(), "Email Has Been Changed", Toast.LENGTH_LONG).show();
                                                    ParseUser.logOut();
                                                    try {
                                                        ParseUser.getCurrentUser().refresh();
                                                    } catch (ParseException d) {
                                                        d.printStackTrace();
                                                    }
                                                    ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.context);
                                                    startActivityForResult(builder.build(), 0);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "The email entered is already taken", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                    });

                                } else {
                                    Log.d("Failed", "Error: " + e.getMessage());
                                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        if (!email2.equals(email3)) {
                            Toast.makeText(getApplicationContext(), "The email you entered does not match in both fields", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter A Valid Email", Toast.LENGTH_LONG).show();
                        }
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
