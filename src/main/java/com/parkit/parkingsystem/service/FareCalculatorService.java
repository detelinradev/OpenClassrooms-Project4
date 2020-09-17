package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == -1L) || (ticket.getOutTime() < (ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect. Stay duration is negative.");
		}

		long inMinute = ticket.getInTime() / (60);
		long outMinute = ticket.getOutTime()/ (60);

		long duration = outMinute - inMinute;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);
			break;
		}
		case BIKE: {
			ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown Parking Type");
		}
	}
}