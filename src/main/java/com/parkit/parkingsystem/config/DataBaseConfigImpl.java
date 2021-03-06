package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DataBaseConfigImpl implements DataBaseConfig {

    private static final Logger logger = LoggerFactory.getLogger("DataBaseConfig");

    @SuppressFBWarnings("DMI_CONSTANT_DB_PASSWORD")
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/prod?serverTimezone=UTC",
                "root", "password");
    }

    @Override
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
