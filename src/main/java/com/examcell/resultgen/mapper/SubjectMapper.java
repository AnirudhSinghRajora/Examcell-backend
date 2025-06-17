package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.model.Subject;

@Mapper(componentModel = "spring") // Integrate with Spring DI
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    // Simple mapping, field names match
    SubjectDto subjectToSubjectDto(Subject subject);

    List<SubjectDto> subjectsToSubjectDtos(List<Subject> subjects);

    // Mapping back (if needed)
    // Subject subjectDtoToSubject(SubjectDto subjectDto);
} 