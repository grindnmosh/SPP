package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class IndListCell extends ArrayAdapter<String> {

    Boolean isChecked;

    String oID;
    private Context context;
    private ArrayList<String> arrayLister = IndListActivity.nameArray;
    private ArrayList<String> oIDArray = IndListActivity.oidArray;


    public IndListCell(Context context, int resource, ArrayList<String> arrayLister, ArrayList<String> oIDArray) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
        this.oIDArray = oIDArray;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        String names = arrayLister.get(position);
        String item = IndListActivity.itemArray.get(position);
        String descrip = IndListActivity.desArray.get(position);
        String checker = IndListActivity.cbArray.get(position);
        final CheckBox[] cBox = new CheckBox[IndListActivity.desArray.size()];
        final boolean[] checkIt = new boolean[IndListActivity.itemArray.size()];
        oID = oIDArray.get(position);
        Log.i("ArrayLister", names);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_indlistcell, null);

        TextView main = (TextView) view.findViewById(R.id.listItem);
        main.setText(item);

        TextView sub  = (TextView) view.findViewById(R.id.listDescrip);
        sub.setText(descrip);

        cBox[position] = (CheckBox) view.findViewById(R.id.checkBox);
        cBox[position].setTag(oIDArray.get(position));
        cBox[position].setChecked(checkIt[position]);
        if (checker.equals("true")) {
            cBox[position].setChecked(!cBox[position].isChecked());
        } else {
            cBox[position].setChecked(cBox[position].isChecked());
        }

        cBox[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                checkIt[position] = isChecked;
                String current = (String) cBox[position].getTag();
                Log.i("OID", oID);

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
                    query.getInBackground(current, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            Log.i("isChecked", String.valueOf(isChecked));

                            object.put("isChecked", String.valueOf(isChecked));
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveInBackground();

                            //cBox[position].setChecked(cBox[position].isChecked());
                        }
                    });
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                    query.fromLocalDatastore();
                    query.getInBackground(current, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            Log.i("isChecked", String.valueOf(isChecked));

                            object.put("isChecked", String.valueOf(isChecked));
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveInBackground();

                            //cBox[position].setChecked(cBox[position].isChecked());
                        }
                    });
                }

            }



        });
        IndListActivity.mainListAdapter.notifyDataSetChanged();
        return view;
    }



}
