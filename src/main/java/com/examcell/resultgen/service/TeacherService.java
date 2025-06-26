package com.examcell.resultgen.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.examcell.resultgen.dto.TeacherDTO;

public interface TeacherService {
    Page<TeacherDTO> getAllTeachers(String search, String department, Pageable pageable);
    TeacherDTO getTeacherById(UUID id);
    TeacherDTO getTeacherByEmployeeId(String employeeId);
    TeacherDTO createTeacher(TeacherDTO teacherDTO);
    TeacherDTO updateTeacher(UUID id, TeacherDTO teacherDTO);
    void deleteTeacher(UUID id);
} 