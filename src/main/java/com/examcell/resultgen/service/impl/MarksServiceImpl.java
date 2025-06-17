package com.examcell.resultgen.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcell.resultgen.dto.MarkEntryDto;
import com.examcell.resultgen.exception.NotFoundException;
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

            // Validate marks range
            double maxMarksForValidation = 100.0; // Default defined in MarksRecord
            if (entry.getMarksObtained() < 0 || entry.getMarksObtained() > maxMarksForValidation) {
                 throw new IllegalArgumentException("Marks obtained (" + entry.getMarksObtained() + ") for subject "+ subject.getCode() +" must be between 0 and " + maxMarksForValidation);
            }

            // --- Create or Update Logic ---
            String key = student.getId() + "-" + subject.getId();
            MarksRecord record = existingMarksMap.get(key);

            if (record != null) { // Update
                record.setMarksObtained(entry.getMarksObtained());
                record.setEnteredBy(professor); // Track who entered/updated
            } else { // Create
                record = new MarksRecord(student, subject, entry.getMarksObtained(), professor);
                 // Default maxMarks is set in the entity constructor/field
            }
            savedRecords.add(record);
        }

        return marksRecordRepository.saveAll(savedRecords);
    }

    @Override
    @Transactional
    public MarksRecord updateMark(String professorEmployeeId, UUID recordId, Double newMarksObtained) {
        // Find professor by Employee ID for tracking
         Professor professor = professorRepository.findByEmployeeId(professorEmployeeId)
                .orElseThrow(() -> new NotFoundException("Professor not found with Employee ID: " + professorEmployeeId));

        MarksRecord record = marksRecordRepository.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Marks Record not found with ID: " + recordId));

        // Validation for new marks
         if (newMarksObtained < 0 || newMarksObtained > record.getMaxMarks()) { // Use record's maxMarks
             throw new IllegalArgumentException("Marks obtained (" + newMarksObtained + ") must be between 0 and " + record.getMaxMarks());
         }

        record.setMarksObtained(newMarksObtained);
        record.setEnteredBy(professor); // Track who made the update

        return marksRecordRepository.save(record);
    }
} 