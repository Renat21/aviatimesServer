package com.example.aviatimes.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Table(name = "card")
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@JsonIgnoreProperties({"user"})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("user")
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    private String name;

    private String surname;

    private String middleName;

    private String birthday;

    private String gender;

    private Boolean luggage;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Ticket ticket;

}
