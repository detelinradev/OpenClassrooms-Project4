package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;
import com.parkit.parkingsystem.service.contracts.ParkingService;
import com.parkit.parkingsystem.util.contracts.InputReaderUtil;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ParkingServiceImpl implements ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm d.MMMM.yyyy");

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;
    private final TimeUtil timeUtil;
    private final FareCalculatorService fareCalculatorService;

    public ParkingServiceImpl(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO, TimeUtil timeUtil,
                          FareCalculatorService fareCalculatorService ) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.timeUtil = timeUtil;
        this.fareCalculatorService = fareCalculatorService;
    }

    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != null && parkingSpot.getId() > 0) {

                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);

                // allot this parking space and mark it's availability as false
                parkingSpotDAO.updateParking(parkingSpot);

                long inTime = timeUtil.getTimeInSeconds();

                Ticket ticket = new Ticket();
                // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
//                ticket.setId(1);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setInTime(inTime);

                ticketDAO.saveTicket(ticket);

                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out
                        .println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is: "
                                + LocalDateTime.ofEpochSecond(inTime, 0, ZoneOffset.UTC).format(dateTimeFormatter));
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
        }
    }

    private String getVehicleRegNumber() {

        System.out.println("Please type the vehicle registration number and press enter key");

        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    public ParkingSpot getNextParkingNumberIfAvailable() {

        int parkingNumber;
        ParkingSpot parkingSpot = null;

        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

            if (parkingNumber > 0) {

                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);

            } else {

                throw new IllegalArgumentException("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {

            logger.error("Error parsing user input for type of vehicle", ie);

        } catch (Exception e) {

            logger.error("Error fetching next available parking slot", e);

        }
        return parkingSpot;
    }

    private ParkingType getVehicleType() {

        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");

        int input = inputReaderUtil.readSelection();

        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();

            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            if(ticket != null) {
                ticket.setOutTime(timeUtil.getTimeInSeconds());

                fareCalculatorService.calculateFare(ticket);
                ticketDAO.updateTicket(ticket);

                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);

                if(parkingSpotDAO.updateParking(parkingSpot)) {

                    DecimalFormat df = new DecimalFormat("#.##");

                    System.out.println("Please pay the parking fare: " + df.format(ticket.getPrice()) + "€");
                    System.out.println("Recorded out-time for vehicle number: " + ticket.getVehicleRegNumber() + " is: "
                            + LocalDateTime.ofEpochSecond(ticket.getOutTime(), 0, ZoneOffset.UTC)
                            .format(dateTimeFormatter));

                }else throw new IllegalArgumentException("Error updating parking");

            }else throw new IllegalArgumentException("Error creating ticket");

        } catch (
                Exception e) {
            logger.error("Unable to process exiting vehicle", e);
        }
    }
}
