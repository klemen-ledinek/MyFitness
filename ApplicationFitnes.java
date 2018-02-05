package com.example.asus.testklemen;

import android.app.Application;
import android.os.Environment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Data.*;
import Data.Cycling;
import Data.Walking;

/**
 * Created by Klemen on 1/24/2018.
 */

public class ApplicationFitnes extends Application {
    private static final String DATA_MAP = "MyFitness";
    private static final String FILE_NAME = "UserData.json";
    private UserData userData;
    private Data.Walking selectedWalking;
    private Data.Runing selectedRuning;
    private Data.Cycling selectedCycling;
    public List<Lokacija> xlista = new ArrayList<>();
    GPS gpsUse;
    GoogleApiClient mGoogleApiClient;

    private double Latitude;
    private double Longtitude;

    @Override
    public void onCreate() {
        super.onCreate();
        if (userData == null) {
            userData = new UserData();
            readFromGson();

            if (!userData.userAccountIsSet()) {
                return;
            }
        }
        //setAllData();

    }

    public Data.Walking getSelectedWalking() {
        return selectedWalking;
    }

    public void setSelectedWalking(Walking selectedWalking) {
        this.selectedWalking = selectedWalking;
    }

    public Runing getSelectedRuning() {
        return selectedRuning;
    }

    public void setSelectedRuning(Runing selectedRuning) {
        this.selectedRuning = selectedRuning;
    }

    public Cycling getSelectedCycling() {
        return selectedCycling;
    }

    public void setSelectedCycling(Cycling selectedCycling) {
        this.selectedCycling = selectedCycling;
    }

    public void setLocation(){
        gpsUse = new GPS(this);

        Longtitude = gpsUse.getLatitude();
        Latitude = gpsUse.getLongitude();
    }


    public double getLatitude() {
        return Latitude;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public UserData getUserData(){
        return userData;
    }
    public void setUserData(UserData usrData){
        usrData = usrData;
        saveInGson();
    }

    public boolean saveInGson() {
        return saveInGson(userData, FILE_NAME);
    }

    public boolean readFromGson() {
        UserData tmp = readFromGson(FILE_NAME);
        if (tmp != null) userData = tmp;
        else return false;
        return true;
    }


    private boolean saveInGson(UserData a, String filename) {
        if (isExternalStorageWritable()) {
            File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                    + filename);
            try {
                long start = System.currentTimeMillis();
                System.out.println("Save " + file.getAbsolutePath() + " " + file.getName());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                PrintWriter pw = new PrintWriter(file);
                String sss = gson.toJson(a);
                System.out.println("Save time gson:" + (double) (System.currentTimeMillis() - start) / 1000);
                pw.println(sss);
                pw.close();
                System.out.println("Save time s:" + (double) (System.currentTimeMillis() - start) / 1000);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error saveInGson! (FileNotFoundException)");
            }
        } else {
            System.out.println(this.getClass().getCanonicalName() + " NOT Writable");
        }
        return false;
    }

    private UserData readFromGson(String name) {
        if (isExternalStorageReadable()) {
            try {
                File file = new File(this.getExternalFilesDir(DATA_MAP), "" + name);
                System.out.println("Load " + file.getAbsolutePath() + " " + file.getName());
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer sb = new StringBuffer();
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    sb.append(strLine).append('\n');
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                UserData a = gson.fromJson(sb.toString(), UserData.class);
                if (a == null) {
                    System.out.println("Error: fromJson Format error");
                } else {
                    System.out.println(a.toString());
                }
                return a;
            } catch (IOException e) {
                System.out.println("Error readFromGson " + e.toString());
            }
        }
        System.out.println("ExternalStorageAvailable is not avaliable");
        return null;
    }






    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
