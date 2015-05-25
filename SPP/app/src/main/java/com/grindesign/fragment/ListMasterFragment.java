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

import com.ginddesign.spp.IndListActivity;
import com.ginddesign.spp.ListMasterActivity;
import com.ginddesign.spp.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListMasterFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String namePos;

    public static ArrayAdapter<String> mainListAdapter;
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> listNameArray = new ArrayList<>();
    ListView lv;
    Context context;
    Timer sched;

    public ListMasterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_listmaster, container, false);

        lv = (ListView) view.findViewById(R.id.list_master);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            nameArray = new ArrayList<>();
            listNameArray = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("listMaster");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List list, com.parse.ParseException e) {


                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            nameArray.add(name);


                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                    for (int c = 0; c < nameArray.size(); c++) {
                        String compare = nameArray.get(c);
                        for (int l = 0; l < list.size(); l++) {
                            if (!listNameArray.contains(compare)) {
                                listNameArray.add(compare);
                            }
                        }
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List list, com.parse.ParseException e) {
                    nameArray = new ArrayList<>();
                    listNameArray = new ArrayList<>();
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            Object object = list.get(i);


                            String name = ((ParseObject) object).getString("Name");
                            nameArray.add(name);


                        }

                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }

                    for (int c = 0; c < nameArray.size(); c++) {
                        String compare = nameArray.get(c);
                        for (int l = 0; l < list.size(); l++) {
                            if (!listNameArray.contains(compare)) {
                                listNameArray.add(compare);

                            }
                        }
                    }
                    mainListAdapter.notifyDataSetChanged();
                }
            });
        }
        Log.i("WTF", listNameArray.toString());
        mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listNameArray);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

        lv.setAdapter(mainListAdapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        namePos = listNameArray.get(position);
        Intent update = new Intent(context, IndListActivity.class);
        update.putExtra("listName", namePos);
        startActivity(update);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }




    private Runnable Timer_Tick = new Runnable() {
        public void run() {
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
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        listNameArray = new ArrayList<>();

                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                Object object = list.get(i);


                                String name = ((ParseObject) object).getString("Name");
                                nameArray.add(name);


                            }

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                        for (int c = 0; c < nameArray.size(); c++) {
                            String compare = nameArray.get(c);
                            for (int l = 0; l < list.size(); l++) {
                                if (!listNameArray.contains(compare)) {
                                    listNameArray.add(compare);
                                    mainListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, com.parse.ParseException e) {
                        nameArray = new ArrayList<>();
                        listNameArray = new ArrayList<>();
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                Object object = list.get(i);


                                String name = ((ParseObject) object).getString("Name");
                                nameArray.add(name);


                            }

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }

                        for (int c = 0; c < nameArray.size(); c++) {
                            String compare = nameArray.get(c);
                            for (int l = 0; l < list.size(); l++) {
                                if (!listNameArray.contains(compare)) {
                                    listNameArray.add(compare);
                                    mainListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
            Log.i("WTF", listNameArray.toString());
            mainListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listNameArray);

            lv.setOnItemClickListener(ListMasterFragment.this);
            lv.setOnItemLongClickListener(ListMasterFragment.this);

            lv.setAdapter(mainListAdapter);

        }
    };

    public void onPause() {
        super.onPause();

        sched.cancel();
    }

    private void TimerMethod()
    {
        getActivity().runOnUiThread(Timer_Tick);
    }

    public void onResume() {
        super.onResume();
        sched = new Timer();
        sched.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 1000);
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