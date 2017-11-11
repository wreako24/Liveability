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
import com.example.nelson.prototype_001.controller.AlgoController;
import com.example.nelson.prototype_001.controller.LiveableDBController;
import com.example.nelson.prototype_001.entity.Coordinate;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;


public class LiveabilityUI extends AppCompatActivity implements OnMapReadyCallback{
// Android objects
    private DrawerLayout draw;
    private boolean flag = false;
    private boolean alreadyExecuted = false;
    private FloatingActionButton fab;
    private Button btnCancel;
    private Button btnApply;
    ArrayList<String> lwDistrictList =new ArrayList<>();
    ArrayList<District>districtRes=new ArrayList<>();
    LiveableDBController dbctrl=new LiveableDBController();
    AlgoController algoCtrl=new AlgoController();

    Rank aRank = new Rank(6,CriteriaCat.ACCESSIBILITY);
    Rank bRank = new Rank(4,CriteriaCat.BUILDING);
    Rank eRank = new Rank(1,CriteriaCat.EDUCATION);
    Rank hRank = new Rank(2,CriteriaCat.HEALTHCARE);
    Rank enRank = new Rank(3,CriteriaCat.ENVIRONMENT);
    Rank tRank = new Rank(5,CriteriaCat.TRANSPORT);
    ArrayList<Rank>rankList;

    private ClusterManager<MyItem> mClusterManager;
    private final static String mLogTag = "Main";

    ArrayList<DataModel> dataModels=new ArrayList<>();
    ListView listView;
    private static DistrictAdapter adapter;
    DatabaseReference mDatabase;
    private LinearLayout sort_popup;



    double sRank1,sRank2,sRank3;


    boolean clickFlag=true;
    boolean found=false;



    GoogleMap gm;

    ArrayList<Data>dList= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Liveability");




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

        mDatabase= FirebaseDatabase.getInstance().getReference();


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
                for (int i = 0; i < dList.size(); i++) {
                    switch(dList.get(i).title){
                        case "ACCESSIBILITY":
                            aRank.setPosition(i+1,CriteriaCat.ACCESSIBILITY);
                            break;
                        case "BUILDING":
                            bRank.setPosition(i+1,CriteriaCat.BUILDING);
                            break;
                        case "ENVIRONMENT":
                            enRank.setPosition(i+1,CriteriaCat.ENVIRONMENT);
                            break;
                        case "HEALTHCARE":
                            hRank.setPosition(i+1,CriteriaCat.HEALTHCARE);
                            break;
                        case "TRANSPORT":
                            tRank.setPosition(i+1,CriteriaCat.TRANSPORT);
                            break;
                        case "EDUCATION":
                            eRank.setPosition(i+1,CriteriaCat.EDUCATION);
                    }


                }

                rankList=new ArrayList<>();

                rankList.add(aRank);
                rankList.add(bRank);
                rankList.add(enRank);
                rankList.add(hRank);
                rankList.add(tRank);
                rankList.add(eRank);

                Log.e(mLogTag,String.valueOf(aRank.getRankWeightage()));

                draw.closeDrawer(Gravity.RIGHT);

                updateData(CriteriaCat.ORIGINAL);

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



        initData(CriteriaCat.ORIGINAL);





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


    public void updateData(final CriteriaCat criCat){

        new AsyncTask<Void, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                progressDialog = new ProgressDialog(LiveabilityUI.this);
                progressDialog.setMessage("Refreshing List");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... params) {

                dataModels= new ArrayList<>();
                districtRes=new ArrayList<>();
                dbctrl.refresh(rankList);


                return "";

            }

