package com.parkit.parkingsystem.constants;

import com.parkit.parkingsystem.service.contracts.ParkingService;

/**
 * Represents different parking commands available when user enters the
 * application.
 * Consists of method <code>execute</code>, which when called from
 * <code>ParkingCommand</code> enum process the application function
 * chosen from the user, and <code>getCommand</code> which returns the
 * <code>int</code> value associated with given <code>enum</code>
 * <code>ParkingCommand</code>.
 * <p>
 *     Each <code>enum</code> is associated with <code>int</code> number
 * through constructor injection which allows to each <code>ParkingCommand</code>
 * to be represented by <code>int</code> value delivered by get method.
 * <p>
 *     Except for the operational commands there is
 * <code>ParkingCommand.NOT_FOUND</code> that only prints on console and
 * returns with no further action.
 */
public enum ParkingCommand {

    ENTER_PARKING(1) {
        @Override
        public boolean execute(ParkingService parkingService) {

            parkingService.processIncomingVehicle();

            return true;
        }
    }, EXIT_PARKING(2) {
        @Override
        public boolean execute(ParkingService parkingService) {

            parkingService.processExitingVehicle();

            return true;
        }
    }, EXIT_APP(3) {
        @Override
        public boolean execute(ParkingService parkingService) {

            System.out.println("Exiting from the system!");

            return false;
        }
    }, NOT_FOUND(0) {
        @Override
        public boolean execute(ParkingService parkingService) {

            System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");

            return true;
        }
    };

    private final int command;

    /**
     * Stores <code>int</code> variable passed as parameter and creates
     * instance of <code>ParkingCommand</code>.
     *
     * @param command <code>int</code> variable representing <code>enum</code>
     *                as number
     */
    ParkingCommand(int command) {
        this.command = command;
    }

    /**
     * Retrieves <code>int</code> value associated with given <code>enum</code>
     * <code>ParkingCommand</code>
     *
     * @return <code>int</code> variable representing <code>enum</code>
     *               as number
     */
    public int getCommand() {
        return command;
    }

    /**
     * Process the application function represented by <code>enum</code>
     * <code>ParkingCommand</code> chosen from the user.
     *
     * @param parkingService  instance of <code>ParkingService</code>
     *                        allowing methods of <code>ParkingService</code>
     *                        class to be executed in this method
     * @return <code>boolean</code> variable that indicates if the
     * <code>enum</code> <code>ParkingService.EXIT_APP</code> was chosen
     */
    public abstract boolean execute(ParkingService parkingService);
}
