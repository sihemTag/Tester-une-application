package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class SessionControllerTest {

    @MockBean
    SessionService sessionService;

    @MockBean
    SessionMapper sessionMapper;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static Session session;
    private static Teacher teacher;
    private static User user;
    private static List<User> users = new ArrayList<>();
    private static List<Long>  usersId;
    private static SessionDto sessionDto;

    @BeforeAll
    static void beforeAll(){
        teacher = new Teacher(1L,"last name", "first name",LocalDateTime.now(), null);
        user = new User(1L, "email@test.com", "last name", "first name","password", true, LocalDateTime.now(), null);
        users.add(user);
        session = new Session(1L, "name", new Date(12/04/2025),"desc",teacher,users,LocalDateTime.now(),null);
        usersId = new ArrayList<>();
        usersId.add(1L);
        sessionDto = new SessionDto(1L,"name",new Date(12/04/2025), 1L,"desc",usersId,LocalDateTime.now(),null);
    }

    @Test
     void findByIdWhenSessionExists() throws Exception {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc.perform(get("/api/session/"+1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
     void findByIdWhenSessionNotFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/session/"+1L))
                .andExpect(status().isNotFound());
    }

    @Test
     void findByIdWhenParamInvalid() throws Exception {
        mockMvc.perform(get("/api/session/invalidParam"))
                .andExpect(status().isBadRequest());
    }

    @Test
     void createSessionTestOk() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
     void updateSessionTestOk() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(),any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(put("/api/session/"+1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void deleteSession_ShouldReturnOk_WhenUserExistsAndAuthorized() throws Exception {
        Long sessionId = 1L;
        when(sessionService.getById(sessionId)).thenReturn(session);

        mockMvc.perform(delete("/api/session/"+1L))
                .andExpect(status().isOk());

        verify(sessionService).delete(sessionId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/api/session/"+1L))
                .andExpect(status().isNotFound());

        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/invalidParam"))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).delete(anyLong());
    }

    @Test
     void participateTestOk() throws Exception {
        doNothing().when(sessionService).participate(1L, 2L);

        mockMvc.perform(post("/api/session/"+1L+"/participate/"+2L))
                .andExpect(status().isOk());

       verify(sessionService).participate(1L,2L);
    }

    @Test
     void participateTestThrowsBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/invalidParam/participate/invalidParam"))
                .andExpect(status().isBadRequest());
        verify(sessionService, never()).participate(anyLong(),anyLong());
    }

    @Test
     void noLongerParticipateTestOk() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        mockMvc.perform(delete("/api/session/"+1L+"/participate/"+2L))
                .andExpect(status().isOk());
    }

    @Test
     void noLongerParticipateTestThrowsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/invalidParam/participate/invalidParam"))
                .andExpect(status().isBadRequest());
        verify(sessionService, never()).noLongerParticipate(anyLong(),anyLong());
    }



}
