package com.examcell.resultgen.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api") // Base path
@RequiredArgsConstructor
@CrossOrigin
public class ProfessorController {

    private final ProfessorService professorService;

    /**
     * GET /api/professors/{employeeId}/subjects
     * Retrieves subjects assigned to a specific professor.
     */
    @GetMapping("/professors/{employeeId}/subjects")
    public ResponseEntity<List<SubjectDto>> getProfessorSubjects(
            @PathVariable String employeeId) {
        // No authorization check needed here per requirements
        List<SubjectDto> subjects = professorService.getAssignedSubjects(employeeId);
        return ResponseEntity.ok(subjects);
    }

    /**
     * GET /api/subjects/{subjectId}/students
     * Retrieves students enrolled in a specific subject.
     */
    @GetMapping("/subjects/{subjectId}/students")
    public ResponseEntity<List<StudentDto>> getStudentsInSubject(
            @PathVariable UUID subjectId) {
         // No authorization check needed here per requirements
         // Service layer just fetches students by subject
        List<StudentDto> students = professorService.getStudentsBySubject(subjectId);
        return ResponseEntity.ok(students);
    }
} 