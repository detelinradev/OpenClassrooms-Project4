package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import com.parkit.parkingsystem.service.contracts.InteractiveShell;
import com.parkit.parkingsystem.service.InteractiveShellImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger("App");
    public static void main(String[] args){
        logger.info("Initializing Parking System");
        InteractiveShell interactiveShell = new InteractiveShellImpl(new FareCalculatorServiceImpl
                .Builder(DiscountType.NO_DISCOUNT)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build());
        interactiveShell.loadInterface();
    }
}
