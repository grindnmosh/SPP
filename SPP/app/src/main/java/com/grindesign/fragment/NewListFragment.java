package com.grindesign.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ginddesign.spp.IndListActivity;
import com.ginddesign.spp.ListMasterActivity;
import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class NewListFragment extends Fragment {

    Button cancel;
    Button save;
    Spinner s;
    Context context;
    String lName;
    String iName;
    String descrip;
    String oid;
    EditText listName;
    EditText itemName;
    EditText itemDescrip;
    String passedName;
    public static ArrayList<String> listNameArray = new ArrayList<>();

    public static ArrayAdapter<String> loadsAdapter;

    public NewListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_newlist, container, false);

        listNameArray = new ArrayList<>();
        listNameArray.add("Current Lists");
        save = (Button) view.findViewById(R.id.save);
        cancel = (Button) view.findViewById(R.id.cancel);
        s = (Spinner) view.findViewById(R.id.listSpin);
        listName = (EditText) view.findViewById(R.id.listName);
        itemName = (EditText) view.findViewById(R.id.newItem);
        itemDescrip = (EditText) view.findViewById(R.id.itemDescrip);

        final Intent i = getActivity().getIntent();
        listNameArray.addAll(i.getStringArrayListExtra("listNameArray"));
        passedName = i.getStringExtra("listName");
        oid = i.getStringExtra("Object ID");
        Log.i("OID", oid);

        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, listNameArray);
        s.setAdapter(loadsAdapter);

        if (passedName.equals("none")){
            s.setSelection(0);
        } else {
            int spinPos = loadsAdapter.getPosition(passedName);
            s.setSelection(spinPos);
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            String item = object.getString("item");
                            String descrip = object.getString("Descrip");
                            Log.i("Test", descrip);
                            Log.i("Test", item);
                            itemName.setText(item);
                            itemDescrip.setText(descrip);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        }
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listNameArray.get(position).equals("Current Lists")) {
                    listName.setText("");
                } else {
                    listName.setText(listNameArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lName = listName.getText().toString().trim();
                        iName = itemName.getText().toString().trim();
                        descrip = itemDescrip.getText().toString().trim();
                        Log.i("OID", oid);
                        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (!passedName.equals("none")) {
                            if (!lName.equals("") && !iName.equals("")) {

                                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                    Log.i("OID", oid);
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                                    query.getInBackground(oid, new GetCallback<ParseObject>() {
                                        public void done(ParseObject listMaster, com.parse.ParseException e) {
                                            listMaster.put("Name", lName);
                                            listMaster.put("item", iName);
                                            listMaster.put("Descrip", descrip);
                                            listMaster.put("isChecked", "false");
                                            listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                            listMaster.pinInBackground();
                                            listMaster.saveInBackground();
                                            IndListFragment.mainListAdapter.notifyDataSetChanged();
                                            Intent home = new Intent(context, IndListActivity.class);
                                            home.putExtra("listName", passedName);
                                            startActivity(home);
                                        }
                                    });
                                } else {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                                    query.fromLocalDatastore();
                                    query.getInBackground(oid, new GetCallback<ParseObject>() {
                                        public void done(ParseObject listMaster, com.parse.ParseException e) {
                                            listMaster.put("Name", lName);
                                            listMaster.put("item", iName);
                                            listMaster.put("Descrip", descrip);
                                            listMaster.put("isChecked", "false");
                                            listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                            listMaster.pinInBackground();
                                            listMaster.saveEventually();
                                            IndListFragment.mainListAdapter.notifyDataSetChanged();
                                            Intent home = new Intent(context, IndListActivity.class);
                                            home.putExtra("listName", passedName);
                                            startActivity(home);
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                ParseObject listMaster = new ParseObject("listMaster");
                                listMaster.put("Name", lName);
                                listMaster.put("item", iName);
                                listMaster.put("Descrip", descrip);
                                listMaster.put("isChecked", "false");
                                listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                listMaster.pinInBackground();
                                listMaster.saveInBackground();
                                ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                                listNameArray.add(lName);
                                itemName.setText("");
                                itemDescrip.setText("");
                                int spinPos = loadsAdapter.getPosition(lName);
                                s.setSelection(spinPos);

                            } else {
                                ParseObject listMaster = new ParseObject("listMaster");
                                listMaster.put("Name", lName);
                                listMaster.put("item", iName);
                                listMaster.put("Descrip", descrip);
                                listMaster.put("isChecked", "false");
                                listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                listMaster.pinInBackground();
                                listMaster.saveEventually();
                                ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                                listNameArray.add(lName);
                                itemName.setText("");
                                itemDescrip.setText("");
                                int spinPos = loadsAdapter.getPosition(lName);
                                s.setSelection(spinPos);
                            }
                        }
                    }
                });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                lName = listName.getText().toString().trim();
                iName = itemName.getText().toString().trim();
                if (!lName.equals("") || !iName.equals("")) {
                    AlertDialog.Builder lockExit = new AlertDialog.Builder(context);
                    lockExit.setTitle("Leave Without Saving?");
                    lockExit.setMessage("You will lose all information entered.");
                    lockExit.setPositiveButton("Exit Without Saving", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent home = new Intent(context, IndListActivity.class);
                            home.putExtra("listName", passedName);
                            startActivity(home);
                        }
                    });
                    lockExit.setNegativeButton("Stay On Page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    lockExit.setIcon(android.R.drawable.ic_dialog_alert);
                    lockExit.show();
                } else {
                    Intent home = new Intent(context, ListMasterActivity.class);
                    startActivity(home);
                }
            }
        });
        return view;
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
