package com.example.aviatimes.Controllers;


import com.example.aviatimes.Models.Card;
import com.example.aviatimes.Services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/bookTicket/{ticketId}")
    @ResponseBody
    public ResponseEntity<?> bookTicket(Principal user, @PathVariable Long ticketId, @RequestBody Card card){
        return cardService.bookTicket(user, ticketId, card);
    }

    @GetMapping("/getCards")
    @ResponseBody
    public List<Card> getCards(Principal user){
        return cardService.findCardsByUser(user);
    }
}
