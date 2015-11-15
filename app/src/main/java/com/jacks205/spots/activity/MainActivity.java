package com.jacks205.spots.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jacks205.spots.R;
import com.jacks205.spots.Spots;
import com.jacks205.spots.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spots.model.ParkingStructure;
import com.jacks205.spots.model.Segment;
import com.jacks205.spots.views.PieChartView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Spots spots = Spots.getInstance();
//        spots.getParkingData(new OnSpotsDataRetrievedListener() {
//            @Override
//            public void onDataReceived(ParkingStructure[] structures, Date lastUpdated) {
//                String s = "";
//            }
//
//            @Override
//            public void onDataError(Exception e) {
//                Toast.makeText(MainActivity.this, "Unable to retrieve parking data.", Toast.LENGTH_LONG).show();
//            }
//        });


        PieChartView pie = new PieChartView(this, null);
//        pie.setPieSegments(
//                new Segment[]{ new Segment(50, 100), new Segment(25, 100), new Segment(50, 50), new Segment(10, 100)}
//        );
        RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.mainlayout);
        myLayout.addView(pie);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
