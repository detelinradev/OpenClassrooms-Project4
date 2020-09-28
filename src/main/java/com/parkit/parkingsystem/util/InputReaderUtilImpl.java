package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
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

            String input = scan.nextLine();

            return Integer.parseInt(input);

        } catch (NoSuchElementException e) {

            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input");

            throw new UnsuccessfulOperationException("Error while reading user input from Shell");

        } catch (NumberFormatException e) {

            logger.error("Error while parsing user input from Shell", e);
            System.out.println("Invalid input provided. Please enter valid number for proceeding further");

            return -1;
        }
    }

    public String readVehicleRegistrationNumber() {

        String vehicleRegNumber;

        try {

            vehicleRegNumber = scan.nextLine();

        } catch (NoSuchElementException e) {

            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input");

            throw new UnsuccessfulOperationException("Error while reading user input from Shell");
        }

        if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {

            System.out.println("Invalid input provided.Please enter a valid string" +
                    " for vehicle registration number");

            throw new IllegalArgumentException("Incorrect input provided");
        }

        return vehicleRegNumber;
    }
}
