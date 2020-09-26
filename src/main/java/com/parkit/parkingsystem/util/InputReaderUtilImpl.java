package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.util.contracts.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputReaderUtilImpl implements InputReaderUtil {

    private static final Scanner scan = new Scanner(System.in, "UTF-8");
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public int readSelection() {

        try {
            return Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber = scan.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided.Please enter a valid string" +
                        " for vehicle registration number");
            }
            return vehicleRegNumber;
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input");
            throw e;
        }
    }
}
