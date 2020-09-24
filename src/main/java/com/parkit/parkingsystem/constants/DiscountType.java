package com.parkit.parkingsystem.constants;

public enum DiscountType {
    NO_DISCOUNT {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean isRecurringUser) {

            return durationMinutes * vehicleRatePerHour / 60;
        }
    }, FREE_30_MIN {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean isRecurringUser) {

            if (durationMinutes <= 30) return 0;

            return (durationMinutes - 30) * vehicleRatePerHour / 60;
        }
    }, RECURRING_USERS_5PERCENT {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean isRecurringUser) {

            if (isRecurringUser) {
                price = price * 0.95;
            }
            return price;
        }
    };

    public abstract double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean isRecurringUser);
}
