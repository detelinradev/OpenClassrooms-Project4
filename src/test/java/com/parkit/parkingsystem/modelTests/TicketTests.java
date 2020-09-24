package com.parkit.parkingsystem.modelTests;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

@Tag("TicketTests")
@DisplayName("Unit tests for Ticket class")
public class TicketTests {

    private Ticket ticket;
    private ParkingSpot parkingSpot;

    @BeforeEach
    private void setUp(){
        ticket = new Ticket();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setInTime(1L);
        ticket.setOutTime(2L);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.0);
        ticket.setId(1);
    }

    @Test
    public void get_Id_Should_ReturnId(){

        //act
        int id = ticket.getId();

        //assert
        Assertions.assertEquals(1,id);
    }

    @Test
    public void get_ParkingSpot_Should_ReturnParkingSpot(){

        //act
        ParkingSpot parkingSpotTest = ticket.getParkingSpot();

        //assert
        Assertions.assertEquals(parkingSpot,parkingSpotTest);
    }

    @Test
    public void get_VehicleRegNumber_Should_ReturnVehicleRegNumber(){

        //act
        String vehicleRegNumber = ticket.getVehicleRegNumber();

        //assert
        Assertions.assertEquals("ABCDEF",vehicleRegNumber);
    }

    @Test
    public void get_Price_Should_ReturnPrice(){

        //act
        double price = ticket.getPrice();

        //assert
        Assertions.assertEquals(1.0,price);
    }

    @Test
    public void get_InTime_Should_ReturnInTime(){

        //act
        long inTime = ticket.getInTime();

        //assert
        Assertions.assertEquals(1L,inTime);
    }

    @Test
    public void get_OutTime_Should_ReturnOutTime(){

        //act
        long outTime = ticket.getOutTime();

        //assert
        Assertions.assertEquals(2L,outTime);
    }

    @Test
    public void set_Id_Should_SetId(){

        //act
        ticket.setId(2);

        //assert
        Assertions.assertEquals(2,ticket.getId());
    }

    @Test
    public void set_ParkingSpot_Should_SetParkingSpot(){

        //act
        ParkingSpot parkingSpotNew = new ParkingSpot(2,ParkingType.BIKE,true);
        ticket.setParkingSpot(parkingSpotNew);

        //assert
        Assertions.assertEquals(parkingSpotNew,ticket.getParkingSpot());
    }

    @Test
    public void set_VehicleRegNumber_Should_SetVehicleRegNumber(){

        //act
        ticket.setVehicleRegNumber("abc");

        //assert
        Assertions.assertEquals("abc",ticket.getVehicleRegNumber());
    }

    @Test
    public void set_Price_Should_SetPrice(){

        //act
        ticket.setPrice(2.0);

        //assert
        Assertions.assertEquals(2.0,ticket.getPrice());
    }

    @Test
    public void set_InTime_Should_SetInTime(){

        //act
       ticket.setInTime(2L);

        //assert
        Assertions.assertEquals(2L,ticket.getInTime());
    }

    @Test
    public void set_OutTime_Should_SetOutTime(){

        //act
        ticket.setOutTime(3L);

        //assert
        Assertions.assertEquals(3L,ticket.getOutTime());
    }
}
