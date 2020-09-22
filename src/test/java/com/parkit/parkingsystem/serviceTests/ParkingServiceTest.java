package com.parkit.parkingsystem.serviceTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.service.ParkingServiceImpl;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {


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

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_ProcessIncomingVehicle_When_AllParametersPassed(){

		//arrange
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(timeUtil.getTimeInSeconds()).thenReturn(inTime);

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_ParkingSpotIsNull(){

		//arrange
		doReturn(null).when(parkingService).getNextParkingNumberIfAvailable();

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_ParkingSpotIdZero(){

		//arrange
		parkingSpot.setId(0);
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_ParkingSpotIdNegative(){

		//arrange
		parkingSpot.setId(-1);
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_ReadVehicleRegNumberThrowsException(){

		//arrange
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(RuntimeException.class);

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_UpdateParkingThrowException(){

		//arrange
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenThrow(IllegalArgumentException.class);

		//act
		parkingService.processIncomingVehicle();

		//assert
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Tag("ProcessIncomingVehicleTests")
	@Test
	public void process_IncomingVehicle_Should_DoNothingAfter_When_SaveTicketThrowsException(){

		//arrange
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
		when(timeUtil.getTimeInSeconds()).thenReturn(inTime);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenThrow(IllegalArgumentException.class);

		//act
		parkingService.processIncomingVehicle();

		//arrange
		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Tag("GetNextParkingNumberIfAvailableTests")
	@Test
	public void get_NextParkingNumberIfAvailable_Should_ReturnParkingNumber_when_CarAsType(){

		//arrange
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

		//act & assert
		Assertions.assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
	}

	@Tag("GetNextParkingNumberIfAvailableTests")
	@Test
	public void get_NextParkingNumberIfAvailable_Should_ReturnParkingNumber_when_BikeAsType(){

		//arrange
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(1);

		//act & assert
		Assertions.assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
	}

	@Tag("GetNextParkingNumberIfAvailableTests")
	@Test
	public void get_NextParkingNumberIfAvailable_Should_NotReturnParkingNumber_when_InvalidVehicleType(){

		//arrange
		when(inputReaderUtil.readSelection()).thenReturn(-1);

		//act & assert
		Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
	}

	@Tag("GetNextParkingNumberIfAvailableTests")
	@Test
	public void get_NextParkingNumberIfAvailable_Should_NotReturnParkingNumber_when_NoAvailableSlots(){

		//arrange
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

		//act & assert
		Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_ProcessExitingVehicle_When_AllParametersPassed()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(fareCalculatorService,times(1)).calculateFare(ticket);
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil,fareCalculatorService);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_ReadVehicleRegNumberThrowsException()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(IllegalArgumentException.class);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_NullTicket()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(null);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_FareCalculatorThrowsException()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		doThrow(IllegalArgumentException.class).when(fareCalculatorService).calculateFare(ticket);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(fareCalculatorService,times(1)).calculateFare(ticket);
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil,fareCalculatorService);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_NotProcessExitingVehicle_When_UpdateTicketThrowsException()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenThrow(IllegalArgumentException.class);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(fareCalculatorService,times(1)).calculateFare(ticket);
		verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil,fareCalculatorService);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_ProcessExitingVehicle_When_UpdateParkingThrowsException()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenThrow(IllegalArgumentException.class);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(fareCalculatorService,times(1)).calculateFare(ticket);
		verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil,fareCalculatorService);
	}

	@Tag("ProcessExitingVehicleTests")
	@Test
	public void process_ExitingVehicle_Should_ProcessExitingVehicle_When_UpdateParkingReturnsFalse()  {

		//arrange
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);

		//act
		parkingService.processExitingVehicle();

		//assert
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();
		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(fareCalculatorService,times(1)).calculateFare(ticket);
		verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil,fareCalculatorService);
	}
}
