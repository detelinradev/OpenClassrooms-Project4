package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

public class FareCalculatorServiceImpl implements FareCalculatorService {

    private final DiscountType discountType;

    private FareCalculatorServiceImpl(Builder builder) {
        this.discountType = builder.discountType;
    }

    public static class Builder {

        private DiscountType discountType;

        public Builder(DiscountType discountType){
            this.discountType = discountType;
        }

        public Builder withDiscountType(DiscountType discountType) {
            this.discountType = discountType;
            return this;
        }

        public FareCalculatorServiceImpl build() {
            return new FareCalculatorServiceImpl(this);
        }
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == -1L) || (ticket.getOutTime() < (ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect - " + ticket.getOutTime() + " " + ticket.getInTime());
        }

        long inMinute = ticket.getInTime();
        long outMinute = ticket.getOutTime();
        long duration = (outMinute - inMinute) / 60;

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
