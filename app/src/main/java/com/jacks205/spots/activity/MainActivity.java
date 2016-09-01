package com.jacks205.spots.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.jacks205.spots.BuildConfig;
import com.jacks205.spots.R;
import com.jacks205.spots.Spots;
import com.jacks205.spots.SpotsSchool;
import com.jacks205.spots.adapters.SpotsListAdapter;
import com.jacks205.spots.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spots.model.ParkingStructure;

public class MainActivity extends AppCompatActivity implements OnSpotsDataRetrievedListener{

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SpotsListAdapter spotsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();

        listView = (ListView)findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.yellow, R.color.red);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Spots spots = Spots.getInstance();
                spots.getParkingData(MainActivity.this);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(BuildConfig.DEBUG) {
            prefs.edit().clear().commit();
        }

        String school = prefs.getString("school", null);
        if(school == null){
            Intent i = new Intent(MainActivity.this, ChooseSchoolActivity.class);
            startActivity(i);
        } else {
            Spots spots = Spots.getInstance();
            switch (school) {
                case "Cal State University, Fullerton":
                    spots.selectedSchool = SpotsSchool.CSUF;
                    break;
                default:
                    spots.selectedSchool = SpotsSchool.CHAPMAN;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final Spots spots = Spots.getInstance();
        if(spots.selectedSchool != null)
            spots.getParkingData(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void onDataReceived(ParkingStructure[] structures) {
        if(spotsListAdapter == null){
            spotsListAdapter = new SpotsListAdapter(MainActivity.this, structures);
            listView.setAdapter(spotsListAdapter);
        } else {
            spotsListAdapter.setStructures(structures);
            spotsListAdapter.notifyDataSetChanged();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onDataError(Exception e) {
        Toast.makeText(MainActivity.this, "Unable to retrieve parking data.", Toast.LENGTH_LONG).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        actionBar.setElevation(0);
    }
}
