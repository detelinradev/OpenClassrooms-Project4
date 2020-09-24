package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{

            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
             preparedStatement =  connection.prepareStatement("update parking set available = true");
             preparedStatement.execute();

            //clear ticket entries;
           preparedStatement =  connection.prepareStatement("truncate table ticket");
           preparedStatement.execute();

        }catch(ClassNotFoundException | SQLException e){

            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
            dataBaseTestConfig.closePreparedStatement(preparedStatement);
        }
    }


}
