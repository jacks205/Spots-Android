package com.jacks205.spotsparking.model;

import android.text.format.DateUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Ian on 11/4/2015.
 */
public class ParkingStructure {

    private String name;
    private int available;
    private int total;
    private ParkingLevel[] levels;
    private Date lastUpdated;

    public ParkingStructure(String name, int available, int total, ParkingLevel[] levels) {
        this.name = name;
        this.available = available;
        this.total = total;
        this.levels = Arrays.copyOfRange(levels, 1, levels.length);
    }

    public ParkingStructure(String name, int available, int total, Date lastUpdated, ParkingLevel[] levels) {
        this.name = name;
        this.available = available;
        this.total = total;
        this.levels = levels;
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedString() {
        if(lastUpdated == null)
            return "";
        return DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ParkingLevel[] getLevels() {
        return levels;
    }

    public void setLevels(ParkingLevel[] levels) {
        this.levels = levels;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
