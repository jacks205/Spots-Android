package com.jacks205.spots.adapters;

import android.content.Context;
import android.util.Log;
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

    public void setStructures(ParkingStructure[] structures) {
        this.structures = structures;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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

        ParkingLevel[] levels = structures[position].getLevels();
        int totalLevels = levels.length;
        View rowView;
        if(totalLevels > 4){
            rowView = inflater.inflate(R.layout.piechart_list_item_5levels, parent, false);
        }else{
            rowView = inflater.inflate(R.layout.piechart_list_item_2levels, parent, false);
        }

        holder.nameTextView = (TextView)rowView.findViewById(R.id.strutureName);
        String name = structures[position].getName();
        holder.nameTextView.setText(uppercaseEachWord(name));

        holder.totalSpotsTextView = (TextView) rowView.findViewById(R.id.totalSpotsTextView);
        String totalSpotsStr = structures[position].getAvailable() + " spots available";
        holder.totalSpotsTextView.setText(totalSpotsStr);

        holder.level1Tv = (TextView)rowView.findViewById(R.id.level1spots);
        int numSpots = levels[0].getAvailable();
        holder.level1Tv.setText(numSpots + " spots");
        holder.level1Label = (TextView)rowView.findViewById(R.id.level1Label);
        String labelName = levels[0].getName();
        holder.level1Label.setText("LEVEL " + labelName);


        holder.level2Tv = (TextView)rowView.findViewById(R.id.level2spots);
        numSpots = levels[1].getAvailable();
        holder.level2Tv.setText(numSpots + " spots");
        holder.level2Label = (TextView)rowView.findViewById(R.id.level2Label);
        labelName = levels[1].getName();
        holder.level2Label.setText("LEVEL " + labelName);


        if(totalLevels > 4){

            holder.level3Tv = (TextView)rowView.findViewById(R.id.level3spots);
            numSpots = levels[2].getAvailable();
            holder.level3Tv.setText(numSpots + " spots");
            holder.level3Label = (TextView)rowView.findViewById(R.id.level3Label);
            labelName = levels[2].getName();
            holder.level3Label.setText("LEVEL " + labelName);


            holder.level4Tv = (TextView)rowView.findViewById(R.id.level4spots);
            numSpots = levels[3].getAvailable();
            holder.level4Tv.setText(numSpots + " spots");
            holder.level4Label = (TextView)rowView.findViewById(R.id.level4Label);
            labelName = levels[3].getName();
            holder.level4Label.setText("LEVEL " + labelName);


            holder.level5Tv = (TextView)rowView.findViewById(R.id.level5spots);
            numSpots = levels[4].getAvailable();
            holder.level5Tv.setText(numSpots + " spots");
            holder.level5Label = (TextView)rowView.findViewById(R.id.level5Label);
            labelName = levels[4].getName();
            holder.level5Label.setText("LEVEL " + labelName);


        }

        holder.availablePercent = (TextView)rowView.findViewById(R.id.availablePercent);
        double percent = (double)structures[position].getAvailable() / structures[position].getTotal() * 100;
        percent = 100 - percent;
        NumberFormat formatter = new DecimalFormat("###");
        holder.availablePercent.setText(formatter.format(percent) + "%");

        holder.pieChartView = (PieChartView)rowView.findViewById(R.id.pieChartView);
        holder.pieChartView.setLevelSegments(levels);

        return rowView;
    }

    @Override
    public int getCount() {
        return structures.length;
    }

    class Holder {
        TextView nameTextView;
        TextView totalSpotsTextView;
        PieChartView pieChartView;

        TextView level1Label,
                level2Label,
                level3Label,
                level4Label,
                level5Label;

        TextView level1Tv,
            level2Tv,
            level3Tv,
            level4Tv,
            level5Tv;

        TextView availablePercent;
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
