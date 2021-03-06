package com.parkit.parkingsystem.serviceTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.service.ParkingServiceImpl;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.contracts.InputReaderUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Tag("ParkingServiceTests")
@DisplayName("Unit tests for ParkingServiceImpl class")
@ExtendWith(MockitoExtension.class)
public class ParkingServiceTests {


    @InjectMocks
    @Spy
    private ParkingServiceImpl parkingService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private TimeUtil timeUtil;

    @Mock
    FareCalculatorService fareCalculatorService;

    @Mock
    private ArrayList<DiscountType> discounts;

    @Mock
    private HashSet<String> recurringUsers;

    private ParkingSpot parkingSpot;
    private Ticket ticket;
    private long inTime;

    @BeforeEach
    public void setUpPerTest() {
        try {

            parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket = new Ticket();
            inTime = timeUtil.getTimeInSeconds();
            inTime -= 60 * 60;
            ticket.setInTime(inTime);
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
    @Tag("ProcessIncomingVehicleTests")
    @DisplayName("Tests for method processIncomingVehicle in ParkingServiceImpl class")
    public class ProcessIncomingVehicleTests {

        @Test
        public void process_IncomingVehicle_Should_ProcessIncomingVehicle_When_AllParametersPassed() {

            //arrange
            doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
            when(timeUtil.getTimeInSeconds()).thenReturn(inTime);

            //act
            parkingService.processIncomingVehicle();

            //assert
            verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
            verify(parkingService, times(1)).processIncomingVehicle();
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verifyNoMoreInteractions(parkingService, ticketDAO, parkingSpotDAO, inputReaderUtil);

        }

        @Test
        public void process_IncomingVehicle_Should_DoNothingAfter_When_ParkingSpotIsNull() {

            //arrange
            doReturn(null).when(parkingService).getNextParkingNumberIfAvailable();

            //act
            parkingService.processIncomingVehicle();

            //assert
            verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
            verify(parkingService, times(1)).processIncomingVehicle();
            verifyNoMoreInteractions(parkingService, ticketDAO, parkingSpotDAO, inputReaderUtil);
        }

        @Test
        public void process_IncomingVehicle_Should_DoNothingAfter_When_ReadVehicleRegNumberThrowsException() {

            //arrange
            doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(RuntimeException.class);

            //act
            parkingService.processIncomingVehicle();

            //assert
            verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
            verify(parkingService, times(1)).processIncomingVehicle();
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
            verifyNoMoreInteractions(parkingService, ticketDAO, parkingSpotDAO, inputReaderUtil);
        }

        @Test
        public void process_IncomingVehicle_Should_DoNothingAfter_When_UpdateParkingThrowException() {

            //arrange
            doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
            when(parkingSpotDAO.updateParking(parkingSpot)).thenThrow(IllegalArgumentException.class);

            //act
            parkingService.processIncomingVehicle();

            //assert
            verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
            verify(parkingService, times(1)).processIncomingVehicle();
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verifyNoMoreInteractions(parkingService, ticketDAO, parkingSpotDAO, inputReaderUtil);

        }

        @Test
        public void process_IncomingVehicle_Should_DoNothingAfter_When_SaveTicketThrowsException() {

            //arrange
            doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
            when(timeUtil.getTimeInSeconds()).thenReturn(inTime);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenThrow(IllegalArgumentException.class);

            //act
            parkingService.processIncomingVehicle();

            //arrange
            verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
            verify(parkingService, times(1)).processIncomingVehicle();
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verifyNoMoreInteractions(parkingService, ticketDAO, parkingSpotDAO, inputReaderUtil);

        }
    }

    @Nested
    @Tag("GetNextParkingNumberIfAvailableTests")
    @DisplayName("Tests for method getNextParkingNumberIfAvailable in ParkingServiceImpl class")
    public class GetNextParkingNumberIfAvailableTests {

        @Test
        public void get_NextParkingNumberIfAvailable_Should_ReturnParkingNumber_when_CarAsType() {

            //arrange
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

            //act & assert
            Assertions.assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
        }

        @Test
        public void get_NextParkingNumberIfAvailable_Should_ReturnParkingNumber_when_BikeAsType() {

            //arrange
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(1);

            //act & assert
            Assertions.assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
        }

        @Test
        public void get_NextParkingNumberIfAvailable_Should_NotReturnParkingNumber_when_InvalidVehicleType() {

            //arrange
            when(inputReaderUtil.readSelection()).thenReturn(-1);

            //act & assert
            Assertions.assertThrows(IllegalArgumentException.class, parkingService::getNextParkingNumberIfAvailable);
        }

        @Test
        public void get_NextParkingNumberIfAvailable_Should_NotReturnParkingNumber_when_NoAvailableSlots() {

            //arrange
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

            //act & assert
            Assertions.assertEquals(ParkingSpot.NOT_AVAILABLE, parkingService.getNextParkingNumberIfAvailable());
        }
    }

    @Nested
    @Tag("ProcessExitingVehicleTests")
    @DisplayName("Tests for method processExitingVehicle in ParkingServiceImpl class")
    public class ProcessExitingVehicleTests {

        @Test
        public void process_ExitingVehicle_Should_ProcessExitingVehicle_When_AllParametersPassed() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(fareCalculatorService.getDiscounts()).thenReturn(discounts);
            when(fareCalculatorService.getRecurringUsers()).thenReturn(recurringUsers);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verify(fareCalculatorService, times(1)).calculateFare(any(Ticket.class), anyList(), anySet());
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil, fareCalculatorService);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_ReadVehicleRegNumberThrowsException() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(IllegalArgumentException.class);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_NullTicket() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(null);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_FareCalculatorThrowsException() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(fareCalculatorService.getDiscounts()).thenReturn(discounts);
            when(fareCalculatorService.getRecurringUsers()).thenReturn(recurringUsers);
            doThrow(IllegalArgumentException.class).when(fareCalculatorService).calculateFare(ticket, discounts, recurringUsers);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verify(fareCalculatorService, times(1)).calculateFare(ticket, discounts, recurringUsers);
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil, fareCalculatorService);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_UpdateTicketThrowsException() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenThrow(IllegalArgumentException.class);
            when(fareCalculatorService.getDiscounts()).thenReturn(discounts);
            when(fareCalculatorService.getRecurringUsers()).thenReturn(recurringUsers);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verify(fareCalculatorService, times(1)).calculateFare(ticket, discounts, recurringUsers);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil, fareCalculatorService);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_UpdateParkingThrowsException() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenThrow(IllegalArgumentException.class);
            when(fareCalculatorService.getDiscounts()).thenReturn(discounts);
            when(fareCalculatorService.getRecurringUsers()).thenReturn(recurringUsers);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verify(fareCalculatorService, times(1)).calculateFare(ticket, discounts, recurringUsers);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil, fareCalculatorService);
        }

        @Test
        public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_UpdateParkingReturnsFalse() {

            //arrange
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(fareCalculatorService.getDiscounts()).thenReturn(discounts);
            when(fareCalculatorService.getRecurringUsers()).thenReturn(recurringUsers);

            //act
            parkingService.processExitingVehicle();

            //assert
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(any(String.class));
            verify(fareCalculatorService, times(1)).calculateFare(ticket, discounts, recurringUsers);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verifyNoMoreInteractions(ticketDAO, parkingSpotDAO, inputReaderUtil, fareCalculatorService);
        }
    }
}
