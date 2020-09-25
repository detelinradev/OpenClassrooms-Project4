package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.model.ParkingSpot;

import java.util.Optional;

public interface ParkingService {

     void processIncomingVehicle();

     ParkingSpot getNextParkingNumberIfAvailable();

     void processExitingVehicle();
}
