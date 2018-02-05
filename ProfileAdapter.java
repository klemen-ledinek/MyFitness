package com.example.asus.testklemen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Data.Menu;
import Data.Sport;
import Data.UserData;

/**
 * Created by Klemen on 2/2/2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private ArrayList<Menu> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ProfileAdapter(Context context, ArrayList<Menu> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Menu menu =mData.get(position);

        holder.calories.setText(menu.getCalories() + "cal");
        holder.date.setText(menu.getDate().getDay() + "." + menu.getDate().getMonth() +"."+menu.getDate().getYear());
        holder.water.setText(menu.getWater() + "ml");
        holder.cofein.setText(menu.getCoffe() + "cup(s)");

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return 1;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView date;
        public TextView cofein;
        public TextView water;
        public TextView calories;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateText);
            cofein = itemView.findViewById(R.id.coffeinText);
            water = itemView.findViewById(R.id.waterText);
            calories = itemView.findViewById(R.id.caloriesText);


        }


    }



    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}