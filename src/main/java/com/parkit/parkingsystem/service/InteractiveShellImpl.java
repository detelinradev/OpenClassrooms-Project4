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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class InteractiveShellImpl implements InteractiveShell {

    private static final Logger logger = LoggerFactory.getLogger("InteractiveShell");

    private InputReaderUtil inputReaderUtil = new InputReaderUtilImpl();
    private ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAOImpl(new DataBaseConfigImpl());
    private TicketDAO ticketDAO = new TicketDAOImpl(new DataBaseConfigImpl());
    private TimeUtil timeUtil = new TimeUtilImpl();
    private ParkingService parkingService;


    /**
     * Stores <code>FareCalculatorService</code> variable passed as parameter and creates
     * instance of <code>InteractiveShellImpl</code>.
     *
     * @param fareCalculatorService dependency variable presenting  price
     *                             calculating function of the app
     */
    public InteractiveShellImpl(FareCalculatorService fareCalculatorService) {

        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil,
                fareCalculatorService);
    }

    @Override
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
