package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static User user;

    @BeforeAll
    static void beforeAll(){
        user = new User(1L, "email@test.com", "last name", "first name", "password", true, LocalDateTime.now(), null);
    }

    @Test
    void shouldAuthenticateUserAndReturnJwt() throws Exception {
        LoginRequest loginRequest = new LoginRequest("email@test.com", "password");
        //Authentication authentication = new UsernamePasswordAuthenticationToken("email@test.com", "password");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L,"email@test.com","first name","last name",true,"password");

        // Créer une authentification simulée
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest))) // Convertir l'objet en JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("email@test.com"))
                .andExpect(jsonPath("$.firstName").value("first name"))
                .andExpect(jsonPath("$.lastName").value("last name"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        SignupRequest signUpRequest = new SignupRequest("newuser@example.com", "firstname", "lastname", "password123");

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User("newuser@example.com", "firstname", "lastname", "encodedPassword", true));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }



}
