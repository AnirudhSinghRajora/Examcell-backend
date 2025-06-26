package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.StudentDto;
import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Student;
import com.examcell.resultgen.util.SemesterUtil;

@Mapper(componentModel = "spring", uses = {SemesterUtil.class})
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    // Simple mapping, field names match
    @Mapping(source = "username", target = "email")
    @Mapping(source = "branch", target = "department", qualifiedByName = "mapBranchToDepartment")
    @Mapping(target = "semester", source = "batchYear", qualifiedByName = "calculateSemester")
    StudentDto studentToStudentDto(Student student);

    List<StudentDto> studentsToStudentDtos(List<Student> students);

    // Mapping back (if needed)
    // Student studentDtoToStudent(StudentDto studentDto);

    @Named("mapBranchToDepartment")
    default String mapBranchToDepartment(Branch branch) {
        return branch != null ? branch.name() : null;
    }

    @Named("calculateSemester")
    default Integer calculateSemester(String batchYear) {
        if (batchYear == null || batchYear.isEmpty()) {
            return null; // Or throw an exception, depending on desired behavior for null/empty input
        }
        try {
            return SemesterUtil.calculateCurrentSemester(Integer.parseInt(batchYear));
        } catch (NumberFormatException e) {
            // Log the error or handle it as appropriate for your application
            return null; // Or throw a more specific exception
        }
    }
} 