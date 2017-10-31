package com.example.nelson.prototype_001.controller;

import com.example.nelson.prototype_001.entity.Criteria;
import com.example.nelson.prototype_001.entity.District;

import java.util.ArrayList;

/**
 * Created by Nelson on 12/10/2017.
 */

public class DistrictCtrl {

    ArrayList<District> districtArrayList=new ArrayList<District>();

    public DistrictCtrl(ArrayList<District> districtArrayList){

        this.districtArrayList=districtArrayList;
    }

    public ArrayList<District> getDistrictList(){ return this.districtArrayList;}

    public void setDistrictArrayList(ArrayList<District> districtArrayList){

        this.districtArrayList=districtArrayList;
    }

    public double getDistrictCriteriaValue(String districtName){
        for(int i = 0; i<districtArrayList.size();i++) {
            if(districtArrayList.get(i).getName().equalsIgnoreCase(districtName)){
                return districtArrayList.get(i).getValue();
            }
        }
        return -1;
    }

    public District getDistrict(String districtName){
        for(int i = 0; i<districtArrayList.size();i++) {
            if(districtArrayList.get(i).getName().equalsIgnoreCase(districtName)){
                return districtArrayList.get(i);
            }
        }
        return null;
    }

    public ArrayList<District> sortDistrictByCriteria(Criteria c){
        return null;
    }

}
