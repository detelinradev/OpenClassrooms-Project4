package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.model.ParkingSpot;

public interface ParkingService {

     void processIncomingVehicle();

     ParkingSpot getNextParkingNumberIfAvailable();

     void processExitingVehicle();
}
