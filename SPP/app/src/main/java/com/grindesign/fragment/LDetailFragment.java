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
import android.widget.ListView;
import android.widget.TextView;

import com.ginddesign.spp.R;
import com.ginddesign.spp.addInfoCell;
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
    public static TextView dName;
    public static TextView ddob;
    TextView dss;
    public static TextView dAll;
    public static TextView dMed;
    ListView lv;
    public static JSONArray fileName = new JSONArray();
    public static JSONArray fileinfo = new JSONArray();
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> nameInfo = new ArrayList<>();
    public static ArrayAdapter<String> mainListAdapter;
    Context context;

    public LDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ldetail, container, false);

        dName = (TextView) view.findViewById(R.id.dName);
        ddob = (TextView) view.findViewById(R.id.ddob);
        dss = (TextView) view.findViewById(R.id.dss);
        dAll = (TextView) view.findViewById(R.id.dAll);
        dMed = (TextView) view.findViewById(R.id.dMed);
        final TextView dShot = (TextView) view.findViewById(R.id.dShot);
        lv = (ListView) view.findViewById(R.id.linkList);

        final Intent i = getActivity().getIntent();
        ois = i.getStringExtra("object ID");




        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            fileName = new JSONArray();
            fileinfo = new JSONArray();
            nameArray = new ArrayList<>();
            nameInfo = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("children");
                List<ParseObject> objects = query1.find();
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
                        fileName = object.getJSONArray("AdditionalNames");
                        fileinfo = object.getJSONArray("AdditionalInfo");
                        Log.i("TESSSSSST", fileinfo.toString());
                        try {
                            for (int i = 0; i < fileName.length(); i++) {
                                Log.i("Array", "Entry Point Done");
                                String name = fileName.getString(i);
                                String info = fileinfo.getString(i);

                                Log.i("TESSSSSST", name);
                                nameArray.add(name);
                                nameInfo.add(info);
                                Log.i("TESSSSSST", nameInfo.toString());
                                //loadList();
                            }
                            mainListAdapter.notifyDataSetChanged();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
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
                        fileName = object.getJSONArray("AdditionalNames");
                        fileinfo = object.getJSONArray("AdditionalInfo");
                        try {
                            for (int i = 0; i < fileName.length(); i++) {
                                Log.i("Array", "Entry Point Done");
                                String name = fileName.getString(i);
                                String info = fileinfo.getString(i);
                                Log.i("TESSSSSST", name);
                                nameArray.add(name);
                                nameInfo.add(info);

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
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
