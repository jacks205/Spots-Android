package com.jacks205.spotsparking.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jacks205.spotsparking.R;
import com.jacks205.spotsparking.Spots;
import com.jacks205.spotsparking.SpotsSchool;
import com.jacks205.spotsparking.adapters.SpotsListAdapter;
import com.jacks205.spotsparking.constants.Constants;
import com.jacks205.spotsparking.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spotsparking.model.ParkingStructure;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements OnSpotsDataRetrievedListener{

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SpotsListAdapter spotsListAdapter;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();

        MobileAds.initialize(this, Constants.ADMOB_AD_ID);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

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


        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.INVISIBLE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdView.setVisibility(View.INVISIBLE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.pause();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
    protected void onStart() {
        super.onStart();
        if (mAdView != null) {
            int adHeight = mAdView.getHeight();
            listView.setPadding(0, 0, 0, (int)(adHeight * 1.5));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null)
            mAdView.resume();
        final Spots spots = Spots.getInstance();
        if(spots.selectedSchool != null)
            spots.getParkingData(this);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
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
