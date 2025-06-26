package com.examcell.resultgen.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.service.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@CrossOrigin
public class SubjectController {

    private final SubjectService subjectService;

    /**
     * POST /api/subjects
     * Creates a new subject.
     */
    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto subjectDto) {
        SubjectDto createdSubject = subjectService.createSubject(subjectDto);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    /**
     * GET /api/subjects/{id}
     * Retrieves a subject by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable UUID id) {
        SubjectDto subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    /**
     * GET /api/subjects
     * Retrieves all subjects with optional search, semester, and department filters, with pagination and sorting.
     */
    @GetMapping
    public ResponseEntity<Page<SubjectDto>> getAllSubjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<SubjectDto> subjects = subjectService.getAllSubjects(search, semester, department, pageable);
        return ResponseEntity.ok(subjects);
    }

    /**
     * PUT /api/subjects/{id}
     * Updates an existing subject.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable UUID id, @Valid @RequestBody SubjectDto subjectDto) {
        SubjectDto updatedSubject = subjectService.updateSubject(id, subjectDto);
        return ResponseEntity.ok(updatedSubject);
    }

    /**
     * DELETE /api/subjects/{id}
     * Deletes a subject by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable UUID id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/subjects/by-criteria
     * Retrieves subjects by course name, branch name, and semester.
     */
    @GetMapping("/by-criteria")
    public ResponseEntity<List<SubjectDto>> getSubjectsByCourseAndBranchAndSemester(
            @RequestParam String courseName,
            @RequestParam(required = false) String branchName,
            @RequestParam int semester) {
        List<SubjectDto> subjects = subjectService.getSubjectsByCourseAndBranchAndSemester(courseName, branchName, semester);
        return ResponseEntity.ok(subjects);
    }

    /**
     * GET /api/subjects/semester/{semester}
     * Returns a list of subjects for the given semester.
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<SubjectDto>> getSubjectsBySemester(@PathVariable Integer semester) {
        List<SubjectDto> subjects = subjectService.getSubjectsBySemester(semester);
        return ResponseEntity.ok(subjects);
    }
}