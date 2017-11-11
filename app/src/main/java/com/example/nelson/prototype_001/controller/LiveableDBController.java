package com.example.nelson.prototype_001.controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nelson.prototype_001.entity.Coordinate;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.Rank;
import com.example.nelson.prototype_001.liveInterface.DatabaseInterface;
import com.example.nelson.prototype_001.liveInterface.OnGetDataListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nelson on 20/9/2017.
 */

public class LiveableDBController {

    static DatabaseInterface db=new Firebase();
    ArrayList<District>districtList;


    public void init(){
        db.initialize();
    }

    public Coordinate getCoor(String location){
        Coordinate ret=db.getCoor(location);

        return ret;
    }


    public void refresh(ArrayList<Rank>rankList){
        db.refreshData(rankList);
    }

    public ArrayList<District> getList(){

        districtList=db.getData();

       return districtList;
    }







}
