package com.example.aviatimes.Controllers;


import com.example.aviatimes.DTO.TicketDTO;
import com.example.aviatimes.Models.Ticket;
import com.example.aviatimes.Services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping("/dateSorted")
    public List<Ticket> getTicketDate(@RequestBody Ticket ticket){
        return ticketService.findTicketByDate(ticket.getArrivalDate(), ticket.getDepartureDate());
    }

    @GetMapping("/getAll")
    public List<Ticket> getTickets(){
        return ticketService.findAllTickets();
    }

    @PostMapping("/searchTickets")
    @ResponseBody
    public List<Ticket> searchTicket(@RequestBody TicketDTO ticket){
        return ticketService.searchForTickets(ticket);
    }

    @GetMapping("/id/{ticketId}")
    @ResponseBody
    public Ticket getTicket(@PathVariable Long ticketId){
        return ticketService.findTicketById(ticketId);
    }

}
