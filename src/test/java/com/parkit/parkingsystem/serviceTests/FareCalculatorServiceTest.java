package com.parkit.parkingsystem.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import com.parkit.parkingsystem.util.TimeUtilImpl;
import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private static TimeUtil timeUtil;
    private Ticket ticket;
    private long inTime;
    private long outTime;

    @BeforeAll
    private static void setUp() {

        fareCalculatorService = new FareCalculatorServiceImpl.Builder(DiscountType.NO_DISCOUNT)
//                .withDiscountType(DiscountType.FREE_30_MIN)
//                .withDiscountType(DiscountType.RECURRING_USERS_5PERCENT)
                .build();
        timeUtil = new TimeUtilImpl();

    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        inTime = timeUtil.getTimeInSeconds();
        outTime =  timeUtil.getTimeInSeconds();
    }

    @Test
    public void calculate_FareCar_Should_CalculateFareCar_When_AllParametersPassed() {

        //arrange
        inTime -= 60 * 60;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act
        fareCalculatorService.calculateFare(ticket);

        //assert
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculate_FareBike_Should_CalculateFareBike_When_AllParametersPassed() {

        //arrange
        inTime -= 60 * 60;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act
        fareCalculatorService.calculateFare(ticket);

        //assert
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculate_Fare_Should_ThrowException_When_PassedFareUnknownType() {

        //arrange
        inTime -= 60 * 60;
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act & assert
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculate_Fare_Should_ThrowException_When_InTimeInFuture() {

        //arrange
        inTime += 60 * 60;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act & assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculate_FareBike_Should_CalculateCorrectFare_When_LessThanOneHourParkingTime() {

        //arrange
        inTime -= 45 * 60;// 45 minutes parking time should give 3/4th parking fare
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act
        fareCalculatorService.calculateFare(ticket);

        //assert
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void  calculate_FareCar_Should_CalculateCorrectFare_When_LessThanOneHourParkingTime() {

        //arrange
        inTime -= 45 * 60;// 45 minutes parking time should give 3/4th parking fare
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act
        fareCalculatorService.calculateFare(ticket);

        //assert
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculate_FareCar_Should_CalculateCorrectFare_When_MoreThenADayStay() {

        //arrange
        inTime -= 24 * 60 * 60;// 24 hours parking time should give 24 * parking fare per hour
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //act
        fareCalculatorService.calculateFare(ticket);

        //assert
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

}
