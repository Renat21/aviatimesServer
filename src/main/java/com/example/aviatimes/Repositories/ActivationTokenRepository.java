package com.example.aviatimes.Repositories;


import com.example.aviatimes.Models.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationTokenRepository extends JpaRepository<ActivationToken,Integer> {

    ActivationToken findByToken(String token);

    void deleteByToken(String token);
}