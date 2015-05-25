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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ginddesign.spp.QuickContactActivity;
import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class QCNewFragment extends Fragment {

    String[] loads;
    String cat;
    String name;
    String phone;
    String email;
    String notes;
    String compareValue;
    EditText qcName;
    EditText qcPhone;
    EditText qcEmail;
    EditText qcNotes;
    Button cancel;
    Button save;
    TextView title;
    Spinner s;
    Context context;
    public static ArrayAdapter<String> loadsAdapter;

    public QCNewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qcnew, container, false);

        final Intent i = getActivity().getIntent();
        String pageTitle = i.getStringExtra("Title");
        final String ois = i.getStringExtra("Object ID");

        title = (TextView) view.findViewById(R.id.title);
        save = (Button) view.findViewById(R.id.save1);
        cancel = (Button) view.findViewById(R.id.cancel1);
        qcName = (EditText) view.findViewById(R.id.qcName);
        qcPhone = (EditText) view.findViewById(R.id.qcPhone);
        qcEmail = (EditText) view.findViewById(R.id.qcEmail);
        qcNotes = (EditText) view.findViewById(R.id.qcNotes);
        s = (Spinner) view.findViewById(R.id.qcSpin);

        title.setText(pageTitle);

        if (pageTitle.equals("Edit Contact")) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                query.getInBackground(ois, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            phone = object.getString("Phone");
                            email = object.getString("Email");
                            notes = object.getString("Notes");
                            compareValue = object.getString("Cat");
                            qcName.setText(name);
                            qcPhone.setText(phone);
                            qcEmail.setText(email);
                            qcNotes.setText(notes);

                            int spinnerPostion = loadsAdapter.getPosition(compareValue);
                            s.setSelection(spinnerPostion);

                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            }else{
                ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                query.fromLocalDatastore();
                query.getInBackground(ois, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            phone = object.getString("Phone");
                            email = object.getString("Email");
                            notes = object.getString("Notes");
                            qcName.setText(name);
                            qcPhone.setText(phone);
                            qcEmail.setText(email);
                            qcNotes.setText(notes);

                            int spinnerPostion = loadsAdapter.getPosition(compareValue);
                            s.setSelection(spinnerPostion);


                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        }

        loads = getResources().getStringArray(R.array.qc_cat);
        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, loads);
        s.setAdapter(loadsAdapter);




        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loads[position].equals("Current Lists")) {
                    cat = loads[position].trim();
                }
                else {
                    cat = loads[position].trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = qcName.getText().toString().trim();
                phone = qcPhone.getText().toString().trim();
                email = qcEmail.getText().toString().trim();
                notes = qcNotes.getText().toString().trim();

                if (!cat.equals("Current Lists") && !name.equals("") && !phone.equals("")) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                        query.getInBackground(ois, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Cat", cat);
                                object.put("Name", name);
                                object.put("Phone", phone);
                                object.put("Email", email);
                                object.put("Notes", notes);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveInBackground();
                                ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(context, QuickContactActivity.class);
                                startActivity(home);
                            }
                        });

                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                        query.fromLocalDatastore();
                        query.getInBackground(ois, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, com.parse.ParseException e) {
                                object.put("Cat", cat);
                                object.put("Name", name);
                                object.put("Phone", phone);
                                object.put("Email", email);
                                object.put("Notes", notes);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveEventually();
                                ListMasterFragment.mainListAdapter.notifyDataSetChanged();
                                Intent home = new Intent(context, QuickContactActivity.class);
                                startActivity(home);
                            }
                        });
                    }

                }else{
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(context, QuickContactActivity.class);
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
