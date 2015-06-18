package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ginddesign.spp.AddInfoActivity;
import com.ginddesign.spp.QuickContactActivity;
import com.ginddesign.spp.R;
import com.ginddesign.spp.addInfoCell;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class LDetailFragment extends Fragment {

    public static String ois;
    String name;
    String dob;
    String ss;
    String all;
    String med;
    String shot;
    ImageButton addInfoButt;
    public static TextView dName;
    public static TextView ddob;
    TextView dss;
    public static TextView dAll;
    public static TextView dMed;
    ListView lv;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> nameInfo = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    public static ArrayAdapter<String> mainListAdapter;
    Context context;

    public LDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ldetail, container, false);

        dName = (TextView) view.findViewById(R.id.dName);
        ddob = (TextView) view.findViewById(R.id.ddob);
        dss = (TextView) view.findViewById(R.id.dss);
        dAll = (TextView) view.findViewById(R.id.dAll);
        dMed = (TextView) view.findViewById(R.id.dMed);
        addInfoButt = (ImageButton) view.findViewById(R.id.addAdd);
        final TextView dShot = (TextView) view.findViewById(R.id.dShot);
        lv = (ListView) view.findViewById(R.id.linkList);

        final Intent i = getActivity().getIntent();
        ois = i.getStringExtra("object ID");

        addInfoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qc = new Intent(context, AddInfoActivity.class);
                context.startActivity(qc);
            }
        });


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            nameArray = new ArrayList<>();
            nameInfo = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("children");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            try {
                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("AddInfo");
                List<ParseObject> objects = query3.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.getInBackground(ois, new GetCallback<ParseObject>() {
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        name = object.getString("Name");
                        dob = object.getString("dob");
                        ss = object.getString("SS");
                        all = object.getString("Allergies");
                        med = object.getString("Medical");
                        shot = object.getString("Shot");
                        dName.setText(name);
                        ddob.setText(dob);
                        dss.setText(ss);
                        dAll.setText(all);
                        dMed.setText(med);
                        dShot.setText(shot);
                        Linkify.addLinks(dShot, Linkify.WEB_URLS);

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }

                }
            });
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("AddInfo");
            query2.findInBackground(new FindCallback<ParseObject>() {
                public void done(List list, com.parse.ParseException e) {
                    if (e == null) {

                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);
                            String name = ((ParseObject) object).getString("AddName");
                            String info = ((ParseObject) object).getString("AddItem");
                            String oid = ((ParseObject) object).getObjectId();
                            nameArray.add(name);
                            nameInfo.add(info);
                            oidArray.add(oid);
                            Log.i("TESSSSSST", nameInfo.toString());
                            //loadList();
                        }
                        mainListAdapter.notifyDataSetChanged();

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }

                }
            });
        }else{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.fromLocalDatastore();
            query.getInBackground(ois, new GetCallback<ParseObject>() {
                public void done(ParseObject object, com.parse.ParseException e) {
                    if (e == null) {
                        name = object.getString("Name");
                        dob = object.getString("Age");
                        ss = object.getString("SS");
                        all = object.getString("Allergies");
                        med = object.getString("Medical");
                        shot = object.getString("Shot");
                        dName.setText(name);
                        ddob.setText(dob);
                        dss.setText(ss);
                        dAll.setText(all);
                        dMed.setText(med);
                        dShot.setText(shot);
                        Linkify.addLinks(dShot, Linkify.WEB_URLS);


                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("AddInfo");
            query2.fromLocalDatastore();
            query2.findInBackground(new FindCallback<ParseObject>() {
                public void done(List list, com.parse.ParseException e) {
                    if (e == null) {

                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);
                            String name = ((ParseObject) object).getString("AddName");
                            String info = ((ParseObject) object).getString("AddItem");
                            String oid = ((ParseObject) object).getObjectId();
                            nameArray.add(name);
                            nameInfo.add(info);
                            oidArray.add(oid);
                            Log.i("TESSSSSST", nameInfo.toString());
                            //loadList();
                        }
                        mainListAdapter.notifyDataSetChanged();

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }

                }
            });
        }
        loadList();

        return view;
    }

    public void loadList() {
        mainListAdapter = new addInfoCell(context, R.layout.activity_add_info_cell, nameArray);

        lv.setAdapter(mainListAdapter);
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
