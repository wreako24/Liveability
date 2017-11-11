package com.example.nelson.prototype_001.liveInterface;

import com.example.nelson.prototype_001.entity.District;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by Nelson on 11/11/2017.
 */

public interface OnGetDataListener {
    public void onStart();

    public void onSuccess(ArrayList<District>districtList);

    public void onFailed(DatabaseError databaseError);
}

