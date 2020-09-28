package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import com.parkit.parkingsystem.service.contracts.InteractiveShell;
import com.parkit.parkingsystem.service.InteractiveShellImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *  Encapsulates the application's main entry point.
 *
 * @author Detelin Radev
 */
public class App {

    private static final Logger logger = LogManager.getLogger("App");

    /**
     * The application's entry point.
     * <p>
     * Instantiates <code>InteractiveShell</code>, which have
     * <code>FareCalculatorServiceImpl</code> instance as parameter in its
     * constructor. <code>FareCalculatorServiceImpl</code> itself relies
     * on Builder to construct its instance, and the Builder takes as
     * parameter <code>DiscountType</code> <code>enum</code>.
     *
     * It is mandatory that this parameter is primary <code>DiscountType</code>,
     * means it starts with P_ due to the way of calculating parking fare and if
     * there is more attached <code>DiscountType</code> they should be of
     * supplementary type, means start with S_.
     *
     * A chain of discounts should only have one primary discount and it should
     * be passed as parameter to the Builder. All other discounts should be
     * supplementary and added through the Builder's method
     * <code>withDiscountType</code>
     *
     * @param args an array of command-line arguments for the application
     */
    public static void main(String[] args){
        logger.info("Initializing Parking System");
        InteractiveShell interactiveShell = new InteractiveShellImpl(new FareCalculatorServiceImpl
                .Builder(DiscountType.P_NO_DISCOUNT)
                .withDiscountType(DiscountType.S_RECURRING_USERS_5PERCENT)
                .build());
        interactiveShell.loadInterface();
    }
}
