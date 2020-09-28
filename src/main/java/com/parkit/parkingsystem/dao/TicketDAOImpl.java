package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *     Handles interactions with the database table <code>Ticket</code>.
 * Consists of methods <code>saveTicket</code>, <code>updateTicket</code> and
 * <code>getTicket</code> which creates insert, search and update queries to
 * the database table <code>Ticket</code>.
 * <p>
 *     Holds dependency to <code>DataBaseConfig</code> class through
 * constructor injection, that allows to create <code>Connection</code>
 * instances.
 */
public class TicketDAOImpl implements TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    private final DataBaseConfig dataBaseConfig;

    public TicketDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    /**
     *     Handles creating new <code>Ticket</code> and saving it to
     * database table <code>Ticket</code>. Throws new
     * <code>UnsuccessfulOperationException</code> on any result
     * different then <code>true</code> as update works with predefined
     * conditions and expected behavior is to update successfully. Different
     * than 1 created rows is considered exceptional case, and 1 created row
     * returns <code>true</code>.
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress redundant
     * null check warning when using try with resource block from FindBugs
     * plugin version 3.0.5. To be removed in next release when the bug is
     * fixed.
     *
     * @param ticket  instance of <code>Ticket</code> holds new data
     *                     to be added to the database
     * @return <code>boolean</code> variable indicating result of the process,
     * only <code>true</code> expected as valid response, in all other cases
     * exception is thrown
     */
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    @Override
    public boolean saveTicket(Ticket ticket) {

        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET)) {

            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime() * 1000));
            ps.setTimestamp(5,
                    (ticket.getOutTime() == -1L) ? null : (new Timestamp(ticket.getOutTime())));

            int createdRowCount = ps.executeUpdate();

            if (createdRowCount == 1){

                return true;

            }else {

                throw new UnsuccessfulOperationException("Unable to save ticket information. Response from " +
                        "database is invalid");
            }

        } catch (ClassNotFoundException | SQLException | UnsuccessfulOperationException ex) {

            logger.error("Error saving ticket info", ex);
            throw new UnsuccessfulOperationException("Unable to create ticket", ex);
        }
    }

    /**
     *     Handles retrieving a <code>Ticket</code> from the database.
     * When passed string with registration number that do not mach records in
     * the database returns static Ticket.NOT_FOUND
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress redundant
     * null check warning when using try with resource block from FindBugs
     * plugin version 3.0.5. To be removed in next release when the bug is
     * fixed.
     *
     * @param vehicleRegNumber  vehicle registration number as
     *                          <code>String</code>, null returns
     *                          Ticket.NOT_FOUND
     * @return instance of <code>Ticket</code> with registration
     * number equals this passed as parameter or if not found returns
     * Ticket.NOT_FOUND, never null
     */
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    @Override
    public Ticket getTicket(String vehicleRegNumber) {

        ResultSet rs = null;

        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET)) {

            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();

            if (rs.next()) {

                Ticket ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                        ParkingType.valueOf(rs.getString(6)), false);

                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).getTime() / 1000);
                ticket.setOutTime(rs.getTimestamp(5) == null
                        ? LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                        : rs.getTimestamp(5).getTime() / 1000);

                return ticket;

            } else {

                return Ticket.NOT_FOUND;
            }

        } catch (ClassNotFoundException | SQLException | UnsuccessfulOperationException ex) {

            logger.error("Error getting ticket info", ex);
            throw new UnsuccessfulOperationException("Unable to get ticket", ex);

        } finally {

            dataBaseConfig.closeResultSet(rs);
        }
    }

    /**
     *     Handles updates to database table <code>Ticket</code>.
     * Throws new <code>UnsuccessfulOperationException</code> on any result
     * different then <code>true</code> as update works with predefined
     * conditions and expected behavior is to update successfully. Different
     * than 1 updated rows is considered exceptional case, and 1 updated row
     * returns <code>true</code>.
     * <p>
     *     Method has @SuppressFBWarnings annotation to suppress redundant
     * null check warning when using try with resource block from FindBugs
     * plugin version 3.0.5. To be removed in next release when the bug is
     * fixed.
     *
     * @param ticket  instance of <code>Ticket</code> holds changes
     *                     to be updated to the database
     * @return <code>boolean</code> variable indicating result of the process,
     * only <code>true</code> expected as valid response, in all other cases
     * exception is thrown
     */
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    @Override
    public boolean updateTicket(Ticket ticket) {

        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET)) {

            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime()));
            ps.setInt(3, ticket.getId());

            int updateRowCount = ps.executeUpdate();

            if (updateRowCount == 1){

                return true;

            }else {

                throw new UnsuccessfulOperationException("Unable to update ticket information. Response from " +
                        "database is invalid");
            }

        } catch (ClassNotFoundException | SQLException | UnsuccessfulOperationException ex) {

            logger.error("Error updating ticket info", ex);
            throw new UnsuccessfulOperationException("Unable to update ticket information", ex);
        }
    }
}
