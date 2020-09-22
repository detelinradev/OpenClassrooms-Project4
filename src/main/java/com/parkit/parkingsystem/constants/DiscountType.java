package com.parkit.parkingsystem.constants;

public enum DiscountType {
    NO_DISCOUNT {
        @Override
        public double calculatePrice(long durationMinutes, double vehicleRatePerHour) {

            return durationMinutes * vehicleRatePerHour / 60;
        }
    }, FREE_30_MIN {
        @Override
        public double calculatePrice(long durationMinutes, double vehicleRatePerHour) {

            if (durationMinutes <= 30) return 0;

            return (durationMinutes - 30) * vehicleRatePerHour / 60;
        }
    }, RECURRING_USERS_5PERCENT {
        @Override
        public double calculatePrice(long durationMinutes, double vehicleRatePerHour) {

            return 0;
        }
    };

    public abstract double calculatePrice(long durationMinutes, double vehicleRatePerHour);
}
