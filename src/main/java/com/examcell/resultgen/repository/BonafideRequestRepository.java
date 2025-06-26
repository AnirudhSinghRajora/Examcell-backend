package com.examcell.resultgen.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.BonafideRequest;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;

@Repository
public interface BonafideRequestRepository extends JpaRepository<BonafideRequest, UUID> {

    @EntityGraph(attributePaths = {"student", "approvedBy"})
    List<BonafideRequest> findByStudentId(UUID studentId);
    @EntityGraph(attributePaths = {"student", "approvedBy"})
    Page<BonafideRequest> findByStudentId(UUID studentId, Pageable pageable);
    @EntityGraph(attributePaths = {"student", "approvedBy"})
    Page<BonafideRequest> findByStatus(BonafideRequestStatus status, Pageable pageable);
    @EntityGraph(attributePaths = {"student", "approvedBy"})
    Page<BonafideRequest> findByStudentIdAndStatus(UUID studentId, BonafideRequestStatus status, Pageable pageable);
    long countByStatus(BonafideRequestStatus status);
    long countByStudentIdAndStatus(UUID studentId, BonafideRequestStatus status);

    @Override
    @EntityGraph(attributePaths = {"student", "approvedBy"})
    Page<BonafideRequest> findAll(Pageable pageable);
} 