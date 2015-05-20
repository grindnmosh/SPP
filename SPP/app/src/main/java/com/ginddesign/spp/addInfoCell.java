package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class addInfoCell extends ArrayAdapter<String> {


    private Context context;
    private ArrayList<String> arrayLister = LDetailActivity.nameArray;


    public addInfoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
        Log.i("Contxt", arrayLister.toString());
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.i("getView", arrayLister.toString());
        String names = arrayLister.get(position);
        String inform = LDetailActivity.nameInfo.get(position);

        Log.i("inflator", names);
        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_add_info_cell, null);

        Log.i("main", names);
        TextView main = (TextView) view.findViewById(R.id.docTitle);
        main.setText(names);

        Log.i("sub", inform);
        TextView sub  = (TextView) view.findViewById(R.id.docInfo);
        sub.setText(inform);

        return view;
    }
}
