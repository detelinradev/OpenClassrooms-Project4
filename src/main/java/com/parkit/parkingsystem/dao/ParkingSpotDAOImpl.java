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

/**
 *     Handles interactions with the database table <class>Parking</class>.
 * Consists of methods <class>getNextAvailableSlot</class> and
 * <class>updateParking</class> which creates search and update queries to
 * the database table <class>Parking</class>.
 * <p>
 *     Holds dependency to <class>DataBaseConfig</class> class through
 * constructor injection, that allows to create <class>Connection</class>
 * instances.
 */
public class ParkingSpotDAOImpl implements ParkingSpotDAO {

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public final DataBaseConfig dataBaseConfig;

    /**
     *     Stores <class>DataBaseConfig</class> variable passed as parameter and
     * creates instance of <class>ParkingSpotDAOImpl</class>.
     *
     * @param dataBaseConfig  instance of <class>DataBaseConfig</class>
     */
    public ParkingSpotDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    /**
     *     Checks availability in the database table <class>Parking</class> for
     * vehicle of the passed <class>ParkingType</class>.
     * Returns -1 in case there is no available place of the specified type.
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress redundant
     * null check warning when using try with resource block from FindBugs
     * plugin version 3.0.5. To be removed in next release when the bug is
     * fixed.
     *
     * @param parkingType  instance of <class>ParkingType</class> enum, not null
     * @return <class>int</class> variable representing number of the available
     * parking slot, -1 if there is no available slots
     */
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

    /**
     *     Handles updates to database table <class>Parking</class>.
     * Throws new <class>UnsuccessfulOperationException</class> on any result
     * different then <class>true</class> as update works with predefined
     * conditions and expected behavior is to update successfully. Zero updated
     * rows is considered exceptional case.
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress redundant
     * null check warning when using try with resource block from FindBugs
     * plugin version 3.0.5. To be removed in next release when the bug is
     * fixed.
     *
     * @param parkingSpot  instance of <class>ParkingSpot</class> holds changes
     *                     to be updated to the database
     * @return <class>boolean</class> variable indicating result of the process,
     * only <class>true</class> expected as valid response, in all other cases
     * exception is thrown
     */
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
