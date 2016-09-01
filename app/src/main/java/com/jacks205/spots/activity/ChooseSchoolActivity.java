package com.jacks205.spots.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jacks205.spots.R;
import com.jacks205.spots.Spots;
import com.jacks205.spots.SpotsSchool;
import com.jacks205.spots.adapters.SchoolListAdapter;

public class ChooseSchoolActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Button letsGoBtn;
    ListView listview;
    View lastSelectedView;

    String[] schools = new String[]{ "Chapman University", "Cal State University, Fullerton" };
    int schoolIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);

        setActionBar();

        Typeface reg = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.welcomeTextView)).setTypeface(reg);
        ((TextView) findViewById(R.id.chooseLocationTextView)).setTypeface(reg);

        Typeface semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        letsGoBtn = (Button)findViewById(R.id.button);
        letsGoBtn.setTypeface(semibold);
        listview = (ListView)findViewById(R.id.schoolListView);
        SchoolListAdapter schoolListAdapter = new SchoolListAdapter(this, schools);
        listview.setAdapter(schoolListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastSelectedView != null)
                    deselectLastView(lastSelectedView);
                selectView(view);
                schoolIndex = position;
                lastSelectedView = view;
            }

            private void selectView(View view) {
                TextView name = (TextView) view.findViewById(R.id.schoolNameTextView);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                view.setBackgroundColor(getResources().getColor(R.color.black));
                name.setTextColor(Color.WHITE);
                imageView.setImageResource(R.drawable.check);
                letsGoBtn.setBackgroundColor(getResources().getColor(R.color.selectedBtn));
                letsGoBtn.setTextColor(Color.WHITE);
            }

            private void deselectLastView(View view) {
                TextView name = (TextView) view.findViewById(R.id.schoolNameTextView);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                view.setBackgroundColor(Color.TRANSPARENT);
                name.setTextColor(getResources().getColor(R.color.textUnselectedGray));
                imageView.setImageResource(R.drawable.empty);
                letsGoBtn.setBackgroundColor(getResources().getColor(R.color.unselectedBtn));
                letsGoBtn.setTextColor(getResources().getColor(R.color.textUnselectedGray));
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void letsGoOnClick(View v){
        if(schoolIndex == -1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Please select a school!")
                    .setPositiveButton("Ok", null);
            builder.show();
            return;
        }
        String school = schools[schoolIndex];
        preferences.edit().putString("school", school).apply();
        Spots spots = Spots.getInstance();
        switch (school) {
            case "Cal State University, Fullerton":
                spots.selectedSchool = SpotsSchool.CSUF;
                break;
            default:
                spots.selectedSchool = SpotsSchool.CHAPMAN;
        }
        finish();
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
