package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.contracts.FareCalculatorService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *     Calculating price service of the application, where based on list
 * with active discounts and duration of stay, fare is calculated for the
 * passed vehicle's ticket.
 * <p>
 *     Consists of a method calculateFare where ticket price is calculated based
 * on data in discounts and recurringUsers collections, and methods getDiscounts
 * and getRecurringUsers which returns <class>List</class> with discounts and
 * <class>Set</class> with recurring users.
 * <p>
 *     Follows Builder pattern, has private constructor with parameter Builder
 * which is nested static class in this class and has public method
 * <class>build</class> that returns instance of this class. Builder itself
 * takes as parameter in its constructor enum <class>DiscountType</class> and
 * can add arbitrary count of other <class>DiscountType</class> enums through
 * public method <class>withDiscountType</class>.
 * <p>
 *     Following Builder pattern this class is able to build instances with
 * varying discount politics, if more than one, they are applied consecutively
 * over the price and result is returned as a final price. If at any moment
 * price become 0.0 method of this class returns without executing further
 * discounts.
 *
 */
public class FareCalculatorServiceImpl implements FareCalculatorService {

    public List<DiscountType> discounts;
    public Set<String> recurringUsers;

    private FareCalculatorServiceImpl(Builder builder) {

        this.discounts = new ArrayList<>(builder.getDiscounts());
        this.recurringUsers = new HashSet<>();
    }

    public static class Builder {

        private final List<DiscountType> discountsList = new ArrayList<>();

        public Builder(DiscountType discountType) {

            discountsList.add(discountType);
        }

        public Builder withDiscountType(DiscountType discountType) {

            discountsList.add(discountType);

            return this;
        }

        public FareCalculatorServiceImpl build() {

            return new FareCalculatorServiceImpl(this);
        }

        public List<DiscountType> getDiscounts() {
            return discountsList;
        }
    }

    /**
     *     Returns <class>List</class> with instances of enum
     * <class>DiscountType</class>. List is never empty or null, always has at
     * least one instance of <class>DiscountType</class> enum.
     *
     * @return the list with instances of enum <class>DiscountType</class>,
     * never null
     */
    @Override
    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    /**
     *     Returns <class>Set</class> with registered in the database vehicle
     * registration numbers. Never null, could be empty.
     *
     * @return the <class>Set</class> with registered in the database vehicle
     * registration numbers, never null
     */
    @Override
    public Set<String> getRecurringUsers() {
        return recurringUsers;
    }

    /**
     *     Calculates price and updating it in the passed <class>Ticket</class>
     * object. Ticket price is calculated based on data in discounts and
     * recurringUsers collections and the duration of stay in the parking.
     * <p>
     *     Able to calculate price based on varying discount politics, if more
     * than one, they are applied consecutively over the price and result is
     * returned as final price. If at any moment price become 0.0 returns
     * without executing further discounts.
     * <p>
     *     Handles adding new users to the set with recurring users.
     * <p>
     *     Performing checks over validity of <class>inTime</class> and
     * <class>outTime</class> fields of the passed <class>Ticket</class> object.
     * <class>Ticket</class> should have positive <class>OutTime</class> and
     * <class>inTime</class> less than <class>OutTime</class>.
     *
     * @param ticket  instance of <class>Ticket</class> holding data for current
     *                vehicle, not null
     * @param discounts <class>List</class> with <class>DiscountType</class>
     *                 objects, holding the active discounts for the
     *                 application, may be null, but in that case price remains
     *                 0.0 for any duration of stay
     * @param recurringUsers <class>Set</class> with recurring vehicles
     *                       registration numbers, not null
     */
    @Override
    public void calculateFare(Ticket ticket, List<DiscountType> discounts, Set<String> recurringUsers) {
        if ((ticket.getOutTime() == -1L) || (ticket.getOutTime() < (ticket.getInTime()))) {

            throw new IllegalArgumentException
                    ("Out time provided is incorrect - " + ticket.getOutTime() + " " + ticket.getInTime());
        }

        long inMinute = ticket.getInTime();
        long outMinute = ticket.getOutTime();
        long duration = (outMinute - inMinute) / 60;
        double price = 0.0;
        double fare = ticket.getParkingSpot().getParkingType().getFare();
        boolean isRecurringUser = recurringUsers.contains(ticket.getVehicleRegNumber());

        for (DiscountType discount : discounts) {
            price = discount.calculatePrice(price, duration, fare, isRecurringUser);

            if (price == 0.0) return;
        }

        if (!isRecurringUser) recurringUsers.add(ticket.getVehicleRegNumber());

        ticket.setPrice(price);
    }
}
