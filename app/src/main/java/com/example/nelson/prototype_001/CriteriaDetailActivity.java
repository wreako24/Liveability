package com.example.nelson.prototype_001;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nelson.prototype_001.adapter.CDetailAdapter;
import com.example.nelson.prototype_001.entity.DataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CriteriaDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    DatabaseReference mDatabase;
    String location;
    String type;
    private static CDetailAdapter adapter;
    ArrayList<DataModel> dataModels;
    ListView listView;
    LatLng loc;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

    GoogleMap gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        location =extras.getString("key");
        type=extras.getString("type");



        setContentView(R.layout.activity_criteria_detail);
  //
        //Get Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle(location+" - "+type);

        dataModels= new ArrayList<>();
        listView=(ListView)findViewById(R.id.detail_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Location").child(location).child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getCriteriaDetail((Map<String,Object>) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }}
        );

    }

    private void getCriteriaDetail(Map<String,Object> detail) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : detail.entrySet()){
            DataModel d1=new DataModel();
            //Get user map
            Map singleUser = (Map) entry.getValue();

            loc =new LatLng((double)singleUser.get("location_lat"),(double)singleUser.get("location_long"));

           d1.setDistrict((String)singleUser.get("location_name"));
            d1.setDesc((String)singleUser.get("location_address"));
            d1.setComment((String)singleUser.get("location_description"));

            d1.setCoor(loc);

            gm.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,13));
            Marker marker=gm.addMarker(new MarkerOptions().position(loc)
                    .title((String)singleUser.get("location_name")));
            dataModels.add(d1);

            mMarkerArray.add(marker);

        }

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);


        adapter= new CDetailAdapter(dataModels, getApplicationContext(), new PinClick() {
            @Override
            public void onClick(View v, int position) {
                for (Marker marker : mMarkerArray) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                DataModel dataModel= dataModels.get(position);
                Marker marker= gm.addMarker(new MarkerOptions().position(dataModel.getCoor())
                        .title(dataModel.getDistrict()).icon(BitmapDescriptorFactory.defaultMarker(187)));
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(dataModel.getCoor(),14));
                mMarkerArray.add(marker);

            }
        });


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);

                if(dataModel.getComment().equals("NIL")||dataModel.getComment().equals("na")){
                    Snackbar.make(view, "No description available!", Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CriteriaDetailActivity.this);
                    builder.setTitle(dataModel.getDistrict());
                    builder.setMessage(dataModel.getComment());
                    builder.setNegativeButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm=googleMap;
    }

    public void updateLoc(View v){
        int position = (Integer) v.getTag();


        Toast.makeText(getApplicationContext(),position,Toast.LENGTH_SHORT).show();
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
                Intent myIntent = new Intent(CriteriaDetailActivity.this, MainActivity.class);
               CriteriaDetailActivity.this.startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
