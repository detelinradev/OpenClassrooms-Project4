package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {


	@InjectMocks
	@Spy
	private ParkingService parkingService;

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

			ParkingSpot takenParkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
			ticket = new Ticket();
			inTime = timeUtil.getTimeInSeconds();
			inTime -= 60 * 60;
			ticket.setInTime(inTime);
			ticket.setOutTime(-1L);
			ticket.setParkingSpot(takenParkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			ticket.setPrice(0);
			ticket.setId(1);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}
	@Test
	public void processIncomingVehicleTest(){
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(timeUtil.getTimeInSeconds()).thenReturn(inTime);

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void processIncomingVehicleTestWithNullParkingSpot(){

		doReturn(null).when(parkingService).getNextParkingNumberIfAvailable();

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Test
	public void processIncomingVehicleTestWithParkingSpotIDZero(){
		parkingSpot.setId(0);
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void processIncomingVehicleTestWithParkingSpotIDNegative(){
		parkingSpot.setId(-1);
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void processIncomingVehicleTestWithReadVehicleRegNumberThrowsException(){

		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(RuntimeException.class);

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

	@Test
	public void processIncomingVehicleTestWithUpdateParkingThrowException(){
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenThrow(IllegalArgumentException.class);

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void processIncomingVehicleTestWithSaveTicketThrowsException(){
		doReturn(parkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
		when(timeUtil.getTimeInSeconds()).thenReturn(inTime);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenThrow(IllegalArgumentException.class);

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void getNextParkingNumberIfAvailableTest(){
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

		Assertions.assertEquals(parkingSpot,parkingService.getNextParkingNumberIfAvailable());
	}

	@Test
	public void getNextParkingNumberIfAvailableTestWithInvalidVehicleType(){
		when(inputReaderUtil.readSelection()).thenReturn(-1);

		Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
	}

	@Test
	public void getNextParkingNumberIfAvailableTestWithNoAvailableSlots(){
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

		Assertions.assertNull(parkingService.getNextParkingNumberIfAvailable());
	}

	@Test
	public void processExitingVehicleTest()  {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		parkingService.processExitingVehicle();

		verify(ticketDAO,times(1)).getTicket(any(String.class));
		verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO,times(1)).updateParking(parkingSpot);
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();

		verifyNoMoreInteractions(ticketDAO,parkingSpotDAO,inputReaderUtil);
	}

}
