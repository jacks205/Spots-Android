package com.jacks205.spots.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jacks205.spots.R;
import com.jacks205.spots.model.ParkingLevel;
import com.jacks205.spots.model.ParkingStructure;
import com.jacks205.spots.views.PieChartView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by markjackson on 11/21/15.
 */
public class SchoolListAdapter extends BaseAdapter {

    Context context;
    String[] schools;

    private static LayoutInflater inflater;

    public SchoolListAdapter(Context context, String[] schools) {
        this.schools = schools;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View schoolView;
        schoolView = inflater.inflate(R.layout.choose_school_layout, parent, false);
        
        TextView name = (TextView)schoolView.findViewById(R.id.schoolNameTextView);
        name.setText(schools[position]);

        return schoolView;
    }

    @Override
    public int getCount() {
        return schools.length;
    }

}
