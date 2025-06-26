package com.examcell.resultgen.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.examcell.resultgen.dto.BonafideRequestDTO;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;

public interface BonafideService {

    // Student-facing methods
    BonafideRequestDTO submitBonafideRequest(UUID studentId, String reason);
    Page<BonafideRequestDTO> getStudentBonafideRequests(UUID studentId, Pageable pageable);
    BonafideRequestDTO getStudentBonafideRequestById(UUID studentId, UUID requestId);

    // Admin-facing methods
    Page<BonafideRequestDTO> getAllBonafideRequests(BonafideRequestStatus status, Pageable pageable);
    BonafideRequestDTO getBonafideRequestById(UUID requestId);
    BonafideRequestDTO approveBonafideRequest(UUID requestId, UUID adminId);
    BonafideRequestDTO rejectBonafideRequest(UUID requestId, UUID adminId, String remarks);
    void deleteBonafideRequest(UUID requestId);
} 