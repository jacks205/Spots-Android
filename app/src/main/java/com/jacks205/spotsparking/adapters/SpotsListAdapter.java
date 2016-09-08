package com.jacks205.spotsparking.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jacks205.spotsparking.R;
import com.jacks205.spotsparking.model.ParkingLevel;
import com.jacks205.spotsparking.model.ParkingStructure;
import com.jacks205.spotsparking.views.PieChartView;


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
    Typeface openSansReg, openSansSemiBold, openSansLight;

    private static LayoutInflater inflater;

    public SpotsListAdapter(Context context, ParkingStructure[] structures) {
        this.structures = structures;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        openSansReg = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        openSansLight = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");

    }

    public void setStructures(ParkingStructure[] structures) {
        this.structures = structures;
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
        ParkingStructure structure = structures[position];
        ParkingLevel[] levels = structure.getLevels();
        int totalLevels = levels.length;
        View rowView;
        if(totalLevels > 4){
            rowView = inflater.inflate(R.layout.piechart_list_item_5levels, parent, false);
        }else if (totalLevels > 1){
            rowView = inflater.inflate(R.layout.piechart_list_item_2levels, parent, false);
        }else {
            rowView = inflater.inflate(R.layout.piechart_list_item_1level, parent, false);
        }

        holder.nameTextView = (TextView)rowView.findViewById(R.id.strutureName);
        String name = structure.getName();
        holder.nameTextView.setText(uppercaseEachWord(name));
        holder.nameTextView.setTypeface(openSansReg);

        holder.totalSpotsTextView = (TextView) rowView.findViewById(R.id.totalSpotsTextView);
        String totalSpotsStr = structure.getAvailable() + " spots available";
        holder.totalSpotsTextView.setText(totalSpotsStr);
        holder.totalSpotsTextView.setTypeface(openSansReg);

        if(structure.getLastUpdated() != null ) {
            holder.lastUpdatedTv = (TextView)rowView.findViewById(R.id.lastUpdatedTextView);
            String lastUpdated = structure.getLastUpdatedString();
            holder.lastUpdatedTv.setText("Updated " + lastUpdated);
            holder.lastUpdatedTv.setTypeface(openSansReg);
        }

        if(totalLevels > 1) {
            holder.level1Tv = (TextView)rowView.findViewById(R.id.level1spots);
            int numSpots = levels[0].getAvailable();
            holder.level1Tv.setText(numSpots + " spots");
            holder.level1Tv.setTypeface(openSansReg);
            holder.level1Label = (TextView)rowView.findViewById(R.id.level1Label);
            String labelName = levels[0].getName();
            holder.level1Label.setText(labelName);
            holder.level1Label.setTypeface(openSansReg);


            holder.level2Tv = (TextView)rowView.findViewById(R.id.level2spots);
            numSpots = levels[1].getAvailable();
            holder.level2Tv.setText(numSpots + " spots");
            holder.level2Tv.setTypeface(openSansReg);
            holder.level2Label = (TextView)rowView.findViewById(R.id.level2Label);
            labelName = levels[1].getName();
            holder.level2Label.setText(labelName);
            holder.level2Label.setTypeface(openSansReg);

            if(totalLevels > 4){

                holder.level3Tv = (TextView)rowView.findViewById(R.id.level3spots);
                numSpots = levels[2].getAvailable();
                holder.level3Tv.setText(numSpots + " spots");
                holder.level3Tv.setTypeface(openSansReg);
                holder.level3Label = (TextView)rowView.findViewById(R.id.level3Label);
                labelName = levels[2].getName();
                holder.level3Label.setText(labelName);
                holder.level3Label.setTypeface(openSansReg);

                holder.level4Tv = (TextView)rowView.findViewById(R.id.level4spots);
                numSpots = levels[3].getAvailable();
                holder.level4Tv.setText(numSpots + " spots");
                holder.level4Tv.setTypeface(openSansReg);
                holder.level4Label = (TextView)rowView.findViewById(R.id.level4Label);
                labelName = levels[3].getName();
                holder.level4Label.setText(labelName);
                holder.level4Label.setTypeface(openSansReg);

                holder.level5Tv = (TextView)rowView.findViewById(R.id.level5spots);
                numSpots = levels[4].getAvailable();
                holder.level5Tv.setText(numSpots + " spots");
                holder.level5Tv.setTypeface(openSansReg);
                holder.level5Label = (TextView)rowView.findViewById(R.id.level5Label);
                labelName = levels[4].getName();
                holder.level5Label.setText(labelName);
                holder.level5Label.setTypeface(openSansReg);

            }
        }

        holder.availablePercent = (TextView)rowView.findViewById(R.id.availablePercent);
        double percent = (double)structure.getAvailable() / structure.getTotal() * 100;
        percent = 100 - percent;
        NumberFormat formatter = new DecimalFormat("###");
        holder.availablePercent.setText(formatter.format(percent) + "%");
        holder.availablePercent.setTypeface(openSansLight);

        holder.fullView = (TextView)rowView.findViewById(R.id.fullView);
        holder.fullView.setTypeface(openSansSemiBold);

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
        TextView fullView;

        TextView lastUpdatedTv;
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
