package com.parkit.parkingsystem.serviceTests;

import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTests {

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private InteractiveShell interactiveShell;

    @Test
    public void load_Interface_Should_LoadInterface_When_IncomingVehicleSelected() {

        //arrange
        when(inputReaderUtil.readSelection()).thenReturn(1);
        doThrow(IllegalArgumentException.class).when(parkingService).processIncomingVehicle();

        //act
        Assertions.assertThrows(IllegalArgumentException.class, () -> interactiveShell.loadInterface());

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> interactiveShell.loadInterface());

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