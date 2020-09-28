package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * The class <code>ParkingSpot</code> represents data about a spot in the
 * parking acquired from database table <code>Parking</code> bearing information
 * about vehicle type it can hold, is it available and which number the spot is
 * in the database.
 * <p>
 *     It has static variable NOT_AVAILABLE which make use of Null Object
 * Pattern and replace not existing <code>ParkingSpot</code> in the code logic.
 * <p>
 *     <code>equals</code> and <code>hashCode</code> methods are overridden to
 * compare and calculate only <code>number</code> field of this
 * <code>ParkingSpot</code>
 */
public class ParkingSpot {

    /**
     * Make use of Null Object Pattern and replace not existing
     * <code>ParkingSpot</code> in the code logic.
     */
    public final static ParkingSpot NOT_AVAILABLE = new ParkingSpot(-1,ParkingType.CAR,false);

    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * Construct new <code>ParkingSpot</code> from passed parameters.
     *
     * @param number  <code>int</code> variable representing number of the spot
     *                in the database table <code>Parking</code>
     * @param parkingType the type of the vehicle as <code>enum</code>
     *                   <code>ParkingType</code>
     * @param isAvailable <code>boolean</code> variable representing
     *                    availability of the spot
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Returns <code>number</code> of this <code>ParkingSpot</code>
     *
     * @return <code>int</code> variable representing number of the spot
     *         in the database table <code>Parking</code>
     */
    public int getId() {
        return number;
    }

    private void setId(int number) {
        this.number = number;
    }

    /**
     * Returns the <code>ParkingType</code> of this <code>ParkingSpot</code>
     *
     * @return the type of the vehicle as <code>enum</code>
     *         <code>ParkingType</code>
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    private void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Returns the availability status of this <code>ParkingSpot</code>
     *
     * @return <code>boolean</code> variable representing
     *         availability of the spot
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of this <code>ParkingSpot</code>
     *
     * @param available <code>boolean</code> variable representing
     *         availability of the spot
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Overrides <code>equals</code> method from <code>Object</code> with
     * comparing only field <code>number</code> of this <code>ParkingSpot</code>
     *
     * @param o  the reference object with which to compare
     * @return <code>true</code> if this object is the same as the obj
     *         argument, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * Returns a hash code value for the object.
     * Overrides <code>hashCode</code> method from <code>Object</code> with
     * returning field <code>number</code> of this <code>ParkingSpot</code>
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return number;
    }
}
