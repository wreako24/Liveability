package com.example.nelson.prototype_001.liveInterface;

import com.example.nelson.prototype_001.entity.Coordinate;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.Rank;

import java.util.ArrayList;

/**
 * Created by Nelson on 9/11/2017.
 */

public interface DatabaseInterface {

     void initialize();

    Coordinate getCoor(String loc);

    void refreshData(ArrayList<Rank>rankList);

    ArrayList<District> getData();

    ArrayList<Double> getScore(String location);



}
