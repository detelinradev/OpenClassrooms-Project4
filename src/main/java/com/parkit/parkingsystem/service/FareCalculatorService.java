package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		long inMinute = ticket.getInTime().getTimeInMillis() / (1000 * 60);
		long outMinute = ticket.getOutTime().getTimeInMillis() / (1000 * 60);

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
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}