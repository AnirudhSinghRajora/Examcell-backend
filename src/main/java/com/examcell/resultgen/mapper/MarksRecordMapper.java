package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.ResultDto;
import com.examcell.resultgen.model.MarksRecord;

// We need SubjectMapper for the nested SubjectDto
@Mapper(componentModel = "spring", uses = {SubjectMapper.class})
public interface MarksRecordMapper {

    MarksRecordMapper INSTANCE = Mappers.getMapper(MarksRecordMapper.class);

    // Source field name ("subject.semester") maps to target field ("semester")
    @Mapping(source = "subject.semester", target = "semester")
    @Mapping(target = "gradePoint", expression = "java(com.examcell.resultgen.util.GradeCalculatorUtil.calculateGradePoint(marksRecord.getMarksObtained()))")
    // Other fields like marksObtained, maxMarks map automatically by name
    // The 'subject' field in MarksRecord will be mapped to 'subject' (SubjectDto) using SubjectMapper
    ResultDto marksRecordToResultDto(MarksRecord marksRecord);

    List<ResultDto> marksRecordsToResultDtos(List<MarksRecord> marksRecords);

    // Mapping back (if needed, more complex)
    // MarksRecord resultDtoToMarksRecord(ResultDto resultDto);
} 