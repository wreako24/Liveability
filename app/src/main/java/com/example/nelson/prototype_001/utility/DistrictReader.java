package com.example.nelson.prototype_001.utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nelson on 13/9/2017.
 */

public class DistrictReader {
    String mtag="DistrictReader";

    public String read(String jsonStr) throws JSONException {
        String district="null";
        Log.e(mtag, jsonStr);
     //   List<District> items = new ArrayList<>();
        final JSONObject o1 = new JSONObject(jsonStr);
        final JSONArray res = o1.getJSONArray("suggestions");
        final JSONObject addrout = res.getJSONObject(0);
        final JSONObject addr=addrout.getJSONObject("address");

        district=addr.getString("district");
        if(!district.equals("null")) {
            Log.e(mtag, district);
            return district;
        }
        return "";
    }
}
