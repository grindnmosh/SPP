package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
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
    String phoneNum;
    String emailAdd;
    EditText qcName;
    EditText qcPhone;
    EditText qcEmail;
    EditText qcNotes;
    Button cancel;
    Button save;
    Button contactsImport;
    TextView title;
    Spinner s;
    Context context;
    int PICK_CONTACT;
    public static ArrayAdapter<String> loadsAdapter;

    public QCNewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qcnew, container, false);

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
        contactsImport = (Button) view.findViewById(R.id.contactsImport);
        s = (Spinner) view.findViewById(R.id.qcSpin);

        title.setText(pageTitle);

        qcPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (qcPhone.getText().toString().trim().length() != 10) {
                        Toast.makeText(context, "Please Enter A Valid 10 digit Phone Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (pageTitle.equals("Edit Contact")) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (!ois.equals("New")) {
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
                                contactsImport.setVisibility(View.INVISIBLE);
                                int spinnerPostion = loadsAdapter.getPosition(compareValue);
                                s.setSelection(spinnerPostion);

                            } else {
                                Log.d("Failed", "Error: " + e.getMessage());
                            }
                        }
                    });

                } else {
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
                                contactsImport.setVisibility(View.INVISIBLE);
                                int spinnerPostion = loadsAdapter.getPosition(compareValue);
                                s.setSelection(spinnerPostion);


                            } else {
                                Log.d("Failed", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        }

        loads = getResources().getStringArray(R.array.qc_cat);
        loadsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, loads);
        s.setAdapter(loadsAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (loads[position].equals("Select Category")) {
                    cat = loads[position].trim();
                } else {
                    cat = loads[position].trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        contactsImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = qcName.getText().toString().trim();
                phone = qcPhone.getText().toString().trim();
                email = qcEmail.getText().toString().trim();
                notes = qcNotes.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Log.i("Category", cat);

                if (!cat.equals("Select Category") && !name.equals("") && phone.length() == 10 && (email.equals("") || email.matches(emailPattern))){
                    Log.i("Category", cat);
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (!ois.equals("New")) {
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("contacts");
                            query.getInBackground(ois, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, com.parse.ParseException e) {
                                    Log.i("Category", cat);
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
                    } else {
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            ParseObject object = new ParseObject("contacts");
                            Log.i("Category", cat);
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

                        } else {
                            ParseObject object = new ParseObject("contacts");
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
                    }

                } else {
                    if (cat.equals("Select Category")) {
                        Toast.makeText(context, "Please select a category before saving", Toast.LENGTH_SHORT).show();
                    } else if (name.equals("")) {
                        Toast.makeText(context, "Please a valid name before saving", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() != 10) {
                        Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches(emailPattern) || !email.equals("")) {
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == PICK_CONTACT)
        {
            Cursor dataGrabber =  getActivity().getContentResolver().query(data.getData(), null, null, null, null);
            dataGrabber.moveToNext();
            String  name = dataGrabber.getString(dataGrabber.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = dataGrabber.getString(dataGrabber.getColumnIndex(ContactsContract.Contacts._ID));
            String hasPhone = dataGrabber.getString(dataGrabber.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ( hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false" ;
            if (Boolean.parseBoolean(hasPhone))
            {
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext())
                {
                    phoneNum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            while (emails.moveToNext())
            {
                emailAdd = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();

            qcName.setText(name);
            qcPhone.setText(phoneNum);
            qcEmail.setText(emailAdd);
            dataGrabber.close();
        }
    }
}
