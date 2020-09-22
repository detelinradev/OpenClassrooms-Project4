package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpotDAOImpl implements ParkingSpotDAO {

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig;

    public ParkingSpotDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public int getNextAvailableSlot(ParkingType parkingType) {

        int result = -1;
        ResultSet rs = null;

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)) {

            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();

            if (rs.next()) {

                result = rs.getInt(1);
            }

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error fetching next available slot", ex);
            throw new IllegalArgumentException("Unable to fetch next available slot");

        } finally {

            dataBaseConfig.closeResultSet(rs);
        }

        return result;
    }

    public boolean updateParking(ParkingSpot parkingSpot) {

        // update the availability for that parking slot
        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)) {

            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());

            int updateRowCount = ps.executeUpdate();

            return (updateRowCount == 1);

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error updating parking info", ex);
            throw new IllegalArgumentException("Unable to update parking spot");
        }
    }
}