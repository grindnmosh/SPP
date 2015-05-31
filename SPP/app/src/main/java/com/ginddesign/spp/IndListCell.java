package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.grindesign.fragment.IndListFragment;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class IndListCell extends ArrayAdapter<String> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    ImageButton delete;

    String oID;
    private Context context;
    private ArrayList<String> arrayLister = IndListFragment.nameArray;
    private ArrayList<String> oIDArray = IndListFragment.oidArray;

    public IndListCell(){
        super(null,0);
    }

    public IndListCell(Context context, int resource, ArrayList<String> arrayLister, ArrayList<String> oIDArray) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
        this.oIDArray = oIDArray;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        String names = arrayLister.get(position);
        String item = IndListFragment.itemArray.get(position);
        String descrip = IndListFragment.desArray.get(position);
        String checker = IndListFragment.cbArray.get(position);
        final CheckBox[] cBox = new CheckBox[IndListFragment.desArray.size()];
        final boolean[] checkIt = new boolean[IndListFragment.itemArray.size()];

        Log.i("ArrayLister", names);

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = blowUp.inflate(R.layout.activity_indlistcell, null);

        TextView main = (TextView) view.findViewById(R.id.listItem);
        main.setText(item);

        TextView sub  = (TextView) view.findViewById(R.id.listDescrip);
        sub.setText(descrip);

        cBox[position] = (CheckBox) view.findViewById(R.id.checkBox);
        cBox[position].setTag(oIDArray.get(position));
        cBox[position].setChecked(checkIt[position]);
        if (checker.equals("true")) {
            cBox[position].setChecked(!cBox[position].isChecked());
        } else {
            cBox[position].setChecked(cBox[position].isChecked());
        }

        delete = (ImageButton) view.findViewById(R.id.deleteItem);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout)v.getParent();
                oID = oIDArray.get(position);
                Log.i("OID", oID);
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                    query.getInBackground(oID, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            String name = object.get("Name").toString();
                            String item = object.get("item").toString();
                            String descrip = object.get("Descrip").toString();
                            String checkBox = object.get("isChecked").toString();
                            String thisOid = object.getObjectId();
                            object.unpinInBackground();
                            object.deleteEventually();
                            IndListFragment.nameArray.remove(name);
                            IndListFragment.itemArray.remove(item);
                            IndListFragment.desArray.remove(descrip);
                            IndListFragment.oidArray.remove(thisOid);
                            IndListFragment.cbArray.remove(checkBox);
                            IndListFragment.mainListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        cBox[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                checkIt[position] = isChecked;
                String current = (String) cBox[position].getTag();


                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                    query.getInBackground(current, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            Log.i("isChecked", String.valueOf(isChecked));
                            object.put("isChecked", String.valueOf(isChecked));
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveInBackground();
                        }
                    });
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("listMaster");
                    query.fromLocalDatastore();
                    query.getInBackground(current, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            Log.i("isChecked", String.valueOf(isChecked));

                            object.put("isChecked", String.valueOf(isChecked));
                            object.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            object.pinInBackground();
                            object.saveInBackground();

                            //cBox[position].setChecked(cBox[position].isChecked());
                        }
                    });
                }

            }



        });
        IndListFragment.mainListAdapter.notifyDataSetChanged();
        return view;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.deleteItem;
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
