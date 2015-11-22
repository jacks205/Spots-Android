package com.jacks205.spots.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jacks205.spots.R;

public class ChooseSchoolActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("school", "Chapman University");

        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
