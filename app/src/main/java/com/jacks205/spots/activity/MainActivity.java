package com.jacks205.spots.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jacks205.spots.R;
import com.jacks205.spots.Spots;
import com.jacks205.spots.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spots.model.ParkingStructure;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spots spots = Spots.getInstance();
        spots.getParkingData(new OnSpotsDataRetrievedListener() {
            @Override
            public void onDataReceived(ParkingStructure[] structures, Date lastUpdated) {
                String s = "";
            }

            @Override
            public void onDataError(Exception e) {
                Toast.makeText(MainActivity.this, "Unable to retrieve parking data.", Toast.LENGTH_LONG).show();
            }
        });

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
