package com.example.nelson.prototype_001.controller;

import com.example.nelson.prototype_001.entity.Coordinate;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.Rank;
import com.example.nelson.prototype_001.liveInterface.DatabaseInterface;

import java.util.ArrayList;

/**
 * Created by Nelson on 20/9/2017.
 */

public class LiveableDBController {

    static DatabaseInterface db=new Firebase();
    ArrayList<District>districtList;
    ArrayList<Double>scoreList;


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

    public ArrayList<Double> getScore(String location){
        scoreList=new ArrayList<>();
        scoreList=db.getScore(location);

        return scoreList;
    }







}
