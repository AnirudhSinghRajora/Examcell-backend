package com.examcell.resultgen.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.BonafideRequestDTO;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;
import com.examcell.resultgen.service.BonafideService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/bonafide-requests")
@RequiredArgsConstructor
@CrossOrigin
public class BonafideRequestController {

    private final BonafideService bonafideService;

    /**
     * GET /api/admin/bonafide-requests
     * Retrieves all bonafide requests, with optional status filter and pagination.
     */
    @GetMapping
    public ResponseEntity<Page<BonafideRequestDTO>> getAllBonafideRequests(
            @RequestParam(required = false) BonafideRequestStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<BonafideRequestDTO> requests = bonafideService.getAllBonafideRequests(status, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/admin/bonafide-requests/{requestId}
     * Retrieves a specific bonafide request by ID.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<BonafideRequestDTO> getBonafideRequestById(@PathVariable UUID requestId) {
        BonafideRequestDTO request = bonafideService.getBonafideRequestById(requestId);
        return ResponseEntity.ok(request);
    }

    /**
     * POST /api/admin/bonafide-requests/{requestId}/approve
     * Approves a bonafide request.
     * Assumes adminId is passed in the request body or derived from security context.
     * For this demo, let's assume it's passed as a request param.
     */
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<BonafideRequestDTO> approveBonafideRequest(
            @PathVariable UUID requestId,
            @RequestParam UUID adminId) {
        BonafideRequestDTO approvedRequest = bonafideService.approveBonafideRequest(requestId, adminId);
        return ResponseEntity.ok(approvedRequest);
    }

    /**
     * POST /api/admin/bonafide-requests/{requestId}/reject
     * Rejects a bonafide request.
     * Assumes adminId is passed in the request body or derived from security context.
     * Remarks can be optional.
     */
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<BonafideRequestDTO> rejectBonafideRequest(
            @PathVariable UUID requestId,
            @RequestParam UUID adminId,
            @RequestParam(required = false) String remarks) {
        BonafideRequestDTO rejectedRequest = bonafideService.rejectBonafideRequest(requestId, adminId, remarks);
        return ResponseEntity.ok(rejectedRequest);
    }

    /**
     * DELETE /api/admin/bonafide-requests/{requestId}
     * Deletes a specific bonafide request by its ID.
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteBonafideRequest(@PathVariable UUID requestId) {
        bonafideService.deleteBonafideRequest(requestId); // New method
        return ResponseEntity.noContent().build();
    }
}
