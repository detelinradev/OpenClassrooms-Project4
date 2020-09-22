package com.parkit.parkingsystem.constants;

public enum DiscountType {
    NO_DISCOUNT{
        @Override
        public double calculatePrice(Double price, long duration, double vehicleRatePerHour) {

            return price;
        }
    }, FREE_30_MIN {
        @Override
        public double calculatePrice(Double price, long duration, double vehicleRatePerHour) {

            return price;
        }
    }, RECURRING_USERS_5PERCENT {
        @Override
        public double calculatePrice(Double price, long duration, double vehicleRatePerHour) {

            return price;
        }
    };

    public abstract double calculatePrice(Double price, long duration, double vehicleRatePerHour);
}
