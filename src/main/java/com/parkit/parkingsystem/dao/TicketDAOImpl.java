package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TicketDAOImpl implements TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig;

    public TicketDAOImpl(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public boolean saveTicket(Ticket ticket) {

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET)) {

            // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            // ps.setInt(1,ticket.getId());
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime()*1000));
            ps.setTimestamp(5,
                    (ticket.getOutTime() == -1L) ? null : (new Timestamp(ticket.getOutTime())));

            return ps.execute();

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error saving ticket info", ex);
            throw new IllegalArgumentException("Unable to create ticket");
        }
    }

    public Ticket getTicket(String vehicleRegNumber) {

        Ticket ticket = null;
        ResultSet rs = null;

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET)) {

            // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();

            if (rs.next()) {

                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                        ParkingType.valueOf(rs.getString(6)), false);

                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).getTime()/1000);
                ticket.setOutTime(rs.getTimestamp(5) == null
                        ? LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                        : rs.getTimestamp(5).getTime()/1000);
            }
        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error getting ticket info", ex);
            throw new IllegalArgumentException("Unable to get ticket");

        } finally {

            dataBaseConfig.closeResultSet(rs);
        }
        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {

        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET)) {

            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime()));
            ps.setInt(3, ticket.getId());

            return ps.execute();

        } catch (ClassNotFoundException | SQLException ex) {

            logger.error("Error updating ticket info", ex);
            throw new IllegalArgumentException("Unable to update ticket information");
        }
    }
}
