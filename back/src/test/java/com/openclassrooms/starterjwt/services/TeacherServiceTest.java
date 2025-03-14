package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void findByIdTestOk(){
        Teacher teacher = new Teacher(1L,"last name", "first name", LocalDateTime.now(), null);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        assertEquals(teacherService.findById(1L), teacher);
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
     void findByIdUserNotFound(){
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(teacherService.findById(1L));
    }

    @Test
    void findAllTestOk(){
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        Teacher teacher1 = new Teacher(1L,"last name", "first name", LocalDateTime.now(), null);
        Teacher teacher2 = new Teacher(2L,"last name", "first name", LocalDateTime.now(), null);
        teachers.add(teacher1); teachers.add(teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);
        assertEquals(teacherService.findAll(), teachers);
        verify(teacherRepository, times(1)).findAll();
        verifyNoMoreInteractions(teacherRepository);
    }
}
