package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.model.ParkingSpot;

/**
 *     Service of the application, where all stages to successfully park a
 * vehicle are performed with interaction between user input, business
 * related logic and database operations.
 * <p>
 *     Consists of a methods <class>processIncomingVehicle</class>,
 * <class>processExitingVehicle</class> and <class>getNextParkingNumberIfAvailable</class>
 * where vehicle is processed as incoming or outgoing and supportive messages
 * are printed to the console.
 * <p>
 *     Holds <class>FareCalculatorService</class>, <class>InputReaderUtil</class>,
 * <class>ParkingSpotDAO</class>, <class>TicketDAO</class> and <class>TimeUtil</class>
 * variables which are used to create dependencies through constructor injection
 * with different parts of the app in order to apply various operations.
 *
 */
public interface ParkingService {

     /**
      *     Handles incoming vehicle through acquiring valid parking spot from
      * the database and issuing a ticket for parking the vehicle with user
      * provided details. Ticket is then saved in the database and result is
      * printed on the console.
      * <p>
      *     All input and database related exceptions are handled internally and
      * caught in global try catch block with appropriate messages printed to
      * the console.
      */
     void processIncomingVehicle();

     /**
      *     Acquires a valid parking spot for incoming vehicle, through passing
      * the type of the vehicle to the database and wait for positive response
      * if spot is available. In that case <class>ParkingSpot</class> is created
      * with the acquired number of the free parking spot, vehicle type and
      * boolean parameter indicating availability (initially true) and returned
      * to the invoking method. In case response is negative returns flag static
      * instance NOT_AVAILABLE.
      * <p>
      *  This is helper method with no need for public access. It is public for
      *  the sole reason of implementing @Spy function when testing as the
      *  project is focused on testing an application.
      *
      * @return <class>ParkingSpot</class> instance, when no available places
      * exist, returns flag static instance NOT_AVAILABLE, never null
      */
     ParkingSpot getNextParkingNumberIfAvailable();

     /**
      *     Handles exiting vehicle through acquiring a ticket from the database
      * and updating its out-time and price and updating the occupied parking
      * slot availability to true. Ticket and parking spot database tables are
      * then updated with the result, and it is is printed on the console.
      * <p>
      *     All input and database related exceptions are handled internally and
      * caught in global try catch block with appropriate messages printed to
      * the console.
      */
     void processExitingVehicle();
}
