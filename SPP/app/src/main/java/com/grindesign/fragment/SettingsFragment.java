package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ginddesign.spp.MainActivity;
import com.ginddesign.spp.R;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;


public class SettingsFragment extends Fragment {

    EditText currUsername;
    EditText newUser;
    EditText newUserVer;
    Button setBut;
    String email1;
    String email2;
    String email3;
    Context context;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_settings, container, false);

        currUsername = (EditText) view.findViewById(R.id.curUsername);
        newUser = (EditText) view.findViewById(R.id.newUser);
        newUserVer = (EditText) view.findViewById(R.id.newUserVer);
        setBut = (Button) view.findViewById(R.id.setBut);

        setBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1 = currUsername.getText().toString().trim();
                email2 = newUser.getText().toString().trim();
                email3 = newUserVer.getText().toString().trim();
                String PFUser = String.valueOf(ParseUser.getCurrentUser().getUsername());
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Log.i("START", "FIGHT");
                if (email1.equals(PFUser)) {
                    Log.i("ENTRY1", "ENTERED");
                    if (email2.equals(email3) && email2.matches(emailPattern) && email3.matches(emailPattern) && email2.length() > 0 ) {
                        Log.i("ENTRY2", "ENTERED");
                        final ParseUser user = ParseUser.getCurrentUser();
                        user.increment("logins");
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                                    query.whereEqualTo("email", email2);

                                    query.countInBackground(new CountCallback() {

                                        @Override
                                        public void done(int count, ParseException e) {
                                            if (e == null) {
                                                if (count == 0) {
                                                    user.setUsername(email2);
                                                    user.setEmail(email2);
                                                    user.saveInBackground();
                                                    Toast.makeText(getActivity().getApplicationContext(), "Email Has Been Changed", Toast.LENGTH_LONG).show();
                                                    ParseUser.logOut();
                                                    try {
                                                        ParseUser.getCurrentUser().refresh();
                                                    } catch (ParseException d) {
                                                        d.printStackTrace();
                                                    }
                                                    ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.context);
                                                    startActivityForResult(builder.build(), 0);
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "The email entered is already taken", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                    });

                                } else {
                                    Log.d("Failed", "Error: " + e.getMessage());
                                    Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        if (!email2.equals(email3)) {
                            Toast.makeText(getActivity().getApplicationContext(), "The email you entered does not match in both fields", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Please Enter A Valid Email", Toast.LENGTH_LONG).show();
                        }
                    }
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
