package com.jacks205.spots.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jacks205.spots.R;
import com.jacks205.spots.model.ParkingLevel;
import com.jacks205.spots.model.ParkingStructure;
import com.jacks205.spots.views.PieChartView;


import java.util.Date;

/**
 * Created by Ian on 11/15/2015.
 */
public class SpotsListAdapter  extends BaseAdapter{

    Context context;
    ParkingStructure[] structures;
    Date lastUpdated;

    private static LayoutInflater inflater;

    public SpotsListAdapter(Context context, ParkingStructure[] structures, Date lastUpdated) {
        this.structures = structures;
        this.lastUpdated = lastUpdated;
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
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.piechart_list_item, null);

        holder.nameTextView = (TextView)rowView.findViewById(R.id.strutureName);
        String name = structures[position].getName();
        holder.nameTextView.setText(uppercaseEachWord(name));

        holder.pieChartView = (PieChartView)rowView.findViewById(R.id.pieChartView);
        ParkingLevel[] levels = structures[position].getLevels();
        holder.pieChartView.setLevelSegments(levels);

        return rowView;
    }

    @Override
    public int getCount() {
        return structures.length;
    }

    class Holder {
        TextView nameTextView;
        PieChartView pieChartView;
    }


    private static String uppercaseEachWord(String sentence){
        String[] words = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }
}
