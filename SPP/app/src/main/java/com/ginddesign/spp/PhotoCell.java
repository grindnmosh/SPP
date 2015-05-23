package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PhotoCell extends ArrayAdapter<String> {

    Bitmap photoFile;
    private Context context;
    private ArrayList<String> arrayLister = LImageActivity.nameArray;


    public PhotoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_photocell, null);
        String name = arrayLister.get(position);
        if (photoFile != null) {
            photoFile = LImageActivity.imgrray.get(position);
        }
        TextView sub = (TextView) view.findViewById(R.id.photoName);
        sub.setText(name);

        ParseImageView main = (ParseImageView) view.findViewById(R.id.preview);
        main.setImageBitmap(photoFile);

        return view;
    }
}
