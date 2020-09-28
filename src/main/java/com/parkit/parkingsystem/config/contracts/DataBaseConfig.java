package com.parkit.parkingsystem.config.contracts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates <code>Connection</code> object for acquiring database connection
 * and implements helping method <code>closeResultSet</code>.
 * Consists of methods <code>getConnection</code> and <code>closeResultSet</code>.
 */
public interface DataBaseConfig {

    /**
     * Creates and returns <code>Connection</code> object.
     * Allocates JDBC driver and creates connections based on in-place URL,
     * username and password, hardcoded for the purpose of project.
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress hard coded
     * password warning. Vault is to be used in real-world case scenario.
     *
     * @return instance of <code>Connection</code> with connection to
     * the passed URL
     * @throws ClassNotFoundException if the fully qualified name of the
     * desired JDBC driver class passed does not locate a class
     * @throws SQLException if a database access error occurs or the url is
     * <code>null</code>
     */
    Connection getConnection() throws ClassNotFoundException, SQLException;

    /**
     * Closes <code>ResultSet</code> if it is not <code>null</code>.
     *
     * @param rs  <code>ResultSet</code> to be closed, if <code>null</code>
     *            method does nothing
     */
    void closeResultSet(ResultSet rs);
}
