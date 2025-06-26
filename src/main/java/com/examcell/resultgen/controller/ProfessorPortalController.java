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
import org.springframework.web.multipart.MultipartFile;

import com.examcell.resultgen.dto.MarkDTO;
import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.dto.TeacherDashboardDTO;
import com.examcell.resultgen.dto.TeacherMarkDTO;
import com.examcell.resultgen.model.Query;
import com.examcell.resultgen.service.MarksService;
import com.examcell.resultgen.service.ProfessorService;
import com.examcell.resultgen.service.QueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@CrossOrigin
public class ProfessorPortalController {

    private final ProfessorService professorService;
    private final MarksService marksService;
    private final QueryService queryService;

    // 1. Get Teacher Dashboard
    @GetMapping("/dashboard/{teacherId}")
    public ResponseEntity<TeacherDashboardDTO> getTeacherDashboard(@PathVariable UUID teacherId) {
        TeacherDashboardDTO dashboard = professorService.getTeacherDashboard(teacherId);
        return ResponseEntity.ok(dashboard);
    }

    // 2. Get Teacher Queries
    @GetMapping("/{teacherId}/queries")
    public ResponseEntity<Page<QueryDTO>> getTeacherQueries(
            @PathVariable UUID teacherId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        
        // Convert status string to enum if present
        Query.QueryStatus queryStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                queryStatus = Query.QueryStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // Or handle as invalid status
            }
        }
        Page<QueryDTO> queries = queryService.getQueriesForTeacher(teacherId, queryStatus, pageable);
        return ResponseEntity.ok(queries);
    }

    // 3. Respond to a Query
    @PostMapping("/{teacherId}/queries/{queryId}/respond")
    public ResponseEntity<QueryDTO> respondToQuery(
            @PathVariable UUID teacherId,
            @PathVariable UUID queryId,
            @RequestParam String response) {
        QueryDTO updatedQuery = queryService.respondToQueryByTeacher(teacherId, queryId, response);
        return ResponseEntity.ok(updatedQuery);
    }

    // 4. Submit Query to Admin
    @PostMapping("/{teacherId}/queries/admin")
    public ResponseEntity<QueryDTO> submitQueryToAdmin(
            @PathVariable UUID teacherId,
            @Valid @RequestBody QueryDTO queryDTO) {
        QueryDTO createdQuery = queryService.submitQueryToAdmin(teacherId, queryDTO);
        return new ResponseEntity<>(createdQuery, HttpStatus.CREATED);
    }

    // 5. Get Subject Marks for Teacher
    @GetMapping("/{teacherId}/subjects/{subjectId}/marks")
    public ResponseEntity<List<TeacherMarkDTO>> getSubjectMarksForTeacher(
            @PathVariable UUID teacherId,
            @PathVariable UUID subjectId) {
        List<TeacherMarkDTO> marks = marksService.getTeacherMarksByTeacherAndSubject(teacherId, subjectId);
        return ResponseEntity.ok(marks);
    }

    // 6. Create Mark
    @PostMapping("/{teacherId}/marks")
    public ResponseEntity<MarkDTO> createMark(
            @PathVariable UUID teacherId,
            @Valid @RequestBody MarkDTO markDTO) {
        MarkEntryDto markEntryDto = new MarkEntryDto(
            UUID.fromString(markDTO.getStudentId().toString()),
            UUID.fromString(markDTO.getSubjectId().toString()),
            markDTO.getInternal1(),
            markDTO.getInternal2(),
            markDTO.getExternal()
        );
        MarkDTO createdMark = marksService.createMark(teacherId, markEntryDto);
        return new ResponseEntity<>(createdMark, HttpStatus.CREATED);
    }

    // 7. Update Mark
    @PutMapping("/{teacherId}/marks/{markId}")
    public ResponseEntity<MarkDTO> updateMark(
            @PathVariable UUID teacherId,
            @PathVariable UUID markId,
            @RequestBody MarkDTO markDTO) {
        MarkDTO updatedMark = marksService.updateMarkByTeacher(
                teacherId,
                markId,
                markDTO.getInternal1(),
                markDTO.getInternal2(),
                markDTO.getExternal()
        );
        return ResponseEntity.ok(updatedMark);
    }

    // 8. Delete Mark
    @DeleteMapping("/{teacherId}/marks/{markId}")
    public ResponseEntity<Void> deleteMark(
            @PathVariable UUID teacherId,
            @PathVariable UUID markId) {
        marksService.deleteMarkByTeacher(teacherId, markId);
        return ResponseEntity.noContent().build();
    }

    // 9. Upload Marks from Excel
    @PostMapping("/{teacherId}/marks/upload")
    public ResponseEntity<List<MarkDTO>> uploadMarksFromExcel(
            @PathVariable UUID teacherId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("subjectId") UUID subjectId,
            @RequestParam("semester") String semester) {
        List<MarkDTO> uploadedMarks = marksService.uploadMarksFromExcel(teacherId, file, subjectId, semester);
        return ResponseEntity.ok(uploadedMarks);
    }
}