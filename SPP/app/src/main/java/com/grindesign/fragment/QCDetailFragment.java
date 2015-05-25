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
import android.widget.ImageButton;
import android.widget.TextView;

import com.ginddesign.spp.QCNewActivity;
import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class QCDetailFragment extends Fragment {

    public static TextView conName;
    public static TextView conPhone;
    public static TextView conEmail;
    public static TextView conNotes;
    ImageButton makeCall;
    ImageButton sendMess;
    ImageButton sendEmail;
    ImageButton conEdit;
    String ois;
    String name;
    String phone;
    String email;
    String notes;
    Context context;

    public QCDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qcdetail, container, false);

        conName = (TextView) view.findViewById(R.id.conName);
        conPhone = (TextView) view.findViewById(R.id.conPhone);
        conEmail = (TextView) view.findViewById(R.id.conEmail);
        conNotes = (TextView) view.findViewById(R.id.conNotes);
        makeCall = (ImageButton) view.findViewById(R.id.makeCall);
        sendMess = (ImageButton) view.findViewById(R.id.sendMess);
        sendEmail = (ImageButton) view.findViewById(R.id.sendEmail);
        conEdit = (ImageButton) view.findViewById(R.id.conEdit);

        final Intent i = getActivity().getIntent();
        ois = i.getStringExtra("Object ID");


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
                        conName.setText(name);
                        conPhone.setText(phone);
                        conEmail.setText(email);
                        conNotes.setText(notes);

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
                        conName.setText(name);
                        conPhone.setText(phone);
                        conEmail.setText(email);
                        conNotes.setText(notes);


                    } else {
                        Log.d("Failed", "Error: " + e.getMessage());
                    }
                }
            });
        }

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, (Uri.parse("tel:" + conPhone.getText())));
                startActivity(Intent.createChooser(intent, "Call From"));
            }
        });

        sendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + conPhone.getText()));
                startActivity(Intent.createChooser(intent, "Message From"));
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", String.valueOf(conEmail.getText()), null));
                startActivity(Intent.createChooser(emailIntent, "Email From"));
            }
        });

        conEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(context, QCNewActivity.class);
                add.putExtra("Title", "Edit Contact");
                add.putExtra("Object ID", ois);
                startActivity(add);
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
