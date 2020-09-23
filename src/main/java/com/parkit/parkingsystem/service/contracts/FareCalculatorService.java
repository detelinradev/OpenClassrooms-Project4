package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.List;

public interface FareCalculatorService {

    void calculateFare(Ticket ticket, List<DiscountType> discounts);

    List<DiscountType> getDiscounts();
}