package com.example.aviatimes.Services;

import com.example.aviatimes.DTO.ValidateDTO.RegisterResponseDTO;
import com.example.aviatimes.Models.Card;
import com.example.aviatimes.Models.Ticket;
import com.example.aviatimes.Models.User;
import com.example.aviatimes.Repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    UserService userService;

    @Autowired
    TicketService ticketService;

    public ResponseEntity<?> bookTicket(Principal userP, Long ticketId, Card card){
        User user = (User)userService.loadUserByUsername(userP.getName());
        Ticket ticket = ticketService.findTicketById(ticketId);
        if (ticket.getTicketCount() > 0){
            ticket.setTicketCount(ticket.getTicketCount() - 1);
            ticketService.createTicket(ticket);

            card.setTicket(ticket);
            card.setUser(user);
            cardRepository.save(card);

            return ResponseEntity.ok(new RegisterResponseDTO("Билет забронирован"));
        }else {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Таких билетов больше не осталось"));
        }
    }

    public List<Card> findCardsByUser(Principal principal){
        User user = (User)userService.loadUserByUsername(principal.getName());
        return cardRepository.findCardsByUser(user);
    }
}
