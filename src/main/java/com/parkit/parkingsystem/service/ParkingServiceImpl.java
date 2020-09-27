package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
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
import java.util.Arrays;

/**
 *   The <class>ParkingService</class> class is the service of the application, where all stages to successfully park
 * a vehicle are performed with interaction between user input, business related logic and database operations.
 * <p>
 *  It consists of a methods processIncomingVehicle, processExitingVehicle and getNextParkingNumberIfAvailable where
 * vehicle is processed as incoming or outgoing and supportive messages are printed to the console.
 * <p>
 *  It holds <class>FareCalculatorService</class>, <class>InputReaderUtil</class>, <class>ParkingSpotDAO</class>,
 * <class>TicketDAO</class> and <class>TimeUtil</class> variables which are used to create dependency with different
 * parts of the app in order to apply various operations.
 *
 * @author Detelin Radev
 */
public class ParkingServiceImpl implements ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm d.MMMM.yyyy");

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;
    private final TimeUtil timeUtil;
    private final FareCalculatorService fareCalculatorService;

    /**
     *   This constructor stores <class>FareCalculatorService</class>, <class>InputReaderUtil</class>, <class>ParkingSpotDAO</class>,
     * <class>TicketDAO</class> and <class>TimeUtil</class> variables and creates instance of ParkingServiceImpl
     *
     * @param fareCalculatorService dependency variable presenting  price calculating function of the app
     * @param inputReaderUtil       dependency variable presenting  input reading function of the app
     * @param parkingSpotDAO        dependency variable presenting  parking spot handling function of the app
     * @param ticketDAO             dependency variable presenting  ticket handling function of the app
     * @param timeUtil              dependency variable presenting  time handling function of the app
     */
    public ParkingServiceImpl(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO, TimeUtil timeUtil,
                              FareCalculatorService fareCalculatorService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.timeUtil = timeUtil;
        this.fareCalculatorService = fareCalculatorService;
    }

    /**
     *   Method handles incoming vehicle through acquiring valid parking spot from the database and issuing a ticket for
     * parking the vehicle with user provided details. Ticket is then saved in the database and result is printed on the
     * console.
     * <p>
     *  All input and database related exceptions are handled internally and caught in global try catch block with
     * appropriate messages printed to the console.
     */
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

    /**
     *   Method acquires a valid parking spot for incoming vehicle, through passing the type of the vehicle to the database
     * and wait for positive response if spot is available.
     *  In that case <class>ParkingSpot</class> is created with number of the free parking spot, vehicle type and boolean
     * parameter indicating availability (initially true) and returned to the invoking method.
     *  In case response is negative returns flag static instance NOT_AVAILABLE.
     * <p>
     *  This is helper method with no need for public access. It is public for the sole reason of implementing @Spy
     * function when testing as the project is focused on testing an application.
     *
     * @return <class>ParkingSpot</class> instance, when no available places exist, returns flag static
     * instance NOT_AVAILABLE, never null
     */
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
                .filter(parkingType -> parkingType.getNum() == input)
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("Incorrect input provided");
                    throw new IllegalArgumentException("Entered input is invalid");
                });
    }

    /**
     *   Method handles exiting vehicle through acquiring a ticket from the database and updating its out-time and price
     * and updating the occupied parking slot availability to true. Ticket and parking spot database tables are then
     * updated with the result, and it is is printed on the console.
     * <p>
     *  All input and database related exceptions are handled internally and caught in global try catch block with
     * appropriate messages printed to the console.
     */
    @Override
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();

            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

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

        } catch (RuntimeException e) {

            logger.error("Unable to process exiting vehicle", e);
        }
    }
}
