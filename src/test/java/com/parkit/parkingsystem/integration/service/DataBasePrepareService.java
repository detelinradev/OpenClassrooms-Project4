package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBasePrepareService {

    private static final Logger logger = LogManager.getLogger("DataBasePrepareService");

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries() {

        try (Connection connection = dataBaseTestConfig.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement("update parking set available = true");
             PreparedStatement  preparedStatement2 = connection.prepareStatement("truncate table ticket"); ) {

            //set parking entries to available
            preparedStatement1.execute();

            //clear ticket entries;
            preparedStatement2.execute();

        } catch (ClassNotFoundException | SQLException e) {

            logger.error("Error while closing resource", e);
        }

    }


}
