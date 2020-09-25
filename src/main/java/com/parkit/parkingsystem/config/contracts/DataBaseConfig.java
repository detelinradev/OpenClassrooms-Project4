package com.parkit.parkingsystem.config.contracts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface DataBaseConfig {


    Connection getConnection() throws ClassNotFoundException, SQLException;

    void closeResultSet(ResultSet rs);
}
