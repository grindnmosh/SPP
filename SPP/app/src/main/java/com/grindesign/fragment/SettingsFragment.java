package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ginddesign.spp.ListMasterActivity;
import com.ginddesign.spp.R;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class SettingsFragment extends Fragment {

    EditText newUser;
    EditText newUserVer;
    EditText newPass;
    EditText reNewPass;
    Button setBut;
    String email2;
    String email3;
    String pass2;
    String pass3;
    Context context;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_settings, container, false);

        newUser = (EditText) view.findViewById(R.id.newUser);
        newUserVer = (EditText) view.findViewById(R.id.newUserVer);
        newPass = (EditText) view.findViewById(R.id.newPass);
        reNewPass = (EditText) view.findViewById(R.id.reNewPass);
        setBut = (Button) view.findViewById(R.id.setBut);

        setBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email2 = newUser.getText().toString().trim();
                email3 = newUserVer.getText().toString().trim();
                pass2 = newPass.getText().toString().trim();
                pass3 = reNewPass.getText().toString().trim();
                if (!email2.equals("") || !pass2.equals("")) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    Log.i("START", "FIGHT");
                    if (email2.equals(email3) && email2.matches(emailPattern) && email3.matches(emailPattern) && email2.length() > 0) {
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
                                                    newUser.setText("");
                                                    newUserVer.setText("");
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
                        }
                    }

                    if (pass2.equals(pass3) && !pass2.equals("")) {
                        final ParseUser user = ParseUser.getCurrentUser();
                        user.setPassword(pass2);
                        user.saveInBackground();
                        Toast.makeText(getActivity().getApplicationContext(), "Password Has Been Changed", Toast.LENGTH_LONG).show();
                        newPass.setText("");
                        reNewPass.setText("");
                    } else if (pass2.equals("")) {
                        //do nothing
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No Changes Entered", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().setResult(resultCode);
        Intent home = new Intent(context, ListMasterActivity.class);
        startActivity(home);
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
