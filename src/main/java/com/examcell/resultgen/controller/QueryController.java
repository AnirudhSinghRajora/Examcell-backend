package com.examcell.resultgen.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.model.Query.QueryStatus;
import com.examcell.resultgen.service.QueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/queries") // Centralized admin endpoints for query management
@RequiredArgsConstructor
@CrossOrigin
public class QueryController {

    private final QueryService queryService;

    /**
     * GET /api/queries
     * Retrieves all queries with optional search and status filters, with pagination and sorting.
     */
    @GetMapping
    public ResponseEntity<Page<QueryDTO>> getAllQueries(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        QueryStatus queryStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                queryStatus = QueryStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // Or handle as invalid status
            }
        }

        Page<QueryDTO> queries = queryService.findAllQueriesByCriteria(search, queryStatus, pageable);
        return ResponseEntity.ok(queries);
    }

    /**
     * GET /api/queries/{queryId}
     * Retrieves a specific query by its ID.
     */
    @GetMapping("/{queryId}")
    public ResponseEntity<QueryDTO> getQueryById(@PathVariable UUID queryId) {
        QueryDTO query = queryService.getQueryById(queryId);
        return ResponseEntity.ok(query);
    }

    /**
     * PATCH /api/queries/{queryId}/status
     * Updates the status of a specific query.
     */
    @PatchMapping("/{queryId}/status")
    public ResponseEntity<QueryDTO> updateQueryStatus(
            @PathVariable UUID queryId,
            @RequestParam String status) {
        try {
            QueryStatus newStatus = QueryStatus.valueOf(status.toUpperCase());
            QueryDTO updatedQuery = queryService.updateQueryStatus(queryId, newStatus);
            return ResponseEntity.ok(updatedQuery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/queries/{queryId}
     * Deletes a specific query by its ID.
     */
    @DeleteMapping("/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable UUID queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/queries/{queryId}/respond
     * Allows admin to respond to a query.
     */
    @PostMapping("/{queryId}/respond")
    public ResponseEntity<QueryDTO> respondToQuery(
            @PathVariable UUID queryId,
            @RequestParam String response,
            @RequestParam String respondedBy) {
        // For now, just update the response and status to RESOLVED
        QueryDTO updatedQuery = queryService.respondToQueryByAdmin(queryId, response, respondedBy);
        return ResponseEntity.ok(updatedQuery);
    }
} 