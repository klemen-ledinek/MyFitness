package com.example.asus.testklemen;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Data.Menu;
import Data.UserData;

public class Water extends AppCompatActivity {
    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private Menu menu;
    private ArrayList<Menu> menuList;
    private int replaceIndex = -1;
    private boolean checker = false;

private ImageButton btnAdd, btnRemove;
private TextView waterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        applicationFitnes = (ApplicationFitnes)getApplication();
        userData = applicationFitnes.getUserData();
        menuList = userData.getMenuList();
        menu = new Menu();
        checkList();

        btnAdd = (ImageButton) findViewById(R.id.addButton);
        btnRemove = (ImageButton) findViewById(R.id.removeButton);
        waterText = (TextView) findViewById(R.id.waterTextView);

        waterText.setText(String.valueOf(menu.getWater() + " ml"));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkList();

            menu.addWater(200);

            if(replaceIndex == -1){
                menuList.add(menu);
            }else{
                menuList.remove(replaceIndex);
                menuList.add(replaceIndex,menu);
            }
            userData.setMenuList(menuList);
            applicationFitnes.setUserData(userData);
            waterText.setText(String.valueOf(menu.getWater() + " ml"));

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkList();
            if(menu.getWater()>=200){
                menu.addWater(-200);
            }
                if(replaceIndex == -1){
                    menuList.add(menu);
                }else{
                    if(checker){
                        menuList.remove(replaceIndex);
                        menuList.add(replaceIndex,menu);
                    }else{
                        menuList.add(menu);
                    }

                }
                waterText.setText(String.valueOf(menu.getWater() + " ml"));
                userData.setMenuList(menuList);
                applicationFitnes.setUserData(userData);
            }


        });




        // design
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColorMain));
        }

    }
    private void checkList(){
        if(menuList== null){
            menu = new Menu();
            menuList = new ArrayList<Menu>();

        }else if(menuList.size()>0){
            replaceIndex = -1;
            for (Menu tempMenu:menuList) {
                replaceIndex++;
                if(tempMenu.getDate().getDate() == menu.getDate().getDate()){
                    menu = tempMenu;
                    checker = true;
                    return;
                }

            }
        }
    }
}
