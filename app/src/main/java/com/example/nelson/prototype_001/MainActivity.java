package com.example.nelson.prototype_001;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Button;


import com.example.nelson.prototype_001.adapter.Adapter;
import com.example.nelson.prototype_001.adapter.DistrictAdapter;
import com.example.nelson.prototype_001.adapter.DynamicRecyclingView;
import com.example.nelson.prototype_001.adapter.Data;
import com.example.nelson.prototype_001.controller.AddressToDistrictAPI;
import com.example.nelson.prototype_001.controller.AlgoCtrl;
import com.example.nelson.prototype_001.entity.Criteria;
import com.example.nelson.prototype_001.entity.CriteriaCat;
import com.example.nelson.prototype_001.entity.DataModel;
import com.example.nelson.prototype_001.entity.District;
import com.example.nelson.prototype_001.entity.MyItem;
import com.example.nelson.prototype_001.entity.Rank;
import com.example.nelson.prototype_001.hoveranim.HoverOperationAllSwap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
// Android objects
    private DrawerLayout draw;
    private boolean flag = false;
    private FloatingActionButton fab;
    private Button btnCancel;
    private Button btnApply;
    ArrayList<String>districtNList=new ArrayList<>();



    private ClusterManager<MyItem> mClusterManager;
    private final static String mLogTag = "Main";
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static DistrictAdapter adapter;
    DatabaseReference mDatabase;
    private LinearLayout sort_popup;



    Rank eRank = new Rank(1);
    Rank tRank = new Rank(2);
    Rank hRank = new Rank(3);
    Rank enRank = new Rank(4);
    Rank bRank = new Rank(5);
    Rank aRank = new Rank(6);

    int sRank1,sRank2,sRank3;
    ArrayList<District>districtRes;
    int total;
    boolean clickFlag=true;
    boolean found=false;



    private GoogleMap gm;

    ArrayList<Data>dList= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Liveability!");
        //Get Map
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dList.add(new Data(CriteriaCat.EDUCATION.toString(), R.drawable.education_icon));
        dList.add( new Data(CriteriaCat.TRANSPORT.toString(), R.drawable.transport_icon));
        dList.add(new Data(CriteriaCat.HEALTHCARE.toString(), R.drawable.healthcare_icon));
        dList.add(new Data(CriteriaCat.ENVIRONMENT.toString(), R.drawable.environment_icon));
        dList.add(new Data(CriteriaCat.BUILDING.toString(), R.drawable.building_icon));
        dList.add(new Data(CriteriaCat.ACCESSIBILITY.toString(), R.drawable.accessiblity_icon));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        sort_popup = (LinearLayout) findViewById(R.id.lin_sort_container);
        listView=(ListView)findViewById(R.id.list);



        // Find ID and load to object
        draw = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnApply = (Button) findViewById(R.id.btnApply);


        // Open slider
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(draw.isDrawerOpen(GravityCompat.END)) {
                    // Close the drawer and set the background
                    draw.closeDrawer(Gravity.RIGHT);
                    fab.setVisibility(View.VISIBLE);
                }
                else{

                    // Open the drawer and set the background
                    draw.openDrawer(Gravity.RIGHT);
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab.setAlpha((float) 0.7);

        final DynamicRecyclingView listView = (DynamicRecyclingView)findViewById(R.id.listView1);
        final Adapter adapter = new Adapter(this, dList);

        listView.setHoverOperation(new HoverOperationAllSwap(dList
        ));


        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnTouchListener(new View.OnTouchListener() {

                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {

                                            switch (motionEvent.getAction()) {
                                                case MotionEvent.ACTION_MOVE:
                                                    // popup the listView
                                                    int pos= listView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
                                                    Log.e(mLogTag, String.valueOf(pos));
                                                    listView.startMoveById(listView.getItemIdAtPosition(pos));
                                                    break;
                                            }
                                            return false;
                                        }
                                    });





        // Cancel Button to close the slider
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (draw.isDrawerOpen(GravityCompat.END)) {
                    draw.closeDrawer(Gravity.RIGHT);
                    draw.setBackgroundColor(0);
                    refreshList();
                    adapter.notifyDataSetChanged();
                    btnApply.setBackgroundResource(R.drawable.button_border);
                    btnApply.setTextColor(Color.BLACK);
                }

            }
        });

        // Apply button to get the rearranged values
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*btnApply.setBackgroundResource(0);
                btnApply.setBackgroundColor(Color.parseColor("#038596"));
                btnApply.setTextColor(Color.WHITE);*/
                for (int i = 0; i < dList.size(); i++) {
                    switch(dList.get(i).title){
                        case "ACCESSIBILITY":
                            aRank.setPosition(i+1);
                            break;
                        case "BUILDING":
                            bRank.setPosition(i+1);
                            break;
                        case "ENVIRONMENT":
                            enRank.setPosition(i+1);
                            break;
                        case "HEALTHCARE":
                            hRank.setPosition(i+1);
                            break;
                        case "TRANSPORT":
                            tRank.setPosition(i+1);
                            break;
                        case "EDUCATION":
                            eRank.setPosition(i+1);
                    }


                }
                adapter.notifyDataSetChanged();
                Log.e(mLogTag,String.valueOf(aRank.getRankWeightage()));
                draw.closeDrawer(Gravity.RIGHT);


                clickFlag=true;
                refreshData(CriteriaCat.ORIGINAL);

                refreshList();

            }
        });

        draw.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {
                refreshList();
                adapter.notifyDataSetChanged();
                fab.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onDrawerClosed(View view) {
                // your refresh code can be called from here
                refreshList();
                adapter.notifyDataSetChanged();
                fab.setVisibility(View.VISIBLE);


            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


        refreshData(CriteriaCat.ORIGINAL);

    }


    public void refreshList(){

        for (int j = 0; j < dList.size(); j++) {
            switch (dList.get(j).title) {
                case "ACCESSIBILITY":
                    swap(j,aRank.getPosition()-1);
                    break;
                case "BUILDING":
                    swap(j,bRank.getPosition()-1);
                    break;
                case "ENVIRONMENT":
                    swap(j,enRank.getPosition()-1);
                    break;
                case "HEALTHCARE":
                    swap(j,hRank.getPosition()-1);
                    break;
                case "TRANSPORT":
                    swap(j,tRank.getPosition()-1);
                    break;
                case "EDUCATION":
                    swap(j,eRank.getPosition()-1);
                    break;
            }
        }
    }



    public void swap(int indexOne, int indexTwo) {
        Data temp = dList.get(indexOne);
        dList.set(indexOne, dList.get(indexTwo));
        dList.set(indexTwo, temp);
    }

    public void refreshData(final CriteriaCat criCat){

        fab.setVisibility(View.VISIBLE);

        dataModels= new ArrayList<>();

        mDatabase.child("Criteria").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                districtRes=new ArrayList<>();
                districtRes.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ArrayList<Criteria>cList=new ArrayList<Criteria>();

                    Criteria c1;

                    Log.e(mLogTag,String.valueOf(aRank.getRankWeightage()));


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
                    d1.setValue(AlgoCtrl.computeDistrictValue(d1));



                    districtRes.add(d1);
                }



                if(criCat.equals(CriteriaCat.ACCESSIBILITY)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.ACCESSIBILITY),c1.getCriteriaValue(CriteriaCat.ACCESSIBILITY));
                        }
                    });
                }

                if(criCat.equals(CriteriaCat.TRANSPORT)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.TRANSPORT),c1.getCriteriaValue(CriteriaCat.TRANSPORT));
                        }
                    });
                }

                if(criCat.equals(CriteriaCat.HEALTHCARE)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.HEALTHCARE),c1.getCriteriaValue(CriteriaCat.HEALTHCARE));
                        }
                    });
                }

                if(criCat.equals(CriteriaCat.ENVIRONMENT)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.ENVIRONMENT),c1.getCriteriaValue(CriteriaCat.ENVIRONMENT));
                        }
                    });
                }

                if(criCat.equals(CriteriaCat.EDUCATION)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.EDUCATION),c1.getCriteriaValue(CriteriaCat.EDUCATION));
                        }
                    });
                }

                if(criCat.equals(CriteriaCat.BUILDING)){
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1,District c2) {
                            return Double.compare(c2.getCriteriaValue(CriteriaCat.BUILDING),c1.getCriteriaValue(CriteriaCat.BUILDING));
                        }
                    });
                }


                if(criCat.equals(CriteriaCat.ORIGINAL)) {
                    Collections.sort(districtRes, new Comparator<District>() {
                        @Override
                        public int compare(District c1, District c2) {
                            return Double.compare(c2.getValue(), c1.getValue());
                        }
                    });
                }


                for(int i=0;i<districtRes.size();i++) {

                    total+=districtRes.get(i).getValue();

                    dataModels.add(new DataModel(Double.toString(districtRes.get(i).getValue()), districtRes.get(i).getName(), Integer.toString(i+1), districtRes.get(i).getName(),false));

                    switch(i){
                        case 0:
                            sRank1= (int) districtRes.get(i).getValue();
                            break;
                        case 1:
                            sRank2= (int) districtRes.get(i).getValue();
                            break;
                        case 2:
                            sRank3= (int) districtRes.get(i).getValue();
                            break;
                    }


                }
                Log.e(mLogTag,String.valueOf(total));


                adapter= new DistrictAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel dataModel= dataModels.get(position);

                        Intent myIntent = new Intent(MainActivity.this, CriteriaActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",dataModel.getDistrict());
                        extras.putString("score",dataModel.getScore());
                        myIntent.putExtras(extras);

                        MainActivity.this.startActivity(myIntent);


                    }
                });



                if(clickFlag) {
                    for (int i = 0; i < districtRes.size(); i++) {
                        display(districtRes.get(i).getValue(), districtRes.get(i).getName());
                    }
                    clickFlag=false;
                }

               districtNList.clear();
                   for (int i = 0; i < districtRes.size(); i++) {
                       districtNList.add(districtRes.get(i).getName());
                   }

               updateCoor(districtRes.get(0).getName());
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        // Do not iconify the widget; expand it by default

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);

        if (searchAutoComplete != null) {
            searchAutoComplete.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
            searchAutoComplete.setSingleLine(true);
        }

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                String searchString=(String)parent.getItemAtPosition(position);
                searchAutoComplete.setText(""+searchString);

                Intent myIntent = new Intent(MainActivity.this, CriteriaActivity.class);
                Bundle extras = new Bundle();
                extras.putString("key",searchString);
                double score=0;
                extras.putString("score",Double.toString(getScore(searchString)));
                myIntent.putExtras(extras);

                MainActivity.this.startActivity(myIntent);


            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, districtNList);
        searchAutoComplete.setAdapter(adapter);



        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String query) {
                // this is your adapter that will be filtered


                return true;
            }

            public boolean onQueryTextSubmit(final String query) {
                //Here u can get the value "query" which is entered in the search box.

                found=false;
                final String[] district = new String[1];

                Log.e(mLogTag,query);

                for(int i=0;i<districtRes.size();i++){
                    if(query.equals(districtRes.get(i).getName())){
                        found=true;
                        Intent myIntent = new Intent(MainActivity.this, CriteriaActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",query);
                        extras.putString("score",Double.toString(getScore(query)));
                        myIntent.putExtras(extras);

                        MainActivity.this.startActivity(myIntent);
                    }

                }

                if(!found) {
                    new AsyncTask<Void, Void, String>() {
                        ProgressDialog progressDialog;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();


                            progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.setMessage("Searching address");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                        }

                        @Override
                        protected String doInBackground(Void... params) {

                            AddressToDistrictAPI atd = new AddressToDistrictAPI();
                            district[0] = atd.getDistrict(query);

                            if (!district[0].equals("null")) {
                                Intent myIntent = new Intent(MainActivity.this, CriteriaActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("key", district[0]);
                                double score = 0;
                                extras.putString("score", Double.toString(getScore(district[0])));
                                myIntent.putExtras(extras);

                                MainActivity.this.startActivity(myIntent);

                            } else {

                            }
                            return district[0];

                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            progressDialog.dismiss();

                            if(district[0].equals("null"))
                                Toast.makeText(getApplicationContext(),"Address Not Found",Toast.LENGTH_SHORT).show();


                        }
                    }.execute();

                    found=false;
                }




                hideKeyboardwithoutPopulate(MainActivity.this);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);



        return true;
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_sort_criteria:
                // Sort Criteria clicks
                if (flag) {
                    flag = false;
                    sort_popup.setVisibility(View.INVISIBLE);
                } else {
                    flag = true;
                    sort_popup.setVisibility(View.VISIBLE);
                }
                return true;
            case R.id.action_back_home:
                // Back to home page
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;
        }


            return super.onOptionsItemSelected(item);
        }




        public void click_cri_env(View v) {

            refreshData(CriteriaCat.ENVIRONMENT);
            flag = false;
            sort_popup.setVisibility(View.INVISIBLE);

        }

        public void click_cri_edu(View v){
            refreshData(CriteriaCat.EDUCATION);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_trans(View v){
            refreshData(CriteriaCat.TRANSPORT);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_oc(View v){
            refreshData(CriteriaCat.ORIGINAL);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_hc(View v){
            refreshData(CriteriaCat.HEALTHCARE);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }
        public void click_cri_build(View v){
            refreshData(CriteriaCat.BUILDING);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_access(View v){
            refreshData(CriteriaCat.ACCESSIBILITY);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }



    private double getScore(String searchString){
        double score=0;
        for(int i=0;i<districtRes.size();i++){
            if(districtRes.get(i).getName().equals(searchString))
                score=districtRes.get(i).getValue();
        }
        return score;
    }

    private void updateCoor(final String searchString){

        mDatabase.child("District").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                snapshot.child(searchString);
                double lat=0;
                double lng=0;


                lat=Double.parseDouble(snapshot.child(searchString).child("district_lat").getValue().toString());
                lng=Double.parseDouble(snapshot.child(searchString).child("district_long").getValue().toString());

                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng ), 17));
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);


            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }


    private void display(double weightage,String index) {



        gm.setOnCameraIdleListener(mClusterManager);

        mClusterManager = new ClusterManager<>(this, gm);
        final String loc=index;
        final double weight=weightage;
        mDatabase.child("District").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                    snapshot.child(loc);
                    double lat=0;
                    double lng=0;


                    lat=Double.parseDouble(snapshot.child(loc).child("district_lat").getValue().toString());
                    lng=Double.parseDouble(snapshot.child(loc).child("district_long").getValue().toString());

                for (int r = 0; r < Math.round(weight); r++) {

                        MyItem offsetItem = new MyItem(lat, lng, loc, loc);
                        mClusterManager.addItem(offsetItem);


                    }

                mClusterManager.setRenderer(new DefaultClusterRenderer<MyItem>(getApplicationContext(), gm, mClusterManager) {
                    @Override
                    protected String getClusterText(int bucket) {
                        return String.valueOf(bucket);
                    }

                    @Override
                    protected int getBucket(Cluster cluster) {
                        return cluster.getSize();
                    }

                    @Override
                    protected int getColor(int clusterSize) {
                        if(clusterSize==sRank1){
                            return Color.parseColor("#ea5050");
                        }
                        if(clusterSize==sRank2){
                            return Color.parseColor("#0092b7");
                        }
                        if(clusterSize==sRank3){
                            return  Color.parseColor("#18782c");
                        }
                        else
                            return Color.parseColor("#57554b"); // Return any color you want here. You can base it on clusterSize.

                    }
                });



                        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                            @Override
                            public boolean onClusterItemClick(MyItem item) {
                                for (int i = 0; i < dataModels.size(); i++) {
                                    dataModels.get(i).setSelected(false);
                                }

                                int selection = 0;

                                for (int i = 0; i < districtRes.size(); i++) {
                                    if (districtRes.get(i).getName().equals(item.getTitle())) {
                                        selection = i;
                                        break;
                                    }
                                }

                                Log.e(mLogTag, String.valueOf(listView.getItemAtPosition(selection)));


                                dataModels.get(selection).setSelected(true);
                                adapter.notifyDataSetChanged();

                                listView.setSelection(selection);

                                return false;
                            }
                        });

                        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
                            @Override
                            public boolean onClusterClick(Cluster<MyItem> cluster) {

                                Log.e("I clicked @ ", "Cluster which consumes whole list of ClusterItems");


                                for(int i=0;i<dataModels.size();i++){
                                    dataModels.get(i).setSelected(false);
                                }

                                int selection =0;

                                for (MyItem item : cluster.getItems()) {

                                    Log.e(mLogTag,item.getTitle());
                                    for(int i=0;i<districtRes.size();i++){
                                        if(districtRes.get(i).getName().equals(item.getTitle())){
                                            selection=i;
                                            break;
                                        }
                                    }

                                }
                                Log.e(mLogTag,String.valueOf(listView.getItemAtPosition(selection)));


                                dataModels.get(selection).setSelected(true);
                                adapter.notifyDataSetChanged();

                                listView.setSelection(selection);

                                return false;
                            }
                        });



                gm.setOnMarkerClickListener(mClusterManager);
            }
            @Override public void onCancelled(DatabaseError error) { }
        });




    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3521, 103.8198), 100));
       // googleMap.setOnCameraIdleListener(mClusterManager);

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        gm=googleMap;





    }
}

