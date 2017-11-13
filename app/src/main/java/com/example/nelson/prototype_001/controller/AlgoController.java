package com.example.nelson.prototype_001.controller;

import com.example.nelson.prototype_001.entity.Criteria;
import com.example.nelson.prototype_001.entity.CriteriaCat;
import com.example.nelson.prototype_001.entity.District;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Nelson on 20/9/2017.
 */

public class AlgoController {

    public double computeDistrictValue(District district){
        double value=0;
        for(int i =0; i<district.getCriteriaList().size();i++) {
            value += district.getCriteriaList().get(i).getCriteriaValue() * district.getCriteriaList().get(i).getCriteriaRank().getRankWeightage();
        }
        return Math.round(value);
    }




    public ArrayList<District> sortDistrict(ArrayList<District>sortDistrict,final CriteriaCat criCat){

        if(criCat.equals(CriteriaCat.ACCESSIBILITY)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.ACCESSIBILITY),c1.getCriteriaValue(CriteriaCat.ACCESSIBILITY));
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.ORIGINAL)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1, District c2) {
                    return Double.compare(c2.getValue(), c1.getValue());
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.TRANSPORT)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.TRANSPORT),c1.getCriteriaValue(CriteriaCat.TRANSPORT));
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.HEALTHCARE)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.HEALTHCARE),c1.getCriteriaValue(CriteriaCat.HEALTHCARE));
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.ENVIRONMENT)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.ENVIRONMENT),c1.getCriteriaValue(CriteriaCat.ENVIRONMENT));
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.EDUCATION)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.EDUCATION),c1.getCriteriaValue(CriteriaCat.EDUCATION));
                }
            });
        }
        else
        if(criCat.equals(CriteriaCat.BUILDING)){
            Collections.sort(sortDistrict, new Comparator<District>() {
                @Override
                public int compare(District c1,District c2) {
                    return Double.compare(c2.getCriteriaValue(CriteriaCat.BUILDING),c1.getCriteriaValue(CriteriaCat.BUILDING));
                }
            });
        }



        return sortDistrict;
    }


}
