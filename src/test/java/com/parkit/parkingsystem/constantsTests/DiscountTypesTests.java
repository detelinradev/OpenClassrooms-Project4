package com.parkit.parkingsystem.constantsTests;

import com.parkit.parkingsystem.constants.DiscountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscountTypesTests {

    @Test
    public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsLessThenHour(){

        //act
        double price = DiscountType.NO_DISCOUNT.calculatePrice(0.0,45,1.5,false);

        //assert
        Assertions.assertEquals(1.125,price);
    }

    @Test
    public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsMoreThenHour(){

        //act
        double price = DiscountType.NO_DISCOUNT.calculatePrice(0.0,120,1.5,false);

        //assert
        Assertions.assertEquals(3,price);
    }

    @Test
    public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsMoreThenADay(){

        //act
        double price = DiscountType.NO_DISCOUNT.calculatePrice(0.0,1440,1.5,false);

        //assert
        Assertions.assertEquals(36,price);
    }

    @Test
    public void calculate_Price_Should_ReturnZero_When_DiscountTypeIsFREE30MINAndDurationIsLessThen30Min(){

        //act
    double price = DiscountType.FREE_30_MIN.calculatePrice(0.0,15,1.5,false);

        //assert
        Assertions.assertEquals(0,price);
    }

    @Test
    public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsFREE30MINAndDurationIsMoreThenHour(){

        //act
        double price = DiscountType.FREE_30_MIN.calculatePrice(0.0,120,1.5,false);

        //assert
        Assertions.assertEquals(2.25,price);
    }

    @Test
    public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsFREE30MINAndDurationIsMoreThenADay(){

        //act
        double price = DiscountType.FREE_30_MIN.calculatePrice(0.0,1440,1.5,false);

        //assert
        Assertions.assertEquals(35.25,price);
    }
}
