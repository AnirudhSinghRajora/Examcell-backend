package com.examcell.resultgen.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.SubjectMapper;
import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.SubjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Course course = null;
        if (subjectDto.getCourseName() != null && !subjectDto.getCourseName().isEmpty()) {
            try {
                course = Course.valueOf(subjectDto.getCourseName().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Course name: " + subjectDto.getCourseName());
            }
        } else {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }

        Set<Branch> branches = new HashSet<>();
        if (subjectDto.getBranchNames() != null) {
            for (String branchName : subjectDto.getBranchNames()) {
                try {
                    branches.add(Branch.valueOf(branchName.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid Branch name: " + branchName);
                }
            }
        }

        Subject subject = subjectMapper.subjectDtoToSubject(subjectDto);
        subject.setCourse(course);
        subject.setBranches(branches);

        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.subjectToSubjectDto(savedSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDto getSubjectById(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with ID: " + id));
        return subjectMapper.subjectToSubjectDto(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectDto> getAllSubjects(String search, Integer semester, String department, Pageable pageable) {
        Branch branch = (department != null && !department.isEmpty()) ? Branch.valueOf(department.toUpperCase()) : null;
        Page<Subject> subjectsPage = subjectRepository.findAllByCriteria(search, semester, branch, pageable);
        return subjectsPage.map(subjectMapper::subjectToSubjectDto);
    }

    @Override
    @Transactional
    public SubjectDto updateSubject(UUID id, SubjectDto subjectDto) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with ID: " + id));

        if (subjectDto.getCourseName() != null && !subjectDto.getCourseName().isEmpty()) {
            try {
                Course newCourse = Course.valueOf(subjectDto.getCourseName().toUpperCase());
                existingSubject.setCourse(newCourse);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Course name: " + subjectDto.getCourseName());
            }
        }

        // Handle multiple branch names
        Set<Branch> newBranches = new HashSet<>();
        if (subjectDto.getBranchNames() != null) {
            for (String branchName : subjectDto.getBranchNames()) {
                try {
                    newBranches.add(Branch.valueOf(branchName.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid Branch name: " + branchName);
                }
            }
        }
        existingSubject.setBranches(newBranches);

        existingSubject.setName(subjectDto.getName());
        existingSubject.setCode(subjectDto.getCode());
        existingSubject.setDescription(subjectDto.getDescription());
        existingSubject.setSemester(subjectDto.getSemester());
        existingSubject.setCredits(subjectDto.getCredits());

        Subject updatedSubject = subjectRepository.save(existingSubject);
        return subjectMapper.subjectToSubjectDto(updatedSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(UUID id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found with ID: " + id);
        }
        subjectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectsByCourseAndBranchAndSemester(String courseName, String branchName, int semester) {
        Course course = null;
        if (courseName != null && !courseName.isEmpty()) {
            try {
                course = Course.valueOf(courseName.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Course name: " + courseName);
            }
        }

        Branch branch = null;
        if (branchName != null && !branchName.isEmpty()) {
            try {
                branch = Branch.valueOf(branchName.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Branch name: " + branchName);
            }
        }

        List<Subject> subjects = subjectRepository.findByCourseAndBranchAndSemester(course, branch, semester);
        return subjects.stream()
                .map(subjectMapper::subjectToSubjectDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjectsBySemester(Integer semester) {
        return subjectRepository.findBySemester(semester)
            .stream()
            .map(subjectMapper::subjectToSubjectDto)
            .toList();
    }
}
