package com.example.aviatimes.Repositories;


import com.example.aviatimes.Models.Card;
import com.example.aviatimes.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findCardById(Long id);

    List<Card> findCardsByUser(User user);
}
