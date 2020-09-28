package com.parkit.parkingsystem.constants;

/**
 * Represents different vehicle types available.
 * Consists of method <code>getFare</code>, which when called from
 * <code>ParkingType</code> enum returns the <code>Fare</code> value
 * associated with this vehicle type, and <code>getType</code> which returns
 * the <code>int</code> value associated with given <code>enum</code>
 * <code>ParkingType</code>.
 * <p>
 *     Each <code>enum</code> is associated with <code>int</code> number
 * through constructor injection which allows to each <code>ParkingType</code>
 * to be represented by <code>int</code> value delivered by get method.
 */
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

    /**
     * Stores <code>int</code> variable passed as parameter and creates
     * instance of <code>ParkingType</code>.
     *
     * @param num <code>int</code> variable representing <code>enum</code>
     *                as number
     */
    ParkingType(int num) {
        this.num = num;
    }

    /**
     * Returns the <code>Fare</code> associated with this vehicle type
     *
     * @return fare for this vehicle type as <code>double</code> value
     */
    public abstract double getFare();

    /**
     * Retrieves <code>int</code> value associated with given <code>enum</code>
     * <code>ParkingType</code>
     *
     * @return <code>int</code> variable representing <code>enum</code>
     *               as number
     */
    public int getType(){
        return num;
    }
}
