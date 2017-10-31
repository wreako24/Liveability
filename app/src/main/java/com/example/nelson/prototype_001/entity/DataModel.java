package com.example.nelson.prototype_001.entity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nelson on 26/9/2017.
 */
public class DataModel {

    String score;
    String district;
    String ranking;
    String desc;
    String comment;
   LatLng coor;
    boolean isSelected;


    public DataModel(String score, String district, String ranking, String desc, boolean isSelected) {
        this.score = score;
        this.district = district;
        this.ranking = ranking;
        this.desc = desc;
        this.isSelected=isSelected;
    }

    public DataModel(String district, String desc) {
        this.district = district;
        this.desc = desc;
    }

    public DataModel() {
    }

    public LatLng getCoor() {
        return coor;
    }

    public void setCoor(LatLng coor) {
        this.coor = coor;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScore() {
        return score;
    }

    public String getDistrict() {
        return district;
    }

    public String getRanking() {
        return ranking;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}