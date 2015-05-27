package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.grindesign.fragment.LChildFragment;
import com.grindesign.fragment.QuickContactFragment;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ChildNameCell extends ArrayAdapter<String> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    ImageButton delete;
    String oID;
    private Context context;
    private ArrayList<String> arrayLister = LChildFragment.nameArray;

    public ChildNameCell(){
        super(null,0);
    }

    public ChildNameCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = arrayLister.get(position);
        final ArrayList<String> oIDArray = LChildFragment.oidArray;

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_childnamecell, null);

        TextView main = (TextView) view.findViewById(R.id.childName);
        main.setText(name);

        delete = (ImageButton) view.findViewById(R.id.deleteChild);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v.getParent();
                oID = oIDArray.get(position);
                Log.i("OID", oID);
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("children");
                    query.getInBackground(oID, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            String name = object.get("Name").toString();
                            object.unpinInBackground();
                            object.deleteEventually();
                            LChildFragment.nameArray.remove(name);
                            LChildFragment.mainListAdapter.notifyDataSetChanged();

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
