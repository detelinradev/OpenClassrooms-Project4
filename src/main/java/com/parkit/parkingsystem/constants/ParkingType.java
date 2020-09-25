package com.parkit.parkingsystem.constants;

public enum ParkingType {
    CAR(1){
        @Override
        public double getFare() {
            return Fare.CAR_RATE_PER_HOUR;
        }
    },
    BIKE(2) {
        @Override
        public double getFare() {
            return Fare.BIKE_RATE_PER_HOUR;
        }
    };

    private final int num;

    ParkingType(int num) {
        this.num = num;
    }

    public abstract double getFare();

    public int getNum(){
        return num;
    }
}
