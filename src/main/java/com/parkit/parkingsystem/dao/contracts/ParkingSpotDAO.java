package com.parkit.parkingsystem.dao.contracts;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public interface ParkingSpotDAO {

	int getNextAvailableSlot(ParkingType parkingType);

	boolean updateParking(ParkingSpot parkingSpot);

}
