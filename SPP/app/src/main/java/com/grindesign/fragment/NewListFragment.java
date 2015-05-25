package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ginddesign.spp.ListMasterActivity;
import com.ginddesign.spp.R;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class NewListFragment extends Fragment {

    String[] loads;
    Button cancel;
    Button save;
    Spinner s;
    Context context;
    String lName;
    String iName;
    String descrip;
    EditText listName;
    EditText itemName;
    EditText itemDescrip;
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

        loads = getResources().getStringArray(R.array.spinner);
        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, listNameArray);
        s.setAdapter(loadsAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loads[position].equals("Current Lists")) {
                    listName.setText("");
                } else {
                    listName.setText(loads[position]);
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

                if (!lName.equals("") && !iName.equals("")) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
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
                        listName.setText("");
                        itemName.setText("");
                        itemDescrip.setText("");
                        s.setSelection(0);

                    } else {
                        ParseObject listMaster = new ParseObject("listMaster");
                        listMaster.put("Name", lName);
                        listMaster.put("item", iName);
                        listMaster.put("Descrip", descrip);
                        listMaster.setACL(new ParseACL(ParseUser.getCurrentUser()));
                        listMaster.pinInBackground();
                        listMaster.saveEventually();
                        ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                        listName.setText("");
                        itemName.setText("");
                        itemDescrip.setText("");
                        s.setSelection(0);
                    }

                } else {
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(context, ListMasterActivity.class);
                startActivity(home);
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
