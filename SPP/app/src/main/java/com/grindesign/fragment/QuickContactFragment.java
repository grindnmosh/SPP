package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ginddesign.spp.ContactCell;
import com.ginddesign.spp.QCDetailActivity;
import com.ginddesign.spp.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class QuickContactFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> catArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    public static ArrayList<String> phoneArray = new ArrayList<>();
    Context context;
    TextView qIns;

    public QuickContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_quickcontact, container, false);

        final ListView lv = (ListView) view.findViewById(R.id.qcList);
        qIns = (TextView) view.findViewById(R.id.qIns);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            nameArray = new ArrayList<>();
            catArray = new ArrayList<>();
            oidArray = new ArrayList<>();
            phoneArray = new ArrayList<>();
            try {

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("contacts");
                ParseObject.unpinAllInBackground("contacts");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {

                            Object object = list.get(i);

                            String name = ((ParseObject) object).getString("Name");
                            String cat = ((ParseObject) object).getString("Cat");
                            String phone = ((ParseObject) object).getString("Phone");
                            String oid = ((ParseObject) object).getObjectId();


                            nameArray.add(name);
                            catArray.add(cat);
                            oidArray.add(oid);
                            phoneArray.add(phone);
                            if (nameArray.isEmpty()) {
                                qIns.setVisibility(View.VISIBLE);
                            } else {
                                qIns.setVisibility(View.INVISIBLE);
                            }

                        }
                        mainListAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });

        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {

                            Object object = list.get(i);

                            String name = ((ParseObject) object).getString("Name");
                            String cat = ((ParseObject) object).getString("Cat");
                            String phone = ((ParseObject) object).getString("Phone");
                            String oid = ((ParseObject) object).getObjectId();


                            nameArray.add(name);
                            catArray.add(cat);
                            oidArray.add(oid);
                            phoneArray.add(phone);
                            if (nameArray.isEmpty()) {
                                qIns.setVisibility(View.VISIBLE);
                            } else {
                                qIns.setVisibility(View.INVISIBLE);
                            }

                        }
                        mainListAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });

        }
        mainListAdapter = new ContactCell(context, R.layout.activity_contactcell, nameArray);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        lv.setAdapter(mainListAdapter);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        String oidPos = oidArray.get(position);
        Log.i("OID", oidPos);
        Intent detail = new Intent(context, QCDetailActivity.class);
        detail.putExtra("Object ID", oidPos);
        startActivity(detail);
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
