package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FareCalculatorServiceImpl implements FareCalculatorService {

    public List<DiscountType> discounts;
    public Set<String> recurringUsers;

    private FareCalculatorServiceImpl(Builder builder) {

        this.discounts = new ArrayList<>(builder.getDiscounts());
        this.recurringUsers = new HashSet<>();
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

    @Override
    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    @Override
    public Set<String> getRecurringUsers() {
        return recurringUsers;
    }

    @Override
    public void calculateFare(Ticket ticket, List<DiscountType> discounts, Set<String> recurringUsers) {
        if ((ticket.getOutTime() == -1L) || (ticket.getOutTime() < (ticket.getInTime()))) {

            throw new IllegalArgumentException
                    ("Out time provided is incorrect - " + ticket.getOutTime() + " " + ticket.getInTime());
        }

        long inMinute = ticket.getInTime();
        long outMinute = ticket.getOutTime();
        long duration = (outMinute - inMinute) / 60;
        double price = 0.0;
        double fare = ticket.getParkingSpot().getParkingType().getFare();
        boolean isRecurringUser = recurringUsers.contains(ticket.getVehicleRegNumber());

        for (DiscountType discount : discounts) {
            price = discount.calculatePrice(price, duration, fare, isRecurringUser);

            if (price == 0.0) return;
        }

        if (!isRecurringUser) recurringUsers.add(ticket.getVehicleRegNumber());

        ticket.setPrice(price);
    }
}
