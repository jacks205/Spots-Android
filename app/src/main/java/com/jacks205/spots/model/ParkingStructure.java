package com.jacks205.spots.model;

/**
 * Created by Ian on 11/4/2015.
 */
public class ParkingStructure {

    private String name;
    private int available;
    private int total;
    private ParkingLevel[] levels;

    public ParkingStructure(String name, int available, int total, ParkingLevel[] levels) {
        this.name = name;
        this.available = available;
        this.total = total;
        this.levels = levels;
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
}
