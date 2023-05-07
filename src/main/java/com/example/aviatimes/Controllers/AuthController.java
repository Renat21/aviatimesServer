package com.example.aviatimes.Controllers;

import com.example.aviatimes.DTO.ProfileDTO;
import com.example.aviatimes.DTO.UserDTO;
import com.example.aviatimes.DTO.ValidateDTO.RegisterRequestDTO;
import com.example.aviatimes.DTO.ValidateDTO.RegisterResponseDTO;
import com.example.aviatimes.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDto) {
        return userService.loginUser(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userService.existsByUserEmail(registerRequestDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Такой пользователь уже существует!"));
        }
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok(new RegisterResponseDTO("Пользователь зарегистрирован!"));
    }

    @PostMapping("/saveProfile")
    public ResponseEntity<?> saveProfile(Principal principal, @Valid @RequestBody ProfileDTO profileDTO) {
        return userService.changeProfile(principal, profileDTO);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Principal user){
        return userService.collectUserData(user);
    }


    public record JwtResponse(String jwt, Long id,String email, String username, List<String> authorities) {}
}
