package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;
    private static User user;

    @BeforeAll
    static void beforeAll(){
        user = new User(1L, "email@test.com", "last name", "first name","password", true, LocalDateTime.now(), null);
    }

    @Test
    public void findByIdWhenUserExists(){
        UserDto userDto = new UserDto(1L, "email@test.com", "last name", "first name", true,"password", LocalDateTime.now(), null);
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto,response.getBody());
    }

    @Test
    public void findByIdWhenUserNotFound(){
        when(userService.findById(1L)).thenReturn(null);
        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdWhenParamInvalid(){
        ResponseEntity<?> response = userController.findById("invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnOk_WhenUserExistsAndAuthorized() {
        Long userId = 1L;

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());

        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.findById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnUnauthorized_WhenUserIsNotTheOwner() {
        Long userId = 1L;

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("autre@test.com");

        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenIdIsInvalid() {
        ResponseEntity<?> response = userController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).delete(anyLong());
    }


}
