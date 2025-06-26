package com.examcell.resultgen.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.BonafideRequestDTO;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.BonafideRequestMapper;
import com.examcell.resultgen.model.BonafideRequest;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;
import com.examcell.resultgen.model.Role;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.User;
import com.examcell.resultgen.repository.BonafideRequestRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.UserRepository;
import com.examcell.resultgen.service.BonafideService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BonafideServiceImpl implements BonafideService {

    private final BonafideRequestRepository bonafideRequestRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final BonafideRequestMapper bonafideRequestMapper;

    @Override
    @Transactional
    public BonafideRequestDTO submitBonafideRequest(UUID studentId, String reason) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

        BonafideRequest newRequest = new BonafideRequest(student, reason);
        BonafideRequest savedRequest = bonafideRequestRepository.save(newRequest);
        return bonafideRequestMapper.bonafideRequestToBonafideRequestDTO(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BonafideRequestDTO> getStudentBonafideRequests(UUID studentId, Pageable pageable) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

        Page<BonafideRequest> requests = bonafideRequestRepository.findByStudentId(studentId, pageable);
        return requests.map(bonafideRequestMapper::bonafideRequestToBonafideRequestDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BonafideRequestDTO getStudentBonafideRequestById(UUID studentId, UUID requestId) {
        BonafideRequest request = bonafideRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Bonafide request not found with ID: " + requestId));

        if (!request.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Bonafide request does not belong to the specified student.");
        }
        return bonafideRequestMapper.bonafideRequestToBonafideRequestDTO(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BonafideRequestDTO> getAllBonafideRequests(BonafideRequestStatus status, Pageable pageable) {
        Page<BonafideRequest> requests;
        if (status != null) {
            requests = bonafideRequestRepository.findByStatus(status, pageable);
        } else {
            requests = bonafideRequestRepository.findAll(pageable);
        }
        return requests.map(bonafideRequestMapper::bonafideRequestToBonafideRequestDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BonafideRequestDTO getBonafideRequestById(UUID requestId) {
        BonafideRequest request = bonafideRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Bonafide request not found with ID: " + requestId));
        return bonafideRequestMapper.bonafideRequestToBonafideRequestDTO(request);
    }

    @Override
    @Transactional
    public BonafideRequestDTO approveBonafideRequest(UUID requestId, UUID adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with ID: " + adminId));
        // Basic check for admin role (can be enhanced with Spring Security roles)
        if (admin.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("User is not authorized to approve requests.");
        }

        BonafideRequest request = bonafideRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Bonafide request not found with ID: " + requestId));

        if (request.getStatus() != BonafideRequestStatus.PENDING) {
            throw new IllegalArgumentException("Request is not in PENDING status and cannot be approved.");
        }

        request.setStatus(BonafideRequestStatus.APPROVED);
        request.setApprovedBy(admin);
        request.setApprovedAt(Instant.now());

        BonafideRequest updatedRequest = bonafideRequestRepository.save(request);
        return bonafideRequestMapper.bonafideRequestToBonafideRequestDTO(updatedRequest);
    }

    @Override
    @Transactional
    public BonafideRequestDTO rejectBonafideRequest(UUID requestId, UUID adminId, String remarks) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with ID: " + adminId));
        // Basic check for admin role
        if (admin.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("User is not authorized to reject requests.");
        }

        BonafideRequest request = bonafideRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Bonafide request not found with ID: " + requestId));

        if (request.getStatus() != BonafideRequestStatus.PENDING) {
            throw new IllegalArgumentException("Request is not in PENDING status and cannot be rejected.");
        }

        request.setStatus(BonafideRequestStatus.REJECTED);
        request.setApprovedBy(admin);
        request.setApprovedAt(Instant.now());
        request.setRejectionReason(remarks);

        BonafideRequest updatedRequest = bonafideRequestRepository.save(request);
        return bonafideRequestMapper.bonafideRequestToBonafideRequestDTO(updatedRequest);
    }

    @Override
    @Transactional
    public void deleteBonafideRequest(UUID requestId) {
        if (!bonafideRequestRepository.existsById(requestId)) {
            throw new NotFoundException("Bonafide request not found with ID: " + requestId);
        }
        bonafideRequestRepository.deleteById(requestId);
    }
} 