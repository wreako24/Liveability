package com.example.nelson.prototype_001.controller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nelson.prototype_001.LiveabilityUI;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.Rank;
import com.example.nelson.prototype_001.liveInterface.DatabaseInterface;

import java.util.ArrayList;

/**
 * Created by Nelson on 20/9/2017.
 */

public class LiveableDBController {

    DatabaseInterface db=new Firebase();
    ArrayList<District>districtRes=new ArrayList<>();


    public ArrayList<District> init(){


        db=new Firebase();
        districtRes=new ArrayList<>();
        districtRes=db.initialize();

        return districtRes;
    }

    public ArrayList<District> refresh(ArrayList<Rank>rankList){
        districtRes=db.refreshData(rankList);


        return districtRes;
    }




}
