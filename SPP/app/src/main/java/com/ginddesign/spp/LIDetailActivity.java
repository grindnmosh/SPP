package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;


public class LIDetailActivity extends AppCompatActivity {

    Context context;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lidetail);

        final Intent i = getIntent();
        String oid = i.getStringExtra("Object ID");

        ParseQuery<ParseObject> query = new ParseQuery<>(
                "images");
        query.getInBackground(oid, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                ParseFile fileObject = (ParseFile) object.get("spp_image");
                name = object.getString("Name");
                TextView nameOf = (TextView) findViewById(R.id.textView26);
                nameOf.setText(name);
                fileObject.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test", "We've got data in data.");
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            ImageView image = (ImageView) findViewById(R.id.imageView2);
                            image.setImageBitmap(bmp);
                        } else {
                            Log.d("test", "There was a problem downloading the data.");
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lidetail, menu);
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
        else if (id == R.id.action_home) {
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }
        else if (id == R.id.action_share) {
            Log.i("DO", "NOTHING");
        }

        return super.onOptionsItemSelected(item);
    }
}
