package com.example.nelson.prototype_001.entity;

import java.util.ArrayList;

/**
 * Created by Nelson on 15/9/2017.
 */

public class Criteria {
    String criteriaName;
    Rank criteriaRank;
    double criteriaValue;
    CriteriaCat criteriaDescription;
    CriteriaCat criteriaCategory;
    Coordinate coordinate;



    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public Rank getCriteriaRank() {
        return criteriaRank;
    }

    public void setCriteriaRank(Rank criteriaRank) {
        this.criteriaRank = criteriaRank;
    }


    public double getCriteriaValue() {
        return criteriaValue;
    }

    public void setCriteriaValue(double criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    public CriteriaCat getCriteriaDescription() {
        return criteriaDescription;
    }

    public void setCriteriaDescription(CriteriaCat criteriaDescription) {
        this.criteriaDescription = criteriaDescription;
    }

    public CriteriaCat getCriteriaCategory() {
        return criteriaCategory;
    }

    public void setCriteriaCategory(CriteriaCat criteriaCategory) {
        this.criteriaCategory = criteriaCategory;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
