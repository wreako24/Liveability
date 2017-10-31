package com.example.nelson.prototype_001.entity;

/**
 * Created by Nelson on 20/9/2017.
 */

public class RegionRank {

    String name;
    Coordinate rLoc;
    Rank regRank;

    public RegionRank(String name, Coordinate rLoc, Rank regRank) {
        this.name = name;
        this.rLoc = rLoc;
        this.regRank = regRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getrLoc() {
        return rLoc;
    }

    public void setrLoc(Coordinate rLoc) {
        this.rLoc = rLoc;
    }

    public Rank getRegRank() {
        return regRank;
    }

    public void setRegRank(Rank regRank) {
        this.regRank = regRank;
    }
}