            @Override
            protected void onPostExecute(String result) {

                districtRes=dbctrl.getList();

                districtRes=algoCtrl.sortDistrict(districtRes,criCat);


                for(int i=0;i<districtRes.size();i++)
                    Log.e(mLogTag,districtRes.get(i).getName());

                districtRes=algoCtrl.sortDistrict(dbctrl.getList(),criCat);

                for(int i=0;i<districtRes.size();i++)
                    Log.e(mLogTag,districtRes.get(i).getName());

                for(int i=0;i<districtRes.size();i++) {
                    dataModels.add(new DataModel(Double.toString(districtRes.get(i).getValue()), districtRes.get(i).getName(), Integer.toString(i+1), districtRes.get(i).getName(),false));

                    switch(i){
                        case 0:
                            sRank1= districtRes.get(i).getValue();
                            break;
                        case 1:
                            sRank2=  districtRes.get(i).getValue();
                            break;
                        case 2:
                            sRank3= districtRes.get(i).getValue();
                            break;
                    }
                }

                adapter= new DistrictAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel dataModel= dataModels.get(position);

                        Intent myIntent = new Intent(LiveabilityUI.this, CriteriaActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",dataModel.getDistrict());
                        extras.putString("score",dataModel.getScore());
                        myIntent.putExtras(extras);

                        LiveabilityUI.this.startActivity(myIntent);


                    }
                });


                display();



                lwDistrictList.clear();

                for (int i = 0; i < districtRes.size(); i++) {
                    lwDistrictList.add(districtRes.get(i).getName());
                }



                super.onPostExecute(result);
                progressDialog.dismiss();






            }
        }.execute();






    }



    public void initData(final CriteriaCat criCat){

        new AsyncTask<Void, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                progressDialog = new ProgressDialog(LiveabilityUI.this);
                progressDialog.setMessage("Fetching Data");
                progressDialog.setCancelable(false);
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... params) {

                dataModels= new ArrayList<>();
                districtRes=new ArrayList<>();
                dbctrl.init() ;

                return "";

            }

            @Override
            protected void onPostExecute(String result) {

                districtRes=dbctrl.getList();

                districtRes=algoCtrl.sortDistrict(districtRes,CriteriaCat.ORIGINAL);


                for(int i=0;i<districtRes.size();i++)
                    Log.e(mLogTag,districtRes.get(i).getName());

                districtRes=algoCtrl.sortDistrict(dbctrl.getList(),criCat);

                for(int i=0;i<districtRes.size();i++)
                    Log.e(mLogTag,districtRes.get(i).getName());

                for(int i=0;i<districtRes.size();i++) {
                    dataModels.add(new DataModel(Double.toString(districtRes.get(i).getValue()), districtRes.get(i).getName(), Integer.toString(i+1), districtRes.get(i).getName(),false));

                    switch(i){
                        case 0:
                            sRank1= districtRes.get(i).getValue();
                            break;
                        case 1:
                            sRank2=  districtRes.get(i).getValue();
                            break;
                        case 2:
                            sRank3= districtRes.get(i).getValue();
                            break;
                    }
                }

                adapter= new DistrictAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel dataModel= dataModels.get(position);

                        Intent myIntent = new Intent(LiveabilityUI.this, CriteriaActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",dataModel.getDistrict());
                        extras.putString("score",dataModel.getScore());
                        myIntent.putExtras(extras);

                        LiveabilityUI.this.startActivity(myIntent);


                    }
                });


                    display();



                lwDistrictList.clear();

                for (int i = 0; i < districtRes.size(); i++) {
                    lwDistrictList.add(districtRes.get(i).getName());
                }


                super.onPostExecute(result);
                progressDialog.dismiss();


                findViewById(R.id.loadingPanel).setVisibility(View.GONE);





            }
        }.execute();

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

                Intent myIntent = new Intent(LiveabilityUI.this, CriteriaActivity.class);
                Bundle extras = new Bundle();
                extras.putString("key",searchString);
                extras.putString("score",Double.toString(getScore(searchString)));
                myIntent.putExtras(extras);

                LiveabilityUI.this.startActivity(myIntent);


            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, lwDistrictList);
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
                        Intent myIntent = new Intent(LiveabilityUI.this, CriteriaActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",query);
                        extras.putString("score",Double.toString(getScore(query)));
                        myIntent.putExtras(extras);

                        LiveabilityUI.this.startActivity(myIntent);
                    }

                }

                if(!found) {
                    new AsyncTask<Void, Void, String>() {
                        ProgressDialog progressDialog;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();


                        }

                        @Override
                        protected String doInBackground(Void... params) {

                            AddressToDistrictAPI atd = new AddressToDistrictAPI();
                            district[0] = atd.getDistrict(query);

                            if (!district[0].equals("null")) {
                                Intent myIntent = new Intent(LiveabilityUI.this, CriteriaActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("key", district[0]);
                                extras.putString("score", Double.toString(getScore(district[0])));
                                myIntent.putExtras(extras);

                                LiveabilityUI.this.startActivity(myIntent);

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

                hideKeyboardwithoutPopulate(LiveabilityUI.this);
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
                Intent myIntent = new Intent(LiveabilityUI.this, LiveabilityUI.class);
                LiveabilityUI.this.startActivity(myIntent);
                return true;
        }


            return super.onOptionsItemSelected(item);
        }




        public void click_cri_env(View v) {
            updateData(CriteriaCat.ENVIRONMENT);
            flag = false;
            sort_popup.setVisibility(View.INVISIBLE);
        }

        public void click_cri_edu(View v){
            updateData(CriteriaCat.EDUCATION);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_trans(View v){
            updateData(CriteriaCat.TRANSPORT);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_oc(View v){
            updateData(CriteriaCat.ORIGINAL);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_hc(View v){
            updateData(CriteriaCat.HEALTHCARE);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }
        public void click_cri_build(View v){
            updateData(CriteriaCat.BUILDING);
            flag = false;
            sort_popup.setVisibility(View.GONE);
        }

        public void click_cri_access(View v){
            updateData(CriteriaCat.ACCESSIBILITY);
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



    private void display() {

        final double[] lat = new double[1];
        final double[] lng = new double[1];
        gm.setOnCameraIdleListener(mClusterManager);
        mClusterManager = new ClusterManager<>(this, gm);

        new AsyncTask<Void, Void, String>() {
            ProgressDialog progressDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LiveabilityUI.this);
                progressDialog.setMessage("Generating UI");
                progressDialog.setCancelable(false);
                progressDialog.show();


            }

            @Override
            protected String doInBackground(Void... params) {



                for(int i=0;i<districtRes.size();i++){
                    Coordinate retCoor=dbctrl.getCoor(districtRes.get(i).getName());
                    lat[0] =retCoor.getLatitude();
                    lng[0] =retCoor.getLongitude();
                    Coordinate curr=new Coordinate(lat[0], lng[0]);
                    districtRes.get(i).setRegionCoordinate(curr);

                    for (int r = 0; r < Math.round(districtRes.get(i).getValue()); r++) {

                        MyItem offsetItem = new MyItem(lat[0], lng[0],districtRes.get(i).getName(), districtRes.get(i).getName());
                        mClusterManager.addItem(offsetItem);

                    }


                }

                return "";

            }

            @Override
            protected void onPostExecute(String result) {





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
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(districtRes.get(0).getRegionCoordinate().getLatitude(), districtRes.get(0).getRegionCoordinate().getLongitude()), 100));
                super.onPostExecute(result);
                progressDialog.dismiss();
            }
        }.execute();




    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3521, 103.8198), 100));

        gm=googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);





    }
}

