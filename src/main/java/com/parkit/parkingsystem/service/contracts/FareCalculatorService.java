package com.parkit.parkingsystem.service.contracts;

import com.parkit.parkingsystem.constants.DiscountType;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.List;
import java.util.Set;

/**
 *     Calculating price service of the application, where based on list
 * with active discounts and duration of stay, fare is calculated for the
 * passed vehicle's ticket.
 * <p>
 *     Consists of a method calculateFare where ticket price is calculated based
 * on data in discounts and recurringUsers collections, and methods getDiscounts
 * and getRecurringUsers which returns <code>List</code> with discounts and
 * <code>Set</code> with recurring users.
 * <p>
 *     Follows Builder pattern, has private constructor with parameter Builder
 * which is nested static class in this class and has public method
 * <code>build</code> that returns instance of this class. Builder itself
 * takes as parameter in its constructor enum <code>DiscountType</code> and
 * can add arbitrary count of other <code>DiscountType</code> enums through
 * public method <code>withDiscountType</code>.
 * <p>
 *     Following Builder pattern this class is able to build instances with
 * varying discount politics, if more than one, they are applied consecutively
 * over the price and result is returned as a final price. If at any moment
 * price become 0.0 method of this class returns without executing further
 * discounts.
 *
 */
public interface FareCalculatorService {

    /**
     *     Calculates price and updating it in the passed <code>Ticket</code>
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
     *     Performing checks over validity of <code>inTime</code> and
     * <code>outTime</code> fields of the passed <code>Ticket</code> object.
     * <code>Ticket</code> should have positive <code>OutTime</code> and
     * <code>inTime</code> less than <code>OutTime</code>.
     *
     * @param ticket  instance of <code>Ticket</code> holding data for current
     *                vehicle, not null
     * @param discounts <code>List</code> with <code>DiscountType</code>
     *                 objects, holding the active discounts for the
     *                 application, may be null, but in that case price remains
     *                 0.0 for any duration of stay
     * @param recurringUsers <code>Set</code> with recurring vehicles
     *                       registration numbers, not null
     */
    void calculateFare(Ticket ticket, List<DiscountType> discounts, Set<String> recurringUsers);

    /**
     *     Returns <code>List</code> with instances of enum
     * <code>DiscountType</code>. List is never empty or null, always has at
     * least one instance of <code>DiscountType</code> enum.
     *
     * @return the list with instances of enum <code>DiscountType</code>,
     * never null
     */
    List<DiscountType> getDiscounts();

    /**
     *     Returns <code>Set</code> with registered in the database vehicle
     * registration numbers. Never null, could be empty.
     *
     * @return the <code>Set</code> with registered in the database vehicle
     * registration numbers, never null
     */
    Set<String> getRecurringUsers();
}