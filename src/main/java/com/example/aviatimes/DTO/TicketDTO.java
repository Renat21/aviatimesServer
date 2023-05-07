package com.example.aviatimes.DTO;

import com.example.aviatimes.Models.TravelClass;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private String flyingFrom;
    private String flyingTo;
    private LocalDate departing;
    private LocalDate returning;
    private Long adults;
    private Long children;
    private TravelClass travelClass = TravelClass.Economy;
    private List<Boolean> stops;
}

