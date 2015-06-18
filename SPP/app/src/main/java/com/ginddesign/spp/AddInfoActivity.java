package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grindesign.fragment.LChildFragment;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;



public class AddInfoActivity extends ActionBarActivity {

    Button indSave;
    EditText cDocName;
    EditText cDocLink;
    Context context;
    String oid;
    String cdn;
    String cdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);

        cDocName = (EditText) findViewById(R.id.docName);
        cDocLink = (EditText) findViewById(R.id.docLink);
        indSave = (Button) findViewById(R.id.indSave);
        oid = "New";


        indSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdn = cDocName.getText().toString().trim();
                cdl = cDocLink.getText().toString().trim();

                if (!cdn.equals("") && !cdl.equals("")) {
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (!oid.equals("New")) {
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("AddInfo");
                            query.getInBackground(oid, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, com.parse.ParseException e) {
                                    object.put("AddName", cdn);
                                    object.put("AddItem", cdl);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveInBackground();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(AddInfoActivity.this, LChildActivity.class);
                                    startActivity(home);
                                }
                            });

                        } else {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("AddInfo");
                            query.fromLocalDatastore();
                            query.getInBackground(oid, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, com.parse.ParseException e) {
                                    object.put("AddName", cdn);
                                    object.put("AddItem", cdl);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveEventually();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(AddInfoActivity.this, LChildActivity.class);
                                    startActivity(home);
                                }
                            });
                        }
                    } else {
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            ParseObject addIt = new ParseObject("AddInfo");
                            addIt.put("AddName", cdn);
                            addIt.put("AddItem", cdl);
                            addIt.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            addIt.pinInBackground();
                            addIt.saveInBackground();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
                            Intent home = new Intent(AddInfoActivity.this, LChildActivity.class);
                            startActivity(home);
                        } else {
                            ParseObject object = new ParseObject("AddInfo");
                            object.put("AddName", cdn);
                            object.put("AddItem", cdl);
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveEventually();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
                            Intent home = new Intent(AddInfoActivity.this, LChildActivity.class);
                            startActivity(home);
                        }
                    }

                    //Toast.makeText(context, "Document saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Nothing was entered to save. Save not completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addinfo, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
