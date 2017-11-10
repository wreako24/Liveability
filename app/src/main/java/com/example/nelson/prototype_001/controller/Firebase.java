package com.example.nelson.prototype_001.controller;

import android.util.Log;

import com.example.nelson.prototype_001.entity.Criteria;
import com.example.nelson.prototype_001.entity.CriteriaCat;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.Rank;
import com.example.nelson.prototype_001.liveInterface.DatabaseInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nelson on 10/11/2017.
 */

public class Firebase implements DatabaseInterface {


    DatabaseReference mDatabase;
    ArrayList<District>districtRes;
    AlgoController algoCrtl=new AlgoController();

    Rank aRank;
    Rank bRank;
    Rank eRank;
    Rank enRank ;
    Rank hRank;
    Rank tRank;


    @Override
    public ArrayList<District> initialize() {

        aRank = new Rank(6,CriteriaCat.ACCESSIBILITY);
        bRank = new Rank(4,CriteriaCat.BUILDING);
        eRank = new Rank(1,CriteriaCat.EDUCATION);
        enRank = new Rank(3,CriteriaCat.ENVIRONMENT);
        hRank = new Rank(2,CriteriaCat.HEALTHCARE);
        tRank = new Rank(5,CriteriaCat.TRANSPORT);


        districtRes=new ArrayList<>();
        districtRes.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Criteria").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ArrayList<Criteria>cList=new ArrayList<>();

                    Criteria c1;


                    if(postSnapshot.child("Accessibility").child("criteria_value").getValue()!=null) {
                        double aScore=Double.parseDouble(postSnapshot.child("Accessibility").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.ACCESSIBILITY );
                        c1.setCriteriaRank(aRank);
                        c1.setCriteriaValue(Math.log(aScore));
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Building").child("criteria_value").getValue()!=null) {
                        double bScore=Double.parseDouble(postSnapshot.child("Building").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.BUILDING);
                        c1.setCriteriaValue(Math.log(bScore));
                        c1.setCriteriaRank(bRank);
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Education").child("criteria_value").getValue()!=null) {
                        double eScore=Double.parseDouble(postSnapshot.child("Education").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.EDUCATION);
                        c1.setCriteriaValue(Math.log(eScore));
                        c1.setCriteriaRank(eRank);
                        cList.add(c1);

                    }

                    if(postSnapshot.child("Environment").child("criteria_value").getValue()!=null) {
                        double enScore= Double.parseDouble(postSnapshot.child("Environment").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.ENVIRONMENT);
                        c1.setCriteriaValue(Math.log(enScore));
                        c1.setCriteriaRank(enRank);
                        cList.add(c1);

                    }
                    if(postSnapshot.child("Healthcare").child("criteria_value").getValue()!=null) {
                        double hScore=Double.parseDouble(postSnapshot.child("Healthcare").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.HEALTHCARE);
                        c1.setCriteriaValue(Math.log(hScore));
                        c1.setCriteriaRank(hRank);
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Transport").child("criteria_value").getValue()!=null) {
                        double tScore=Double.parseDouble(postSnapshot.child("Transport").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.TRANSPORT);
                        c1.setCriteriaValue(Math.log(tScore));
                        c1.setCriteriaRank(tRank);
                        cList.add(c1);
                    }


                    District d1=new District();
                    d1.setCriteriaList(cList);
                    d1.setName(postSnapshot.getKey().toString());
                    Log.e("Firebase",postSnapshot.getKey().toString());
                    d1.setValue(algoCrtl.computeDistrictValue(d1));


                    districtRes.add(d1);

                }
            }


            @Override public void onCancelled(DatabaseError error) {


            }
        });

        Log.e("Firebase",districtRes.get(0).getName());

        return districtRes;
    }

    @Override
    public ArrayList<District> refreshData(ArrayList<Rank>rankList) {

        for(int i=0;i<rankList.size();i++){
            if(rankList.get(i).getCat().equals(CriteriaCat.ACCESSIBILITY)){
                aRank=rankList.get(i);
            }
            if(rankList.get(i).getCat().equals(CriteriaCat.BUILDING)){
                bRank=rankList.get(i);
            }
            if(rankList.get(i).getCat().equals(CriteriaCat.EDUCATION)){
                eRank=rankList.get(i);
            }
            if(rankList.get(i).getCat().equals(CriteriaCat.ENVIRONMENT)){
                enRank=rankList.get(i);
            }
            if(rankList.get(i).getCat().equals(CriteriaCat.HEALTHCARE)){
                hRank=rankList.get(i);
            }
            if(rankList.get(i).getCat().equals(CriteriaCat.TRANSPORT)){
                tRank=rankList.get(i);
            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Criteria").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                districtRes=new ArrayList<>();
                districtRes.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ArrayList<Criteria>cList=new ArrayList<>();

                    Criteria c1;


                    if(postSnapshot.child("Accessibility").child("criteria_value").getValue()!=null) {
                        double aScore=Double.parseDouble(postSnapshot.child("Accessibility").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.ACCESSIBILITY );
                        c1.setCriteriaRank(aRank);
                        c1.setCriteriaValue(Math.log(aScore));
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Building").child("criteria_value").getValue()!=null) {
                        double bScore=Double.parseDouble(postSnapshot.child("Building").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.BUILDING);
                        c1.setCriteriaValue(Math.log(bScore));
                        c1.setCriteriaRank(bRank);
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Education").child("criteria_value").getValue()!=null) {
                        double eScore=Double.parseDouble(postSnapshot.child("Education").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.EDUCATION);
                        c1.setCriteriaValue(Math.log(eScore));
                        c1.setCriteriaRank(eRank);
                        cList.add(c1);

                    }

                    if(postSnapshot.child("Environment").child("criteria_value").getValue()!=null) {
                        double enScore= Double.parseDouble(postSnapshot.child("Environment").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.ENVIRONMENT);
                        c1.setCriteriaValue(Math.log(enScore));
                        c1.setCriteriaRank(enRank);
                        cList.add(c1);

                    }
                    if(postSnapshot.child("Healthcare").child("criteria_value").getValue()!=null) {
                        double hScore=Double.parseDouble(postSnapshot.child("Healthcare").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.HEALTHCARE);
                        c1.setCriteriaValue(Math.log(hScore));
                        c1.setCriteriaRank(hRank);
                        cList.add(c1);
                    }

                    if(postSnapshot.child("Transport").child("criteria_value").getValue()!=null) {
                        double tScore=Double.parseDouble(postSnapshot.child("Transport").child("criteria_value").getValue().toString());

                        c1=new Criteria();
                        c1.setCriteriaCategory(CriteriaCat.TRANSPORT);
                        c1.setCriteriaValue(Math.log(tScore));
                        c1.setCriteriaRank(tRank);
                        cList.add(c1);
                    }



                    District d1=new District();
                    d1.setCriteriaList(cList);
                    d1.setName(postSnapshot.getKey().toString());
                    d1.setValue(algoCrtl.computeDistrictValue(d1));



                    districtRes.add(d1);
                }

            }
            @Override public void onCancelled(DatabaseError error) {


            }
        });

        return districtRes;
    }

    @Override
    public void getData() {

    }
}
