package com.examcell.resultgen.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.TeacherDTO;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.TeacherMapper;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Role;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.UserRepository;
import com.examcell.resultgen.service.TeacherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final TeacherMapper teacherMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherDTO> getAllTeachers(String search, String department, Pageable pageable) {
        Page<Professor> professorsPage;
        if (search != null && !search.isEmpty() && department != null && !department.isEmpty()) {
            professorsPage = professorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndDepartment(search, search, department, pageable);
        } else if (search != null && !search.isEmpty()) {
            professorsPage = professorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, pageable);
        } else if (department != null && !department.isEmpty()) {
            professorsPage = professorRepository.findByDepartmentContainingIgnoreCase(department, pageable);
        } else {
            professorsPage = professorRepository.findAll(pageable);
        }
        return professorsPage.map(teacherMapper::professorToTeacherDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getTeacherById(UUID id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with ID: " + id));
        return teacherMapper.professorToTeacherDTO(professor);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getTeacherByEmployeeId(String employeeId) {
        Professor professor = professorRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new NotFoundException("Teacher not found with Employee ID: " + employeeId));
        return teacherMapper.professorToTeacherDTO(professor);
    }

    @Override
    @Transactional
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        if (userRepository.existsByUsername(teacherDTO.getEmail())) {
            throw new IllegalArgumentException("Teacher with this email already exists: " + teacherDTO.getEmail());
        }
        if (professorRepository.findByEmployeeId(teacherDTO.getEmployeeId()).isPresent()) {
            throw new IllegalArgumentException("Teacher with this Employee ID already exists: " + teacherDTO.getEmployeeId());
        }

        Professor newProfessor = new Professor(
                teacherDTO.getEmail(),
                passwordEncoder.encode("defaultPassword"), // Placeholder: In a real app, handle initial password securely
                teacherDTO.getFirstName(),
                teacherDTO.getLastName(),
                Role.PROFESSOR,
                teacherDTO.getEmployeeId(),
                teacherDTO.getDepartment()
        );
        Professor savedProfessor = professorRepository.save(newProfessor);
        return teacherMapper.professorToTeacherDTO(savedProfessor);
    }

    @Override
    @Transactional
    public TeacherDTO updateTeacher(UUID id, TeacherDTO teacherDTO) {
        Professor existingProfessor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with ID: " + id));

        // Check for email uniqueness if changed
        if (!existingProfessor.getUsername().equals(teacherDTO.getEmail()) && userRepository.existsByUsername(teacherDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use by another user: " + teacherDTO.getEmail());
        }

        // Check for employeeId uniqueness if changed
        if (!existingProfessor.getEmployeeId().equals(teacherDTO.getEmployeeId()) && professorRepository.findByEmployeeId(teacherDTO.getEmployeeId()).isPresent()) {
            throw new IllegalArgumentException("Employee ID already in use by another teacher: " + teacherDTO.getEmployeeId());
        }

        existingProfessor.setUsername(teacherDTO.getEmail());
        existingProfessor.setFirstName(teacherDTO.getFirstName());
        existingProfessor.setLastName(teacherDTO.getLastName());
        existingProfessor.setEmployeeId(teacherDTO.getEmployeeId());
        existingProfessor.setDepartment(teacherDTO.getDepartment());

        Professor updatedProfessor = professorRepository.save(existingProfessor);
        return teacherMapper.professorToTeacherDTO(updatedProfessor);
    }

    @Override
    @Transactional
    public void deleteTeacher(UUID id) {
        if (!professorRepository.existsById(id)) {
            throw new NotFoundException("Teacher not found with ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    private String generateUniqueEmployeeId() {
        return "EMP" + System.currentTimeMillis(); // Simple placeholder
    }
} 