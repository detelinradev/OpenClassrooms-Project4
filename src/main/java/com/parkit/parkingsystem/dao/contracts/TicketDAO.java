package com.parkit.parkingsystem.dao.contracts;

import com.parkit.parkingsystem.model.Ticket;

import java.util.Optional;

public interface TicketDAO {

	 boolean saveTicket(Ticket ticket);

	 Optional<Ticket> getTicket(String vehicleRegNumber);

	 boolean updateTicket(Ticket ticket);
}
