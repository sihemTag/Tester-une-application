package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SessionServiceTest {

    private static Teacher teacher;
    private static User user;
    private static Session session;
    private static List<User> users;

    @BeforeAll
    static void beforeAll(){
        teacher = new Teacher(1L,"last name", "first name", LocalDateTime.now(), null);
        user = new User(1L, "email@test.com", "last name", "first name","password", true, LocalDateTime.now(), null);
        users = new ArrayList<>();
        users.add(user);
        session =  new Session(1L, "name", new Date("16/03/2025"), "description", teacher,users, LocalDateTime.now(), null);
    }

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void createSessionTestOk(){
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        assertEquals(sessionService.create(session), session);
    }

    @Test
    public void deleteTestOk(){
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findAllTestOk(){
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        when(sessionRepository.findAll()).thenReturn(sessions);
        assertEquals(sessionService.findAll(), sessions);
    }

    @Test
    public void getByIdTestOk(){
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        assertEquals(sessionService.getById(1L), session);
    }

    @Test
    public void getByIdUserNotFound(){
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(sessionService.getById(1L));
    }

    @Test
    public void updateTestOk(){
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session updatedSession = sessionService.update(2L, session);
        assertNotNull(updatedSession);
        assertEquals(session, updatedSession);

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public  void participateDidntAlreadyParticipate(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        sessionService.participate(1L,2L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public  void participateThrowsException(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L,1L));
        verify(sessionRepository, never()).save(session);
    }

    @Test
    public  void noLongerParticipate(){
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        sessionService.noLongerParticipate(1L,1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public  void noLongerParticipateThrowsException(){
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L,2L));
        verify(sessionRepository, never()).save(session);
    }

}
