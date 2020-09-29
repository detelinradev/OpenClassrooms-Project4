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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ParkingServiceImpl implements ParkingService {

    private static final Logger logger = LoggerFactory.getLogger("ParkingService");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm d.MMMM.yyyy");

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;
    private final TimeUtil timeUtil;
    private final FareCalculatorService fareCalculatorService;

    /**
     *     Stores <code>FareCalculatorService</code>, <code>InputReaderUtil</code>,
     * <code>ParkingSpotDAO</code>, <code>TicketDAO</code> and
     * <code>TimeUtil</code> variables passed as parameters and creates instance of
     * <code>ParkingServiceImpl</code>
     *
     * @param fareCalculatorService dependency variable presenting  price
     *                             calculating function of the app
     * @param inputReaderUtil       dependency variable presenting  input
     *                             reading function of the app
     * @param parkingSpotDAO        dependency variable presenting  parking
     *                             spot handling function of the app
     * @param ticketDAO             dependency variable presenting  ticket
     *                             handling function of the app
     * @param timeUtil              dependency variable presenting  time
     *                             handling function of the app
     */
    public ParkingServiceImpl(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO, TimeUtil timeUtil,
                              FareCalculatorService fareCalculatorService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.timeUtil = timeUtil;
        this.fareCalculatorService = fareCalculatorService;
    }

    @Override
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != ParkingSpot.NOT_AVAILABLE) {
                parkingSpot.setAvailable(false);

                parkingSpotDAO.updateParking(parkingSpot);

                String vehicleRegNumber = getVehicleRegNumber();
                long inTime = timeUtil.getTimeInSeconds();

                Ticket ticket = new Ticket();

                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setInTime(inTime);

                ticketDAO.saveTicket(ticket);

                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out
                        .println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is: "
                                + LocalDateTime.ofEpochSecond(inTime, 0, ZoneOffset.UTC).format(dateTimeFormatter));
            } else {

                System.out.println("Error fetching parking number from DB. Parking slots might be full");
            }

        } catch (RuntimeException e) {

            logger.error("Unable to process incoming vehicle", e);
        }
    }

    private String getVehicleRegNumber() {

        System.out.println("Please type the vehicle registration number and press enter key");

        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    @Override
    public ParkingSpot getNextParkingNumberIfAvailable() {

        int parkingNumber;
        ParkingSpot parkingSpot;

        ParkingType parkingType = getVehicleType();
        parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

        if (parkingNumber > 0) {

            parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);

        } else {

            parkingSpot = ParkingSpot.NOT_AVAILABLE;
        }

        return parkingSpot;
    }

    private ParkingType getVehicleType() {

        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");

        int input = inputReaderUtil.readSelection();

        return Arrays.stream(ParkingType.values())
                .filter(parkingType -> parkingType.getType() == input)
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("Incorrect input provided");
                    throw new IllegalArgumentException("Entered input is invalid");
                });
    }

    @Override
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();

            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

            if(!ticket.equals(Ticket.NOT_FOUND)) {

                ticket.setOutTime(timeUtil.getTimeInSeconds());

                fareCalculatorService.calculateFare(ticket, fareCalculatorService.getDiscounts(),
                        fareCalculatorService.getRecurringUsers());

                ticketDAO.updateTicket(ticket);

                ParkingSpot parkingSpot = ticket.getParkingSpot();

                parkingSpot.setAvailable(true);

                parkingSpotDAO.updateParking(parkingSpot);

                DecimalFormat df = new DecimalFormat("#.##");

                System.out.println("Please pay the parking fare: " + df.format(ticket.getPrice()) + "€");
                System.out.println("Recorded out-time for vehicle number: " + ticket.getVehicleRegNumber() + " is: "
                        + LocalDateTime.ofEpochSecond(ticket.getOutTime(), 0, ZoneOffset.UTC)
                        .format(dateTimeFormatter));
            }else{
                System.out.println("Error fetching ticket from DB. Registration number is not registered.");
            }

        } catch (RuntimeException e) {

            logger.error("Unable to process exiting vehicle", e);
        }
    }
}
