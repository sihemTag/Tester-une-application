package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SessionControllerTest {

    @Mock
    SessionService sessionService;

    @Mock
    SessionMapper sessionMapper;

    @InjectMocks
    SessionController sessionController;

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
    public void findByIdWhenUserExists(){
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto,response.getBody());
    }

    @Test
    public void findByIdWhenUserNotFound(){
        when(sessionService.getById(1L)).thenReturn(null);
        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdWhenParamInvalid(){
        ResponseEntity<?> response = sessionController.findById("invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createTestOk(){
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void updateTestOk(){
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(),any(Session.class))).thenReturn(session);

        ResponseEntity<?> response = sessionController.update("1",sessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteSession_ShouldReturnOk_WhenUserExistsAndAuthorized() {
        Long sessionId = 1L;
        when(sessionService.getById(sessionId)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save(String.valueOf(sessionId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(sessionId);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(sessionService.getById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturnBadRequest_WhenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void participateTestOk(){
        ResponseEntity<?> response = sessionController.participate("1", "2");
        verify(sessionService, times(1)).participate(1L,2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void participateTestThrowsBadRequest(){
        ResponseEntity<?> response = sessionController.participate("invalid","invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).participate(anyLong(),anyLong());
    }

    @Test
    public void noLongerParticipateTestOk(){
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");
        verify(sessionService, times(1)).noLongerParticipate(1L,1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void noLongerParticipateTestThrowsBadRequest(){
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid","invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).noLongerParticipate(anyLong(),anyLong());
    }



}
