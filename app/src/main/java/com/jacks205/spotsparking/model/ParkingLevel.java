package com.jacks205.spotsparking.model;

/**
 * Created by Ian on 11/4/2015.
 */
public class ParkingLevel {

    private String name;
    private int available;
    private int total;

    public ParkingLevel(String name, int available, int total) {
        this.name = name;
        this.available = available;
        this.total = total;
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
}
