package com.parkit.parkingsystem.constants;

/**
 * Holds SQL prepared statements for use in the application
 */
public class DBConstants {

    /**
     * Retrieves from database table parking first available slot for passed
     * parking type
     */
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where " +
            "AVAILABLE = true and TYPE = ?";

    /**
     * Updates database table parking for passed parking number with passed
     * boolean variable for availability status
     */
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    /**
     * Creating in database table ticket record for passed <codeTicket></code>
     * object
     */
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE," +
            " IN_TIME, OUT_TIME) values(?,?,?,?,?)";

    /**
     * Updates database table ticket record for passed <code>id</code> with
     * passed <code>price</code> and with passed <code>outTime</code>
     */
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * Retrieves from database table ticket record for <code>Ticket</code>> with passed
     * registration number
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME," +
            " p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=?" +
            " order by t.IN_TIME desc  limit 1";
}
