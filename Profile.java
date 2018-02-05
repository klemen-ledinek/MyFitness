package com.example.asus.testklemen;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Data.Menu;
import Data.Sport;
import Data.UserData;

public class Profile extends AppCompatActivity{

    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private ArrayList<Sport> sportArrayList;
    private ArrayList<Menu> menuArrayList;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private ProfileSportAdapter profileSportAdapter;

    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        applicationFitnes = (ApplicationFitnes) getApplication();
        userData = applicationFitnes.getUserData();
        // design
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColorMain));
        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
      sportArrayList = userData.getSportList();
        menuArrayList = userData.getMenuList();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        spinner = (Spinner) findViewById(R.id.spinnerSelect);

        List<String> stringList = new ArrayList<>();
        stringList.add("Meni");
        stringList.add("Šport");

        ArrayAdapter<String> adapter =new  ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);



        final Context con = this;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(spinner.getSelectedItemId() == 0){
                        profileAdapter = new ProfileAdapter(con, menuArrayList);
                        recyclerView.setAdapter(profileAdapter);
                    }if(spinner.getSelectedItemId() == 1){
                    profileSportAdapter = new ProfileSportAdapter(con, sportArrayList);
                    recyclerView.setAdapter(profileSportAdapter);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
