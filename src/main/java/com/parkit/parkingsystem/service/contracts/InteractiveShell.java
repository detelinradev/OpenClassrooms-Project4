package com.parkit.parkingsystem.service.contracts;

/**
 *     The mediator shell between the user and the application.
 * <p>
 *     Consists of a method loadInterface where the main menu is loaded
 * and user can make choice of several actions.
 * <p>
 *     Holds <code>FareCalculatorService</code> variable what is used to create
 * dependency through constructor injection with price calculating function
 * of the app in order to apply various discounts at runtime.
 * <p>
 *     Instantiate with hard dependencies all parts of the application in
 * order to provide the parking functionality.
 *
 */
public interface InteractiveShell {

    /**
     *     The main menu is loaded in the method through loop and user can make
     * choice of several actions presented as enum types and iterated with
     * stream. If no valid action is chosen specific enum is loaded which prints
     * appropriate message. One of the enum commands brake the loop and exit the
     * application when chosen.
     *
     */
    void loadInterface();

}
