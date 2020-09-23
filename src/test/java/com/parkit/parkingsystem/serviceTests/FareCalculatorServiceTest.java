package com.parkit.parkingsystem.serviceTests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    @InjectMocks
    private final FareCalculatorServiceImpl fareCalculatorService = new FareCalculatorServiceImpl
            .Builder(DiscountType.NO_DISCOUNT)
            .build();

    @Mock
    private Ticket ticket;

    @Mock
    private ParkingSpot parkingSpot;

    @Mock
    private ParkingType parkingType;

    private List<DiscountType> discounts;

    @Mock
    private DiscountType discountType;

    @BeforeEach
    private void setUpPerTest() {

        discounts = new ArrayList<>();
        discounts.add(discountType);
    }

    @Test
    public void calculate_Fare_Should_CalculateFare_When_AllParametersPassed() {

        //arrange
        when(ticket.getInTime()).thenReturn(3600L);
        when(ticket.getOutTime()).thenReturn(7200L);
        when(ticket.getParkingSpot()).thenReturn(parkingSpot);
        when(parkingSpot.getParkingType()).thenReturn(parkingType);
        when(parkingType.getFare()).thenReturn(1.0);
        when(discountType.calculatePrice(0.0,60L, 1.0)).thenReturn(1.0);
        doNothing().when(ticket).setPrice(1.0);

        //act
        fareCalculatorService.calculateFare(ticket,discounts);

        //assert
        verify(ticket, times(2)).getInTime();
        verify(ticket, times(3)).getOutTime();
        verify(ticket, times(1)).getParkingSpot();
        verify(ticket, times(1)).setPrice(1.0);
        verify(parkingSpot, times(1)).getParkingType();
        verify(parkingType, times(1)).getFare();
        verify(discountType, times(1)).calculatePrice(0.0,60L, 1.0);
        verifyNoMoreInteractions(ticket,parkingSpot,parkingType, discountType);

    }

    @Test
    public void calculate_Fare_Should_CalculateFare_When_AllParametersPassedAndCalculatePriceReturnZero() {

        //arrange
        when(ticket.getInTime()).thenReturn(3600L);
        when(ticket.getOutTime()).thenReturn(7200L);
        when(ticket.getParkingSpot()).thenReturn(parkingSpot);
        when(parkingSpot.getParkingType()).thenReturn(parkingType);
        when(parkingType.getFare()).thenReturn(1.0);
        when(discountType.calculatePrice(0.0,60L, 1.0)).thenReturn(0.0);

        //act
        fareCalculatorService.calculateFare(ticket,discounts);

        //assert
        verify(ticket, times(2)).getInTime();
        verify(ticket, times(3)).getOutTime();
        verify(ticket, times(1)).getParkingSpot();
        verify(parkingSpot, times(1)).getParkingType();
        verify(parkingType, times(1)).getFare();
        verify(discountType, times(1)).calculatePrice(0.0,60L, 1.0);
        verifyNoMoreInteractions(ticket,parkingSpot,parkingType, discountType);

    }

    @Test
    public void calculate_Fare_Should_ThrowException_When_InTimeInFuture() {

        //arrange
        when(ticket.getInTime()).thenReturn(7600L);
        when(ticket.getOutTime()).thenReturn(7200L);

        //act & assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,discounts));
    }

    @Test
    public void calculate_Fare_Should_ThrowException_When_OutTimeMinusOne() {

        //arrange
        when(ticket.getInTime()).thenReturn(3600L);
        when(ticket.getOutTime()).thenReturn(-1L);

        //act & assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,discounts));
    }

}
