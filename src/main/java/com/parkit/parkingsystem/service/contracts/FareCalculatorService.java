package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public interface FareCalculatorService {

    void calculateFare(Ticket ticket);
}