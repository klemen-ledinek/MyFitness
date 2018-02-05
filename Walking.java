package com.example.asus.testklemen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Data.Loc;
import Data.Lokacija;
import Data.Runing;
import Data.Sport;
import Data.UserData;

public class Walking extends AppCompatActivity implements
        OnMapReadyCallback {
    private GoogleMap map;
    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private Data.Walking walking;
    private ImageButton startButton, pauseButton, stopButton;
    private GetLoctaionData getLoctaionData;
    private  Handler handler = new Handler();
    private int deleteIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        applicationFitnes = (ApplicationFitnes) getApplication();
        userData = applicationFitnes.getUserData();
        walking = new Data.Walking();
        startButton = (ImageButton) findViewById(R.id.startImage);
        pauseButton = (ImageButton) findViewById(R.id.pauseImage);
        stopButton = (ImageButton) findViewById(R.id.stopImage);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLaout);
        mapFragment.getMapAsync(this);




        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                int cMinute = calender.get(Calendar.MINUTE);
                int cSecond = calender.get(Calendar.SECOND);

                walking.setStartingTime(new Time(cHourOfDay, cMinute, cSecond));
                Loc loc = new Loc();
                applicationFitnes.setLocation();
                loc.longtitude = applicationFitnes.getLongtitude();
                loc.lantitude = applicationFitnes.getLatitude();
                walking.addLocation(loc);
                runOnUiThread(new Runnable() {
                    public void run() {
                        getCurentLocation();// Update the UI
                    }
                });

                getLoctaionData = new GetLoctaionData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        getLoctaionData.execute();// Update the UI
                    }
                });



            }
        });


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                int cMinute = calender.get(Calendar.MINUTE);
                int cSecond = calender.get(Calendar.SECOND);

                walking.setFinishTime(new Time(cHourOfDay, cMinute, cSecond));
                Loc loc = new Loc();
                applicationFitnes.setLocation();
                loc.longtitude = applicationFitnes.getLongtitude();
                loc.lantitude = applicationFitnes.getLatitude();
                walking.addLocation(loc);

                runOnUiThread(new Runnable() {
                    public void run() {
                        getCurentLocation();// Update the UI
                    }
                });
                getLoctaionData.cancel(true);// Update the UI

                Sport sport = new Sport();
                boolean check = false;
                Sport deletItem = new Sport();
                if(sport.getWalkingList()==null)
                    sport.setWalkingList(new ArrayList<Data.Walking>());
                sport.addWalking(walking);
                sport.setDate(new Date(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)));
                if(userData.getSportList() != null){
                    for (Sport s:userData.getSportList()) {
                        if(s.getDate()==null){

                        }else{
                        if(s.getDate().getYear() == sport.getDate().getYear()&& s.getDate().getMonth() == sport.getDate().getMonth() &&s.getDate().getDay() == sport.getDate().getDay()) {
                            sport = s;
                            if(sport.getWalkingList() == null){
                                sport.setWalkingList(new ArrayList<Data.Walking>());
                            }
                            sport.addWalking(walking);
                            deletItem = s;
                            check = true;
                            break;
                        }
                     }
                     deleteIndex++;
                    }
                }else {
                    userData.setSportList(new ArrayList<Sport>());
                }
                if(check){
                    check=false;
                    userData.deleteFromList(deletItem);
                }
                userData.addToSportList(sport);

                applicationFitnes.setUserData(userData);
                applicationFitnes.saveInGson();
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        setupMap();
        getCurentLocation();

    }

    private void setupMap() {

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);



    }

    void getCurentLocation(){


        applicationFitnes.setLocation();


        double latitude = applicationFitnes.getLongtitude();
        double longitude = applicationFitnes.getLatitude();
        LatLng myLocation = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(myLocation).title("MyLocation"));

        CameraPosition cameraPosition = new CameraPosition(myLocation,15,0,0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


     class GetLoctaionData extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            //Looper.prepare();
           runOnUiThread(new Runnable() {
                public void run() {
                    new CountDownTimer(Integer.MAX_VALUE, 5000) {

                        public void onTick(long millisUntilFinished) {
                            if (isCancelled()) {
                                return ;
                            }
                            Loc loc = new Loc();
                            applicationFitnes.setLocation();
                            loc.lantitude = applicationFitnes.getLatitude();
                            loc.longtitude = applicationFitnes.getLongtitude();
                            walking.addLocation(loc);
                            if(map != null){

                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        handler.post(new Runnable() { // This thread runs in the UI
                                            @Override
                                            public void run() {
                                                getCurentLocation();// Update the UI
                                            }
                                        });
                                    }
                                };
                                new Thread(runnable).start();

                            }

                        }


                        public void onFinish() {
                        }

                    }.start();

                }
            });
            if (isCancelled()) {
                return null;
            }
            // Looper.loop();
            return null;


        }
        @Override
        protected void onPostExecute(String result) {
           // Looper.myLooper().quit();
        }

         @Override
         protected void onCancelled() {
             super.onCancelled();
         }
    }


}
