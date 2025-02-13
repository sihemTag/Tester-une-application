package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeacherControllerTest {

    @Mock
    TeacherService teacherService;

    @Mock
    TeacherMapper teacherMapper;

    @InjectMocks
    TeacherController teacherController;

    private static Teacher teacher;

    @BeforeAll
    static void beforeAll(){
        teacher = new Teacher(1L,"last name", "first name",LocalDateTime.now(), null);
    }


    @Test
    public void findByIdWhenUserExists(){
        TeacherDto teacherDto = new TeacherDto(1L,"last name", "first name",LocalDateTime.now(), null);
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto,response.getBody());
    }

    @Test
    public void findByIdWhenUserNotFound(){
        when(teacherService.findById(1L)).thenReturn(null);
        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdWhenParamInvalid(){
        ResponseEntity<?> response = teacherController.findById("invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findAllTestOk(){
        List<Teacher> teachers = new ArrayList<>();
        List<TeacherDto> teachersDTO = new ArrayList<>();
        teachers.add(teacher);
        TeacherDto teacherDto = new TeacherDto(1L,"last name", "first name",LocalDateTime.now(), null);
        teachersDTO.add(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDTO);

        ResponseEntity<?> response = teacherController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
