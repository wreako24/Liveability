package com.example.nelson.prototype_001.entity;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Nelson on 20/9/2017.
 */

public class District {
    String name;
    Coordinate regionCoordinate;
    double radius;
    double value;
    String address;
    ArrayList<Criteria> criteriaList;

    public District() {
    }

    public District(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getRegionCoordinate() {
        return regionCoordinate;
    }

    public void setRegionCoordinate(Coordinate regionCoordinate) {
        this.regionCoordinate = regionCoordinate;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Criteria> getCriteriaList() {
        return criteriaList;
    }


    public double getCriteriaValue(CriteriaCat c1){

        if(c1.equals(CriteriaCat.ACCESSIBILITY)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.ACCESSIBILITY)){
                    Log.e("District-",String.valueOf(criteriaList.get(i).getCriteriaValue()));
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }

        if(c1.equals(CriteriaCat.BUILDING)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.BUILDING)){
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }

        if(c1.equals(CriteriaCat.EDUCATION)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.EDUCATION)){
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }

        if(c1.equals(CriteriaCat.ENVIRONMENT)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.ENVIRONMENT)){
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }

        if(c1.equals(CriteriaCat.HEALTHCARE)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.HEALTHCARE)){
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }

        if(c1.equals(CriteriaCat.TRANSPORT)){
            for(int i=0;i<criteriaList.size();i++){
                if(criteriaList.get(i).getCriteriaCategory().equals(CriteriaCat.TRANSPORT)){
                    return criteriaList.get(i).getCriteriaValue();
                }
            }
        }


        return 0;
    }

    public void setCriteriaList(ArrayList<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
