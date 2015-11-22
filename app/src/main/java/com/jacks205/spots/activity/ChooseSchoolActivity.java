package com.jacks205.spots.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jacks205.spots.R;

public class ChooseSchoolActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Button letsGoBtn;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);

        setActionBar();

        letsGoBtn = (Button)findViewById(R.id.button);
        listview = (ListView)findViewById(R.id.schoolListView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        preferences.edit().putString("school", "Chapman University");

//        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        TextView title = (TextView)findViewById(R.id.actionBarTitle);
        title.setText("");
        actionBar.setElevation(0);
    }
}
