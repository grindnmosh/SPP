package com.ginddesign.spp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
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
import com.grindesign.fragment.LImageFragment;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PhotoCell extends ArrayAdapter<String> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    ImageButton delete;
    String oID;
    private Context context;
    private ArrayList<String> arrayLister = LImageFragment.nameArray;

    public PhotoCell(){
        super(null,0);
    }

    public PhotoCell(Context context, int resource, ArrayList<String> arrayLister) {
        super(context, resource, arrayLister);
        this.context = context;
        this.arrayLister = arrayLister;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = arrayLister.get(position);
        String date = LImageFragment.createArray.get(position);
        final ArrayList<String> oIDArray = LImageFragment.oidArray;

        LayoutInflater blowUp = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = blowUp.inflate(R.layout.activity_photocell, null);

        TextView main = (TextView) view.findViewById(R.id.photoName);
        main.setText(name);

        TextView sub = (TextView) view.findViewById(R.id.dCreate);
        sub.setText(date);

        SwipeLayout swipeLayout =  (SwipeLayout)view.findViewById(R.id.swipe);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                LImageFragment.sched.cancel();
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

            }
        });

        delete = (ImageButton) view.findViewById(R.id.deleteImg);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v.getParent();
                oID = oIDArray.get(position);
                Log.i("OID", oID);
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("images");
                    query.getInBackground(oID, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, com.parse.ParseException e) {
                            String name = object.get("Name").toString();
                            Date creation = (object).getCreatedAt();
                            String thisOid = object.getObjectId();
                            object.unpinInBackground();
                            object.deleteEventually();
                            LImageFragment.nameArray.remove(name);
                            LImageFragment.createArray.remove(creation);
                            LImageFragment.oidArray.remove(thisOid);
                            LImageFragment.mainListAdapter.notifyDataSetChanged();
                            Intent i = new Intent(context, LImageActivity.class);
                            view.getContext().startActivity(i);
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
