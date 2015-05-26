package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grindesign.fragment.LDetailFragment;

import java.util.ArrayList;


public class addInfoCell extends ArrayAdapter<String> {


    private Context context;
    private ArrayList<String> arrayLister = LDetailFragment.nameArray;


    public addInfoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
        Log.i("Contxt", arrayLister.toString());
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        String names = arrayLister.get(position);
        String inform = LDetailFragment.nameInfo.get(position);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_add_info_cell, null);

        TextView main = (TextView) view.findViewById(R.id.docTitle);
        main.setText(names);

        TextView sub  = (TextView) view.findViewById(R.id.docInfo);
        sub.setText(inform);
        Linkify.addLinks(sub, Linkify.WEB_URLS);

        return view;
    }
}
