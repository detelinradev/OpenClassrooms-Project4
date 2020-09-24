package com.parkit.parkingsystem.serviceTests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import org.junit.jupiter.api.*;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag("FareCalculatorServiceTests")
@DisplayName("Unit tests for FareCalculatorServiceImpl class")
@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTests {

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
    private Set<String> recurringUsers;

    @Mock
    private DiscountType discountType;

    @BeforeEach
    private void setUpPerTest() {

        discounts = new ArrayList<>();
        discounts.add(discountType);
        recurringUsers = new HashSet<>();
    }

    @Nested
    @Tag("methodCalculateFareTests")
    @DisplayName("Tests for method calculateFare in FareCalculatorServiceImpl class")
    public class CalculateFareTests {

        @Test
        public void calculate_Fare_Should_CalculateFare_When_AllParametersPassed() {

            //arrange
            when(ticket.getInTime()).thenReturn(3600L);
            when(ticket.getOutTime()).thenReturn(7200L);
            when(ticket.getVehicleRegNumber()).thenReturn("abc");
            when(ticket.getParkingSpot()).thenReturn(parkingSpot);
            when(parkingSpot.getParkingType()).thenReturn(parkingType);
            when(parkingType.getFare()).thenReturn(1.0);
            when(discountType.calculatePrice(0.0,60L, 1.0,false)).thenReturn(1.0);
            doNothing().when(ticket).setPrice(1.0);

            //act
            fareCalculatorService.calculateFare(ticket,discounts,recurringUsers);

            //assert
            verify(ticket, times(2)).getInTime();
            verify(ticket, times(3)).getOutTime();
            verify(ticket, times(1)).getParkingSpot();
            verify(ticket, times(2)).getVehicleRegNumber();
            verify(ticket, times(1)).setPrice(1.0);
            verify(parkingSpot, times(1)).getParkingType();
            verify(parkingType, times(1)).getFare();
            verify(discountType, times(1)).calculatePrice(0.0,60L, 1.0,false);
            verifyNoMoreInteractions(ticket,parkingSpot,parkingType, discountType);

        }

        @Test
        public void calculate_Fare_Should_CalculateFare_When_AllParametersPassedAndCalculatePriceReturnZero() {

            //arrange
            when(ticket.getInTime()).thenReturn(3600L);
            when(ticket.getOutTime()).thenReturn(7200L);
            when(ticket.getParkingSpot()).thenReturn(parkingSpot);
            when(ticket.getVehicleRegNumber()).thenReturn("abc");
            when(parkingSpot.getParkingType()).thenReturn(parkingType);
            when(parkingType.getFare()).thenReturn(1.0);
            when(discountType.calculatePrice(0.0,60L, 1.0,false)).thenReturn(0.0);

            //act
            fareCalculatorService.calculateFare(ticket,discounts,recurringUsers);

            //assert
            verify(ticket, times(2)).getInTime();
            verify(ticket, times(3)).getOutTime();
            verify(ticket, times(1)).getParkingSpot();
            verify(ticket, times(1)).getVehicleRegNumber();
            verify(parkingSpot, times(1)).getParkingType();
            verify(parkingType, times(1)).getFare();
            verify(discountType, times(1)).calculatePrice(0.0,60L, 1.0,false);
            verifyNoMoreInteractions(ticket,parkingSpot,parkingType, discountType);

        }

        @Test
        public void calculate_Fare_Should_ThrowException_When_InTimeInFuture() {

            //arrange
            when(ticket.getInTime()).thenReturn(7600L);
            when(ticket.getOutTime()).thenReturn(7200L);

            //act & assert
            assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,discounts,recurringUsers));
        }

        @Test
        public void calculate_Fare_Should_ThrowException_When_OutTimeMinusOne() {

            //arrange
            when(ticket.getInTime()).thenReturn(3600L);
            when(ticket.getOutTime()).thenReturn(-1L);

            //act & assert
            assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket,discounts,recurringUsers));
        }
    }
}
