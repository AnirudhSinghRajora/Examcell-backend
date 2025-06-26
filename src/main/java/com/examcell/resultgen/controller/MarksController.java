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
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.service.MarksService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
@CrossOrigin
public class MarksController {

    private final MarksService marksService;

    /**
     * GET /api/marks
     * Retrieves all marks records with optional filters, pagination, and sorting.
     */
    @GetMapping
    public ResponseEntity<Page<MarkDTO>> getAllMarks(
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) UUID subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<MarkDTO> marks = marksService.getAllMarksByCriteria(studentId, subjectId, pageable);
        return ResponseEntity.ok(marks);
    }

    /**
     * GET /api/marks/{markId}
     * Retrieves a specific mark record by its ID.
     */
    @GetMapping("/{markId}")
    public ResponseEntity<MarkDTO> getMarkById(@PathVariable UUID markId) {
        MarkDTO mark = marksService.getMarkById(markId);
        return ResponseEntity.ok(mark);
    }

    /**
     * GET /api/marks/student/{studentId}
     * Retrieves all marks for a specific student.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<MarkDTO>> getMarksByStudentId(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "subject.name,asc") String[] sort) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<MarkDTO> marks = marksService.getMarksByStudentId(studentId, pageable);
        return ResponseEntity.ok(marks);
    }

    /**
     * POST /api/marks
     * Creates new mark records (batch or single).
     * This endpoint is suitable for admin to add marks for students.
     */
    @PostMapping
    public ResponseEntity<List<MarksRecord>> createNewMarks(@RequestBody List<MarkEntryDto> markEntries) {
        String adminEmployeeId = "ADMIN_SYSTEM";
        List<MarksRecord> createdMarks = marksService.batchEnterMarks(adminEmployeeId, markEntries);
        return new ResponseEntity<>(createdMarks, HttpStatus.CREATED);
    }

    /**
     * PUT /api/marks/{markId}
     * Updates an existing mark record.
     */
    @PutMapping("/{markId}")
    public ResponseEntity<MarkDTO> updateMark(
            @PathVariable UUID markId,
            @RequestBody MarkDTO markDTO) {
        String adminEmployeeId = "ADMIN_SYSTEM";
        MarkDTO updatedMark = marksService.updateMarkByAdmin(
                adminEmployeeId,
                markId,
                markDTO.getInternal1(),
                markDTO.getInternal2(),
                markDTO.getExternal()
        );
        return ResponseEntity.ok(updatedMark);
    }

    /**
     * DELETE /api/marks/{markId}
     * Deletes a specific mark record.
     */
    @DeleteMapping("/{markId}")
    public ResponseEntity<Void> deleteMark(@PathVariable UUID markId) {
        marksService.deleteMarkByAdmin(markId);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/marks/upload-excel
     * Uploads an Excel file containing marks for a given subject and semester.
     * Assumes this is an admin functionality.
     */
    @PostMapping("/upload-excel")
    public ResponseEntity<List<MarkDTO>> uploadMarksFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam UUID subjectId,
            @RequestParam String semester) {
        String adminEmployeeId = "ADMIN_SYSTEM";
        List<MarkDTO> uploadedMarks = marksService.uploadMarksFromExcel(adminEmployeeId, file, subjectId, semester);
        return ResponseEntity.ok(uploadedMarks);
    }

    /**
     * GET /api/marks/download-template
     * Downloads an Excel template for marks entry.
     */
    @GetMapping("/download-template")
    public ResponseEntity<Void> downloadExcelTemplateForMarks() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
} 