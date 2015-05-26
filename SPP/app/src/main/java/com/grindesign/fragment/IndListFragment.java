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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ginddesign.spp.IndListCell;
import com.ginddesign.spp.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class IndListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> itemArray = new ArrayList<>();
    public static ArrayList<String> desArray = new ArrayList<>();
    public static ArrayList<String> oidArray = new ArrayList<>();
    public static ArrayList<String> cbArray = new ArrayList<>();
    public static ArrayList<String> listArray = new ArrayList<>();
    Context context;
    String passedName;
    CheckBox check;
    TextView listName;
    android.support.v7.widget.ShareActionProvider mShareActionProvider;

    public IndListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_indlist, container, false);

        final Intent i = getActivity().getIntent();
        passedName = i.getStringExtra("listName");

        listName = (TextView) view.findViewById(R.id.indListName);
        final ListView lv = (ListView) view.findViewById(R.id.list);
        check = (CheckBox) view.findViewById(R.id.checkBox);

        listName.setText(passedName);



        nameArray = new ArrayList<>();
        itemArray = new ArrayList<>();
        desArray = new ArrayList<>();
        oidArray = new ArrayList<>();
        cbArray = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("listMaster");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.whereEqualTo("Name", passedName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (ParseObject listmasterobject : list) {
                            String name = listmasterobject.get("Name").toString();
                            String item = listmasterobject.get("item").toString();
                            String descrip = listmasterobject.get("Descrip").toString();
                            String checkBox = listmasterobject.get("isChecked").toString();
                            String oid = (listmasterobject).getObjectId();
                            Log.i("TEST Run", name);
                            nameArray.add(name);
                            itemArray.add(item);
                            desArray.add(descrip);
                            oidArray.add(oid);
                            cbArray.add(checkBox);
                            Log.i("TEST Run", desArray.toString());
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.whereEqualTo("Name", passedName);
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    Log.i("Array", "Entry Point Done");

                    if (e == null) {
                        for (ParseObject listmasterobject : list) {
                            String name = listmasterobject.get("Name").toString();
                            String item = listmasterobject.get("item").toString();
                            String descrip = listmasterobject.get("Descrip").toString();
                            String checkBox = listmasterobject.get("isChecked").toString();
                            String oid = (listmasterobject).getObjectId();
                            Log.i("TEST Run", name);
                            nameArray.add(name);
                            itemArray.add(item);
                            desArray.add(descrip);
                            oidArray.add(oid);
                            cbArray.add(checkBox);
                            Log.i("TEST Run", desArray.toString());
                            mainListAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        mainListAdapter = new IndListCell(context, R.layout.activity_indlistcell, itemArray, oidArray);

        lv.setAdapter(mainListAdapter);



        return view;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


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
