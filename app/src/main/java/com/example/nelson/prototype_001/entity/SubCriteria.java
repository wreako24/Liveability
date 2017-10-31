package com.example.nelson.prototype_001.entity;

/**
 * Created by Nelson on 15/9/2017.
 */

public class SubCriteria extends Criteria {

    String subCriteriaName;
    boolean isSelected;

    public String getSubCriteriaName() {
        return subCriteriaName;
    }

    public void setSubCriteriaName(String subCriteriaName) {
        this.subCriteriaName = subCriteriaName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
