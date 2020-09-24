package com.parkit.parkingsystem.constants;

import com.parkit.parkingsystem.service.contracts.ParkingService;

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

    ParkingCommand(int command) {
        this.command = command;
    }

    public int getCommand() {
        return command;
    }

    public abstract boolean execute(ParkingService parkingService);
}
