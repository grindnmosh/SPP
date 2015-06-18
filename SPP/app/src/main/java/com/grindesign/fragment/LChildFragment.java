package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.ginddesign.spp.ChildNameCell;
import com.ginddesign.spp.LDetailActivity;
import com.ginddesign.spp.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class LChildFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    String oidPos;
    ListView lv;
    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> oidArray;
    Context context;
    TextView cIns;

    public LChildFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lchild, container, false);

        lv = (ListView) view.findViewById(R.id.childList);
        cIns = (TextView) view.findViewById(R.id.cIns);

        nameArray = new ArrayList<>();
        oidArray = new ArrayList<>();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            String oid = ((ParseObject) object).getObjectId();

                            nameArray.add(name);
                            if (nameArray.isEmpty()) {
                                cIns.setVisibility(View.VISIBLE);
                            } else {
                                cIns.setVisibility(View.INVISIBLE);
                            }
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


        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            String oid = ((ParseObject) object).getObjectId();

                            nameArray.add(name);
                            if (nameArray.isEmpty()) {
                                cIns.setVisibility(View.VISIBLE);
                            } else {
                                cIns.setVisibility(View.INVISIBLE);
                            }
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
        }

        mainListAdapter = new ChildNameCell(context, R.layout.activity_childnamecell, nameArray);

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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);

                            String oid = ((ParseObject) object).getObjectId();
                            oidArray.add(oid);
                            if (nameArray != null) {
                                Log.i("Array", oidArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }
                        Log.i("OIDs", oidArray.toString());
                        oidPos = oidArray.get(position);
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);
                            String oid = ((ParseObject) object).getObjectId();
                            if (oidPos.equals(oid)) {
                                Log.i("ObjectID", oid);
                                Intent update = new Intent(context, LDetailActivity.class);
                                update.putExtra("object ID", oid);
                                startActivity(update);
                            }
                        }
                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List list, com.parse.ParseException e) {

                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("Array", "Entry Point Done");
                            Object object = list.get(i);

                            String oid = ((ParseObject) object).getObjectId();
                            oidArray.add(oid);
                            if (nameArray != null) {
                                Log.i("Array", oidArray.toString());
                                mainListAdapter.notifyDataSetChanged();
                            }

                        }
                        Log.i("OIDs", oidArray.toString());
                        oidPos = oidArray.get(position);
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);
                            String oid = ((ParseObject) object).getObjectId();
                            if (oidPos.equals(oid)) {
                                Log.i("ObjectID", oid);
                                Intent update = new Intent(context, LDetailActivity.class);
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
