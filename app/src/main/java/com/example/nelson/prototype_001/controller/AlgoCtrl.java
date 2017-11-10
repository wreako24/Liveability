package com.example.nelson.prototype_001.controller;

import com.example.nelson.prototype_001.entity.Criteria;
import com.example.nelson.prototype_001.entity.District;

/**
 * Created by Nelson on 20/9/2017.
 */

public class AlgoCtrl {

    public static double computeDistrictValue(District district){
        double value=0;
        for(int i =0; i<district.getCriteriaList().size();i++) {
            value += district.getCriteriaList().get(i).getCriteriaValue() * district.getCriteriaList().get(i).getCriteriaRank().getRankWeightage();
        }
        return Math.round(value);
    }


}
