package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class QCDetailActivity extends AppCompatActivity {

    TextView conName;
    TextView conPhone;
    TextView conEmail;
    TextView conNotes;
    ImageButton makeCall;
    ImageButton sendMess;
    ImageButton sendEmail;
    ImageButton conEdit;
    String ois;
    String name;
    String phone;
    String email;
    String notes;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcdetail);

        conName = (TextView) findViewById(R.id.conName);
        conPhone = (TextView) findViewById(R.id.conPhone);
        conEmail = (TextView) findViewById(R.id.conEmail);
        conNotes = (TextView) findViewById(R.id.conNotes);
        makeCall = (ImageButton) findViewById(R.id.makeCall);
        sendMess = (ImageButton) findViewById(R.id.sendMess);
        sendEmail = (ImageButton) findViewById(R.id.sendEmail);
        conEdit = (ImageButton) findViewById(R.id.conEdit);

        final Intent i = getIntent();
        ois = i.getStringExtra("Object ID");


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
                        conName.setText(name);
                        conPhone.setText(phone);
                        conEmail.setText(email);
                        conNotes.setText(notes);

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
                        conName.setText(name);
                        conPhone.setText(phone);
                        conEmail.setText(email);
                        conNotes.setText(notes);


                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, (Uri.parse("tel:" + conPhone.getText())));
                startActivity(Intent.createChooser(intent, "Call From"));
            }
        });

        sendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + conPhone.getText()));
                startActivity(Intent.createChooser(intent, "Message From"));
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", String.valueOf(conEmail.getText()), null));
                startActivity(Intent.createChooser(emailIntent, "Email From"));
            }
        });

        conEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(QCDetailActivity.this, QCNewActivity.class);
                add.putExtra("Title", "Edit Contact");
                add.putExtra("Object ID", ois);
                startActivity(add);
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qcdetail, menu);
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
            Intent logout = new Intent(this, MainActivity.class);
            this.startActivity(logout);
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
            Intent add = new Intent(this, LNewActivity.class);
            this.startActivity(add);
        }
        else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, conName.getText() + " " + conPhone.getText() + " " + conEmail.getText() + " " + conNotes.getText());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        }

        return super.onOptionsItemSelected(item);
    }
}
