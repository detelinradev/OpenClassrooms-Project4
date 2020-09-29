package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TicketDAOImpl implements TicketDAO {

    private static final Logger logger = LoggerFactory.getLogger("TicketDAO");

    private final DataBaseConfig dataBaseConfig;

    /**
     *     Stores <code>DataBaseConfig</code> variable passed as parameter and
     * creates instance of <code>TicketDAOImpl</code>.
     *
     * @param dataBaseConfig  instance of <code>DataBaseConfig</code>
     */
    public TicketDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

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
