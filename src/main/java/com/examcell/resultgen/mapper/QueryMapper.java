package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.QueryDTO;
import com.examcell.resultgen.model.Query;

@Mapper(componentModel = "spring")
public interface QueryMapper {

    QueryMapper INSTANCE = Mappers.getMapper(QueryMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(target = "studentName", expression = "java(query.getStudent() != null ? query.getStudent().getFirstName() + \" \" + query.getStudent().getLastName() : null)")
    @Mapping(target = "studentRollNo", expression = "java(query.getStudent() != null ? query.getStudent().getRollNumber() : null)")
    @Mapping(target = "studentEmail", expression = "java(query.getStudent() != null ? query.getStudent().getUsername() : null)")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(target = "teacherName", expression = "java(query.getTeacher() != null ? query.getTeacher().getFirstName() + \" \" + query.getTeacher().getLastName() : null)")
    @Mapping(target = "faculty", expression = "java(query.getTeacher() != null ? query.getTeacher().getDepartment() : \"Admin\")")
    @Mapping(source = "queryText", target = "description")
    @Mapping(target = "title", expression = "java(query.getSubject() + \" Query\")")
    @Mapping(target = "priority", constant = "MEDIUM")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "createdAt", target = "updatedAt")
    QueryDTO queryToQueryDTO(Query query);

    List<QueryDTO> queriesToQueryDTOs(List<Query> queries);

    // Mapping back for create/update (if needed, ensure consistency with DTO)
    // @Mapping(target = "student", ignore = true)
    // @Mapping(target = "teacher", ignore = true)
    // @Mapping(target = "createdAt", ignore = true)
    // Query queryDTOToQuery(QueryDTO queryDTO);
} 