package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.grindesign.fragment.LDetailFragment;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class addInfoCell extends ArrayAdapter<String> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    ImageButton delete;
    private Context context;
    private ArrayList<String> arrayLister = LDetailFragment.nameArray;

    public addInfoCell(){
        super(null,0);
    }

    public addInfoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final String names = arrayLister.get(position);
        final String inform = LDetailFragment.nameInfo.get(position);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_add_info_cell, null);

        TextView main = (TextView) view.findViewById(R.id.docTitle);
        main.setText(names);

        TextView sub  = (TextView) view.findViewById(R.id.docInfo);
        sub.setText(inform);
        Linkify.addLinks(sub, Linkify.WEB_URLS);

        delete = (ImageButton) view.findViewById(R.id.deleteAdd);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v.getParent();
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                    query.getInBackground(LDetailFragment.ois, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            LDetailFragment.nameArray.remove(names);
                            LDetailFragment.nameInfo.remove(inform);
                            object.put("AdditionalNames", LDetailFragment.nameArray);
                            object.put("AdditionalNames", LDetailFragment.nameArray);
                            LDetailFragment.mainListAdapter.notifyDataSetChanged();


                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return 0;
    }

    @Override
    public void openItem(int i) {

    }

    @Override
    public void closeItem(int i) {

    }

    @Override
    public void closeAllExcept(SwipeLayout swipeLayout) {

    }

    @Override
    public void closeAllItems() {

    }

    @Override
    public List<Integer> getOpenItems() {
        return null;
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return null;
    }

    @Override
    public void removeShownLayouts(SwipeLayout swipeLayout) {

    }

    @Override
    public boolean isOpen(int i) {
        return false;
    }

    @Override
    public Attributes.Mode getMode() {
        return null;
    }

    @Override
    public void setMode(Attributes.Mode mode) {

    }
}
