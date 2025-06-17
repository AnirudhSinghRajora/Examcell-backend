package com.examcell.resultgen.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.service.MarksService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
public class MarksController {

    private final MarksService marksService;

    /**
     * POST /api/marks
     * Batch entry of marks by a professor.
     * Requires professorEmployeeId as a request parameter for tracking.
     */
    @PostMapping
    public ResponseEntity<?> batchEnterMarks(
            @RequestParam String professorEmployeeId,
            @Valid @RequestBody List<MarkEntryDto> markEntries) {

        try {
            List<MarksRecord> savedRecords = marksService.batchEnterMarks(professorEmployeeId, markEntries);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Marks entered successfully", "count", savedRecords.size()));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * PUT /api/marks/{recordId}
     * Updates an existing mark entry.
     * Requires professorEmployeeId as a request parameter for tracking.
     */
    @PutMapping("/{recordId}")
    public ResponseEntity<MarksRecord> updateMark(
            @PathVariable UUID recordId,
            @RequestParam String professorEmployeeId,
            @RequestBody Map<String, Double> payload) {

        Double newMarksObtained = payload.get("marksObtained");
        if (newMarksObtained == null) {
            return ResponseEntity.badRequest().body(null);
        }

        MarksRecord updatedRecord = marksService.updateMark(professorEmployeeId, recordId, newMarksObtained);
        return ResponseEntity.ok(updatedRecord);
    }
} 