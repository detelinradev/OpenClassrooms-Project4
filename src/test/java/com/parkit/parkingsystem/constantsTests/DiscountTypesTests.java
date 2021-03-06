package com.parkit.parkingsystem.constantsTests;

import com.parkit.parkingsystem.constants.DiscountType;
import org.junit.jupiter.api.*;

@Tag("DiscountTypesTests")
@DisplayName("Unit tests for DiscountType enum")
public class DiscountTypesTests {

    @Nested
    @Tag("DiscountType_NO_DISCOUNT_Tests")
    @DisplayName("Tests for DiscountType NO_DISCOUNT in DiscountType enum")
    public class DiscountType_NO_DISCOUNT_Tests{

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsLessThenHour(){

            //act
            double price = DiscountType.P_NO_DISCOUNT.calculatePrice(0.0,45,1.5,false);

            //assert
            Assertions.assertEquals(1.125,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsMoreThenHour(){

            //act
            double price = DiscountType.P_NO_DISCOUNT.calculatePrice(0.0,120,1.5,false);

            //assert
            Assertions.assertEquals(3,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsNO_DISCOUNTAndDurationIsMoreThenADay(){

            //act
            double price = DiscountType.P_NO_DISCOUNT.calculatePrice(0.0,1440,1.5,false);

            //assert
            Assertions.assertEquals(36,price);
        }
    }

    @Nested
    @Tag("DiscountType_FREE_30_MIN_Tests")
    @DisplayName("Tests for DiscountType FREE_30_MIN in DiscountType enum")
    public class DiscountType_FREE_30_MIN_Tests{

        @Test
        public void calculate_Price_Should_ReturnZero_When_DiscountTypeIsFREE30MINAndDurationIsLessThen30Min(){

            //act
            double price = DiscountType.P_FREE_30_MIN.calculatePrice(0.0,15,1.5,false);

            //assert
            Assertions.assertEquals(0,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsFREE30MINAndDurationIsMoreThenHour(){

            //act
            double price = DiscountType.P_FREE_30_MIN.calculatePrice(0.0,120,1.5,false);

            //assert
            Assertions.assertEquals(2.25,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsFREE30MINAndDurationIsMoreThenADay(){

            //act
            double price = DiscountType.P_FREE_30_MIN.calculatePrice(0.0,1440,1.5,false);

            //assert
            Assertions.assertEquals(35.25,price);
        }
    }

    @Nested
    @Tag("DiscountType_RECURRING_USERS_5PERCENT_Tests")
    @DisplayName("Tests for DiscountType RECURRING_USERS_5PERCENT in DiscountType enum")
    public class DiscountType_RECURRING_USERS_5PERCENT_Tests{

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndNODISCOUNTAndDurationIsLessThenHour(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(1.125,45,1.5,true);

            //assert
            Assertions.assertEquals(1.0687499999999999,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndNODISCOUNTAndDurationIsMoreThenHour(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(3.0,120,1.5,true);

            //assert
            Assertions.assertEquals(2.8499999999999996,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndNODISCOUNTAndDurationIsMoreThenADay(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(36,1440,1.5,true);

            //assert
            Assertions.assertEquals(34.199999999999996,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndFREE30MINAndDurationIsLessThen30Min(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(0.0,45,1.5,true);

            //assert
            Assertions.assertEquals(0.0,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndFREE30MINAndDurationIsMoreThenHour(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(2.25,120,1.5,true);

            //assert
            Assertions.assertEquals(2.1374999999999997,price);
        }

        @Test
        public void calculate_Price_Should_ReturnCorrectPrice_When_DiscountTypeIsRECURRING_USERS_5PERCENTAndFREE30MINAndDurationIsMoreThenADay(){

            //act
            double price = DiscountType.S_RECURRING_USERS_5PERCENT.calculatePrice(35.25,1440,1.5,true);

            //assert
            Assertions.assertEquals(33.4875,price);
        }
    }
}

