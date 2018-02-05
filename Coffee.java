package com.example.asus.testklemen;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import Data.Menu;
import Data.UserData;

public class Coffee extends AppCompatActivity {
    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private Menu menu;
    private ArrayList<Menu> menuList;
    private int replaceIndex = -1;

    private ImageButton btnAdd, btnRemove;
    private TextView cupsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        applicationFitnes = (ApplicationFitnes)getApplication();
        userData = applicationFitnes.getUserData();
        menuList = userData.getMenuList();

        menu = new Menu();
        checkList();


        btnAdd = (ImageButton) findViewById(R.id.addButton);
        btnRemove = (ImageButton) findViewById(R.id.removeButton);
        cupsView = (TextView) findViewById(R.id.cups);



        cupsView.setText(String.valueOf(menu.getCoffe()) + " cup(s)");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkList();

                menu.addCofffe(1);

                if(replaceIndex == -1){
                    menuList.add(menu);
                }else{
                    menuList.remove(replaceIndex);
                    menuList.add(replaceIndex,menu);
                }
                userData.setMenuList(menuList);
                applicationFitnes.setUserData(userData);


                cupsView.setText(String.valueOf(menu.getCoffe()) + " cup(s)");

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkList();
                if(menu.getCoffe()>=1){
                    menu.addCofffe(-1);
                }
                if(replaceIndex == -1){
                    menuList.add(menu);
                }else{
                    menuList.remove(replaceIndex);
                    menuList.add(replaceIndex,menu);
                }
                userData.setMenuList(menuList);
                applicationFitnes.setUserData(userData);

                cupsView.setText(String.valueOf(menu.getCoffe()) + " cup(s)");

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
                }

            }
        }
    }
}
