package com.parkit.parkingsystem.daoTests;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("TicketDAOTests")
@DisplayName("Unit tests for TicketDAO class")
public class TicketDAOTests {

    @InjectMocks
    private TicketDAO ticketDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private Ticket ticket;

    @BeforeEach
    public void setUpPerTest() {
        try {

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket = new Ticket();
            ticket.setInTime(1L);
            ticket.setOutTime(-1L);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticket.setId(1);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Nested
    @Tag("methodSaveTicketTests")
    @DisplayName("Tests for method saveTicket in TicketDAO class")
    public class SaveTicketTests {

        @Test
        public void save_Ticket_Should_Save_Ticket_When_ConnectionIsEstablishedAndPreparedStatementIsAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.execute()).thenReturn(true);

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
            Assertions.assertThrows(IllegalArgumentException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_ThrowException_When_PreparedStatementIsNotAcquired() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_NotSave_Ticket_When_ExecuteDoNotReturnResultSet() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.execute()).thenReturn(false);

            //act & assert
            Assertions.assertFalse(ticketDAO.saveTicket(ticket));
            verify(dataBaseConfig, times(1)).getConnection();
            verify(connection, times(1)).prepareStatement(DBConstants.SAVE_TICKET);
            verify(connection, times(1)).close();
            verifyNoMoreInteractions(dataBaseConfig, connection);
        }

        @Test
        public void save_Ticket_Should_ThrowException_When_ExecuteThrowException() throws SQLException, ClassNotFoundException {

            //arrange
            when(dataBaseConfig.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
            when(preparedStatement.execute()).thenThrow(SQLException.class);

            //act & assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> ticketDAO.saveTicket(ticket));
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


    }
}
