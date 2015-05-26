package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.grindesign.fragment.LDetailFragment;
import com.grindesign.fragment.LIDetailFragment;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class LIDetailActivity extends AppCompatActivity {

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lidetail);
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
            Intent home = new Intent(this, ListMasterActivity.class);
            this.startActivity(home);
        }
        else if (id == R.id.action_share) {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("images");
                    List<ParseObject> objects = query1.find();
                    ParseObject.pinAllInBackground(objects);
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                ParseQuery<ParseObject> query = new ParseQuery<>("images");
                query.getInBackground(LIDetailFragment.oid, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, com.parse.ParseException e) {
                        ParseFile fileObject = (ParseFile) object.get("spp_image");
                        String imgName = object.getString("Name");
                        TextView nameIt = (TextView) findViewById(R.id.textView26);
                        nameIt.setText(imgName);
                        fileObject.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                if (e == null) {
                                    Log.d("test", "We've got data in data.");
                                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                                            bmp, "Title", null);
                                    Uri imageUri = Uri.parse(path);
                                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                                    startActivity(Intent.createChooser(share, "Select"));
                                } else {
                                    Log.d("test", "There was a problem downloading the data.");
                                }
                            }
                        });
                    }
                });
            } else {
                ParseQuery<ParseObject> query = new ParseQuery<>("images");
                query.fromLocalDatastore();
                query.getInBackground(LIDetailFragment.oid, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, com.parse.ParseException e) {
                        ParseFile fileObject = (ParseFile) object.get("spp_image");
                        String imgName = object.getString("Name");
                        TextView nameIt = (TextView) findViewById(R.id.textView26);
                        nameIt.setText(imgName);
                        fileObject.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                if (e == null) {
                                    Log.d("test", "We've got data in data.");
                                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                                            bmp, "Title", null);
                                    Uri imageUri = Uri.parse(path);
                                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                                    startActivity(Intent.createChooser(share, "Select"));
                                } else {
                                    Log.d("test", "There was a problem downloading the data.");
                                }
                            }
                        });
                    }
                });
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
