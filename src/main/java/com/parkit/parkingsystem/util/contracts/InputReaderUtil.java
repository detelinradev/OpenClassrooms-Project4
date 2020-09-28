package com.parkit.parkingsystem.util.contracts;

import com.parkit.parkingsystem.exception.UnsuccessfulOperationException;

/**
 * Provides utility methods for reading user input in the application.
 * Consists of methods <code>readSelection</code> and
 * <code>readVehicleRegistrationNumber</code> which reads from the console
 * user input and returns <code>int</code> and <code>String</code> output.
 */
public interface InputReaderUtil {

    /**
     * Read user input from the console and returns <code>int</code> value.
     * It is expected behavior if input is present but not valid, in that case
     * parse exception is swallowed and -1 is returned, but in case no line was
     * found exception is thrown.
     *
     * @return <code>int</code> representation of the input, -1 when not
     * valid <code>int</code>
     * @throws UnsuccessfulOperationException if no line was found
     */
    int readSelection();

    /**
     * Read user input from the console and returns <code>String</code> value.
     *
     * @return <code>String</code> representation of the input
     * @throws UnsuccessfulOperationException if no line was found
     * @throws IllegalArgumentException if the input as <code>String</code> is
     * <code>null</code> or has zero length
     */
    String readVehicleRegistrationNumber();

}
