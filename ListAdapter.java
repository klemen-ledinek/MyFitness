package com.example.asus.testklemen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Data.RowAdapterClass;

/**
 * Created by Klemen on 2/2/2018.
 */

public class ListAdapter extends ArrayAdapter<RowAdapterClass> {



    private LayoutInflater mInflater;

    public ListAdapter(Context context, ArrayList<RowAdapterClass> results) {
        super(context,0,results);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {




        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, viewGroup, false);
        }
        RowAdapterClass rac = getItem(position);
        if(view != null){
            TextView txtType = (TextView) view.findViewById(R.id.type);
            TextView txtStartTime = (TextView) view.findViewById(R.id.startTime);
            TextView txtStopTime = (TextView) view.findViewById(R.id.endTime);





            txtType.setText(rac.getType());
            txtStartTime.setText(rac.getStart());
            txtStopTime.setText(rac.getStop());

            return view;
        }
        return  null;
    }


}
