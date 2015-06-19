package com.ginddesign.spp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
    Button canBooty;
    EditText cDocName;
    EditText cDocLink;
    Context context;
    String oid;
    String ois;
    String cdn;
    String cdl;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);

        cDocName = (EditText) findViewById(R.id.docName);
        cDocLink = (EditText) findViewById(R.id.docLink);
        indSave = (Button) findViewById(R.id.indSave);
        canBooty = (Button) findViewById(R.id.canBooty);

        //oid = "New";
        final Intent i = getIntent();
        oid = i.getStringExtra("Object ID");
        ois = i.getStringExtra("object ID");
        name = i.getStringExtra("Name");
        Log.i("TEST", name);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (!oid.equals("New")) {
            Log.i("Test", "test");
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(name);
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            String name = object.getString("AddName");
                            String item = object.getString("AddItem");
                            Log.i("Test", name);
                            Log.i("Test", item);
                            cDocName.setText(name);
                            cDocLink.setText(item);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(name);
                query.fromLocalDatastore();
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            String name = object.getString("AddName");
                            String item = object.getString("AddItem");

                            cDocName.setText(name);
                            cDocLink.setText(item);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            }
        }

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

                            ParseQuery<ParseObject> query = ParseQuery.getQuery(name);
                            query.getInBackground(oid, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, com.parse.ParseException e) {
                                    object.put("AddName", cdn);
                                    object.put("AddItem", cdl);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveInBackground();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(AddInfoActivity.this, LDetailActivity.class);
                                    home.putExtra("object ID", ois);
                                    startActivity(home);
                                }
                            });

                        } else {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery(name);
                            query.fromLocalDatastore();
                            query.getInBackground(oid, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, com.parse.ParseException e) {
                                    object.put("AddName", cdn);
                                    object.put("AddItem", cdl);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveEventually();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(AddInfoActivity.this, LDetailActivity.class);
                                    home.putExtra("object ID", ois);
                                    startActivity(home);
                                }
                            });
                        }
                    } else {
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            ParseObject addIt = new ParseObject(name);
                            addIt.put("AddName", cdn);
                            addIt.put("AddItem", cdl);
                            addIt.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            addIt.pinInBackground();
                            addIt.saveInBackground();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
                            Intent home = new Intent(AddInfoActivity.this, LDetailActivity.class);
                            home.putExtra("object ID", ois);
                            startActivity(home);
                        } else {
                            ParseObject object = new ParseObject(name);
                            object.put("AddName", cdn);
                            object.put("AddItem", cdl);
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveEventually();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
                            Intent home = new Intent(AddInfoActivity.this, LDetailActivity.class);
                            home.putExtra("object ID", ois);
                            startActivity(home);
                        }
                    }

                    //Toast.makeText(context, "Document saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Nothing was entered to save. Save not completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        canBooty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(AddInfoActivity.this, LDetailActivity.class);
                home.putExtra("object ID", ois);
                startActivity(home);
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

        if (id == R.id.action_settings) {
            ParseUser.logOut();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        } else if (id == R.id.action_qc) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent qc = new Intent(AddInfoActivity.this, QuickContactActivity.class);
                    startActivity(qc);
                }
            });
            lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            lockExit.setIcon(android.R.drawable.ic_dialog_alert);
            lockExit.show();
        } else if (id == R.id.action_home) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent home = new Intent(AddInfoActivity.this, ListMasterActivity.class);
                    startActivity(home);
                }
            });
            lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            lockExit.setIcon(android.R.drawable.ic_dialog_alert);
            lockExit.show();
        } else if (id == R.id.action_image) {
            Intent image = new Intent(this, LImageActivity.class);
            this.startActivity(image);
        } else if (id == R.id.action_child) {
            Intent child = new Intent(this, LChildActivity.class);
            this.startActivity(child);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent home = new Intent(this, LDetailActivity.class);
        home.putExtra("object ID", ois);
        this.startActivity(home);
    }
}
