package com.parkit.parkingsystem.dao.contracts;

import com.parkit.parkingsystem.model.Ticket;

public interface TicketDAO {

	 boolean saveTicket(Ticket ticket);

	 Ticket getTicket(String vehicleRegNumber);

	 boolean updateTicket(Ticket ticket);
}
