package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.grindesign.fragment.ListMasterFragment;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;


public class NewListActivity extends AppCompatActivity {

    String[] loads;
    Button cancel;
    Button save;
    Spinner s;
    Context context = this;
    String lName;
    String iName;
    String descrip;
    EditText listName;
    EditText itemName;
    EditText itemDescrip;
    public static ArrayList<String> listNameArray = new ArrayList<>();

    public static ArrayAdapter<String> loadsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlist);
        listNameArray = new ArrayList<>();
        listNameArray.add("Current Lists");
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        s = (Spinner) findViewById(R.id.listSpin);
        listName = (EditText) findViewById(R.id.listName);
        itemName = (EditText) findViewById(R.id.newItem);
        itemDescrip = (EditText) findViewById(R.id.itemDescrip);

        final Intent i = getIntent();
        listNameArray.addAll(i.getStringArrayListExtra("listNameArray"));

        loads = getResources().getStringArray(R.array.spinner);
        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, listNameArray);
        s.setAdapter(loadsAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loads[position].equals("Current Lists")) {
                    listName.setText("");
                } else {
                    listName.setText(loads[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lName = listName.getText().toString().trim();
                iName = itemName.getText().toString().trim();
                descrip = itemDescrip.getText().toString().trim();

                if (!lName.equals("") && !iName.equals("")) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {


                        ParseObject listMaster = new ParseObject("listMaster");
                        listMaster.put("Name", lName);
                        listMaster.put("item", iName);
                        listMaster.put("Descrip", descrip);
                        listMaster.put("isChecked", "false");
                        listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                        listMaster.pinInBackground();
                        listMaster.saveInBackground();
                        ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                        listName.setText("");
                        itemName.setText("");
                        itemDescrip.setText("");
                        s.setSelection(0);

                    } else {
                        ParseObject listMaster = new ParseObject("listMaster");
                        listMaster.put("Name", lName);
                        listMaster.put("item", iName);
                        listMaster.put("Descrip", descrip);
                        listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                        listMaster.pinInBackground();
                        listMaster.saveEventually();
                        ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                        listName.setText("");
                        itemName.setText("");
                        itemDescrip.setText("");
                        s.setSelection(0);
                    }

                } else {
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(NewListActivity.this, ListMasterActivity.class);
                startActivity(home);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newlist, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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
