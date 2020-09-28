package com.parkit.parkingsystem.dao.contracts;

import com.parkit.parkingsystem.model.Ticket;

/**
 *     Handles interactions with the database table <code>Ticket</code>.
 * Consists of methods <code>saveTicket</code>, <code>updateTicket</code> and
 * <code>getTicket</code> which creates insert, search and update queries to
 * the database table <code>Ticket</code>.
 * <p>
 *     Holds dependency to <code>DataBaseConfig</code> class through
 * constructor injection, that allows to create <code>Connection</code>
 * instances.
 */
public interface TicketDAO {

	/**
	 *     Handles creating new <code>Ticket</code> and saving it to
	 * database table <code>Ticket</code>. Throws new
	 * <code>UnsuccessfulOperationException</code> on any result
	 * different then <code>true</code> as update works with predefined
	 * conditions and expected behavior is to update successfully. Different
	 * than 1 created rows is considered exceptional case, and 1 created row
	 * returns <code>true</code>.
	 * <p>
	 *     Method has @SuppressFBWarnings annotation to suppress redundant
	 * null check warning when using try with resource block from FindBugs
	 * plugin version 3.0.5. To be removed in next release when the bug is
	 * fixed.
	 *
	 * @param ticket  instance of <code>Ticket</code> holds new data
	 *                     to be added to the database
	 * @return <code>boolean</code> variable indicating result of the process,
	 * only <code>true</code> expected as valid response, in all other cases
	 * exception is thrown
	 */
	 boolean saveTicket(Ticket ticket);

	/**
	 *     Handles retrieving a <code>Ticket</code> from the database.
	 * When passed string with registration number that do not mach records in
	 * the database returns static Ticket.NOT_FOUND
	 * <p>
	 *     Method has @SuppressFBWarnings annotation to suppress redundant
	 * null check warning when using try with resource block from FindBugs
	 * plugin version 3.0.5. To be removed in next release when the bug is
	 * fixed.
	 *
	 * @param vehicleRegNumber  vehicle registration number as
	 *                          <code>String</code>, null returns
	 *                          Ticket.NOT_FOUND
	 * @return instance of <code>Ticket</code> with registration
	 * number equals this passed as parameter or if not found returns
	 * Ticket.NOT_FOUND, never null
	 */
	 Ticket getTicket(String vehicleRegNumber);

	/**
	 *     Handles updates to database table <code>Ticket</code>.
	 * Throws new <code>UnsuccessfulOperationException</code> on any result
	 * different then <code>true</code> as update works with predefined
	 * conditions and expected behavior is to update successfully. Different
	 * than 1 updated rows is considered exceptional case, and 1 updated row
	 * returns <code>true</code>.
	 * <p>
	 *     Method has @SuppressFBWarnings annotation to suppress redundant
	 * null check warning when using try with resource block from FindBugs
	 * plugin version 3.0.5. To be removed in next release when the bug is
	 * fixed.
	 *
	 * @param ticket  instance of <code>Ticket</code> holds changes
	 *                     to be updated to the database
	 * @return <code>boolean</code> variable indicating result of the process,
	 * only <code>true</code> expected as valid response, in all other cases
	 * exception is thrown
	 */
	 boolean updateTicket(Ticket ticket);
}
