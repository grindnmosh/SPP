package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ginddesign.spp.LIDetailActivity;
import com.ginddesign.spp.LImageActivity;
import com.ginddesign.spp.PhotoCell;
import com.ginddesign.spp.R;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LImageFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static ArrayAdapter<String> mainListAdapter;
    Context context;
    ImageButton snapShot;
    EditText picName;
    TextView imgIns;
    String name;
    Timer sched;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> createArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    ListView lv;

    public LImageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_limage, container, false);

        picName = (EditText) view.findViewById(R.id.nameImage);
        snapShot = (ImageButton) view.findViewById(R.id.snapPic);
        imgIns = (TextView) view.findViewById(R.id.imgIns);
        lv = (ListView) view.findViewById(R.id.ilist);

        nameArray = new ArrayList<>();
        createArray = new ArrayList<>();
        oidArray = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("images");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        name = object.getString("Name");
                        String oid = (object).getObjectId();
                        Date creation = (object).getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String date = sdf.format(creation);

                        nameArray.add(name);
                        oidArray.add(oid);
                        createArray.add(date);
                        nameArray.add(name);
                        if (nameArray.isEmpty()) {
                            imgIns.setVisibility(View.VISIBLE);
                        } else {
                            imgIns.setVisibility(View.INVISIBLE);
                        }

                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        String name = object.getString("Name");
                        String oid = (object).getObjectId();
                        Date creation = (object).getCreatedAt();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String date = sdf.format(creation);

                        nameArray.add(name);
                        oidArray.add(oid);
                        createArray.add(date);
                        if (nameArray.isEmpty()) {
                            imgIns.setVisibility(View.VISIBLE);
                        } else {
                            imgIns.setVisibility(View.INVISIBLE);
                        }
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        }
        mainListAdapter = new PhotoCell(context, R.layout.activity_photocell, nameArray);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);

        snapShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = picName.getText().toString().trim();
                if (!name.equals("")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Toast.makeText(context, "Please Provide Name of Image Before launching camera", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void TimerMethod()
    {
        getActivity().runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("images");
                    List<ParseObject> objects = query1.find();
                    ParseObject.pinAllInBackground(objects);
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        createArray = new ArrayList<>();
                        oidArray = new ArrayList<>();
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject object = objects.get(i);
                            name = object.getString("Name");
                            String oid = (object).getObjectId();
                            Date creation = (object).getCreatedAt();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(creation);

                            nameArray.add(name);
                            oidArray.add(oid);
                            createArray.add(date);
                            if (nameArray.isEmpty()) {
                                imgIns.setVisibility(View.VISIBLE);
                            } else {
                                imgIns.setVisibility(View.INVISIBLE);
                            }
                        }
                        mainListAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        createArray = new ArrayList<>();
                        oidArray = new ArrayList<>();
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject object = objects.get(i);
                            name = object.getString("Name");
                            String oid = (object).getObjectId();
                            Date creation = (object).getCreatedAt();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(creation);

                            nameArray.add(name);
                            oidArray.add(oid);
                            createArray.add(date);
                            if (nameArray.isEmpty()) {
                                imgIns.setVisibility(View.VISIBLE);
                            } else {
                                imgIns.setVisibility(View.INVISIBLE);
                            }
                        }
                        mainListAdapter.notifyDataSetChanged();
                    }
                });
            }
            mainListAdapter = new PhotoCell(context, R.layout.activity_photocell, nameArray);

            lv.setOnItemClickListener(LImageFragment.this);
            lv.setOnItemLongClickListener(LImageFragment.this);

            lv.setAdapter(mainListAdapter);
            Log.i("test", "run");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            String imgName = picName.getText().toString().trim();
            if (!imgName.equals("")) {
                String cleanName = imgName.replaceAll("\\s+", "");
                final ParseObject images = new ParseObject("images");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteMe = stream.toByteArray();
                ParseFile imageFile = new ParseFile(cleanName + ".png", byteMe);
                images.setACL(new ParseACL(ParseUser.getCurrentUser()));
                images.put("Name", imgName);
                images.put("spp_image", imageFile);
                images.pinInBackground();
                images.saveInBackground();
                name = "";
                picName.setText("");

            }
        }
    }


    public void onPause() {
        super.onPause();

        sched.cancel();
    }

    public void onResume() {
        super.onResume();
        sched = new Timer();
        sched.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 10000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String oidPos = oidArray.get(position);
        Log.i("OID", oidPos);
        Intent detail = new Intent(context, LIDetailActivity.class);
        detail.putExtra("Object ID", oidPos);
        startActivity(detail);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
