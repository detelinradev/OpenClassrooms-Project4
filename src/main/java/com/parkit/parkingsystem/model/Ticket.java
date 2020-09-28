package com.parkit.parkingsystem.model;

/**
 * The class <code>Ticket</code> holds the information about a vehicle's stay in
 * the parking including in and out time, price for the stay, details about the
 * vehicle and where it is located.
 * <p>
 * It has static variable NOT_FOUND which make use of Null Object
 * Pattern and replace not existing <code>Ticket</code> in the code logic.
 * <p>
 * <code>toString</code> method is overridden to
 * includes all fields and their values for this <code>Ticket</code>.
 */
public class Ticket {

    /**
     * Make use of Null Object Pattern and replace not existing
     * <code>Ticket</code> in the code logic.
     */
    public static final Ticket NOT_FOUND = new Ticket();

    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price = 0.0;
    private long inTime;
    private long outTime = -1L;

    /**
     * Returns <code>id</code> of this <code>Ticket</code>.
     *
     * @return <code>int</code> variable representing id of this
     * <code>Ticket</code>
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the <code>id</code> of this <code>Ticket</code>.
     *
     * @param id <code>int</code> variable representing id of this
     *           <code>Ticket</code>
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the <code>ParkingSpot</code> of this <code>Ticket</code>.
     *
     * @return instance of <code>ParkingSpot</code>
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets the <code>ParkingSpot</code> of this <code>Ticket</code>.
     *
     * @param parkingSpot instance of <code>ParkingSpot</code>
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Returns the <code>vehicleRegNumber</code> of this <code>Ticket</code> as
     * <code>String</code>.
     *
     * @return vehicle number of this <code>Ticket</code>
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets the <code>vehicleRegNumber</code> of this <code>Ticket</code>.
     *
     * @param vehicleRegNumber vehicle number as <code>String</code>
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Returns <code>price</code> field of this <code>Ticket</code>.
     *
     * @return <code>double</code> variable representing the price field
     * of this <code>Ticket</code>
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the <code>price</code> field of this <code>Ticket</code>.
     *
     * @param price <code>double</code> variable representing the price field
     *             of this <code>Ticket</code>
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns <code>inTime</code> field of this <code>Ticket</code>.
     *
     * @return <code>long</code> variable representing the inTime field
     * of this <code>Ticket</code>
     */
    public long getInTime() {
        return inTime;
    }

    /**
     * Sets the <code>inTime</code> field of this <code>Ticket</code>.
     *
     * @param inTime <code>long</code> variable representing the inTime field
     *             of this <code>Ticket</code>
     */
    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    /**
     * Returns <code>outTime</code> field of this <code>Ticket</code>.
     *
     * @return <code>long</code> variable representing the outTime field
     * of this <code>Ticket</code>
     */
    public long getOutTime() {
        return outTime;
    }

    /**
     * Sets the <code>outTime</code> field of this <code>Ticket</code>.
     *
     * @param outTime <code>long</code> variable representing the outTime field
     *             of this <code>Ticket</code>
     */
    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    /**
     * Returns a string representation of the object.
     * Overrides <code>toString</code> method from <code>Object</code> with
     * concatenated names and values as string of all fields of this
     * <code>Ticket</code> .
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", parkingSpot=" + parkingSpot +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", price=" + price +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                '}';
    }
}
