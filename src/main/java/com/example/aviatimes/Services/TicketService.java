package com.example.aviatimes.Services;


import com.example.aviatimes.DTO.TicketDTO;
import com.example.aviatimes.Models.Ticket;
import com.example.aviatimes.Repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserService userService;
    public List<Ticket> findTicketByDate(LocalDateTime starts, LocalDateTime before){
        return ticketRepository.findTicketByArrivalDateAfterAndAndDepartureDateBefore(starts, before);
    }

    public List<Ticket> findTicketByDateAndStops(LocalDateTime starts, LocalDateTime before, Long stops){
        return ticketRepository.findTicketByArrivalDateAfterAndAndDepartureDateBeforeAndStops(starts,before, stops);
    }

    public Ticket createTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket findTicketById(Long ticketId){
        return ticketRepository.findTicketById(ticketId);
    }

    public List<Ticket> searchForTickets(TicketDTO ticketDTO){
        if (ticketDTO.getDeparting() != null && !Objects.equals(ticketDTO.getFlyingTo(), "") && !Objects.equals(ticketDTO.getFlyingFrom(), "")) {
            List<Ticket> tickets = ticketRepository.searchForTickets(ticketDTO.getDeparting().atTime(0, 1), ticketDTO.getDeparting().plusDays(1L).atTime(0, 1),
                    ticketDTO.getFlyingTo(), ticketDTO.getFlyingFrom(),
                    ticketDTO.getAdults() + ticketDTO.getChildren(), ticketDTO.getTravelClass().toString());
            if (ticketDTO.getStops() != null && ticketDTO.getStops().size() > 0 && !ticketDTO.getStops().contains(true))
                return tickets;

            if (ticketDTO.getStops() != null && ticketDTO.getStops().size() > 0){
                tickets = tickets.stream().filter(i -> ticketDTO.getStops().get(Math.toIntExact(i.getStops()))).collect(Collectors.toList());
            }
            return tickets;
        }
        if (ticketDTO.getStops() != null && ticketDTO.getStops().size() > 0 && !ticketDTO.getStops().contains(true)){
            return ticketRepository.findAll();
        }
        if (ticketDTO.getStops() != null && ticketDTO.getStops().size() > 0){
            return ticketRepository.findAll().stream().filter(i -> ticketDTO.getStops().get(Math.toIntExact(i.getStops()))).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
