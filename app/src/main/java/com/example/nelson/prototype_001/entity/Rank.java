package com.example.nelson.prototype_001.entity;

/**
 * Created by Nelson on 20/9/2017.
 */

/**
 * Created by Nelson on 20/9/2017.
 */

public class Rank {
    int position;
    CriteriaCat cat;
    double rankWeightage;
    final int MULTIPLIER=3;

    public Rank(int position,CriteriaCat cat) {
        this.position  = position;
        this.cat=cat;

        switch(position) {
            case 1: rankWeightage = 9*MULTIPLIER;
                break;
            case 2: rankWeightage = 3*MULTIPLIER;
                break;
            case 3: rankWeightage = 1*MULTIPLIER;
                break;
            case 4: rankWeightage = 0.33*MULTIPLIER;
                break;
            case 5: rankWeightage = 0.166*MULTIPLIER;
                break;
            case 6: rankWeightage = 0.0833*MULTIPLIER;
                break;
            default: rankWeightage = 0;
                break;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position,CriteriaCat cat) {

        this.position=position;
        this.cat=cat;

        switch(position) {
            case 1: rankWeightage = 9*MULTIPLIER;
                break;
            case 2: rankWeightage = 3*MULTIPLIER;
                break;
            case 3: rankWeightage = 1*MULTIPLIER;
                break;
            case 4: rankWeightage = 0.33*MULTIPLIER;
                break;
            case 5: rankWeightage = 0.166*MULTIPLIER;
                break;
            case 6: rankWeightage = 0.0833*MULTIPLIER;
                break;
            default: rankWeightage = 0;
                break;
        }
    }

    public CriteriaCat getCat() {
        return cat;
    }

    public void setCat(CriteriaCat cat) {
        this.cat = cat;
    }

    public double getRankWeightage() {

        return rankWeightage;
    }

    public void setRankWeightage(double rankWeightage) {
        this.rankWeightage = rankWeightage;
    }
}
