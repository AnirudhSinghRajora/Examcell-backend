package com.examcell.resultgen.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.examcell.resultgen.dto.SubjectDto;

public interface SubjectService {
    SubjectDto createSubject(SubjectDto subjectDto);
    SubjectDto getSubjectById(UUID id);
    Page<SubjectDto> getAllSubjects(String search, Integer semester, String department, Pageable pageable);
    SubjectDto updateSubject(UUID id, SubjectDto subjectDto);
    void deleteSubject(UUID id);
    List<SubjectDto> getSubjectsByCourseAndBranchAndSemester(String courseName, String branchName, int semester);
    List<SubjectDto> getSubjectsBySemester(Integer semester);
} 