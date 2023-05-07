package com.example.aviatimes.Models;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ticket")
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long ticketCount=1L;

    private LocalDateTime departureDate;

    private String origin;

    private String originName;

    private LocalDateTime arrivalDate;

    private String destination;

    private String destinationName;

    private Long stops = 1L;

    private Long priceChild;

    private Long priceAdult;

    private String travelClass = TravelClass.Economy.toString();

    private String company;

    private Long luggage;

    private Long luggageCost;

    private String flight;
}
