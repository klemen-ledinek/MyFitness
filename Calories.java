package com.example.asus.testklemen;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import Data.Menu;
import Data.UserData;

public class Calories extends AppCompatActivity {



    EditText inputCalories;

    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private Menu menu;
    private ArrayList<Menu> menuList;
    private int replaceIndex = -1;

    private Button btnAdd;
    private TextView caloriesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        applicationFitnes = (ApplicationFitnes)getApplication();
        userData = applicationFitnes.getUserData();

        applicationFitnes = (ApplicationFitnes)getApplication();
        userData = applicationFitnes.getUserData();
        menuList = userData.getMenuList();

        menu = new Menu();
        checkList();


        btnAdd = (Button) findViewById(R.id.addButton);
        inputCalories=(EditText) findViewById(R.id.caloriesInput);
        caloriesText = (TextView) findViewById(R.id.caloriesTextView);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkList();

                menu.addCalories(Integer.parseInt(inputCalories.getText().toString()));
                caloriesText.setText(String.valueOf(inputCalories.getText().toString()) + " kcal");
                if(replaceIndex == -1){
                    menuList.add(menu);
                }else{
                    menuList.remove(replaceIndex);
                    menuList.add(replaceIndex,menu);
                }
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
                }

            }
        }
    }
}
