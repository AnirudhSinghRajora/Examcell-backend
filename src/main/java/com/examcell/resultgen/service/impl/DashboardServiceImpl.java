package com.examcell.resultgen.service.impl;

import com.examcell.resultgen.dto.DashboardDTO;
import com.examcell.resultgen.model.BonafideRequest.BonafideRequestStatus;
import com.examcell.resultgen.model.Query.QueryStatus;
import com.examcell.resultgen.repository.BonafideRequestRepository;
import com.examcell.resultgen.repository.MarksRecordRepository;
import com.examcell.resultgen.repository.ProfessorRepository;
import com.examcell.resultgen.repository.QueryRepository;
import com.examcell.resultgen.repository.StudentRepository;
import com.examcell.resultgen.repository.SubjectRepository;
import com.examcell.resultgen.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final QueryRepository queryRepository;
    private final BonafideRequestRepository bonafideRequestRepository;
    private final MarksRecordRepository marksRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getDashboardStatistics() {
        long totalStudents = studentRepository.count();
        long totalProfessors = professorRepository.count();
        long totalSubjects = subjectRepository.count();
        long totalQueriesOpen = queryRepository.countByStatus(QueryStatus.OPEN);
        long totalBonafideRequestsPending = bonafideRequestRepository.countByStatus(BonafideRequestStatus.PENDING);
        long totalMarksRecords = marksRecordRepository.count();

        return new DashboardDTO(
                totalStudents,
                totalProfessors,
                totalSubjects,
                totalQueriesOpen,
                totalBonafideRequestsPending,
                totalMarksRecords
        );
    }
} 