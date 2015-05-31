package com.grindesign.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ginddesign.spp.LChildActivity;
import com.ginddesign.spp.LImageActivity;
import com.ginddesign.spp.R;


public class LockersFragment extends Fragment {

    Button cLocker;
    Button iLocker;
    Context context;

    public LockersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lockers, container, false);

        cLocker = (Button) view.findViewById(R.id.cLButt);
        iLocker = (Button) view.findViewById(R.id.iLButt);



        cLocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(context, LChildActivity.class);
                startActivity(home);
            }
        });

        iLocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(context, LImageActivity.class);
                startActivity(home);
            }
        });

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
