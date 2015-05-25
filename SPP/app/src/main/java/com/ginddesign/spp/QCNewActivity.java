package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class QCNewActivity extends AppCompatActivity {

    String[] loads;
    String cat;
    String name;
    String phone;
    String email;
    String notes;
    String compareValue;
    EditText qcName;
    EditText qcPhone;
    EditText qcEmail;
    EditText qcNotes;
    Button cancel;
    Button save;
    TextView title;
    Spinner s;
    Context context = this;
    public static ArrayAdapter<String> loadsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcnew);

        final Intent i = getIntent();
        String pageTitle = i.getStringExtra("Title");
        final String ois = i.getStringExtra("Object ID");

        title = (TextView) findViewById(R.id.title);
        save = (Button) findViewById(R.id.save1);
        cancel = (Button) findViewById(R.id.cancel1);
        qcName = (EditText) findViewById(R.id.qcName);
        qcPhone = (EditText) findViewById(R.id.qcPhone);
        qcEmail = (EditText) findViewById(R.id.qcEmail);
        qcNotes = (EditText) findViewById(R.id.qcNotes);
        s = (Spinner) findViewById(R.id.qcSpin);

        title.setText(pageTitle);

        if (pageTitle.equals("Edit Contact")) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                query.getInBackground(ois, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            phone = object.getString("Phone");
                            email = object.getString("Email");
                            notes = object.getString("Notes");
                            compareValue = object.getString("Cat");
                            qcName.setText(name);
                            qcPhone.setText(phone);
                            qcEmail.setText(email);
                            qcNotes.setText(notes);

                            int spinnerPostion = loadsAdapter.getPosition(compareValue);
                            s.setSelection(spinnerPostion);

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            }else{
                ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                query.fromLocalDatastore();
                query.getInBackground(ois, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            phone = object.getString("Phone");
                            email = object.getString("Email");
                            notes = object.getString("Notes");
                            qcName.setText(name);
                            qcPhone.setText(phone);
                            qcEmail.setText(email);
                            qcNotes.setText(notes);

                            int spinnerPostion = loadsAdapter.getPosition(compareValue);
                            s.setSelection(spinnerPostion);


                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        }

        loads = getResources().getStringArray(R.array.qc_cat);
        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, loads);
        s.setAdapter(loadsAdapter);




        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loads[position].equals("Current Lists")) {
                    cat = loads[position].trim();
                }
                else {
                    cat = loads[position].trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = qcName.getText().toString().trim();
                phone = qcPhone.getText().toString().trim();
                email = qcEmail.getText().toString().trim();
                notes = qcNotes.getText().toString().trim();

                if (!cat.equals("Current Lists") && !name.equals("") && !phone.equals("")) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                        query.getInBackground(ois, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Cat", cat);
                                object.put("Name", name);
                                object.put("Phone", phone);
                                object.put("Email", email);
                                object.put("Notes", notes);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveInBackground();
                                ListMasterActivity.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(QCNewActivity.this, QuickContactActivity.class);
                                startActivity(home);
                            }
                        });

                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                        query.fromLocalDatastore();
                        query.getInBackground(ois, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Cat", cat);
                                object.put("Name", name);
                                object.put("Phone", phone);
                                object.put("Email", email);
                                object.put("Notes", notes);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveEventually();
                                ListMasterActivity.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(QCNewActivity.this, QuickContactActivity.class);
                                startActivity(home);
                            }
                        });
                    }

                }else{
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(QCNewActivity.this, QuickContactActivity.class);
                startActivity(home);
            }
        });
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
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
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
