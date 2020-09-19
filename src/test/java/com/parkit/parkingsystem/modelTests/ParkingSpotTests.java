package com.parkit.parkingsystem.modelTests;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkingSpotTests {

    private  ParkingSpot parkingSpot;

    @BeforeEach
    private void setUp() {

        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    public void get_Id_Should_ReturnId() {

        //act
        int id = parkingSpot.getId();

        //assert
        Assertions.assertEquals(1, id);
    }

    @Test
    public void get_ParkingType_Should_ReturnParkingType() {

        //act
        ParkingType parkingType = parkingSpot.getParkingType();

        //assert
        Assertions.assertEquals(ParkingType.CAR, parkingType);
    }

    @Test
    public void get_IsAvailable_Should_ReturnIsAvailable() {

        //act
        boolean isAvailable = parkingSpot.isAvailable();

        //assert
        Assertions.assertTrue(isAvailable);
    }

    @Test
    public void set_Id_Should_SetId(){

        //act
        parkingSpot.setId(2);

        //assert
        Assertions.assertEquals(2,parkingSpot.getId());
    }

    @Test
    public void set_ParkingType_Should_SetParkingType(){

        //act
        parkingSpot.setParkingType(ParkingType.BIKE);

        //assert
        Assertions.assertEquals(ParkingType.BIKE,parkingSpot.getParkingType());
    }

    @Test
    public void set_IsAvailable_Should_SetIsAvailable(){

        //act
        parkingSpot.setAvailable(false);

        //assert
        Assertions.assertFalse(parkingSpot.isAvailable());
    }
}
