package com.parkit.parkingsystem.integration;

import static org.mockito.Mockito.when;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.TimeUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static TimeUtil timeUtil;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        fareCalculatorService = new FareCalculatorService();
    }


    @BeforeEach
    private void setUpPerTest() {

    }

    @AfterEach
    private void cleanAfterTest(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void parkingACar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) -300);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

    @Test
    public void parkingLotExitCar_Should_CreateAPriceAndOutTime_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        parkingACar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Assertions.assertTrue(ticketDAO.getTicket("ABCDEF").getPrice() > 0.0);
        Assertions.assertTrue(ticketDAO.getTicket("ABCDEF").getOutTime() > 0);
    }

    @Test
    public void parkingABike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) -300);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(5, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));

    }

    @Test
    public void parkingLotExitBike_Should_CreateAPriceAndOutTime_When_CorrectParametersPassed(){

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        parkingABike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Assertions.assertTrue(ticketDAO.getTicket("ABCDEF").getPrice() > 0.0);
        Assertions.assertTrue(ticketDAO.getTicket("ABCDEF").getOutTime() > 0);
    }

    @Test
    public void parkingACar_Should_NotCreateATicket_When_ChoiceOfTaskIsWrong() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(3);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNull(ticketDAO.getTicket("ABCDEF"));

    }

}
