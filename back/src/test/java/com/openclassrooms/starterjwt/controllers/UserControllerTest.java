package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    private static User user;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll(){
        user = new User(1L, "email@test.com", "last name", "first name","password", true, LocalDateTime.now(), null);
    }

    @Test
    public void findByIdWhenUserExists() throws Exception {
        UserDto userDto = new UserDto(1L, "email@test.com", "last name", "first name", true,"password", LocalDateTime.now(), null);
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/"+1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void findByIdWhenUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/"+1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdWhenParamInvalid() throws Exception {
        mockMvc.perform(get("/api/user/invalidParam"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldReturnOk_WhenUserExistsAndAuthorized() throws Exception {
        Long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/user/"+1L))
                .andExpect(status().isOk());

        verify(userService).delete(userId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/api/user/"+1L))
                .andExpect(status().isNotFound());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnUnauthorized_WhenUserIsNotTheOwner() throws Exception {
        Long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn("autre@test.com");
        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/user/"+1L))
                .andExpect(status().isUnauthorized());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/invalidParam"))
                .andExpect(status().isBadRequest());
        verify(userService, never()).delete(anyLong());
    }
}
