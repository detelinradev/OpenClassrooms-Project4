package com.parkit.parkingsystem.daoTests;

import com.parkit.parkingsystem.config.contracts.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAOImpl;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("TicketDAOTests")
@DisplayName("Unit tests for TicketDAO class")
public class TicketDAOTests {

    @InjectMocks
    private TicketDAOImpl ticketDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Mock
    Timestamp timestamp;

    private Ticket ticket;
    private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeEach
    public void setUpPerTest() {
        try {

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticket.setInTime(1L);
            ticket.setOutTime(1L);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0.0);
            ticket.setId(1);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @AfterEach
    public void tearDown() {
        dataBaseTestConfig.closePreparedStatement(preparedStatement);
        dataBaseTestConfig.closeResultSet(resultSet);
        dataBaseTestConfig.closeConnection(connection);
    }

    @Nested
    @Tag("methodSaveTicketTests")
    @DisplayName("Tests for method saveTicket in TicketDAO class")
    public class SaveTicketTests {

        @Test
        public void save_Ticket_Should_SaveTicket_When_ConnectionIsEstablishedAndPreparedStatementIsAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            //act & assert
            Assertions.assertTrue(ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_ThrowException_When_ConnectionIsNotEstablished() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenThrow(ClassNotFoundException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_ThrowException_When_PreparedStatementIsNotAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_NotSaveTicket_When_ExecuteUpdateReturnZeroRows() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_ThrowException_When_ExecuteUpdateThrowException() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }
    }

    @Nested
    @Tag("methodGetTicketTests")
    @DisplayName("Tests for method getTicket in TicketDAO class")
    public class GetTicketTests {

        @Test
        public void get_Ticket_Should_ReturnTicket_When_ConnectionIsEstablishedAndPreparedStatementIsReturningResultSet()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(1);
            when(resultSet.getInt(2)).thenReturn(1);
            when(resultSet.getString(6)).thenReturn("CAR");
            when(resultSet.getDouble(3)).thenReturn(0.0);
            when(resultSet.getTimestamp(4)).thenReturn(timestamp);
            when(resultSet.getTimestamp(5)).thenReturn(timestamp);
            when(timestamp.getTime()).thenReturn(1000L);

            //act
            Ticket ticketNew = ticketDAO.getTicket("ABCDEF");

            //assert
            Assertions.assertEquals(ticket.getId(), ticketNew.getId());
            Assertions.assertEquals(ticket.getInTime(), ticketNew.getInTime());
            Assertions.assertEquals(ticket.getOutTime(), ticketNew.getOutTime());
            Assertions.assertEquals(ticket.getParkingSpot(), ticketNew.getParkingSpot());
            Assertions.assertEquals(ticket.getPrice(), ticketNew.getPrice());
            Assertions.assertEquals(ticket.getVehicleRegNumber(), ticketNew.getVehicleRegNumber());
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
            verify(preparedStatement, times(1)).executeQuery();
            verify(preparedStatement, times(1)).setString(1, "ABCDEF");
            verify(preparedStatement, times(1)).close();
            verify(connection, times(1)).prepareStatement(DBConstants.GET_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }

        @Test
        public void get_Ticket_Should_ThrowException_When_ConnectionIsNotEstablished()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenThrow(ClassNotFoundException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.getTicket("ABCDEF"));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(null);
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }

        @Test
        public void get_Ticket_Should_ThrowException_When_ExecuteQueryThrowException()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.getTicket("ABCDEF"));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(null);
            verify(connection, times(1)).prepareStatement(DBConstants.GET_TICKET);
            verify(connection, times(1)).close();
            verify(preparedStatement, times(1)).executeQuery();
            verify(preparedStatement, times(1)).setString(1, "ABCDEF");
            verify(preparedStatement, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }

        @Test
        public void get_Ticket_Should_ThrowException_When_PreparedStatementIsNotAcquired()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.getTicket("ABCDEF"));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(null);
            verify(connection, times(1)).prepareStatement(DBConstants.GET_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }

        @Test
        public void get_Ticket_Should_ThrowException_When_ResultSetNextThrowException()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.getTicket("ABCDEF"));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
            verify(connection, times(1)).prepareStatement(DBConstants.GET_TICKET);
            verify(connection, times(1)).close();
            verify(preparedStatement, times(1)).executeQuery();
            verify(preparedStatement, times(1)).setString(1, "ABCDEF");
            verify(preparedStatement, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }

        @Test
        public void get_Ticket_Should_ReturnTicketNOT_FOUND_When_ResultSetNextReturnFalse()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            //act & assert
            Assertions.assertEquals(Ticket.NOT_FOUND, ticketDAO.getTicket("ABCDEF"));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
            verify(connection, times(1)).prepareStatement(DBConstants.GET_TICKET);
            verify(connection, times(1)).close();
            verify(preparedStatement, times(1)).executeQuery();
            verify(preparedStatement, times(1)).setString(1, "ABCDEF");
            verify(preparedStatement, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection, preparedStatement);
        }
    }

    @Nested
    @Tag("methodUpdateTicketTests")
    @DisplayName("Tests for method updateTicket in TicketDAO class")
    public class UpdateTicketTests {

        @Test
        public void update_Ticket_Should_UpdateTicket_When_ConnectionIsEstablishedAndPreparedStatementIsAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            //act & assert
            Assertions.assertTrue(ticketDAO.updateTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void update_Ticket_Should_ThrowException_When_ConnectionIsNotEstablished()
                throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenThrow(ClassNotFoundException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.updateTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void update_Ticket_Should_ThrowException_When_PreparedStatementIsNotAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.updateTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void update_Ticket_Should_NotUpdateTicket_When_ExecuteUpdateReturnZeroRows() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.updateTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void update_Ticket_Should_ThrowException_When_ExecuteUpdateThrowException() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.updateTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }
    }
}
