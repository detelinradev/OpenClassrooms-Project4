package com.parkit.parkingsystem.util.contracts;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Provides utility methods for acquiring time in the application.
 * Consists of methods <code>getTime</code> and <code>getTimeInSeconds</code>
 * which  returns the current date-time using the system clock and the current
 * date-time to the number of seconds from the epoch.
 */
public interface TimeUtil {

    /**
     * Obtains the current date-time from the system clock in the UTC-timezone.
     *
     * @return the current date-time using the system clock and UTC time-zone,
     * not null
     */
    LocalDateTime getTime();

    /**
     * Obtains the current date-time from the system clock in the UTC-timezone
     * and converts it to the number of seconds from the epoch of
     * 1970-01-01T00:00:00Z.
     *
     * @return the number of seconds from the epoch of 1970-01-01T00:00:00Z
     * till current date-time
     */
    long getTimeInSeconds();

}
