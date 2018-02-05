package com.example.asus.testklemen;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import Data.UserData;

public class MainActivity extends AppCompatActivity {


    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    String accountName;


    ImageButton imgBtnWalk, imgBtnRun, imgBtnCycle,imgBtnWater, imgBtnCoffee,imgBtnKcal;
    Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationFitnes = (ApplicationFitnes)getApplication();
        userData = applicationFitnes.getUserData();

        if(!userData.userAccountIsSet()){
            chooseAccount();
        }


        // design
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColorMain));
        }

        imgBtnWalk=(ImageButton)findViewById(R.id.img_btn_walk);
        imgBtnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WalkIntent=new Intent(MainActivity.this,Walking.class);
                startActivity(WalkIntent);
            }
        });

        imgBtnRun=(ImageButton)findViewById(R.id.img_btn_running);
        imgBtnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RunIntent=new Intent(MainActivity.this,Running.class);
                startActivity(RunIntent);
            }
        });

        imgBtnCycle=(ImageButton)findViewById(R.id.img_btn_cycle);
        imgBtnCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CycleIntent=new Intent(MainActivity.this,Cycling.class);
                startActivity(CycleIntent);
            }
        });

        imgBtnWater=(ImageButton)findViewById(R.id.img_btn_water);
        imgBtnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WaterIntent=new Intent(MainActivity.this,Water.class);
                startActivity(WaterIntent);
            }
        });

        imgBtnCoffee=(ImageButton)findViewById(R.id.img_btn_caffee);
        imgBtnCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WaterIntent=new Intent(MainActivity.this,Coffee.class);
                startActivity(WaterIntent);
            }
        });

        imgBtnKcal=(ImageButton)findViewById(R.id.img_btn_kcal);
        imgBtnKcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WaterIntent=new Intent(MainActivity.this,Calories.class);
                startActivity(WaterIntent);
            }
        });

        profileBtn=(Button)findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent=new Intent(MainActivity.this,Profile.class);
                startActivity(ProfileIntent);
            }
        });
    }

    public void chooseAccount(){
        Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 999);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999&& resultCode == RESULT_OK) {
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.i("Choosen accountName:", accountName);
            userData.setUserAccount(accountName);
            applicationFitnes.setUserData(userData);
            applicationFitnes.saveInGson();
        }
    }

}
