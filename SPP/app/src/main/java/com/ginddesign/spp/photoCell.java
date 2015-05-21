package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class PhotoCell extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> arrayLister = IndListActivity.nameArray;


    public PhotoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        String name = arrayLister.get(position);
        String item = IndListActivity.itemArray.get(position);
        String descrip = IndListActivity.desArray.get(position);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_photocell, null);

        TextView main = (TextView) view.findViewById(R.id.listItem);
        main.setText(item);

        TextView sub = (TextView) view.findViewById(R.id.listDescrip);
        sub.setText(descrip);

        return view;
    }
}
