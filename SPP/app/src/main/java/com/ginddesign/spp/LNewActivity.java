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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;


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
    TextView cTitle;
    String name;
    String dob;
    String ss;
    String allergy;
    String med;
    String shot;
    String oid;
    Context context = this;
    JSONArray fileName = new JSONArray();
    JSONArray fileInfo = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnew);

        final Intent i = getIntent();
        String pageTitle = i.getStringExtra("Title");
        oid = i.getStringExtra("Object ID");

        cTitle = (TextView) findViewById(R.id.cTitle);
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

            cTitle.setText(pageTitle);
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            dob = object.getString("dob");
                            ss = object.getString("SS");
                            allergy = object.getString("Allergies");
                            med = object.getString("Medical");
                            shot = object.getString("Shot");
                            fileName = object.getJSONArray("AdditionalNames");
                            fileInfo = object.getJSONArray("AdditionalInfo");
                            cName.setText(name);
                            cdob.setText(dob);
                            css.setText(ss);
                            cAllergy.setText(allergy);
                            cMed.setText(med);
                            cShot.setText(shot);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            }else{
                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                query.fromLocalDatastore();
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            dob = object.getString("dob");
                            ss = object.getString("ss");
                            allergy = object.getString("Allergies");
                            med = object.getString("Medical");
                            shot = object.getString("Shot");
                            fileName = object.getJSONArray("AdditionalNames");
                            fileInfo = object.getJSONArray("AdditionalInfo");
                            cName.setText(name);
                            cdob.setText(dob);
                            css.setText(ss);
                            cAllergy.setText(allergy);
                            cMed.setText(med);
                            cShot.setText(shot);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });
            }


        indSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cdn = cDocName.getText().toString().trim();
                String cdl = cDocLink.getText().toString().trim();

                fileName.put(cdn);
                fileInfo.put(cdl);
                Log.i("Saved", fileName.toString());
                cDocName.setText("");
                cDocLink.setText("");
                Toast.makeText(context, "Document Temporarily saved, Save It to finalize", Toast.LENGTH_SHORT).show();
            }
        });

        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cName.getText().toString().trim();
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

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                        query.getInBackground(oid, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Name", name);
                                object.put("dob", dob);
                                object.put("SS", ss);
                                object.put("Allergies", allergy);
                                object.put("Medical", med);
                                object.put("Shot", shot);
                                object.put("AdditionalNames", fileName);
                                object.put("AdditionalInfo", fileInfo);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveInBackground();
                                LChildActivity.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(LNewActivity.this, LChildActivity.class);
                                startActivity(home);
                            }
                        });

                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                        query.fromLocalDatastore();
                        query.getInBackground(oid, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Name", name);
                                object.put("dob", dob);
                                object.put("SS", ss);
                                object.put("Allergies", allergy);
                                object.put("Medical", med);
                                object.put("Shot", shot);
                                object.put("AdditionalNames", fileName);
                                object.put("AdditionalInfo", fileInfo);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveEventually();
                                LChildActivity.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(LNewActivity.this, LChildActivity.class);
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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent lock = new Intent(this, ListMasterActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_image) {
            Intent add = new Intent(this, LImageActivity.class);
            this.startActivity(add);
        }
        return super.onOptionsItemSelected(item);
    }
}
