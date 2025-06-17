package com.examcell.resultgen.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.SemesterResultSummaryDto;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.YearResultSummaryDto;
import com.examcell.resultgen.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Security Context helpers and /me endpoints removed.
    // Student identified explicitly via rollNumber in the path.

    /**
     * GET /api/students/{rollNumber}/subjects
     * Retrieves subjects for a specific student's current semester.
     */
    @GetMapping("/{rollNumber}/subjects")
    public ResponseEntity<List<SubjectDto>> getStudentSubjects(
           @PathVariable String rollNumber) {
        // No authorization check needed here per requirements
        List<SubjectDto> subjects = studentService.getCurrentSubjects(rollNumber);
        return ResponseEntity.ok(subjects);
    }

    /**
     * GET /api/students/{rollNumber}/results
     * Retrieves all results for a specific student.
     */
    @GetMapping("/{rollNumber}/results")
    public ResponseEntity<List<ResultDto>> getAllResults(
            @PathVariable String rollNumber) {
        // No authorization check needed here per requirements
        List<ResultDto> results = studentService.getAllResults(rollNumber);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/students/{rollNumber}/results/{semester}
     * Retrieves results for a specific student and semester.
     */
    @GetMapping("/{rollNumber}/results/{semester}")
    public ResponseEntity<SemesterResultSummaryDto> getResultsBySemester(
            @PathVariable String rollNumber,
            @PathVariable int semester) {
        // No authorization check needed here per requirements
        SemesterResultSummaryDto resultSummary = studentService.getResultsBySemester(rollNumber, semester);
        return ResponseEntity.ok(resultSummary);
    }

    /**
     * GET /api/students/{rollNumber}/year-results/{yearNumber}
     * Retrieves results for a specific student and year (two semesters).
     */
    @GetMapping("/{rollNumber}/year-results/{yearNumber}")
    public ResponseEntity<YearResultSummaryDto> getResultsByYear(
            @PathVariable String rollNumber,
            @PathVariable int yearNumber) {
        YearResultSummaryDto yearResult = studentService.getResultsByYear(rollNumber, yearNumber);
        return ResponseEntity.ok(yearResult);
    }
} 