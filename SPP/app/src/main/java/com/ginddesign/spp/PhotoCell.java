package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PhotoCell extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> arrayLister = LImageActivity.nameArray;


    public PhotoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = arrayLister.get(position);
        Date create = LImageActivity.createArray.get(position);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_photocell, null);

        TextView main = (TextView) view.findViewById(R.id.photoName);
        main.setText(name);

        TextView sub = (TextView) view.findViewById(R.id.dCreate);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" );
        sub.setText(sdf.format(create));

        return view;
    }
}
