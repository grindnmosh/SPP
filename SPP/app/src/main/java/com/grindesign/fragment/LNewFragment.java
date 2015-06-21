package com.grindesign.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ginddesign.spp.LChildActivity;
import com.ginddesign.spp.LDetailActivity;
import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.Calendar;


public class LNewFragment extends Fragment {

    Button saveIt;
    Button cancel;
    EditText cName;
    EditText cdob;
    EditText css;
    EditText cAllergy;
    EditText cMed;
    EditText cShot;
    TextView cTitle;
    String name;
    String dob;
    String ss;
    String allergy;
    String med;
    String shot;
    String oid;
    private int day;
    private int month;
    private int year;
    Context context;

    public LNewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lnew, container, false);

        Calendar cal;
        final Intent i = getActivity().getIntent();
        String pageTitle = i.getStringExtra("Title");
        oid = i.getStringExtra("Object ID");

        cTitle = (TextView) view.findViewById(R.id.cTitle);
        cName = (EditText) view.findViewById(R.id.cName);
        cdob = (EditText) view.findViewById(R.id.cdob);
        css = (EditText) view.findViewById(R.id.css);
        cAllergy = (EditText) view.findViewById(R.id.cAllergy);
        cMed = (EditText) view.findViewById(R.id.cMed);
        cShot = (EditText) view.findViewById(R.id.cShot);
        saveIt = (Button) view.findViewById(R.id.saveIt);
        cancel = (Button) view.findViewById(R.id.cancel2);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);






        cdob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog();
                }
            }
        });

        cdob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DateDialog();

            }
        });



        cTitle.setText(pageTitle);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (!oid.equals("New")) {
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            dob = object.getString("dob");
                            ss = object.getString("SS");
                            allergy = object.getString("Allergies");
                            med = object.getString("Medical");
                            shot = object.getString("Shot");
                            cName.setText(name);
                            cdob.setText(dob);
                            css.setText(ss);
                            cAllergy.setText(allergy);
                            cMed.setText(med);
                            cShot.setText(shot);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                query.fromLocalDatastore();
                query.getInBackground(oid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, com.parse.ParseException e) {
                        if (e == null) {
                            name = object.getString("Name");
                            dob = object.getString("dob");
                            ss = object.getString("ss");
                            allergy = object.getString("Allergies");
                            med = object.getString("Medical");
                            shot = object.getString("Shot");
                            cName.setText(name);
                            cdob.setText(dob);
                            css.setText(ss);
                            cAllergy.setText(allergy);
                            cMed.setText(med);
                            cShot.setText(shot);
                        } else {
                            Log.d("Failed", "Error: " + e.getMessage());
                        }
                    }
                });

            }
        }

        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cName.getText().toString().trim();
                dob = cdob.getText().toString().trim();
                ss = css.getText().toString().trim().replaceAll("\\s+", "");
                allergy = cAllergy.getText().toString().trim();
                med = cMed.getText().toString().trim();
                shot = cShot.getText().toString().trim();
                String social = "^[0-9]{3}(\\))?\\-?[0-9]{2}\\-?[0-9]{4}$";
                if (ss.equals("") || ss.matches(social)) {
                    if (!name.equals("")) {
                        Log.i("Enter", "The Dragon");
                        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (!oid.equals("New")) {
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                                query.getInBackground(oid, new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, com.parse.ParseException e) {
                                        object.put("Name", name);
                                        object.put("dob", dob);
                                        object.put("SS", ss);
                                        object.put("Allergies", allergy);
                                        object.put("Medical", med);
                                        object.put("Shot", shot);
                                        object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                        object.pinInBackground();
                                        object.saveInBackground();
                                        Intent home = new Intent(context, LDetailActivity.class);
                                        home.putExtra("object ID", oid);
                                        startActivity(home);
                                    }
                                });

                            } else {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                                query.fromLocalDatastore();
                                query.getInBackground(oid, new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, com.parse.ParseException e) {
                                        object.put("Name", name);
                                        object.put("dob", dob);
                                        object.put("SS", ss);
                                        object.put("Allergies", allergy);
                                        object.put("Medical", med);
                                        object.put("Shot", shot);
                                        object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                        object.pinInBackground();
                                        object.saveEventually();
                                        Intent home = new Intent(context, LDetailActivity.class);
                                        home.putExtra("object ID", oid);
                                        startActivity(home);
                                    }
                                });
                            }
                        } else {
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                ParseObject object = new ParseObject("children");
                                object.put("Name", name);
                                object.put("dob", dob);
                                object.put("SS", ss);
                                object.put("Allergies", allergy);
                                object.put("Medical", med);
                                object.put("Shot", shot);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveInBackground();
                                Intent home = new Intent(context, LChildActivity.class);
                                startActivity(home);
                            } else {
                                ParseObject object = new ParseObject("children");
                                object.put("Name", name);
                                object.put("dob", dob);
                                object.put("SS", ss);
                                object.put("Allergies", allergy);
                                object.put("Medical", med);
                                object.put("Shot", shot);
                                object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                object.pinInBackground();
                                object.saveEventually();
                                Intent home = new Intent(context, LChildActivity.class);
                                startActivity(home);
                            }
                        }

                    } else {
                        Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!css.getText().toString().trim().matches(social)) {
                        Toast.makeText(context, "Please Enter Valid Social Security Number or Leave Blank", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cName.getText().toString().trim();
                dob = cdob.getText().toString().trim();
                ss = css.getText().toString().trim();
                allergy = cAllergy.getText().toString().trim();
                med = cMed.getText().toString().trim();
                shot = cShot.getText().toString().trim();

                if (!name.equals("") || !dob.equals("") || !ss.equals("") || !allergy.equals("") || !med.equals("") || !shot.equals("")) {
                    AlertDialog.Builder lockExit = new AlertDialog.Builder(context);
                    lockExit.setTitle("Leave Without Saving?");
                    lockExit.setMessage("You will lose all information entered.");
                    lockExit.setPositiveButton("Exit Without Saving", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!oid.equals("New")) {
                                Intent home = new Intent(context, LDetailActivity.class);
                                home.putExtra("object ID", oid);
                                startActivity(home);
                            } else {
                                Intent home = new Intent(context, LChildActivity.class);
                                startActivity(home);
                            }
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
                    if (!oid.equals("New")) {
                        Intent home = new Intent(context, LDetailActivity.class);
                        home.putExtra("object ID", oid);
                        startActivity(home);
                    } else {
                        Intent home = new Intent(context, LChildActivity.class);
                        startActivity(home);
                    }
                }
            }
        });

        return view;
    }

    public void DateDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {

                cdob.setText((monthOfYear + 1) + "/" + (dayOfMonth) + "/" + year);

            }};

        DatePickerDialog dpDialog=new DatePickerDialog(context, listener, year, month, day);
        dpDialog.show();

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
