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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.BonafideRequestDTO;
import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.SemesterResultSummaryDto;
import com.examcell.resultgen.dto.StudentDashboardDTO;
import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.dto.YearResultSummaryDto;
import com.examcell.resultgen.service.BonafideService;
import com.examcell.resultgen.service.QueryService;
import com.examcell.resultgen.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student") // Student-facing endpoints
@RequiredArgsConstructor
@CrossOrigin
public class StudentPortalController {

    private final StudentService studentService;
    private final QueryService queryService;
    private final BonafideService bonafideService;

    // In a real application, studentId would be extracted from JWT/SecurityContext
    // For now, we'll assume it's passed or a placeholder. For the demo, use a fixed student ID or pass via path.
    // Or for the purpose of this demo, we can just use a @PathVariable for studentId for all student-specific endpoints.

    /**
     * GET /api/student/{studentId}/dashboard
     * Retrieves dashboard summary for the authenticated student.
     */
    @GetMapping("/{studentId}/dashboard")
    public ResponseEntity<StudentDashboardDTO> getStudentDashboard(@PathVariable UUID studentId) {
        System.out.println("DEBUG: getStudentDashboard called with studentId: " + studentId);
        try {
            StudentDashboardDTO dashboard = studentService.getStudentDashboard(studentId);
            System.out.println("DEBUG: Dashboard data retrieved successfully");
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            System.err.println("DEBUG: Error in getStudentDashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * GET /api/student/{studentId}/subjects
     * Retrieves subjects for a specific student's current semester.
     * Assuming rollNumber can be derived from studentId or is managed internally.
     * For simplicity, let's keep using rollNumber for now as it's in StudentService methods.
     */
    @GetMapping("/{studentId}/subjects")
    public ResponseEntity<List<SubjectDto>> getStudentSubjects(
            @PathVariable UUID studentId) {
        // Need to get roll number from studentId, or pass roll number directly.
        // For now, let's assume we can get student by ID and then its roll number.
        String rollNumber = studentService.getStudentById(studentId).getRollNumber();
        List<SubjectDto> subjects = studentService.getCurrentSubjects(rollNumber);
        return ResponseEntity.ok(subjects);
    }

    /**
     * GET /api/student/{studentId}/results
     * Retrieves all results for a specific student.
     */
    @GetMapping("/{studentId}/results")
    public ResponseEntity<List<ResultDto>> getAllResults(
            @PathVariable UUID studentId) {
        String rollNumber = studentService.getStudentById(studentId).getRollNumber();
        List<ResultDto> results = studentService.getAllResults(rollNumber);
        System.out.println("DEBUG: getAllResults called with studentId: " + studentId + ", rollNumber: " + rollNumber);
        System.out.println("DEBUG: Number of results returned: " + results.size());
        if (!results.isEmpty()) {
            System.out.println("DEBUG: First result: " + results.get(0));
        }
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/student/{studentId}/results/{semester}
     * Retrieves results for a specific student and semester.
     */
    @GetMapping("/{studentId}/results/{semester}")
    public ResponseEntity<SemesterResultSummaryDto> getResultsBySemester(
            @PathVariable UUID studentId,
            @PathVariable int semester) {
        String rollNumber = studentService.getStudentById(studentId).getRollNumber();
        SemesterResultSummaryDto resultSummary = studentService.getResultsBySemester(rollNumber, semester);
        return ResponseEntity.ok(resultSummary);
    }

    /**
     * GET /api/student/{studentId}/year-results/{yearNumber}
     * Retrieves results for a specific student and year (two semesters).
     */
    @GetMapping("/{studentId}/year-results/{yearNumber}")
    public ResponseEntity<YearResultSummaryDto> getResultsByYear(
            @PathVariable UUID studentId,
            @PathVariable int yearNumber) {
        String rollNumber = studentService.getStudentById(studentId).getRollNumber();
        YearResultSummaryDto yearResult = studentService.getResultsByYear(rollNumber, yearNumber);
        return ResponseEntity.ok(yearResult);
    }

    // Query Endpoints for Student Portal
    /**
     * POST /api/student/{studentId}/queries
     * Allows a student to submit a new query.
     */
    @PostMapping("/{studentId}/queries")
    public ResponseEntity<QueryDTO> submitQuery(
            @PathVariable UUID studentId,
            @Valid @RequestBody QueryDTO queryDTO) {
        QueryDTO createdQuery = queryService.createQuery(studentId, queryDTO);
        return new ResponseEntity<>(createdQuery, HttpStatus.CREATED);
    }

    /**
     * GET /api/student/{studentId}/queries
     * Retrieves all queries submitted by a specific student.
     */
    @GetMapping("/{studentId}/queries")
    public ResponseEntity<Page<QueryDTO>> getStudentQueries(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<QueryDTO> queries = queryService.getQueriesByStudentId(studentId, pageable);
        return ResponseEntity.ok(queries);
    }

    /**
     * GET /api/student/{studentId}/queries/{queryId}
     * Retrieves a specific query submitted by the student.
     */
    @GetMapping("/{studentId}/queries/{queryId}")
    public ResponseEntity<QueryDTO> getStudentQueryById(
            @PathVariable UUID studentId,
            @PathVariable UUID queryId) {
        QueryDTO query = queryService.getQueryByStudentIdAndQueryId(studentId, queryId);
        return ResponseEntity.ok(query);
    }

    // Bonafide Request Endpoints for Student Portal
    /**
     * POST /api/student/{studentId}/bonafide-requests
     * Allows a student to submit a bonafide certificate request.
     */
    @PostMapping("/{studentId}/bonafide-requests")
    public ResponseEntity<BonafideRequestDTO> submitBonafideRequest(
            @PathVariable UUID studentId,
            @Valid @RequestBody BonafideRequestDTO.SubmitRequest request) {
        // Convert the request to a reason string
        String reason = request.getPurpose();
        if ("Other".equals(request.getPurpose()) && request.getCustomPurpose() != null) {
            reason = request.getCustomPurpose();
        }
        if (request.getAdditionalInfo() != null && !request.getAdditionalInfo().trim().isEmpty()) {
            reason += " - " + request.getAdditionalInfo();
        }
        
        BonafideRequestDTO createdRequest = bonafideService.submitBonafideRequest(studentId, reason);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * GET /api/student/{studentId}/bonafide-requests
     * Retrieves all bonafide requests submitted by a specific student.
     */
    @GetMapping("/{studentId}/bonafide-requests")
    public ResponseEntity<Page<BonafideRequestDTO>> getStudentBonafideRequests(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestedAt,desc") String[] sort) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<BonafideRequestDTO> requests = bonafideService.getStudentBonafideRequests(studentId, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/student/{studentId}/bonafide-requests/{requestId}
     * Retrieves a specific bonafide request submitted by the student.
     */
    @GetMapping("/{studentId}/bonafide-requests/{requestId}")
    public ResponseEntity<BonafideRequestDTO> getStudentBonafideRequestById(
            @PathVariable UUID studentId,
            @PathVariable UUID requestId) {
        BonafideRequestDTO request = bonafideService.getStudentBonafideRequestById(studentId, requestId);
        return ResponseEntity.ok(request);
    }
} 