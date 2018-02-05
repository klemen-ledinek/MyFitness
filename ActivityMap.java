package com.example.asus.testklemen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Stack;

import Data.*;

public class ActivityMap extends AppCompatActivity implements
        OnMapReadyCallback {
    private GoogleMap map;
    private ApplicationFitnes applicationFitnes;
    private UserData userData;
    private Data.Walking walking;
    private Data.Runing runing;
    private Data.Cycling cycling;
    private ArrayList<Loc> locList;
    private ArrayList<Loc> tspList;
    private Handler handler = new Handler();
    private int rez[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        applicationFitnes = (ApplicationFitnes) getApplication();
        userData = applicationFitnes.getUserData();
        locList = new ArrayList<>();
        tspList = new ArrayList<>();
        walking = applicationFitnes.getSelectedWalking();
        runing = applicationFitnes.getSelectedRuning();
        cycling = applicationFitnes.getSelectedCycling();



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLaout);
        mapFragment.getMapAsync(this);

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
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }
        map.setMyLocationEnabled(true);
        doStuff();

        for(int i=1;i<locList.size();i++){
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(locList.get(i-1).longtitude,locList.get(i-1).lantitude), new LatLng(locList.get(i).longtitude,locList.get(i).lantitude))
                    .width(5)
                    .color(Color.RED));
        }

        setInOrder();

        for(int i=1;i<tspList.size();i++){
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(tspList.get(i-1).longtitude,tspList.get(i-1).lantitude), new LatLng(tspList.get(i).longtitude,tspList.get(i).lantitude))
                    .width(5)
                    .color(Color.BLUE));
        }

    }
    void setMapPoints(ArrayList<Loc> locs){

        LatLng myLocation = new LatLng(locs.get(0).longtitude, locs.get(0).lantitude);
        map.addMarker(new MarkerOptions().position(myLocation).title("MyLocation"));

        LatLng myLocation1 = new LatLng(locs.get(locs.size()-1).longtitude, locs.get(locs.size()-1).lantitude);
        map.addMarker(new MarkerOptions().position(myLocation1).title("MyLocation"));

        CameraPosition cameraPosition = new CameraPosition(myLocation,15,0,0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


     /*  for(Loc loc:locs){

           LatLng myLocation = new LatLng(loc.longtitude, loc.lantitude);
           map.addMarker(new MarkerOptions().position(myLocation).title("MyLocation"));

           CameraPosition cameraPosition = new CameraPosition(myLocation,15,0,0);
           map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/





    }

    void doStuff(){
        if(walking != null){
            locList = walking.getLocationTracker();
            setMapPoints(locList);
        }else if(runing != null) {
            locList = runing.getLocationTracker();
            setMapPoints(locList);
        }else if(cycling !=null){
            locList = cycling.getLocationTracker();
            setMapPoints(locList);// Update the UI


        }else {
            Toast.makeText(this,"Niste izbrali nič",Toast.LENGTH_LONG).show();
        }
        walking = null;
        runing = null;
        cycling = null;
        applicationFitnes.setSelectedCycling(cycling);
        applicationFitnes.setSelectedRuning(runing);
        applicationFitnes.setSelectedWalking(walking);


        rez = new int[locList.size()];
    }

    public void setInOrder(){
        int matrix[][] = new int[locList.size()][locList.size()];

        for(int i=0;i<locList.size();i++){
            for(int j=0;j<locList.size();j++){
                float result[] = new float[1];
                Location.distanceBetween(locList.get(i).lantitude,locList.get(i).longtitude,locList.get(j).lantitude,locList.get(j).longtitude,result);
                matrix[i][j] = Math.round(result[0]);
            }
        }
        tsp(matrix);
    }


    public void tsp(int adjacencyMatrix[][])
    {
        Stack<Integer> stack = new Stack<>();
        int numberOfNodes = adjacencyMatrix[1].length - 1;
        int[] visited = new int[numberOfNodes + 1];
        visited[0] = 1;
        stack.push(0);
        int element, dst = 0, i;
        int min;
        boolean minFlag = false;

        int check=0;
        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 1;
            min = Integer.MAX_VALUE;
            while (i <= numberOfNodes)
            {
                if (adjacencyMatrix[element][i] >= 0 && visited[i] == 0)
                {
                    if (min > adjacencyMatrix[element][i])
                    {
                        min = adjacencyMatrix[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag)
            {
                visited[dst] = 1;
                stack.push(dst);
                System.out.print(dst + "\t");
                rez[check]=dst;
                check++;
                minFlag = false;
                continue;
            }
            stack.pop();
        }

       tspList.add(locList.get(0));
        int z=1;
        for(int l=0;l<numberOfNodes;l++){
            tspList.add(locList.get(rez[l]));
        }
    }
}
