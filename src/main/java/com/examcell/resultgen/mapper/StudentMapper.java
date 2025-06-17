package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    // Simple mapping, field names match
    StudentDto studentToStudentDto(Student student);

    List<StudentDto> studentsToStudentDtos(List<Student> students);

    // Mapping back (if needed)
    // Student studentDtoToStudent(StudentDto studentDto);
} 