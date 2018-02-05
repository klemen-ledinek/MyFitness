package com.example.asus.testklemen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Data.*;
import Data.Walking;

/**
 * Created by Klemen on 2/2/2018.
 */

public class ProfileSportAdapter extends RecyclerView.Adapter<ProfileSportAdapter.ViewHolder> {
    private ArrayList<Sport> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ProfileAdapter.ItemClickListener mClickListener;
    private Context context;
    private ApplicationFitnes applicationFitnes;
    private UserData userData;

    // data is passed into the constructor
    public ProfileSportAdapter(Context context1, ArrayList<Sport> data) {
        this.mInflater = LayoutInflater.from(context1);
        this.mData = data;
        context = context1;
        applicationFitnes = (ApplicationFitnes) ((Profile)context1).getApplication();
        userData = applicationFitnes.getUserData();
    }

    // inflates the row layout from xml when needed
    @Override
    public ProfileSportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sportrecyclerview_row, parent, false);
        return new ProfileSportAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(final ProfileSportAdapter.ViewHolder holder, int position) {
       if(mData == null)
           return;

        final Sport menu =mData.get(position);

        holder.date.setText(menu.getDate().getDay() + "." + menu.getDate().getMonth() + "." + menu.getDate().getYear());
        ArrayList<RowAdapterClass> adapter = new ArrayList<>();
        if(menu.getWalkingList() != null){
            for (Data.Walking walking:menu.getWalkingList()) {
                RowAdapterClass rac = new RowAdapterClass();
                rac.setType("Walking");
                rac.setStart(walking.getStartingTime().toString());
                rac.setStop(walking.getFinishTime().toString());
                rac.setWalking(walking);
                adapter.add(rac);
            }
        }


    if(menu.getRuningList() != null){
        for (Data.Runing runing:menu.getRuningList()) {
            RowAdapterClass rac = new RowAdapterClass();
            rac.setType("Running");
            rac.setStart(runing.getStartingTime().toString());
            rac.setStop(runing.getFinishTime().toString());
            rac.setRuning(runing);
            adapter.add(rac);
        }

    }
    if(menu.getCyclingList() != null){
        for (Data.Cycling cycling:menu.getCyclingList()) {
            RowAdapterClass rac = new RowAdapterClass();
            rac.setType("Cycling");
            rac.setStart(cycling.getStartingTime().toString());
            rac.setStop(cycling.getFinishTime().toString());
            rac.setCycling(cycling);

            adapter.add(rac);
        }
    }

        if(adapter != null){
            holder.walkingListView.setAdapter(new ListAdapter(context, adapter));
            justifyListViewHeightBasedOnChildren(holder.walkingListView);

            holder.walkingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = holder.walkingListView.getItemAtPosition(position);

                    RowAdapterClass roc = (RowAdapterClass) o;
                    if(roc.getType() == "Walking"){
                       applicationFitnes.setSelectedWalking(roc.getWalking());
                    }else if(roc.getType() == "Running"){
                        applicationFitnes.setSelectedRuning(roc.getRuning());
                    }else if(roc.getType()=="Cycling"){
                        applicationFitnes.setSelectedCycling(roc.getCycling());
                    }



                    Intent WalkIntent=new Intent(context,ActivityMap.class);
                    context.startActivity(WalkIntent);

                }
            });
        }

    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = (ListAdapter) listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView date;
        public ListView walkingListView;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateView);
            walkingListView = itemView.findViewById(R.id.walkingListView);


        }


    }



    // allows clicks events to be caught
    public void setClickListener(ProfileSportAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = (ProfileAdapter.ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
