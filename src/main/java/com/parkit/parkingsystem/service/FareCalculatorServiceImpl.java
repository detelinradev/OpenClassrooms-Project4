package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

import java.util.ArrayList;
import java.util.List;

public class FareCalculatorServiceImpl implements FareCalculatorService {

    public List<DiscountType> discounts;

    private FareCalculatorServiceImpl(Builder builder) {

        this.discounts = new ArrayList<>(builder.getDiscounts()) ;
    }

    public static class Builder {

        private final List<DiscountType> discountsList = new ArrayList<>();

        public Builder(DiscountType discountType) {

            discountsList.add(discountType);
        }

        public Builder withDiscountType(DiscountType discountType) {

            discountsList.add(discountType);

            return this;
        }

        public FareCalculatorServiceImpl build() {

            return new FareCalculatorServiceImpl(this);
        }

        public List<DiscountType> getDiscounts() {
            return discountsList;
        }
    }

    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    public void calculateFare(Ticket ticket, List<DiscountType> discounts) {
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
            price = discount.calculatePrice(price, duration, fare);

            if (price == 0.0) return;
        }

        ticket.setPrice(price);
    }
}
