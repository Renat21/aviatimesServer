package com.example.aviatimes.Controllers;

import com.example.aviatimes.Models.ActivationToken;
import com.example.aviatimes.Repositories.ActivationTokenRepository;
import com.example.aviatimes.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ActivateController {

    private final ActivationTokenRepository activationTokenRepository;
    private final UserService userService;

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        ActivationToken activationToken = activationTokenRepository.findByToken(code);
        if (activationToken != null && activationToken.compareDate()) {
            userService.activateUser(code);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "localhost:3000/login");
            return new ResponseEntity(headers, HttpStatus.FOUND);
        } else
            return ResponseEntity.ok("Токен не действителен");
    }
}
