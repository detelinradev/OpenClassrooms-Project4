package com.parkit.parkingsystem.constants;

public enum ParkingType {
    CAR{
        @Override
        public double getFare() {
            return Fare.CAR_RATE_PER_HOUR;
        }
    },
    BIKE {
        @Override
        public double getFare() {
            return Fare.BIKE_RATE_PER_HOUR;
        }
    };

    public abstract double getFare();
}
