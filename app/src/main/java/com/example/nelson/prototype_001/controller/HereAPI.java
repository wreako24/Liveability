package com.example.nelson.prototype_001.controller;

import android.util.Log;

import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.utility.DistrictReader;
import com.example.nelson.prototype_001.utility.HTTPHandler;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Nelson on 29/9/2017.
 */

public class HereAPI {

    String district="null";
    boolean found;
    private final static String mLogTag = "AtoD";
    ArrayList<String>districtList=new ArrayList<>();

    public HereAPI( ArrayList<String>districtList ) {
        this.districtList=districtList;
    }

    public String getDistrict(String searchTerm){
        Log.e(mLogTag,searchTerm);
        String url="https://autocomplete.geocoder.cit.api.here.com/6.2/suggest.json?query="+searchTerm+"&country=SGP&app_id=2mjzas8VoLat9KLK0hU7&app_code=93z5MNghBt4J_GBOnS0uig";
        Log.e(mLogTag,url);
        HTTPHandler sh = new HTTPHandler();
        String jsonInfo = sh.makeServiceCall(url,"places");


        try {
            district=new DistrictReader().read(jsonInfo);
        } catch (JSONException e) {
            district="null";
            e.printStackTrace();
        }

        for(int i=0;i<districtList.size();i++){
            if(districtList.get(i).equals(district))
                found=true;
        }

        if(district.equals("null")||!found) {
            district="null";
        }


        return district;
    }

}
