package com.example.nelson.prototype_001.controller;

import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.liveInterface.DatabaseInterface;

import java.util.ArrayList;

/**
 * Created by Nelson on 20/9/2017.
 */

public class LiveableDBController {

    ArrayList<District>districtRes;

    public ArrayList<District> init(){
        DatabaseInterface db=new Firebase();
        districtRes=db.initialize();

        return districtRes;
    }




}
