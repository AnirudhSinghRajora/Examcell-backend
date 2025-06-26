package com.examcell.resultgen.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.examcell.resultgen.model.Query;
import com.examcell.resultgen.model.Query.QueryStatus;

@Repository
public interface QueryRepository extends JpaRepository<Query, UUID> {

    List<Query> findByStudentId(UUID studentId);
    Page<Query> findByStudentId(UUID studentId, Pageable pageable);
    List<Query> findByTeacherId(UUID teacherId);
    Page<Query> findByTeacherId(UUID teacherId, Pageable pageable);

    Page<Query> findByStatus(QueryStatus status, Pageable pageable);
    long countByStatus(QueryStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT q FROM Query q WHERE " +
           "(:search IS NULL OR LOWER(q.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(q.queryText) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR q.status = :status)")
    Page<Query> findAllBySearchAndStatus(
            @Param("search") String search,
            @Param("status") QueryStatus status,
            Pageable pageable);

    Page<Query> findByStudentIdAndStatus(UUID studentId, QueryStatus status, Pageable pageable);
    Page<Query> findByTeacherIdAndStatus(UUID teacherId, QueryStatus status, Pageable pageable);

    long countByStudentIdAndStatus(UUID studentId, QueryStatus status);
    long countByTeacherIdAndStatus(UUID teacherId, QueryStatus status);
} 