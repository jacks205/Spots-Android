package com.jacks205.spots.listener;

import com.jacks205.spots.model.ParkingStructure;

import java.util.Date;

/**
 * Created by Ian on 11/4/2015.
 */
public interface OnSpotsDataRetrievedListener {
    void onDataReceived(ParkingStructure[] structures);
    void onDataError(Exception e);
}
