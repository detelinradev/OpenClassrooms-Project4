package com.parkit.parkingsystem.dao.contracts;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 *     Handles interactions with the database table <code>Parking</code>.
 * Consists of methods <code>getNextAvailableSlot</code> and
 * <code>updateParking</code> which creates search and update queries to
 * the database table <code>Parking</code>.
 * <p>
 *     Holds dependency to <code>DataBaseConfig</code> class through
 * constructor injection, that allows to create <code>Connection</code>
 * instances.
 */
public interface ParkingSpotDAO {

	/**
	 *     Checks availability in the database table <code>Parking</code> for
	 * vehicle of the passed <code>ParkingType</code>.
	 * Returns -1 in case there is no available place of the specified type.
	 * <p>
	 *     Method has @SuppressFBWarnings annotation to suppress redundant
	 * null check warning when using try with resource block from FindBugs
	 * plugin version 3.0.5. To be removed in next release when the bug is
	 * fixed.
	 *
	 * @param parkingType  instance of <code>ParkingType</code> enum, not null
	 * @return <code>int</code> variable representing number of the available
	 * parking slot, -1 if there is no available slots
	 */
	int getNextAvailableSlot(ParkingType parkingType);

	/**
	 *     Handles updates to database table <code>Parking</code>.
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
	 * @param parkingSpot  instance of <code>ParkingSpot</code> holds changes
	 *                     to be updated to the database
	 * @return <code>boolean</code> variable indicating result of the process,
	 * only <code>true</code> expected as valid response, in all other cases
	 * exception is thrown
	 */
	boolean updateParking(ParkingSpot parkingSpot);

}
