package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.model.ParkingSpot;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpotDAOImpl implements ParkingSpotDAO {

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public final DataBaseConfig dataBaseConfig;

    public ParkingSpotDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    public int getNextAvailableSlot(ParkingType parkingType) {

        ResultSet rs = null;

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)) {

            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(1);

            }else{

                return -1;
            }

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error fetching next available slot", ex);
            throw new UnsuccessfulOperationException("Unable to fetch next available slot",ex);

        } finally {

            dataBaseConfig.closeResultSet(rs);
        }
    }

    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    public boolean updateParking(ParkingSpot parkingSpot) {

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)) {

            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());

            int updateRowCount = ps.executeUpdate();

            if (updateRowCount == 1){

                return true;

            }else {

                throw new UnsuccessfulOperationException("Unable to update parking spot due to wrong count of " +
                        "rows for SQL Data Manipulation Language statements");
            }

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error updating parking info", ex);
            throw new UnsuccessfulOperationException("Unable to update parking spot",ex);
        }
    }
}
