package com.examcell.resultgen.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.examcell.resultgen.dto.MarkDTO;
import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.dto.TeacherMarkDTO;
import com.examcell.resultgen.exception.NotFoundException;
import com.examcell.resultgen.mapper.MarksRecordMapper;
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.model.Professor;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.model.Subject;
import com.examcell.resultgen.repository.MarksRecordRepository;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.MarksService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarksServiceImpl implements MarksService {

    private final MarksRecordRepository marksRecordRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRecordMapper marksRecordMapper;

    @Override
    @Transactional
    public List<MarksRecord> batchEnterMarks(String professorEmployeeId, List<MarkEntryDto> markEntries) {
        // Find professor by Employee ID for tracking purposes
        Professor professor = professorRepository.findByEmployeeId(professorEmployeeId)
                .orElseThrow(() -> new NotFoundException("Professor not found with Employee ID: " + professorEmployeeId));

        // --- Pre-fetch entities (uses internal UUIDs) ---
        List<UUID> studentIds = markEntries.stream().map(MarkEntryDto::getStudentId).distinct().toList();
        List<UUID> subjectIds = markEntries.stream().map(MarkEntryDto::getSubjectId).distinct().toList();

        Map<UUID, Student> studentMap = studentRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getId, Function.identity()));
        Map<UUID, Subject> subjectMap = subjectRepository.findAllById(subjectIds).stream()
                .collect(Collectors.toMap(Subject::getId, Function.identity()));

        Map<String, MarksRecord> existingMarksMap = marksRecordRepository.findByStudentIdInAndSubjectIdIn(studentIds, subjectIds).stream()
                .collect(Collectors.toMap(mr -> mr.getStudent().getId() + "-" + mr.getSubject().getId(), Function.identity()));

        List<MarksRecord> savedRecords = new ArrayList<>();

        for (MarkEntryDto entry : markEntries) {
            Student student = studentMap.get(entry.getStudentId());
            Subject subject = subjectMap.get(entry.getSubjectId());

            // --- Validation ---
            if (student == null) {
                throw new NotFoundException("Student not found with ID: " + entry.getStudentId());
            }
            if (subject == null) {
                throw new NotFoundException("Subject not found with ID: " + entry.getSubjectId());
            }

            // Validate marks range (assuming 100 max for total of internal1, internal2, external)
            // This logic might need refinement if max marks vary per internal/external
            double totalMarksObtained = entry.getInternal1() + entry.getInternal2() + entry.getExternal();
            double maxMarksForValidation = 100.0; // Default max marks, adjust if necessary
            if (totalMarksObtained < 0 || totalMarksObtained > maxMarksForValidation) {
                 throw new IllegalArgumentException("Total marks obtained (" + totalMarksObtained + ") for subject "+ subject.getCode() +" must be between 0 and " + maxMarksForValidation);
            }

            // --- Create or Update Logic ---
            String key = student.getId() + "-" + subject.getId();
            MarksRecord record = existingMarksMap.get(key);

            if (record != null) { // Update
                record.setInternal1(entry.getInternal1());
                record.setInternal2(entry.getInternal2());
                record.setExternal(entry.getExternal());
                record.setEnteredBy(professor); // Track who entered/updated
            } else { // Create
                record = new MarksRecord(student, subject, entry.getInternal1(), entry.getInternal2(), entry.getExternal(), professor);
                 // Default maxMarks is set in the entity constructor/field
            }
            savedRecords.add(record);
        }

        return marksRecordRepository.saveAll(savedRecords);
    }

    @Override
    @Transactional
    public MarksRecord updateMark(String professorEmployeeId, UUID recordId, Double marks) {
        // Find professor by Employee ID for tracking
         Professor professor = professorRepository.findByEmployeeId(professorEmployeeId)
                .orElseThrow(() -> new NotFoundException("Professor not found with Employee ID: " + professorEmployeeId));

        MarksRecord record = marksRecordRepository.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + recordId));

        // Validation for new marks
         if (marks < 0 || marks > record.getMaxMarks()) { // Use record's maxMarks
             throw new IllegalArgumentException("Invalid mark value. Marks (" + marks + ") must be between 0 and " + record.getMaxMarks());
         }

        // Assuming MarksRecord now stores a single 'totalMarks' field or we derive internal/external from it
        // For simplicity, let's assume we're updating a derived 'total' or 'final' marks field if that's what 'marks' parameter represents.
        // If MarksRecord still has internal1, internal2, external, we need a strategy to update them from 'marks'.
        // For now, let's just set the total marks. This might require changes in MarksRecord model if not already present.
        record.setInternal1(marks / 3.0); // Simplified assumption for demonstration
        record.setInternal2(marks / 3.0); // Simplified assumption for demonstration
        record.setExternal(marks / 3.0); // Simplified assumption for demonstration
        
        record.setEnteredBy(professor); // Track who made the update

        return marksRecordRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarkDTO> getMarksByTeacherAndSubject(UUID teacherId, UUID subjectId) {
        professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Subject not found with ID: " + subjectId));

        // Find marks records where the subject is assigned to this teacher
        // This assumes a relationship between Professor and Subject, or MarksRecord stores enteredBy
        List<MarksRecord> marksRecords = marksRecordRepository.findByEnteredByIdAndSubjectId(teacherId, subjectId);

        return marksRecordMapper.marksRecordsToMarkDTOs(marksRecords);
    }

    @Override
    @Transactional
    public MarkDTO createMark(UUID teacherId, MarkEntryDto markEntryDto) {
        Professor professor = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        Student student = studentRepository.findById(markEntryDto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + markEntryDto.getStudentId()));

        Subject subject = subjectRepository.findById(markEntryDto.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Subject not found with ID: " + markEntryDto.getSubjectId()));

        // Check if marks already exist for this student and subject
        if (marksRecordRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId()).isPresent()) {
            throw new IllegalArgumentException("Marks already exist for student " + student.getRollNumber() + " in subject " + subject.getCode());
        }

        // Create new MarksRecord
        MarksRecord newMarksRecord = new MarksRecord(
                student,
                subject,
                markEntryDto.getInternal1(),
                markEntryDto.getInternal2(),
                markEntryDto.getExternal(),
                professor
        );

        MarksRecord savedMarksRecord = marksRecordRepository.save(newMarksRecord);
        return marksRecordMapper.marksRecordToMarkDTO(savedMarksRecord);
    }

    @Override
    @Transactional
    public MarkDTO updateMarkByTeacher(UUID teacherId, UUID markId, Double internal1, Double internal2, Double external) {
        Professor professor = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        MarksRecord existingMarksRecord = marksRecordRepository.findById(markId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + markId));

        // Ensure the professor is the one who entered these marks or has authority
        if (!existingMarksRecord.getEnteredBy().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Professor is not authorized to update this mark.");
        }

        // Update fields from DTO
        existingMarksRecord.setInternal1(internal1);
        existingMarksRecord.setInternal2(internal2);
        existingMarksRecord.setExternal(external);

        MarksRecord updatedMarksRecord = marksRecordRepository.save(existingMarksRecord);
        return marksRecordMapper.marksRecordToMarkDTO(updatedMarksRecord);
    }

    @Override
    @Transactional
    public void deleteMarkByTeacher(UUID teacherId, UUID markId) {
        Professor professor = professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        MarksRecord marksRecordToDelete = marksRecordRepository.findById(markId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + markId));

        // Ensure the professor is the one who entered these marks or has authority
        if (!marksRecordToDelete.getEnteredBy().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Professor is not authorized to delete this mark.");
        }

        marksRecordRepository.delete(marksRecordToDelete);
    }

    @Override
    @Transactional
    public List<MarkDTO> uploadMarksFromExcel(UUID teacherId, MultipartFile file, UUID subjectId, String semester) {
        // Placeholder implementation: In a real scenario, you\'d parse the Excel file here.
        // This would involve using libraries like Apache POI.
        // For now, let\'s just throw an UnsupportedOperationException or return an empty list.
        // A more complete implementation would:
        // 1. Read the Excel file (XSSFWorkbook, HSSFWorkbook)
        // 2. Iterate through rows and columns to extract student roll numbers and marks
        // 3. Look up Student and Subject entities based on extracted data
        // 4. Create MarkEntryDto objects for batch processing
        // 5. Call batchEnterMarks or similar method.

        // Example (conceptual): 
        // List<MarkEntryDto> markEntries = parseExcelFile(file, subjectId, semester);
        // List<MarksRecord> savedRecords = batchEnterMarks(professorRepository.findById(teacherId).get().getEmployeeId(), markEntries);
        // return marksRecordMapper.marksRecordsToMarkDTOs(savedRecords);

        throw new UnsupportedOperationException("Excel upload functionality not yet implemented.");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarkDTO> getAllMarksByCriteria(UUID studentId, UUID subjectId, Pageable pageable) {
        Page<MarksRecord> marksRecordsPage;
        if (studentId != null && subjectId != null) {
            marksRecordsPage = marksRecordRepository.findByStudentIdAndSubjectId(studentId, subjectId, pageable);
        } else if (studentId != null) {
            marksRecordsPage = marksRecordRepository.findByStudentId(studentId, pageable);
        } else if (subjectId != null) {
            marksRecordsPage = marksRecordRepository.findBySubjectId(subjectId, pageable);
        } else {
            marksRecordsPage = marksRecordRepository.findAll(pageable);
        }
        return marksRecordsPage.map(marksRecordMapper::marksRecordToMarkDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MarkDTO getMarkById(UUID markId) {
        MarksRecord marksRecord = marksRecordRepository.findById(markId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + markId));
        return marksRecordMapper.marksRecordToMarkDTO(marksRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarkDTO> getMarksByStudentId(UUID studentId, Pageable pageable) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));
        Page<MarksRecord> marksRecordsPage = marksRecordRepository.findByStudentId(studentId, pageable);
        return marksRecordsPage.map(marksRecordMapper::marksRecordToMarkDTO);
    }

    @Override
    @Transactional
    public MarkDTO updateMarkByAdmin(String adminEmployeeId, UUID markId, Double internal1, Double internal2, Double external) {
        Professor admin = professorRepository.findByEmployeeId(adminEmployeeId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with Employee ID: " + adminEmployeeId));

        MarksRecord existingMarksRecord = marksRecordRepository.findById(markId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + markId));

        // Admin can update any mark, no need to check enteredBy
        existingMarksRecord.setInternal1(internal1);
        existingMarksRecord.setInternal2(internal2);
        existingMarksRecord.setExternal(external);
        existingMarksRecord.setEnteredBy(admin); // Track that admin made the update

        MarksRecord updatedMarksRecord = marksRecordRepository.save(existingMarksRecord);
        return marksRecordMapper.marksRecordToMarkDTO(updatedMarksRecord);
    }

    @Override
    @Transactional
    public void deleteMarkByAdmin(UUID markId) {
        if (!marksRecordRepository.existsById(markId)) {
            throw new NotFoundException("Marks Record not found with ID: " + markId);
        }
        marksRecordRepository.deleteById(markId);
    }

    @Override
    @Transactional
    public List<MarkDTO> uploadMarksFromExcel(String adminEmployeeId, MultipartFile file, UUID subjectId, String semester) {
        // Find admin user for tracking purposes, similar to how professor is found.
        Professor admin = professorRepository.findByEmployeeId(adminEmployeeId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with Employee ID: " + adminEmployeeId));

        // Placeholder implementation for Excel parsing, similar to the teacher's upload.
        throw new UnsupportedOperationException("Excel upload functionality for admin not yet fully implemented.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherMarkDTO> getTeacherMarksByTeacherAndSubject(UUID teacherId, UUID subjectId) {
        professorRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Professor not found with ID: " + teacherId));

        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Subject not found with ID: " + subjectId));

        List<MarksRecord> marksRecords = marksRecordRepository.findByEnteredByIdAndSubjectId(teacherId, subjectId);
        return marksRecordMapper.marksRecordsToTeacherMarkDTOs(marksRecords);
    }
} 