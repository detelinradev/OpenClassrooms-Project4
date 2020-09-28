package com.parkit.parkingsystem.model;


public class Ticket {

	public static final Ticket NOT_FOUND = new Ticket();

	private int id;
	private ParkingSpot parkingSpot;
	private String vehicleRegNumber;
	private double price = 0.0;
	private long inTime;
	private long outTime = -1L;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;

	}

	public long getInTime() {
		return inTime;
	}

	public void setInTime(long inTime) {
		this.inTime = inTime;
	}

	public long getOutTime() {
		return outTime;
	}

	public void setOutTime(long outTime) {
		this.outTime = outTime;
	}

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
