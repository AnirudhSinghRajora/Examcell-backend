package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.TeacherDTO;
import com.examcell.resultgen.model.Professor;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(source = "username", target = "email")
    TeacherDTO professorToTeacherDTO(Professor professor);

    List<TeacherDTO> professorsToTeacherDTOs(List<Professor> professors);

    // Mapping back (if needed for POST/PUT)
    // Professor teacherDTOToProfessor(TeacherDTO teacherDTO);
} 