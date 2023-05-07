package com.example.aviatimes.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ProfileDTO {
    private String username;

    private String password;

    private String oldPassword;

    private String email;
}