package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.util.TimeUtil;
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
	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@Mock
	private static ParkingSpotDAO parkingSpotDAO;

	@Mock
	private static TicketDAO ticketDAO;

	@Mock
	private static TimeUtil timeUtil;

	private ParkingSpot freeParkingSpot;
	private Ticket ticket;
	private long inTime;

	@BeforeEach
	public void setUpPerTest() {
		try {

			ParkingSpot takenParkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			freeParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
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
	public void processIncomingVehicleTest() throws Exception {
		doReturn(freeParkingSpot).when(parkingService).getNextParkingNumberIfAvailable();
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(freeParkingSpot)).thenReturn(true);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(timeUtil.getTimeInSeconds()).thenReturn(inTime);

		parkingService.processIncomingVehicle();

		verify(parkingService,times(1)).getNextParkingNumberIfAvailable();
		verify(parkingService,times(1)).processIncomingVehicle();
		verify(parkingSpotDAO,times(1)).updateParking(freeParkingSpot);
		verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
		verify(inputReaderUtil,times(1)).readVehicleRegistrationNumber();

		verifyNoMoreInteractions(parkingService,ticketDAO,parkingSpotDAO,inputReaderUtil);

	}

	@Test
	public void processExitingVehicleTest() throws Exception {
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		parkingService.processExitingVehicle();

		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

}
