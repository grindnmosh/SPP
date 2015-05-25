package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;


public class IndListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> itemArray = new ArrayList<>();
    public static ArrayList<String> desArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    public static ArrayList<String> cbArray = new ArrayList<>();
    Context context = this;
    String passedName;
    CheckBox check;
    TextView listName;
    Boolean isChecked;
    String oid;
    android.support.v7.widget.ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indlist);

        final Intent i = getIntent();
        passedName = i.getStringExtra("listName");

        listName = (TextView) findViewById(R.id.indListName);
        final ListView lv = (ListView) findViewById(R.id.list);
        check = (CheckBox) findViewById(R.id.checkBox);

        listName.setText(passedName);



        nameArray = new ArrayList<>();
        itemArray = new ArrayList<>();
        desArray = new ArrayList<>();
        oidArray = new ArrayList<>();
        cbArray = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("listMaster");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.whereEqualTo("Name", passedName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (ParseObject listmasterobject : list) {
                            String name = listmasterobject.get("Name").toString();
                            String item = listmasterobject.get("item").toString();
                            String descrip = listmasterobject.get("Descrip").toString();
                            String checkBox = listmasterobject.get("isChecked").toString();
                            String oid = (listmasterobject).getObjectId();
                            Log.i("TEST Run", name);
                            nameArray.add(name);
                            itemArray.add(item);
                            desArray.add(descrip);
                            oidArray.add(oid);
                            cbArray.add(checkBox);
                            Log.i("TEST Run", desArray.toString());
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.whereEqualTo("Name", passedName);
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (ParseObject listmasterobject : list) {
                            String name = listmasterobject.get("Name").toString();
                            String item = listmasterobject.get("item").toString();
                            String descrip = listmasterobject.get("Descrip").toString();
                            String checkBox = listmasterobject.get("isChecked").toString();
                            String oid = (listmasterobject).getObjectId();
                            Log.i("TEST Run", name);
                            nameArray.add(name);
                            itemArray.add(item);
                            desArray.add(descrip);
                            oidArray.add(oid);
                            cbArray.add(checkBox);
                            Log.i("TEST Run", desArray.toString());
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        mainListAdapter = new IndListCell(context, R.layout.activity_indlistcell, itemArray, oidArray);

        lv.setAdapter(mainListAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_indlist, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
            } catch (ParseException d) {
                d.printStackTrace();
            }
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.context);
            startActivityForResult(builder.build(), 0);
        }
        else if (id == R.id.action_qc) {
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent lock = new Intent(this, ListMasterActivity.class);
            this.startActivity(lock);
        }
        else if (id == R.id.action_add) {
            Intent add = new Intent(this, NewListActivity.class);
            this.startActivity(add);
        }
        else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(itemArray));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
