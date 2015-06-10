package com.grindesign.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ginddesign.spp.LChildActivity;
import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Calendar;


public class LNewFragment extends Fragment {

    Button saveIt;
    Button cancel;
    Button indSave;
    EditText cName;
    EditText cdob;
    EditText css;
    EditText cAllergy;
    EditText cMed;
    EditText cShot;
    EditText cDocName;
    EditText cDocLink;
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
    JSONArray fileName = new JSONArray();
    JSONArray fileInfo = new JSONArray();

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
        cDocName = (EditText) view.findViewById(R.id.docName);
        cDocLink = (EditText) view.findViewById(R.id.docLink);
        saveIt = (Button) view.findViewById(R.id.saveIt);
        cancel = (Button) view.findViewById(R.id.cancel2);
        indSave = (Button) view.findViewById(R.id.indSave);

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

        css.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (css.getText().toString().trim().length() != 9) {
                        css.setText("");
                        Toast.makeText(context, "Please Enter Valid Social Security Number or Leave Blank", Toast.LENGTH_SHORT).show();
                    }
                }
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
                            fileName = object.getJSONArray("AdditionalNames");
                            fileInfo = object.getJSONArray("AdditionalInfo");
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
                            fileName = object.getJSONArray("AdditionalNames");
                            fileInfo = object.getJSONArray("AdditionalInfo");
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

        indSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cdn = cDocName.getText().toString().trim();
                String cdl = cDocLink.getText().toString().trim();

                if (!cdn.equals("") && !cdl.equals("")) {
                    fileName.put(cdn);
                    fileInfo.put(cdl);
                    Log.i("Saved", fileName.toString());
                    cDocName.setText("");
                    cDocLink.setText("");
                    Toast.makeText(context, "Document Temporarily saved, Save It to finalize", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Nothing was entered to save. Save not completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cName.getText().toString().trim();
                dob = cdob.getText().toString().trim();
                ss = css.getText().toString().trim();
                allergy = cAllergy.getText().toString().trim();
                med = cMed.getText().toString().trim();
                shot = cShot.getText().toString().trim();

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
                                    object.put("AdditionalNames", fileName);
                                    object.put("AdditionalInfo", fileInfo);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveInBackground();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(context, LChildActivity.class);
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
                                    object.put("AdditionalNames", fileName);
                                    object.put("AdditionalInfo", fileInfo);
                                    object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                                    object.pinInBackground();
                                    object.saveEventually();
                                    LChildFragment.mainListAdapter.notifyDataSetChanged();
                                    Intent home = new Intent(context, LChildActivity.class);
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
                            object.put("AdditionalNames", fileName);
                            object.put("AdditionalInfo", fileInfo);
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveInBackground();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
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
                            object.put("AdditionalNames", fileName);
                            object.put("AdditionalInfo", fileInfo);
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveEventually();
                            LChildFragment.mainListAdapter.notifyDataSetChanged();
                            Intent home = new Intent(context, LChildActivity.class);
                            startActivity(home);
                        }
                    }

                }else{
                    Toast.makeText(context, "Please fill out all fields before saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(context, LChildActivity.class);
                startActivity(home);
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
