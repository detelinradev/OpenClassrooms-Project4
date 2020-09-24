package com.parkit.parkingsystem.serviceTests;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.service.FareCalculatorServiceImpl;
import com.parkit.parkingsystem.service.InteractiveShellImpl;
import com.parkit.parkingsystem.service.contracts.ParkingService;
import com.parkit.parkingsystem.util.contracts.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@Tag("InteractiveShellTests")
@DisplayName("Unit tests for InteractiveShellImpl class")
@ExtendWith(MockitoExtension.class)
public class InteractiveShellTests {

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private final InteractiveShellImpl interactiveShell = new InteractiveShellImpl(new FareCalculatorServiceImpl
            .Builder(DiscountType.NO_DISCOUNT)
            .build());

    @Test
    public void load_Interface_Should_LoadInterface_When_IncomingVehicleSelected() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(1);
        doThrow(IllegalArgumentException.class).when(parkingService).processIncomingVehicle();

        //act
        Assertions.assertThrows(IllegalArgumentException.class, interactiveShell::loadInterface);

        //assert
        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingService, times(1)).processIncomingVehicle();
        verifyNoMoreInteractions(inputReaderUtil, parkingService);
    }

    @Test
    public void load_Interface_Should_LoadInterface_When_ExitingVehicleSelected() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(2);
        doThrow(IllegalArgumentException.class).when(parkingService).processExitingVehicle();

        //act
        Assertions.assertThrows(IllegalArgumentException.class, interactiveShell::loadInterface);

        //assert
        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingService, times(1)).processExitingVehicle();
        verifyNoMoreInteractions(inputReaderUtil, parkingService);
    }

    @Test
    public void load_Interface_Should_LoadInterface_When_ShutDownSystemSelected() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(3);

        //act
        interactiveShell.loadInterface();

        //assert
        verify(inputReaderUtil, times(1)).readSelection();
        verifyNoMoreInteractions(inputReaderUtil, parkingService);
    }
}
