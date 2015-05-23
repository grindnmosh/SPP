package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


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
        String photoFile = LImageActivity.imgrray.get(position);
        Log.i("ArrayLister", name);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_photocell, null);

        TextView sub = (TextView) view.findViewById(R.id.photoName);
        sub.setText(name);

        ParseImageView main = (ParseImageView) view.findViewById(R.id.preview);
        Picasso.with(context).load(photoFile).into(main);

        /*try {
            byte[] bitmapdata = photoFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            ParseImageView main = (ParseImageView) view.findViewById(R.id.preview);
            main.setImageBitmap(bitmap);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }*/

        return view;
    }
}
