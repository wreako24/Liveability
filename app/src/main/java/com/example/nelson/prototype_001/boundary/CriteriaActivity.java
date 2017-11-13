package com.example.nelson.prototype_001.boundary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nelson.prototype_001.R;
import com.example.nelson.prototype_001.controller.LiveableDBController;
import com.example.nelson.prototype_001.entity.Coordinate;
import com.example.nelson.prototype_001.entity.MyItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;

public class CriteriaActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Android objects
    private boolean flag = false;

    private Button btnCancel;
    private Button btnApply;
    private DrawerLayout draw;
    private LinearLayout sort_popup;
    private FloatingActionButton fab;
    private GoogleMap gm;


    final String mLogTag="CriteriaAct";
    private String value;
    DatabaseReference mDatabase;
    private ClusterManager<MyItem> mClusterManager;

    double envValue=0;
    double hcValue=0;
    double buildValue=0;
    double eduValue=0;
    double transValue=0;
    double accessValue=0;

    LiveableDBController dbCtrl= new LiveableDBController();

    ArrayList<Double> scoreList;
    Coordinate retCoor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setupWindowAnimations();

        setContentView(R.layout.criteria_layout);


        //Declare button
        final TextView envText = (TextView) findViewById(R.id.tv_environment_rate);
        final TextView hcText = (TextView) findViewById(R.id.tv_healthcare_rate);
        final TextView buildText = (TextView) findViewById(R.id.tv_building_rate);
        final TextView eduText = (TextView) findViewById(R.id.tv_education_rate);
        final TextView transText = (TextView) findViewById(R.id.tv_transport_rate);
        final TextView accessText = (TextView) findViewById(R.id.tv_accessability_rate);


        value = extras.getString("key");
        final double score = Double.parseDouble(extras.getString("score"));

        setTitle(value);

        //Get Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_criteria);
        mapFragment.getMapAsync(this);


        new AsyncTask<Void, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                progressDialog = new ProgressDialog(CriteriaActivity.this);
                progressDialog.setMessage("Fetching Scores for "+value);
                progressDialog.setCancelable(false);
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... params) {

                scoreList=dbCtrl.getScore(value);
                return "";

            }

            @Override
            protected void onPostExecute(String result) {

                envValue=scoreList.get(0);
                hcValue=scoreList.get(1);
                buildValue=scoreList.get(2);
                eduValue=scoreList.get(3);
                transValue=scoreList.get(4);
                accessValue=scoreList.get(5);




                double total = envValue + hcValue + buildValue + eduValue + transValue + accessValue;
                envText.setText(String.valueOf((int) ((envValue / total) * 100)) + "%");
                hcText.setText(String.valueOf((int) ((hcValue / total) * 100)) + "%");
                buildText.setText(String.valueOf((int) ((buildValue / total) * 100)) + "%");
                eduText.setText(String.valueOf((int) ((eduValue / total) * 100)) + "%");
                transText.setText(String.valueOf((int) ((transValue / total) * 100)) + "%");
                accessText.setText(String.valueOf((int) ((accessValue / total) * 100)) + "%");


                display(score);
                super.onPostExecute(result);
                progressDialog.dismiss();



            }
        }.execute();


        // Action Tool Bar consist of Search, Sort and Home button

        // Find ID and load to object
        draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        sort_popup = (LinearLayout) findViewById(R.id.lin_sort_container);
    }


    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    public void clickEnv(View v){
        Log.e(mLogTag,"Click environment");
        if(envValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Environment");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    public void clickEdu(View v){
        if(eduValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Education");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    public void clickBuild(View v){
        if(buildValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Building");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    public void clickHc(View v){
        if(hcValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Healthcare");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    public void clickTrans(View v){
        if(transValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Transport");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    public void clickAccess(View v){
        if(accessValue!=0) {
            Intent myIntent = new Intent(CriteriaActivity.this, CriteriaDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("key", value);
            extras.putString("type", "Accessibility");
            myIntent.putExtras(extras);

            CriteriaActivity.this.startActivity(myIntent);
        }else{
            Snackbar.make(v, "There is no data for this criteria! ", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cd, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            case R.id.action_back_home:
                Intent i = new Intent(this, LiveabilityUI.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void display(final double weightage) {

        gm.setOnCameraIdleListener(mClusterManager);

        mClusterManager = new ClusterManager<>(this, gm);

        new AsyncTask<Void, Void, String>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... params) {




                retCoor=new Coordinate();
                retCoor=dbCtrl.getCoor(value);



                return "";

            }

            @Override
            protected void onPostExecute(String result) {

                double lat=0;
                double lng=0;

                lat =retCoor.getLatitude();
                lng =retCoor.getLongitude();

                for (int r = 0; r < (int)Math.round(weightage); r++) {
                    MyItem offsetItem = new MyItem(lat, lng, value, value);
                    mClusterManager.addItem(offsetItem);
                }
                LatLng currLoc = new LatLng(lat, lng);

                gm.addCircle(new CircleOptions()
                        .center(currLoc)
                        .radius(2500)
                        .strokeColor(Color.RED)
                        .strokeWidth(4));

                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc,13));


                mClusterManager.setRenderer(new DefaultClusterRenderer<MyItem>(getApplicationContext(), gm, mClusterManager) {
                    @Override
                    protected String getClusterText(int bucket) {
                        return String.valueOf(bucket);
                    }

                    @Override
                    protected int getBucket(Cluster cluster) {
                        return cluster.getSize();
                    }
                });


                gm.setOnMarkerClickListener(mClusterManager);




                super.onPostExecute(result);
            }
        }.execute();




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3521, 103.8198), 10));
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setPadding(0,0,0,180);

        gm=googleMap;
    }


}
