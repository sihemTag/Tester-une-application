package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
 class TeacherControllerTest {

    @MockBean
    TeacherService teacherService;

    @MockBean
    TeacherMapper teacherMapper;

    @Autowired
    private MockMvc mockMvc;

    private static Teacher teacher;

    @BeforeAll
    static void beforeAll(){
        teacher = new Teacher(1L,"last name", "first name",LocalDateTime.now(), null);
    }


    @Test
     void findByIdWhenUserExists() throws Exception {
        TeacherDto teacherDto = new TeacherDto(1L,"last name", "first name",LocalDateTime.now(), null);
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

       mockMvc.perform(get("/api/teacher/"+1L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").exists());
    }

    @Test
     void findByIdWhenUserNotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/teacher/"+1L))
                .andExpect(status().isNotFound());
    }

    @Test
     void findByIdWhenParamInvalid() throws Exception {
        mockMvc.perform(get("/api/teacher/invalidParam"))
                .andExpect(status().isBadRequest());
        verify(teacherService, never()).findById(anyLong());
    }

    @Test
     void findAllTestOk() throws Exception {
        List<Teacher> teachers = new ArrayList<>();
        List<TeacherDto> teachersDTO = new ArrayList<>();
        teachers.add(teacher);
        TeacherDto teacherDto = new TeacherDto(1L,"last name", "first name",LocalDateTime.now(), null);
        teachersDTO.add(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDTO);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }
}
