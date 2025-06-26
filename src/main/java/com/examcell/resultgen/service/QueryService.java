package com.examcell.resultgen.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.model.Query;

public interface QueryService {

    Page<QueryDTO> getQueriesForTeacher(UUID teacherId, Query.QueryStatus status, Pageable pageable);
    QueryDTO respondToQueryByTeacher(UUID teacherId, UUID queryId, String response);
    QueryDTO submitQueryToAdmin(UUID teacherId, QueryDTO queryDTO);

    // Student-facing query methods
    QueryDTO createQuery(UUID studentId, QueryDTO queryDTO); // Student submits a new query
    Page<QueryDTO> getQueriesByStudentId(UUID studentId, Pageable pageable); // Get all queries by a student
    QueryDTO getQueryByStudentIdAndQueryId(UUID studentId, UUID queryId); // Get a specific query by a student

    // Admin/Centralized query methods
    Page<QueryDTO> findAllQueriesByCriteria(String search, Query.QueryStatus status, Pageable pageable);
    QueryDTO getQueryById(UUID queryId);
    QueryDTO updateQueryStatus(UUID queryId, Query.QueryStatus newStatus);
    void deleteQuery(UUID queryId);

    // Admin can respond to a query
    QueryDTO respondToQueryByAdmin(UUID queryId, String response, String respondedBy);
}
