package com.example.nelson.prototype_001.Boundary;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.nelson.prototype_001.R;
import com.example.nelson.prototype_001.utility.HTTPHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    protected int getLayoutId() {
        return R.layout.activity_maps;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(1.3521,103.8198) , 14.0f) );



        startDemo();
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }


    /**
     * Run the demo-specific code.
     */
    protected abstract void startDemo();

    protected GoogleMap getMap() {
        return mMap;
    }
}
