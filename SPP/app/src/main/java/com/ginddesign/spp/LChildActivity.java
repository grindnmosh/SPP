package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LChildActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String namePos;
    String oidPos;
    ListView lv;
    private Timer sched;
    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<String>();
    public static ArrayList<String> oidArray = new ArrayList<String>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lchild);

        lv = (ListView) findViewById(R.id.childList);

        String[] childList = getResources().getStringArray(R.array.childList);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("rf");
                List<ParseObject> objects = query1.find();
                //ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            //query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");

                            nameArray.add(name);
                            if (nameArray != null) {
                                Log.i("Array", nameArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });

            mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, nameArray);

            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);

            lv.setAdapter(mainListAdapter);
        }
        resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lchild, menu);
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
            ParseLoginBuilder builder = new ParseLoginBuilder(LChildActivity.this);
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
        else if (id == R.id.action_add) {
            Intent add = new Intent(this, LNewActivity.class);
            this.startActivity(add);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        oidPos = oidArray.get(position);
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);
                            String oid = ((ParseObject) object).getObjectId();
                            if (oidPos.equals(oid)) {
                                Log.i("ObjectID", oid);
                                Intent update = new Intent(LChildActivity.this, LDetailActivity.class);
                                update.putExtra("object ID", oid);
                                startActivity(update);
                            }



                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });


        }



    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {


            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {




                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("rf");
                query2.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List list, com.parse.ParseException e) {
                        Log.i("Array", "Entry POint Done");
                        nameArray = new ArrayList<String>();
                        oidArray = new ArrayList<String>();
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {

                                Object object = list.get(i);



                                String name = ((ParseObject) object).getString("Name");
                                String state = ((ParseObject) object).getString("State");
                                Number year = ((ParseObject) object).getNumber("Age");
                                String oid = ((ParseObject) object).getObjectId();



                                nameArray.add(name);
                                oidArray.add(oid);
                                if (nameArray != null) {
                                    Log.i("Array", oidArray.toString());
                                    mainListAdapter.notifyDataSetChanged();
                                }

                            }


                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });



            }else{
                //. Do Nothing
            }
        }
    };

    public void resume() {
        sched = new Timer();
        sched.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 5000);
    }


}
