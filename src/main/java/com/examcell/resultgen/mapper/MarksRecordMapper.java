package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.MarkDTO;
import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.dto.TeacherMarkDTO;
import com.examcell.resultgen.model.MarksRecord;
import com.examcell.resultgen.util.GradeCalculatorUtil;

// We need SubjectMapper for the nested SubjectDto
@Mapper(componentModel = "spring", uses = {SubjectMapper.class, StudentMapper.class, GradeCalculatorUtil.class})
public interface MarksRecordMapper {

    MarksRecordMapper INSTANCE = Mappers.getMapper(MarksRecordMapper.class);

    // Source field name ("subject.semester") maps to target field ("semester")
    @Mapping(source = "subject.semester", target = "semester")
    @Mapping(target = "marksObtained", source = ".", qualifiedByName = "calculateTotalMarks")
    @Mapping(target = "maxMarks", expression = "java(100.0)")
    @Mapping(target = "gradePoint", source = ".", qualifiedByName = "calculateGradePointFromRecord")
    @Mapping(target = "grade", source = ".", qualifiedByName = "calculateGradeFromRecord")
    // Other fields like marksObtained, maxMarks map automatically by name
    // The 'subject' field in MarksRecord will be mapped to 'subject' (SubjectDto) using SubjectMapper
    ResultDto marksRecordToResultDto(MarksRecord marksRecord);

    List<ResultDto> marksRecordsToResultDtos(List<MarksRecord> marksRecords);

    // New mapping to MarkDTO
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(target = "studentName", expression = "java(marksRecord.getStudent().getFirstName() + \" \" + marksRecord.getStudent().getLastName())")
    @Mapping(target = "studentRollNo", source = "student.rollNumber")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(source = "subject.semester", target = "semester")
    @Mapping(target = "marks", source = ".", qualifiedByName = "calculateTotalMarks")
    @Mapping(target = "grade", source = ".", qualifiedByName = "calculateGradeFromRecord")
    MarkDTO marksRecordToMarkDTO(MarksRecord marksRecord);

    List<MarkDTO> marksRecordsToMarkDTOs(List<MarksRecord> marksRecords);

    // Mapping back (if needed, more complex)
    // MarksRecord resultDtoToMarksRecord(ResultDto resultDto);

    @Named("calculateTotalMarks")
    default Double calculateTotalMarks(MarksRecord marksRecord) {
        // Calculate weighted total marks: (internal1 + internal2) * 0.4 + external * 0.6
        double internalAverage = (marksRecord.getInternal1() + marksRecord.getInternal2()) / 2.0;
        double totalMarks = (internalAverage * 0.4) + (marksRecord.getExternal() * 0.6);
        // Round to 2 decimal places
        return Math.round(totalMarks * 100.0) / 100.0;
    }

    @Named("calculateGradePointFromRecord")
    default Double calculateGradePointFromRecord(MarksRecord marksRecord) {
        Double total = calculateTotalMarks(marksRecord);
        return GradeCalculatorUtil.calculateGradePoint(total);
    }

    @Named("calculateGradeFromRecord")
    default String calculateGradeFromRecord(MarksRecord marksRecord) {
        Double total = calculateTotalMarks(marksRecord);
        return GradeCalculatorUtil.calculateGrade(total);
    }

    // Mapping to TeacherMarkDTO for teacher marks table
    @Mapping(source = "id", target = "id")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(target = "studentName", expression = "java(marksRecord.getStudent().getFirstName() + \" \" + marksRecord.getStudent().getLastName())")
    @Mapping(target = "studentRollNo", source = "student.rollNumber")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "subject.code", target = "subjectCode")
    @Mapping(target = "marks", source = ".", qualifiedByName = "calculateTotalMarks")
    @Mapping(target = "grade", source = ".", qualifiedByName = "calculateGradeFromRecord")
    @Mapping(target = "examType", expression = "java(\"Final\")")
    @Mapping(target = "academicYear", expression = "java(com.examcell.resultgen.util.SemesterUtil.getAcademicYearForSemester(marksRecord.getStudent().getBatchYear(), marksRecord.getSubject().getSemester()))")
    @Mapping(source = "internal1", target = "internal1")
    @Mapping(source = "internal2", target = "internal2")
    @Mapping(source = "external", target = "external")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    TeacherMarkDTO marksRecordToTeacherMarkDTO(MarksRecord marksRecord);

    List<TeacherMarkDTO> marksRecordsToTeacherMarkDTOs(List<MarksRecord> marksRecords);
} 