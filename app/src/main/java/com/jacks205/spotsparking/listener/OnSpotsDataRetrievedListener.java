package com.jacks205.spotsparking.listener;

import com.jacks205.spotsparking.model.ParkingStructure;

/**
 * Created by Ian on 11/4/2015.
 */
public interface OnSpotsDataRetrievedListener {
    void onDataReceived(ParkingStructure[] structures);
    void onDataError(Exception e);
}
