package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

import java.util.ArrayList;
import java.util.List;

public class FareCalculatorServiceImpl implements FareCalculatorService {

    private static final List<DiscountType> discounts = new ArrayList<>();

    private FareCalculatorServiceImpl(Builder builder) {

        DiscountType discountType = builder.discountType;
    }

    public static class Builder {

        private DiscountType discountType;

        public Builder(DiscountType discountType) {

            this.discountType = discountType;
            discounts.add(discountType);
        }

        public Builder withDiscountType(DiscountType discountType) {

            this.discountType = discountType;
            discounts.add(discountType);

            return this;
        }

        public FareCalculatorServiceImpl build() {

            return new FareCalculatorServiceImpl(this);
        }
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == -1L) || (ticket.getOutTime() < (ticket.getInTime()))) {

            throw new IllegalArgumentException
                    ("Out time provided is incorrect - " + ticket.getOutTime() + " " + ticket.getInTime());
        }

        long inMinute = ticket.getInTime();
        long outMinute = ticket.getOutTime();
        long duration = (outMinute - inMinute) / 60;
        double price = 0.0;
        double fare = ticket.getParkingSpot().getParkingType().getFare();

        for (DiscountType discount : discounts) {

            price = discount.calculatePrice(duration, fare);
        }

        ticket.setPrice(price);

    }
}
