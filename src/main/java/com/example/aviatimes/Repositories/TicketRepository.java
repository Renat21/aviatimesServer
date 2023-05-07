package com.example.aviatimes.Repositories;

import com.example.aviatimes.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findTicketById(Long id);

    @Query("select t from Ticket t where t.arrivalDate > ?1 and t.departureDate < ?2")
    List<Ticket> findTicketByArrivalDateAfterAndAndDepartureDateBefore(LocalDateTime arrival_date, LocalDateTime departure_date);

    List<Ticket> findTicketByArrivalDateAfterAndAndDepartureDateBeforeAndStops(LocalDateTime arrival_date, LocalDateTime departure_date, Long stops);


    @Query("select t from Ticket t where t.departureDate >= ?1 and t.departureDate <= ?2 " +
            "and t.destinationName = ?3 and t.originName = ?4 and t.ticketCount >= ?5 and t.travelClass = ?6")
    List<Ticket> searchForTickets(LocalDateTime departureDate, LocalDateTime departureDateBefore,
                                  String destinationName, String originName, Long ticketCount, String travelClass);
}