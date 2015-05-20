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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;


public class LNewActivity extends AppCompatActivity {

    Button saveIt;
    Button cancel;
    Button indSave;
    EditText cName;
    EditText cdob;
    EditText css;
    EditText cAllergy;
    EditText cMed;
    EditText cShot;
    EditText cDocName;
    EditText cDocLink;
    String name;
    String dob;
    String ss;
    String allergy;
    String med;
    String shot;
    String docName;
    String docLink;
    Context context = this;
    ArrayList<String> file = new ArrayList<String>();
    ArrayList<String> drawer = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnew);

        cName = (EditText) findViewById(R.id.cName);
        cdob = (EditText) findViewById(R.id.cdob);
        css = (EditText) findViewById(R.id.css);
        cAllergy = (EditText) findViewById(R.id.cAllergy);
        cMed = (EditText) findViewById(R.id.cMed);
        cShot = (EditText) findViewById(R.id.cShot);
        cDocName = (EditText) findViewById(R.id.docName);
        cDocLink = (EditText) findViewById(R.id.docLink);
        saveIt = (Button) findViewById(R.id.saveIt);
        cancel = (Button) findViewById(R.id.cancel2);
        indSave = (Button) findViewById(R.id.indSave);


        indSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cdn = cDocName.getText().toString().trim();
                String cdl = cDocLink.getText().toString().trim();

                file.add(cdn);
                file.add(cdl);
                drawer.addAll(file);
                Log.i("Saved", drawer.toString());
                file = new ArrayList<String>();
                cDocName.setText("");
                cDocLink.setText("");
                Toast.makeText(context, "Document Temporarily saved, Save It to finalize", Toast.LENGTH_SHORT).show();
            }
        });

        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cName.getText().toString().trim();
                Log.i("NAME", name);
                dob = cdob.getText().toString().trim();
                ss = css.getText().toString().trim();
                allergy = cAllergy.getText().toString().trim();
                med = cMed.getText().toString().trim();
                shot = cShot.getText().toString().trim();

                if (!name.equals("")) {
                    Log.i("Enter", "The Dragon");
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                        Log.i("Parse", "Entered");
                        final ParseObject children = new ParseObject("children");
                        children.put("Name", name);
                        children.put("dob", dob);
                        children.put("SS", ss);
                        children.put("Allergies", allergy);
                        children.put("Medical", med);
                        children.put("Shot", shot);
                        Log.i("children", children.toString());
                        children.put("Additional", drawer);
                        children.setACL(new ParseACL(ParseUser.getCurrentUser()));
                        //listMaster.pinInBackground();
                        children.saveInBackground();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                        Log.i("QUERY", query.toString());
                        query.whereEqualTo("Name", "Michael");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, com.parse.ParseException e) {
                                if (e == null) {
                                    Log.d("Names", "Retrieved " + list.size() + " names");
                                } else {
                                    Log.d("Names", "Error: " + e.getMessage());
                                }
                            }
                        });
                        LChildActivity.mainListAdapter.notifyDataSetChanged();
                        Intent home = new Intent(LNewActivity.this, LChildActivity.class);
                        startActivity(home);

                    } else {
                        ParseObject children = new ParseObject("children");
                        children.put("Name", name);
                        children.put("dob", dob);
                        children.put("SS", ss);
                        children.put("Allergies", allergy);
                        children.put("Medical", med);
                        children.put("Shots", shot);
                        children.put("Additional", drawer);
                        children.setACL(new ParseACL(ParseUser.getCurrentUser()));
                        //listMaster.pinInBackground();
                        children.saveEventually();
                        LChildActivity.mainListAdapter.notifyDataSetChanged();

                    }

                }else{
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LNewActivity.this, LChildActivity.class);
                startActivity(home);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lnew, menu);
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
            ParseLoginBuilder builder = new ParseLoginBuilder(LNewActivity.this);
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
        else if (id == R.id.action_image) {
            Intent add = new Intent(this, LImageActivity.class);
            this.startActivity(add);
        }
        return super.onOptionsItemSelected(item);
    }
}
