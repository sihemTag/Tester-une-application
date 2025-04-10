package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByIdTestOk(){
        User user = new User(1L, "email@test.com", "last name", "first name","password", true, LocalDateTime.now(), null);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(userService.findById(1L), user);
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserByIdUserNotFound(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(userService.findById(1L));
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserByIdTestOk(){
        doNothing().when(userRepository).deleteById(1L);
       userService.delete(1L);
       verify(userRepository, times(1)).deleteById(1L);
       verifyNoMoreInteractions(userRepository);
    }

}
