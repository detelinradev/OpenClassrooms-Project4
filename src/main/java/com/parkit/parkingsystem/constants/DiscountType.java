package com.parkit.parkingsystem.constants;

/**
 * Represents different discount schemes applied when calculating fare for
 * vehicle that have used the parking.
 * <p>
 * There are primary and supplementary types. Primary types calculates fare
 * based on duration and vehicle rate and secondary only operates on passed
 * price.
 * So the secondary have to be applied only in chain with primary
 * discount type, and primary should not be applied in chain with other
 * primary types.
 * For distinguishing both types - primary starts with P_
 * and supplementary with S_.
 */
public enum DiscountType {
    P_NO_DISCOUNT {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean discountFlag) {

            return durationMinutes * vehicleRatePerHour / 60;
        }
    }, P_FREE_30_MIN {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean discountFlag) {

            if (durationMinutes <= 30) return 0;

            return (durationMinutes - 30) * vehicleRatePerHour / 60;
        }
    }, S_RECURRING_USERS_5PERCENT {
        @Override
        public double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean discountFlag) {

            if (discountFlag) {
                price = price * 0.95;
            }
            return price;
        }
    };

    /**
     * Calculates price for the passed <code>enum</code>
     * <code>DiscountType</code> from passed parameters.
     *
     * @param price  the price for the stay as a <code>double</code> value,
     *              initially 0.0, when supplementary type applied could be
     *              a positive value
     * @param durationMinutes  duration of the stay in minutes as a
     *                       <code>long</code> value, could be 0 if the stay
     *                       is less then a minute
     * @param vehicleRatePerHour  predefined rate for vehicle per hour as
     *                           <code>double</code> value
     * @param discountFlag  <code>boolean</code> variable indicating
     *                         if user is recurring
     * @return the price as <code>double</code> value, could be 0
     */
    public abstract double calculatePrice(double price, long durationMinutes, double vehicleRatePerHour, boolean discountFlag);
}
