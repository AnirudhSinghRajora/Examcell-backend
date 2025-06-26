package com.examcell.resultgen.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.QueryMapper;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Query;
import com.examcell.resultgen.model.Query.QueryStatus;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.QueryRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.service.QueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private final QueryRepository queryRepository;
    private final QueryMapper queryMapper;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<QueryDTO> getQueriesForTeacher(UUID teacherId, QueryStatus status, Pageable pageable) {
        professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));
        
        Page<Query> queries;
        if (status != null) {
            queries = queryRepository.findByTeacherIdAndStatus(teacherId, status, pageable);
        } else {
            queries = queryRepository.findByTeacherId(teacherId, pageable); // Use the new Pageable method
        }
        return queries.map(queryMapper::queryToQueryDTO);
    }

    @Override
    @Transactional
    public QueryDTO respondToQueryByTeacher(UUID teacherId, UUID queryId, String response) {
        Professor teacher = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new NotFoundException("Query not found with ID: " + queryId));

        if (!query.getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("Professor is not assigned to this query.");
        }

        query.setResponse(response);
        query.setStatus(QueryStatus.RESOLVED);
        query.setCreatedAt(Instant.now()); // Update timestamp as well

        Query updatedQuery = queryRepository.save(query);
        return queryMapper.queryToQueryDTO(updatedQuery);
    }

    @Override
    @Transactional
    public QueryDTO submitQueryToAdmin(UUID teacherId, QueryDTO queryDTO) {
        Professor teacher = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        Query newQuery = new Query(teacher, queryDTO.getSubject(), queryDTO.getQueryText());
        newQuery.setStatus(QueryStatus.OPEN); // Admin queries are always open initially
        newQuery.setCreatedAt(Instant.now());

        Query savedQuery = queryRepository.save(newQuery);
        return queryMapper.queryToQueryDTO(savedQuery);
    }

    @Override
    @Transactional
    public QueryDTO createQuery(UUID studentId, QueryDTO queryDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

        // Find the teacher who teaches this subject
        List<Professor> teachers = professorRepository.findByAssignedSubjectsName(queryDTO.getSubject());
        Professor teacher = teachers.stream().findFirst().orElse(null);
        
        if (teacher == null) {
            System.out.println("No teacher found for subject: " + queryDTO.getSubject() + ". Query will be assigned to admin.");
        } else {
            System.out.println("Found teacher: " + teacher.getFirstName() + " " + teacher.getLastName() + " for subject: " + queryDTO.getSubject());
        }

        Query newQuery = new Query(student, queryDTO.getSubject(), queryDTO.getQueryText());
        newQuery.setTeacher(teacher); // Set the teacher if found, null for admin queries
        newQuery.setStatus(QueryStatus.OPEN);
        newQuery.setCreatedAt(Instant.now());

        Query savedQuery = queryRepository.save(newQuery);
        return queryMapper.queryToQueryDTO(savedQuery);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QueryDTO> getQueriesByStudentId(UUID studentId, Pageable pageable) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

        Page<Query> queries = queryRepository.findByStudentId(studentId, pageable); // Assuming no status filter here for now
        return queries.map(queryMapper::queryToQueryDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public QueryDTO getQueryByStudentIdAndQueryId(UUID studentId, UUID queryId) {
        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new NotFoundException("Query not found with ID: " + queryId));

        if (!query.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Query does not belong to the specified student.");
        }
        return queryMapper.queryToQueryDTO(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QueryDTO> findAllQueriesByCriteria(String search, QueryStatus status, Pageable pageable) {
        Page<Query> queries = queryRepository.findAllBySearchAndStatus(search, status, pageable);
        return queries.map(queryMapper::queryToQueryDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public QueryDTO getQueryById(UUID queryId) {
        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new NotFoundException("Query not found with ID: " + queryId));
        return queryMapper.queryToQueryDTO(query);
    }

    @Override
    @Transactional
    public QueryDTO updateQueryStatus(UUID queryId, QueryStatus newStatus) {
        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new NotFoundException("Query not found with ID: " + queryId));
        query.setStatus(newStatus);
        Query updatedQuery = queryRepository.save(query);
        return queryMapper.queryToQueryDTO(updatedQuery);
    }

    @Override
    @Transactional
    public void deleteQuery(UUID queryId) {
        if (!queryRepository.existsById(queryId)) {
            throw new NotFoundException("Query not found with ID: " + queryId);
        }
        queryRepository.deleteById(queryId);
    }

    @Override
    @Transactional
    public QueryDTO respondToQueryByAdmin(UUID queryId, String response, String respondedBy) {
        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new NotFoundException("Query not found with ID: " + queryId));
        query.setResponse(response);
        query.setRespondedBy(respondedBy);
        query.setRespondedAt(java.time.Instant.now());
        query.setStatus(Query.QueryStatus.RESOLVED);
        Query updatedQuery = queryRepository.save(query);
        return queryMapper.queryToQueryDTO(updatedQuery);
    }
} 