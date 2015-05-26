package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ginddesign.spp.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class LIDetailFragment extends Fragment {

    Context context;
    public static String oid;

    public LIDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_lidetail, container, false);

        final Intent i = getActivity().getIntent();
        oid = i.getStringExtra("Object ID");

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("images");
                List<ParseObject> objects = query1.find();
                ParseObject.pinAllInBackground(objects);
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
            ParseQuery<ParseObject> query = new ParseQuery<>("images");
            query.getInBackground(oid, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, com.parse.ParseException e) {
                    ParseFile fileObject = (ParseFile) object.get("spp_image");
                    String imgName = object.getString("Name");
                    TextView nameIt = (TextView) view.findViewById(R.id.textView26);
                    nameIt.setText(imgName);
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                Log.d("test", "We've got data in data.");
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ImageView image = (ImageView) view.findViewById(R.id.imageView2);
                                image.setImageBitmap(bmp);
                            } else {
                                Log.d("test", "There was a problem downloading the data.");
                            }
                        }
                    });
                }
            });
        } else {
            ParseQuery<ParseObject> query = new ParseQuery<>("images");
            query.fromLocalDatastore();
            query.getInBackground(oid, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, com.parse.ParseException e) {
                    ParseFile fileObject = (ParseFile) object.get("spp_image");
                    String imgName = object.getString("Name");
                    TextView nameIt = (TextView) view.findViewById(R.id.textView26);
                    nameIt.setText(imgName);
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                Log.d("test", "We've got data in data.");
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ImageView image = (ImageView) view.findViewById(R.id.imageView2);
                                image.setImageBitmap(bmp);
                            } else {
                                Log.d("test", "There was a problem downloading the data.");
                            }
                        }
                    });
                }
            });
        }
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
