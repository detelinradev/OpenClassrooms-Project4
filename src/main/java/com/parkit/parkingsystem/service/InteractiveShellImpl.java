package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfigImpl;
import com.parkit.parkingsystem.constants.ParkingCommand;
import com.parkit.parkingsystem.dao.TicketDAOImpl;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.ParkingSpotDAOImpl;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.service.contracts.InteractiveShell;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;
import com.parkit.parkingsystem.service.contracts.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtilImpl;
import com.parkit.parkingsystem.util.TimeUtilImpl;
import com.parkit.parkingsystem.util.contracts.InputReaderUtil;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 *   The InteractiveShell class is the mediator shell between the user and the application.
 *  It consists of a method loadInterface where the main menu is loaded and user can make choice of several actions.
 *  It holds FareCalculatorService variable what is used to create dependency with price calculating function of the app
 * in order to apply various discounts at runtime.
 *  It holds hard dependencies to all parts of the application in order to provide the parking functionality.
 *
 *  @author Detelin Radev
 */
public class InteractiveShellImpl implements InteractiveShell {

    private static final Logger logger = LogManager.getLogger("InteractiveShell");

    private InputReaderUtil inputReaderUtil = new InputReaderUtilImpl();
    private ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAOImpl(new DataBaseConfigImpl());
    private TicketDAO ticketDAO = new TicketDAOImpl(new DataBaseConfigImpl());
    private TimeUtil timeUtil = new TimeUtilImpl();
    private ParkingService parkingService;


    /**
     * This constructor stores FareCalculatorService variable and creates instance of InteractiveShellImpl
     *
     * @param fareCalculatorService dependency variable presenting  price calculating function of the app
     */
    public InteractiveShellImpl(FareCalculatorService fareCalculatorService) {

        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil,
                fareCalculatorService);
    }

    /**
     *  The main menu is loaded in the method through loop and user can make choice of several actions presented
     * as enum types and iterated with stream. If no valid action is chosen specific enum is loaded which prints appropriate
     * message. One of the enum commands brake the loop when chosen.
     *
     */
    public void loadInterface() {

        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;

        while (continueApp) {

            loadMenu();

            int option = inputReaderUtil.readSelection();

            continueApp = Arrays.stream(ParkingCommand.values())
                    .filter(parkingCommand -> parkingCommand.getCommand() == option)
                    .findFirst()
                    .orElse(ParkingCommand.NOT_FOUND).execute(parkingService);
        }
    }

    private void loadMenu() {
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }
}
