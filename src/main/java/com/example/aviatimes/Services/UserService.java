package com.example.aviatimes.Services;


import com.example.aviatimes.Config.JWT.JWTUtil;
import com.example.aviatimes.Controllers.AuthController;
import com.example.aviatimes.DTO.ProfileDTO;
import com.example.aviatimes.DTO.UserDTO;
import com.example.aviatimes.DTO.ValidateDTO.RegisterRequestDTO;
import com.example.aviatimes.DTO.ValidateDTO.RegisterResponseDTO;
import com.example.aviatimes.Models.ActivationToken;
import com.example.aviatimes.Models.Role;
import com.example.aviatimes.Models.User;
import com.example.aviatimes.Repositories.ActivationTokenRepository;
import com.example.aviatimes.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JWTUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    public void save(User user){
        userRepo.save(user);
//        try {
//            createActivationCode(user.getEmail());
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
    }
    public boolean existsByUserEmail(String email){
        return userRepo.findUserByEmail(email).isPresent();
    }

    public boolean existsByUserName(String name){
        return userRepo.findByUsername(name).isPresent();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user with username = " + username));
    }

    public ResponseEntity<?> changeProfile(Principal principal,  ProfileDTO profileDTO){
        User user = (User) loadUserByUsername(principal.getName());

        if (!Objects.equals(profileDTO.getEmail(), user.getEmail()) && !Objects.equals(profileDTO.getEmail(), "") && !existsByUserEmail(profileDTO.getEmail())){
            user.setEmail(profileDTO.getEmail());
            userRepo.save(user);
        }else if (!Objects.equals(profileDTO.getEmail(), user.getEmail()) && !Objects.equals(profileDTO.getEmail(), "")){
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Пользователь с таким email уже есть!"));
        }

        if (!Objects.equals(profileDTO.getUsername(), user.getUsername()) && !Objects.equals(profileDTO.getUsername(), "") && !existsByUserName(profileDTO.getUsername())){
            user.setUsername(profileDTO.getUsername());
            userRepo.save(user);
        }else if (!Objects.equals(profileDTO.getUsername(), user.getUsername()) && !Objects.equals(profileDTO.getUsername(), "")){
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Пользователь с таким именем уже есть!"));
        }

        if (profileDTO.getPassword().length() >= 5 && profileDTO.getOldPassword().length() >= 5 && Objects.equals(passwordEncoder.encode(profileDTO.getOldPassword()), user.getPassword())){
            user.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
            userRepo.save(user);
        }else if (profileDTO.getPassword().length() >= 5 && profileDTO.getOldPassword().length() >= 5){
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Пароль не изменен"));
        }

        return ResponseEntity.ok(new RegisterResponseDTO("Изменения сохранились"));
    }

    public User findUserByName(String name){
        return userRepo.findByUsername(name).get();
    }

    public void registerUser(RegisterRequestDTO registerRequestDTO){
        save(User.builder()
                .email(registerRequestDTO.getEmail())
                .username(registerRequestDTO.getUserName())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .authorities( Set.of(Role.ROLE_ADMIN))
                .active(false).build()
        );
    }

    public ResponseEntity<?> loginUser(UserDTO userDto){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        user.setEmail(userDto.getEmail());
        String jwt = jwtUtil.generateToken(user.getUsername());

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return  ResponseEntity.ok(new AuthController.JwtResponse(jwt, user.getId(),user.getEmail(), user.getUsername(), authorities));
    }

    public ResponseEntity<?> collectUserData(Principal user){
        User userObj=(User) loadUserByUsername(user.getName());

        return ResponseEntity.ok(
                UserDTO.builder()
                .userName(userObj.getUsername())
                .email(userObj.getEmail())
                .Roles(userObj.getAuthorities().toArray())
                .build()
        );
    }


    public void activateUser(String code) {
        User user = activationTokenRepository.findByToken(code).getUser();
        if (user == null) {
            return;
        }
        user.setActive(true);
        activationTokenRepository.deleteByToken(code);
        save(user);
    }

    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email).get();
    }

    public void createActivationCode(String userEmail) throws MessagingException {
        User user = findUserByEmail(userEmail);
        String token = UUID.randomUUID().toString();
        ActivationToken myToken = new ActivationToken(token, user, new Date());
        activationTokenRepository.save(myToken);

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = "Привет, " + user.getUsername() + "!" +
                    " для активации аккаунта перейдите <a href='http://localhost:8080/activate/" + token + "'>по ссылке для подтверждения почты</a>"
                    +"а затем продолжите логин <a href='http://localhost:3000/login/'>по ссылке</a>";
            emailService.sendSimpleMessage(user.getEmail(), message);
        }
    }
}