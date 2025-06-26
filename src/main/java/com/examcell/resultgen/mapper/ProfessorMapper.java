package com.examcell.resultgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.TeacherDTO;
import com.examcell.resultgen.model.Professor;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    @Mapping(source = "username", target = "email")
    TeacherDTO professorToTeacherDTO(Professor professor);

    // Method to convert TeacherDTO back to Professor if needed for updates/creation
    // @Mapping(source = "email", target = "username")
    // @Mapping(target = "password", ignore = true) // Password should not be mapped directly
    // Professor teacherDTOToProfessor(TeacherDTO teacherDTO);
} 