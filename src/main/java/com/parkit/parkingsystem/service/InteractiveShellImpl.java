package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfigImpl;
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

public class InteractiveShellImpl implements InteractiveShell {

    private static final Logger logger = LogManager.getLogger("InteractiveShell");

    private InputReaderUtil inputReaderUtil = new InputReaderUtilImpl();
    private final ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAOImpl(new DataBaseConfigImpl());
    private final TicketDAO ticketDAO = new TicketDAOImpl(new DataBaseConfigImpl());
    private final TimeUtil timeUtil = new TimeUtilImpl();
    private final FareCalculatorService fareCalculatorService = new FareCalculatorServiceImpl();
    private ParkingService parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil,
            fareCalculatorService);

    public void loadInterface() {

        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;

        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case 1: {
                    parkingService.processIncomingVehicle();
                    break;
                }
                case 2: {
                    parkingService.processExitingVehicle();
                    break;
                }
                case 3: {
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                }
                default:
                    System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }

    private void loadMenu() {
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }
}
