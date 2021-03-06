package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.model.ParkingSpot;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParkingSpotDAOImpl implements ParkingSpotDAO {

    private static final Logger logger = LoggerFactory.getLogger("ParkingSpotDAO");

    public final DataBaseConfig dataBaseConfig;

    /**
     *     Stores <code>DataBaseConfig</code> variable passed as parameter and
     * creates instance of <code>ParkingSpotDAOImpl</code>.
     *
     * @param dataBaseConfig  instance of <code>DataBaseConfig</code>
     */
    public ParkingSpotDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    @Override
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
    @Override
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
