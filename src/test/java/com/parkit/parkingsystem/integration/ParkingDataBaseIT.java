package com.parkit.parkingsystem.integration;

import static org.mockito.Mockito.when;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAOImpl;
import com.parkit.parkingsystem.dao.TicketDAOImpl;
import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingServiceImpl;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.contracts.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.contracts.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.contracts.ParkingService;
import com.parkit.parkingsystem.util.contracts.InputReaderUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private FareCalculatorService fareCalculatorService;
    private ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static TimeUtil timeUtil;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAOImpl(dataBaseTestConfig);
        ticketDAO = new TicketDAOImpl(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }


    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.NO_DISCOUNT).build();
    }

    @AfterEach
    private void cleanAfterTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void parkingCar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 3000);


        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

    @Test
    public void exitingCar_Should_CreateAPriceAndOutTime_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.NO_DISCOUNT).build();
        parkingCar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void parkingBike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 3000);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(5, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));

    }

    @Test
    public void exitingBike_Should_CreateAPriceAndOutTime_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.NO_DISCOUNT).build();
        parkingBike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void parkingCar_Should_NotCreateATicket_When_ChoiceOfTaskIsWrong() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(3);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertThrows(UnsuccessfulOperationException.class, () -> ticketDAO.getTicket("ABCDEF"));

    }

    @Test
    public void parkingCar_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 300);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

    @Test
    public void exitingCar_Should_CreateAPriceEqualsZeroAndOutTime_When_TimeInParkingLessThan30minAndDiscountType30MinFree() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.FREE_30_MIN).build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingCar_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertEquals(0.0, ticket.getPrice());
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingCar_Should_CreateAPriceGreaterThanZeroAndOutTime_When_TimeInParkingMoreThan30minAndDiscountType30MinFree() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.FREE_30_MIN).build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingCar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void parkingBike_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 300);

        //act
        parkingService.processIncomingVehicle();

        //assert
        Assertions.assertNotNull(ticketDAO.getTicket("ABCDEF"));
        Assertions.assertEquals(5, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));

    }

    @Test
    public void exitingBike_Should_CreateAPriceEqualsZeroAndOutTime_When_TimeInParkingLessThan30minAndDiscountType30MinFree() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.FREE_30_MIN).build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingBike_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertEquals(0.0, ticket.getPrice());
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingBike_Should_CreateAPriceGreaterThanZeroAndOutTime_When_TimeInParkingMoreThan30minAndDiscountType30MinFree() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.FREE_30_MIN).build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingBike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingCar_Should_CreateAPriceEqualsZeroAndOutTime_When_TimeInParkingLessThan30minAndDiscountType30MinFreeAndRecurringUsers5Percent() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.FREE_30_MIN)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingCar_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertEquals(0.0, ticket.getPrice());
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingCar_Should_CreateAPriceGreaterThanZeroAndOutTime_When_TimeInParkingMoreThan30minAndDiscountType30MinFreeAndRecurringUsers5Percent() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.FREE_30_MIN)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingCar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingBike_Should_CreateAPriceEqualsZeroAndOutTime_When_TimeInParkingLessThan30minAndDiscountType30MinFreeAndRecurringUsers5Percent() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.FREE_30_MIN)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingBike_Should_CreateATicketWithLessThan30MinStayAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertEquals(0.0, ticket.getPrice());
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingBike_Should_CreateAPriceGreaterThanZeroAndOutTime_When_TimeInParkingMoreThan30minAndDiscountType30MinFreeAndRecurringUsers5Percent() {

        //arrange
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.FREE_30_MIN)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingBike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingCar_Should_CreateAPriceAndOutTime_When_CorrectParametersPassedAndDiscountTypeNODISCOUNTAndRECURRINGUSER5PERCENT() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.NO_DISCOUNT)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        parkingCar_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

    @Test
    public void exitingBike_Should_CreateAPriceAndOutTime_When_CorrectParametersPassedAndDiscountTypeNODISCOUNTAndRECURRINGUSER5PERCENT() {

        //arrange
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        fareCalculatorService = new FareCalculatorServiceImpl
                .Builder(DiscountType.NO_DISCOUNT)
                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        parkingService = new ParkingServiceImpl(inputReaderUtil, parkingSpotDAO, ticketDAO, timeUtil
                , fareCalculatorService);
        parkingBike_Should_CreateATicketAndUpdateParkingAvailability_When_CorrectParametersPassed();
        when(timeUtil.getTimeInSeconds()).thenReturn(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        //act
        parkingService.processExitingVehicle();

        //assert
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Assertions.assertTrue(ticket.getPrice() > 0.0);
        Assertions.assertTrue(ticket.getOutTime() > 0);
    }

}
