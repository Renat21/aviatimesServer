package com.example.aviatimes.utils;


import com.example.aviatimes.Models.Ticket;
import com.example.aviatimes.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DBInit implements CommandLineRunner {

    @Autowired
    TicketService ticketService;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 11; i++) {
            Ticket ticket = new Ticket();
            ticket.setArrivalDate(LocalDateTime.of(2023, 6, 10, 11, 30).plusDays(i));
            ticket.setDestination("FMA");
            ticket.setDestinationName("Moscow");
            ticket.setDepartureDate(LocalDateTime.of(2023, 6, 10 + i, 12 + i, 30).plusDays(i));
            ticket.setTicketCount(21L);
            ticket.setOrigin("AST");
            ticket.setOriginName("Astrahan");
            ticket.setStops(1L);
            ticket.setPriceAdult(8000L + i * 100);
            ticket.setPriceChild(1200L + i * 100);
            ticket.setCompany("pobeda");
            ticket.setLuggage(24L);
            ticket.setLuggageCost(2400L);
            ticket.setFlight("FP" + (i + 1) * 32);
            ticketService.createTicket(ticket);
        }
    }
}
