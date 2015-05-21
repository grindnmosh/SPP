package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactCell extends ArrayAdapter<String> {


    private Context context;
    private ArrayList<String> arrayLister = QuickContactActivity.nameArray;


    public ContactCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
        Log.i("Contxt", arrayLister.toString());
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.i("getView", arrayLister.toString());
        String name = arrayLister.get(position);
        String cat = QuickContactActivity.catArray.get(position);
        String phone = QuickContactActivity.phoneArray.get(position);

        Log.i("inflator", name);
        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_contactcell, null);

        Log.i("main", name);
        TextView main = (TextView) view.findViewById(R.id.qcNameDisplay);
        main.setText(name);

        //Log.i("sub", cat);
        TextView sub1  = (TextView) view.findViewById(R.id.qcCategory);
        sub1.setText(cat);

        Log.i("sub", phone);
        TextView sub2  = (TextView) view.findViewById(R.id.qcPhNumber);
        sub2.setText(phone);

        return view;
    }

}
